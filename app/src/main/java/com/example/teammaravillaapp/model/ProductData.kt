package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

object ProductData {

    val allProducts: List<Product> = listOf(
        // Frutas
        Product("Manzana", R.drawable.manzana, ProductCategory.FRUITS),
        Product("Plátano", R.drawable.platano, ProductCategory.FRUITS),
        Product("Naranja", R.drawable.naranja, ProductCategory.FRUITS),
        Product("Pera", R.drawable.pera, ProductCategory.FRUITS),
        Product("Fresa", R.drawable.fresa, ProductCategory.FRUITS),
        Product("Kiwi", R.drawable.kiwi, ProductCategory.FRUITS),
        Product("Mango", R.drawable.mango, ProductCategory.FRUITS),
        Product("Uva", R.drawable.uva, ProductCategory.FRUITS),
        Product("Melón", R.drawable.melon, ProductCategory.FRUITS),
        Product("Sandía", R.drawable.sandia, ProductCategory.FRUITS),

        // Verduras
        Product("Zanahoria", R.drawable.zanahoria, ProductCategory.VEGETABLES),
        Product("Lechuga", R.drawable.lechuga, ProductCategory.VEGETABLES),
        Product("Tomate", R.drawable.tomate, ProductCategory.VEGETABLES),
        Product("Cebolla", R.drawable.cebolla, ProductCategory.VEGETABLES),
        Product("Pimiento", R.drawable.pimiento, ProductCategory.VEGETABLES),
        Product("Patata", R.drawable.patata, ProductCategory.VEGETABLES),
        Product("Calabacín", R.drawable.calabacin, ProductCategory.VEGETABLES),
        Product("Pepino", R.drawable.pepino, ProductCategory.VEGETABLES),
        Product("Ajo", R.drawable.ajo, ProductCategory.VEGETABLES),
        Product("Brócoli", R.drawable.brocoli, ProductCategory.VEGETABLES),

        // Carnes
        Product("Pollo", R.drawable.pollo, ProductCategory.MEAT),
        Product("Ternera", R.drawable.ternera, ProductCategory.MEAT),
        Product("Cerdo", R.drawable.cerdo, ProductCategory.MEAT),
        Product("Pavo", R.drawable.pavo, ProductCategory.MEAT),
        Product("Cordero", R.drawable.cordero, ProductCategory.MEAT),
        Product("Jamón", R.drawable.jamon, ProductCategory.MEAT),
        Product("Chorizo", R.drawable.chorizo, ProductCategory.MEAT),
        Product("Salchicha", R.drawable.salchicha, ProductCategory.MEAT),
        Product("Albóndigas", R.drawable.albondigas, ProductCategory.MEAT),
        Product("Carne picada", R.drawable.carne_picada, ProductCategory.MEAT),

        // Pescados
        Product("Atún", R.drawable.atun, ProductCategory.FISH),
        Product("Salmón", R.drawable.salmon, ProductCategory.FISH),
        Product("Merluza", R.drawable.merluza, ProductCategory.FISH),
        Product("Bacalao", R.drawable.bacalao, ProductCategory.FISH),
        Product("Sardina", R.drawable.sardina, ProductCategory.FISH),
        Product("Calamares", R.drawable.calamares, ProductCategory.FISH),
        Product("Gambas", R.drawable.gambas, ProductCategory.FISH),
        Product("Mejillón", R.drawable.mejillon, ProductCategory.FISH),
        Product("Bonito", R.drawable.bonito, ProductCategory.FISH),
        Product("Lubina", R.drawable.lubina, ProductCategory.FISH),

        // Lácteos
        Product("Leche", R.drawable.leche, ProductCategory.DAIRY),
        Product("Yogur", R.drawable.yogur, ProductCategory.DAIRY),
        Product("Queso", R.drawable.queso, ProductCategory.DAIRY),
        Product("Mantequilla", R.drawable.mantequilla, ProductCategory.DAIRY),
        Product("Kéfir", R.drawable.kefir, ProductCategory.DAIRY),
        Product("Nata", R.drawable.nata, ProductCategory.DAIRY),
        Product("Requesón", R.drawable.requeson, ProductCategory.DAIRY),
        Product("Cuajada", R.drawable.cuajada, ProductCategory.DAIRY),
        Product("Batido", R.drawable.batido, ProductCategory.DAIRY),
        Product("Petit", R.drawable.petit, ProductCategory.DAIRY),

        // Panadería
        Product("Pan", R.drawable.pan, ProductCategory.BAKERY),
        Product("Bollo", R.drawable.bollo, ProductCategory.BAKERY),
        Product("Barra", R.drawable.barra, ProductCategory.BAKERY),
        Product("Baguette", R.drawable.baguette, ProductCategory.BAKERY),
        Product("Pan de molde", R.drawable.pan_de_molde, ProductCategory.BAKERY),
        Product("Croissant", R.drawable.croissant, ProductCategory.BAKERY),
        Product("Donut", R.drawable.donut, ProductCategory.BAKERY),
        Product("Napolitana", R.drawable.napolitana, ProductCategory.BAKERY),
        Product("Torta", R.drawable.torta, ProductCategory.BAKERY),
        Product("Empanada", R.drawable.empanada, ProductCategory.BAKERY),

        // Limpieza
        Product("Detergente", R.drawable.detergente, ProductCategory.CLEANING),
        Product("Suavizante", R.drawable.suavizante, ProductCategory.CLEANING),
        Product("Lejía", R.drawable.lejia, ProductCategory.CLEANING),
        Product("Lavavajillas", R.drawable.lavavajillas, ProductCategory.CLEANING),
        Product("Multiusos", R.drawable.multiusos, ProductCategory.CLEANING),
        Product("Bayetas", R.drawable.bayetas, ProductCategory.CLEANING),
        Product("Estropajo", R.drawable.estropajo, ProductCategory.CLEANING),
        Product("Ambientador", R.drawable.ambientador, ProductCategory.CLEANING),
        Product("Papel cocina", R.drawable.papel_cocina, ProductCategory.CLEANING),
        Product("Papel higiénico", R.drawable.papel_higienico, ProductCategory.CLEANING),

        // Bebidas
        Product("Monster", R.drawable.monster, ProductCategory.DRINKS),
        Product("Agua", R.drawable.agua, ProductCategory.DRINKS),
        Product("Coca-Cola", R.drawable.cocacola, ProductCategory.DRINKS),
        Product("Nestea", R.drawable.nestea, ProductCategory.DRINKS),
        Product("Zumo", R.drawable.zumo, ProductCategory.DRINKS),
        Product("Aquarius", R.drawable.aquarius, ProductCategory.DRINKS),
        Product("Vino blanco", R.drawable.vino_blanco, ProductCategory.DRINKS),
        Product("Vino tinto", R.drawable.vino_tinto, ProductCategory.DRINKS),
        Product("Café", R.drawable.cafe, ProductCategory.DRINKS),
        Product("Té", R.drawable.te, ProductCategory.DRINKS),

        Product("Pasta", R.drawable.pasta, ProductCategory.PASTA),
        Product("Arroz", R.drawable.arroz, ProductCategory.RICE),

        Product("Aceite", R.drawable.aceite, ProductCategory.OTHER),
        Product("Azúcar", R.drawable.azucar, ProductCategory.OTHER),
        Product("Harina", R.drawable.harina, ProductCategory.OTHER),
        Product("Aceitunas", R.drawable.aceitunas, ProductCategory.OTHER),
        Product("Pesto", R.drawable.pesto, ProductCategory.OTHER),
        Product("Huevo", R.drawable.huevo, ProductCategory.OTHER),
    )

    val byName = allProducts.associateBy { it.name }

    fun product(name: String): Product =
        requireNotNull(byName[name]) { "Falta '$name' en allProducts de ProductData" }
}