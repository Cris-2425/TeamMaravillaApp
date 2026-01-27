# TeamMaravillaApp — Notas internas (apuntes de implementación)

> Documento de apoyo para mantener la estructura del proyecto clara:
> qué existe, por qué existe y cómo se usa (Hilt, Room, Retrofit, DataStore, Coil, estados, flows, navegación, sesión, etc.).

---

## 0) Mapa mental del proyecto (visión rápida)

**Arquitectura (MVVM + Repository + Data sources)**

UI (Compose)
-> ViewModel (StateFlow / SharedFlow eventos)
-> Repository (reglas, cache, errores)
-> Local (Room: Entity/DAO/DB/Migrations)
-> Remote (Retrofit: API/DTO/Mappers)
-> Prefs (DataStore: sesión/flags)

**Regla:** UI no llama a DAO ni a Retrofit directamente.

---

## 1) Dependencias y “qué aporta cada una”

- **Hilt**: inyectar repos, DAOs, Retrofit, DataStore, etc.
- **Room**: base de datos local (listas, recetas, productos guardados…).
- **Retrofit**: consumir API (catálogo de productos, etc.).
- **DataStore**: sesión y settings simples (loggedIn, token, user…).
- **Coil**: carga de imágenes (url/drawable) en Compose.

---

## 2) Hilt (inyección de dependencias)

### 2.1 Piezas mínimas
- `Application` con `@HiltAndroidApp`
- `MainActivity` con `@AndroidEntryPoint`
- ViewModels: `@HiltViewModel` + `@Inject constructor(...)`
- Módulos (`@Module`):
    - `DatabaseModule` (Room)
    - `NetworkModule` (Retrofit/OkHttp)
    - `RepositoryModule` (bind repos)

### 2.2 Patrón de módulos (guía)
- **@Provides** para objetos “construidos” (Retrofit, OkHttp, Room DB, DAOs).
- **@Binds** para interfaces -> implementación (`ProductRepository -> DefaultProductRepository`).

### 2.3 Errores típicos (cuando explota)
- `Cannot create an instance of ...ViewModel`
    - Falta `@AndroidEntryPoint` en Activity
    - ViewModel sin `@HiltViewModel`
    - Dependencia del constructor no está provista en módulos

---

## 3) Room (DB local)

### 3.1 Piezas
- `Entity`: tablas (`RecipeEntity`, `ListEntity`, `ProductEntity`…)
- `DAO`: queries
- `AppDatabase`: `@Database(entities=[...], version = X)`
- `Migrations`: cuando cambias schema

### 3.2 Relación N:M (recetas-ingredientes)
- Tabla cruce `RecipeIngredientCrossRef(recipeId, productId)`
- Queries tipo `@Transaction` con `@Relation` si usas POJOs de relación.

### 3.3 Reglas prácticas
- Lecturas reactivas: `Flow<List<...>>`
- Escrituras: `suspend` (y se hacen desde repo o VM con `viewModelScope.launch`)

### 3.4 Migraciones
- Si subes versión, añade `Migration(old, new)` al builder.
- Si es demo y no importa perder datos: `fallbackToDestructiveMigration()` (solo si lo aceptas).

---

## 4) Retrofit (API remota)

### 4.1 Piezas
- `ApiService` (endpoints)
- DTOs (`ProductDto`)
- Mapper DTO -> domain (`Product`)
- Repo: `getProducts()`, `searchProducts()`, etc.

### 4.2 Regla de oro
Retrofit solo lo toca el **Repository** (o un RemoteDataSource), nunca la UI.

### 4.3 Errores típicos
- “No carga nada”: revisar baseUrl, internet permission, parsing JSON (DTOs).
- Manejar errores: envolver en `Result`/`sealed class`.

---

## 5) DataStore (sesión / preferencias)

### 5.1 Qué guardar aquí
- `isLoggedIn`
- `userId` / `email`
- `token` (si existiese)
- flags UI (onboarding, tema…)

### 5.2 Patrón recomendado (SessionStore)
- `val sessionFlow: Flow<Session>`
- `suspend fun saveSession(...)`
- `suspend fun clearSession()`

### 5.3 Consumo
- Un `AppViewModel` o `SessionViewModel` observa `sessionFlow`
- Decide navegación: login vs home

---

## 6) Coil (imágenes en Compose)

### 6.1 Uso típico
- `AsyncImage(model = url, contentDescription = ...)`
- `contentScale = ContentScale.Crop` para recortar.
- Define tamaños para evitar “saltos” de layout.

---

## 7) ViewModels, estado UI y eventos

### 7.1 UiState (estado)
Modelo recomendado:
- `data class UiState(...)`
- Exponer: `val uiState: StateFlow<UiState>`

UI:
- `val state by vm.uiState.collectAsState()`

### 7.2 Eventos one-shot (snackbar, navegación)
- `MutableSharedFlow<UiEvent>` (sin replay)
- Compose recoge con `LaunchedEffect(Unit) { vm.events.collect { ... } }`

Ejemplos de evento:
- `UiEvent.Navigate("route")`
- `UiEvent.ShowSnackbar("...")`

---

## 8) Coroutines + Flow (scopes y patrones)

### 8.1 Dónde lanzar cosas
- ViewModel: `viewModelScope.launch { ... }`
- Repo: `withContext(Dispatchers.IO)` cuando haga falta (si no devuelve Flow).

### 8.2 Patrones útiles
- `combine()` para mezclar flows (p.ej. filtros + lista).
- `stateIn()` para convertir Flow a StateFlow.

### 8.3 Errores típicos
- Coleccionar flow en Compose sin `LaunchedEffect` -> duplicidades por recomposición.
- Meter `.launch` en Composables sin control.

---

## 9) Repositories (contratos claros)

Cada feature con su repo:
- `ProductRepository`
- `ListsRepository`
- `RecipesRepository`
- `SessionRepository/Store` (DataStore)

Responsabilidades:
- Unificar fuentes (Room/Retrofit)
- Mappers (Entity/DTO <-> Domain)
- Errores (Result/UiState)

---

## 10) Modelos: DTO vs Entity vs Domain

- DTO: lo que viene de API
- Entity: lo que guardas en Room
- Domain: lo que usa la app

Recomendación: tener mappers explícitos:
- `dto.toDomain()`
- `entity.toDomain()`
- `domain.toEntity()`

---

## 11) Navegación + sesión

### 11.1 Navegación
- Rutas: `login`, `home`, `listDetail/{id}`, `recipeDetail/{id}`…
- Navegar por eventos desde VM o directamente desde UI (elige 1 estilo y manténlo).

### 11.2 Sesión
- Si `isLoggedIn = false` -> login
- Si `true` -> home
- Logout: `clearSession()` + navegar a login

---

## 12) Seed de datos (si lo usas)

Objetivo:
- Insertar datos iniciales si DB está vacía.

Dónde:
- Mejor en repos (ej. `recipesRepository.seedIfEmpty()`), llamado desde un VM “app-level”.

Precaución:
- No sobrecargar init con mil llamadas sin control.
- Si hay remote + local, decide si seed es solo local o solo demo.

---

## 13) Checklist de “añadir una feature” (rápido)

### Nueva tabla Room
- [ ] Entity
- [ ] DAO
- [ ] DB version + Migration
- [ ] Repo methods
- [ ] ViewModel UiState + events
- [ ] UI

### Nuevo endpoint Retrofit
- [ ] DTO
- [ ] ApiService
- [ ] Mapper
- [ ] Repo wrapper (errores)
- [ ] ViewModel
- [ ] UI

---

## 14) Notas “anti-crash” (cosas que ya nos pasaron)
- Si crashea al crear un ViewModel: revisar Hilt annotations y módulos.
- Si Compose hace cosas raras: evitar colecciones repetidas por recomposición.
- Si seed da problemas: ejecutarlo una vez y comprobar “empty” correctamente.

---

## 15) TODO de documentación (para completar)
- [ ] Pegar árbol real de paquetes del proyecto
- [ ] Listar clases reales: ViewModels, repos, DAOs, Entities
- [ ] Añadir mini-snippets exactos (1 por tecnología)
