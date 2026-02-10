package com.example.teammaravillaapp.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.json.JSONArray

/**
 * Contiene todas las migraciones de Room para mantener la integridad de la base de datos
 * al actualizar la versión de la app.
 *
 * Cada migración define los pasos necesarios para transformar tablas antiguas
 * al nuevo esquema sin perder datos.
 */
object RoomMigrations {

    /**
     * Migración de la versión 3 a 4:
     * - Agrega columna `createdAt` a la tabla `user_lists` con valor por defecto 0.
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE user_lists ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    /**
     * Migración de la versión 4 a 5:
     * - Crea tabla `list_items`.
     * - Crea nueva tabla `user_lists_new`.
     * - Copia datos de `user_lists` y transforma `productIds` a filas en `list_items`.
     * - Reemplaza tabla antigua.
     */
    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 1) Crear tabla list_items
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS list_items (
                    listId TEXT NOT NULL,
                    productId TEXT NOT NULL,
                    addedAt INTEGER NOT NULL,
                    position INTEGER NOT NULL,
                    PRIMARY KEY(listId, productId)
                )
                """.trimIndent()
            )

            // Crear tabla user_lists_new sin productIds
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS user_lists_new (
                    id TEXT NOT NULL,
                    name TEXT NOT NULL,
                    background TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    PRIMARY KEY(id)
                )
                """.trimIndent()
            )

            // Copiar cabeceras
            db.execSQL(
                """
                INSERT INTO user_lists_new (id, name, background, createdAt)
                SELECT id, name, background, createdAt
                FROM user_lists
                """.trimIndent()
            )

            // Transformar productIds a filas en list_items
            val cursor = db.query("SELECT id, createdAt, productIds FROM user_lists")
            cursor.use { c ->
                val colId = c.getColumnIndex("id")
                val colCreatedAt = c.getColumnIndex("createdAt")
                val colProductIds = c.getColumnIndex("productIds")

                while (c.moveToNext()) {
                    val listId = c.getString(colId)
                    val createdAt = c.getLong(colCreatedAt)
                    val raw = c.getString(colProductIds)

                    val ids: List<String> = parseProductIds(raw).distinct()

                    ids.forEachIndexed { index, pid ->
                        db.execSQL(
                            "INSERT OR REPLACE INTO list_items (listId, productId, addedAt, position) VALUES (?, ?, ?, ?)",
                            arrayOf(listId, pid, createdAt + index, index)
                        )
                    }
                }
            }

            // Reemplazar tabla antigua
            db.execSQL("DROP TABLE user_lists")
            db.execSQL("ALTER TABLE user_lists_new RENAME TO user_lists")
        }

        /** Convierte un string de productIds a lista de IDs, soportando JSON y formato antiguo. */
        private fun parseProductIds(raw: String?): List<String> {
            if (raw.isNullOrBlank()) return emptyList()

            // Intento parsear como JSON
            runCatching {
                val arr = JSONArray(raw)
                return buildList(arr.length()) {
                    for (i in 0 until arr.length()) add(arr.getString(i))
                }.filter { it.isNotBlank() }
            }

            // Fallback: formato viejo separado por "|"
            return raw.split("|").filter { it.isNotBlank() }
        }
    }

    /**
     * Migración de la versión 5 a 6:
     * - Agrega columnas `checked` y `quantity` a `list_items`.
     * - Room representa Boolean como INTEGER 0/1.
     */
    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE list_items ADD COLUMN checked INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE list_items ADD COLUMN quantity INTEGER NOT NULL DEFAULT 1")
        }
    }

    /**
     * Migración de la versión 6 a 7:
     * - Modifica tabla `recipe_ingredients` para agregar columnas `quantity`, `unit`, `position`.
     * - Copia datos existentes y reemplaza tabla antigua.
     * - Crea índices recomendados.
     */
    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS recipe_ingredients_new (
                    recipeId INTEGER NOT NULL,
                    productId TEXT NOT NULL,
                    quantity REAL,
                    unit TEXT,
                    position INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY(recipeId, productId)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                INSERT INTO recipe_ingredients_new (recipeId, productId, quantity, unit, position)
                SELECT recipeId, productId, NULL, NULL, 0
                FROM recipe_ingredients
                """.trimIndent()
            )

            db.execSQL("DROP TABLE recipe_ingredients")
            db.execSQL("ALTER TABLE recipe_ingredients_new RENAME TO recipe_ingredients")

            db.execSQL("CREATE INDEX IF NOT EXISTS index_recipe_ingredients_recipeId ON recipe_ingredients(recipeId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_recipe_ingredients_productId ON recipe_ingredients(productId)")
        }
    }

    /**
     * Migración de la versión 7 a 8:
     * - Agrega columna `imageRes` a la tabla `products`.
     */
    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE products ADD COLUMN imageRes INTEGER")
        }
    }

    /**
     * Migración de la versión 8 a 9:
     * - Reestructura tabla `recipes` para garantizar que `instructions` nunca sea NULL.
     */
    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS recipes_new (
                    id INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    imageRes INTEGER,
                    instructions TEXT NOT NULL,
                    PRIMARY KEY(id)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                INSERT INTO recipes_new (id, title, imageRes, instructions)
                SELECT id, title, imageRes, COALESCE(instructions, '')
                FROM recipes
                """.trimIndent()
            )

            db.execSQL("DROP TABLE recipes")
            db.execSQL("ALTER TABLE recipes_new RENAME TO recipes")
        }
    }
}