# 🛒 Team Maravilla App

Aplicación Android desarrollada en **Kotlin** con **Jetpack Compose**.  
Su objetivo es ofrecer una experiencia moderna y visual para gestionar listas de compra, recetas y productos, con posibilidad de personalización del estilo y fondo de cada lista.

---

## 📱 Descripción General

**Team Maravilla App** permite al usuario:

- Crear y personalizar listas de compra.  
- Añadir o eliminar productos fácilmente.  
- Filtrar categorías visibles.  
- Visualizar recetas con sus ingredientes.  
- Guardar recetas como favoritas.  
- Cambiar el estilo o vista de las listas.  
- Acceder a secciones de perfil, login y configuración.

> En esta versión, las pantallas son funcionales de forma independiente, sin navegación integrada.  
> Se han incluido todas las **vistas principales**, con componentes reutilizables y repositorios de datos simulados en memoria.

---

## 🧩 Estructura del Proyecto

```plaintext
com.example.teammaravillaapp
│
├── data/ → Repositorios en memoria ("Fake DB")
│   ├── FakeUserLists.kt
│   ├── FakeUserRecipes.kt
│   └── FakeUserPrefs.kt
│
├── model/ → Modelos de datos y enums
│   ├── Product.kt, ProductCategory.kt, ProductData.kt
│   ├── Recipe.kt, RecipeData.kt
│   ├── UserList.kt, ListBackgrounds.kt
│   ├── QuickActionData.kt, CardInfo.kt, SearchFieldData.kt
│
├── component/ → Componentes reutilizables de Compose
│   ├── ListCard, ProductBubble, RecipeCard
│   ├── QuickActionButton, BackButton, BottomBar, etc.
│
├── page/ → Pantallas principales de la app
│   ├── Home.kt
│   ├── CreateListt.kt
│   ├── ListDetail.kt
│   ├── Recipes.kt, RecipesDetail.kt
│   ├── Profile.kt, Login.kt
│   ├── CategoryFilter.kt, ListViewTypes.kt
│
├── ui/theme/ → Colores, tipografías y estilos (Compose Theme)
│
├── util/ → Constantes globales y utilidades
│   └── TAG_GLOBAL.kt
│
└── MainActivity.kt → Punto de entrada principal
```
---

## 🎨 Tecnologías Utilizadas

- **Kotlin**
- **Jetpack Compose (Material 3)**
- **Android Studio / Gradle KTS**
- **State management:** `remember`, `mutableStateOf`, `mutableStateListOf`
- **Diseño adaptable:** `LazyColumn`, `FlowRow`, `Surface`, `Scaffold`

---

## 🧠 Arquitectura Interna

El proyecto sigue una estructura **modular y organizada por capas**, inspirada en MVVM (sin ViewModel todavía):

- **Model** → Entidades puras con datos (inmutables).  
- **Data** → Repositorios en memoria (simulan base de datos).  
- **Component** → UI reutilizable y desacoplada.  
- **Page** → Pantallas completas que combinan componentes.  
- **Util** → Constantes y helpers globales.

---

## 🧾 Estado Actual

| Área | Estado | Descripción |
|------|--------|-------------|
| 🎨 Interfaz visual | ✅ Completa | Todas las pantallas diseñadas y funcionales. |
| 🗂️ Repositorios | ✅ Implementados | Datos en memoria para listas y recetas. |
| ⚙️ Lógica básica | ✅ Operativa | Añadir/eliminar productos, favoritos, filtros. |
| 🔄 Navegación | ⏳ Pendiente | A implementar con `NavHost` y rutas. |
| 💾 Persistencia real | ⏳ Pendiente | Reemplazar `FakeUser*` por Room o DataStore. |

---

## 🧑‍💻 Autores

**Desarrolladores:** Cristian R, Rolando O.
**Instituto:** IES Teis — 2º DAM  
**Asignatura:** Programación Multimedia y Dispositivos Móviles  

---

## 📸 Capturas (opcional)

> 

---

## 🚀 Futuras Mejoras

- Integrar **sistema de navegación Compose** (`NavHost`).  
- Añadir **persistencia local** (Room o DataStore).  
- Implementar **login funcional** y perfil con datos reales.  
- Sincronización opcional con backend remoto.

---

## 🧩 Licencia

Este proyecto se distribuye para fines educativos.  
Uso libre y modificación permitida bajo reconocimiento de autoría.

---

🟢 **Estado final:**  
> Proyecto estable, compilable y totalmente presentable para evaluación.  
> Cumple todos los criterios de diseño modular, documentación y funcionalidad básica.
