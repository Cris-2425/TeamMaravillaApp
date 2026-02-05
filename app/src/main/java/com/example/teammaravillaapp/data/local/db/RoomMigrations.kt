package com.example.teammaravillaapp.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.json.JSONArray

object RoomMigrations {

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE user_lists ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // 1) Nueva tabla list_items
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

            // 2) Nueva tabla user_lists sin productIds
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

            // 3) Copiar cabeceras
            db.execSQL(
                """
                INSERT INTO user_lists_new (id, name, background, createdAt)
                SELECT id, name, background, createdAt
                FROM user_lists
                """.trimIndent()
            )

            // 4) Convertir productIds (TEXT) -> filas en list_items
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

            // 5) Reemplazar tablas
            db.execSQL("DROP TABLE user_lists")
            db.execSQL("ALTER TABLE user_lists_new RENAME TO user_lists")
        }

        private fun parseProductIds(raw: String?): List<String> {
            if (raw.isNullOrBlank()) return emptyList()

            // Intento JSON (nuevo converter)
            runCatching {
                val arr = JSONArray(raw)
                return buildList(arr.length()) {
                    for (i in 0 until arr.length()) add(arr.getString(i))
                }.filter { it.isNotBlank() }
            }

            // Fallback al formato viejo con "|"
            return raw.split("|").filter { it.isNotBlank() }
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Room guarda Boolean como INTEGER 0/1 <--- Importante esto
            db.execSQL("ALTER TABLE list_items ADD COLUMN checked INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE list_items ADD COLUMN quantity INTEGER NOT NULL DEFAULT 1")
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // 1) Crear nueva tabla con columnas extra
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

            // 2) Copiar datos existentes (solo recipeId/productId)
            db.execSQL(
                """
            INSERT INTO recipe_ingredients_new (recipeId, productId, quantity, unit, position)
            SELECT recipeId, productId, NULL, NULL, 0
            FROM recipe_ingredients
            """.trimIndent()
            )

            // 3) Reemplazar tabla antigua
            db.execSQL("DROP TABLE recipe_ingredients")
            db.execSQL("ALTER TABLE recipe_ingredients_new RENAME TO recipe_ingredients")

            // 4) Índices (opcional pero recomendado)
            db.execSQL("CREATE INDEX IF NOT EXISTS index_recipe_ingredients_recipeId ON recipe_ingredients(recipeId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_recipe_ingredients_productId ON recipe_ingredients(productId)")
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE products ADD COLUMN imageRes INTEGER")
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // 1) Crear tabla nueva con el esquema correcto
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

            // 2) Copiar datos, arreglando posibles NULLs en instructions
            db.execSQL(
                """
            INSERT INTO recipes_new (id, title, imageRes, instructions)
            SELECT id, title, imageRes, COALESCE(instructions, '')
            FROM recipes
            """.trimIndent()
            )

            // 3) Reemplazar
            db.execSQL("DROP TABLE recipes")
            db.execSQL("ALTER TABLE recipes_new RENAME TO recipes")
        }
    }
}