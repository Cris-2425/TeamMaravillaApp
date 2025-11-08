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
**Asignatura:** Programación Multimedia y Dispositivos Móviles  

---

## 📸 Capturas de Pantallas

### 🏠 Pantalla de Inicio (Home)
Permite acceder a las listas recientes, crear nuevas listas y consultar favoritos o historial.
![Home](imagenes/home.png)

---

### 🛍️ Crear Lista (CreateListt)
Pantalla para crear una nueva lista de compra, elegir su fondo y usar plantillas sugeridas.
![CreateListt](imagenes/create_list.png)

---

### 📋 Detalle de Lista (ListDetail)
Visualiza y gestiona los productos de una lista concreta.  
Permite añadir o quitar elementos fácilmente.
![ListDetail](imagenes/list_detail.png)

---

### 🧮 Filtrar Categorías (CategoryFilter)
Permite activar o desactivar categorías de productos visibles dentro de la app.
![CategoryFilter](imagenes/category_filter.png)

---

### 🎨 Estilo de Vista (ListViewTypes)
Selecciona entre diferentes estilos de presentación: lista, mosaico u otros.
![ListViewTypes](imagenes/list_view_types.png)

---

### 🍳 Recetas (Recipes)
Explora recetas disponibles y marca tus favoritas.  
Muestra imagen e ingredientes de cada una.
![Recipes](imagenes/recipes.png)

---

### 🍽️ Detalle de Receta (RecipesDetail)
Visualiza una receta concreta con todos sus ingredientes listados de forma visual.
![RecipesDetail](imagenes/recipes_detail.png)

---

### 👤 Perfil (Profile)
Pantalla de usuario con opciones de configuración o accesos rápidos personales.
![Profile](imagenes/profile.png)

---

### 🔐 Login / Registro (Login)
Formulario para introducir usuario y contraseña.  
Interfaz simple y coherente con el resto del diseño.
![Login](imagenes/login.png)

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
