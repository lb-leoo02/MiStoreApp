# MiStoreApp - Challenge Técnico Mobile (AranguriApps)

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-2.4.20-blue.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.12.1-purple.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/Unit_Tests-7_Passed-success.svg)]()

Aplicación móvil de e-commerce multiplataforma (Android & iOS) desarrollada con Kotlin Multiplatform (KMP) y Compose Multiplatform, consumiendo datos en tiempo real de la API REST pública de FakeStore.

---

## Tabla de Contenidos
1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Stack Tecnológico y Librerías](#stack-tecnológico-y-librerías)
3. [Arquitectura del Software](#arquitectura-del-software)
4. [Características Principales](#características-principales)
5. [Orquestación de IA y Bitácora de Prompts](#orquestación-de-ia-y-bitácora-de-prompts)
6. [Pruebas Unitarias (Unit Testing)](#pruebas-unitarias-unit-testing)
7. [Cómo Compilar y Ejecutar el Proyecto](#cómo-compilar-y-ejecutar-el-proyecto)

---

## Descripción del Proyecto

MiStoreApp es una aplicación móvil completa orientada a e-commerce que permite explorar un catálogo de productos, buscar artículos en tiempo real, visualizar detalles con imágenes en alta resolución, gestionar un carrito de compras dinámico y simular el proceso de pago.

El proyecto está diseñado desde cero aplicando Clean Architecture y el patrón de diseño MVVM, garantizando un código desacoplado, testeable y extensible para Android e iOS compartiendo más del 95% del código base.

---

## Stack Tecnológico y Librerías

* **Lenguaje:** Kotlin 2.4.20 (Multiplatform)
* **UI Declarativa:** Compose Multiplatform (Material 3 con tema Dark Glassmorphism)
* **Cliente de Red (HTTP):** Ktor Client 3.6.0 (`ktor-client-core`, `ktor-client-okhttp`, `ktor-client-darwin`)
* **Serialización de Datos:** `kotlinx.serialization` (JSON parsing)
* **Carga de Imágenes Remotas:** Coil 3.1.0 (`coil-compose`, `coil-network-ktor3`)
* **Gestión de Estado y Ciclo de Vida:** Jetpack Lifecycle & ViewModel KMP (`androidx.lifecycle.ViewModel`, `StateFlow`)
* **Catálogo de Versiones:** Gradle Version Catalog (`libs.versions.toml`)

---

## Arquitectura del Software

El proyecto sigue la arquitectura recomendada por Google y JetBrains para Kotlin Multiplatform:

```text
shared/
 ├── src/commonMain/kotlin/com/bonfigli/storeapp/
 │    ├── data/          --> Capa de Red y Clientes HTTP (Ktor Client)
 │    ├── domain/        --> Modelos de Datos de Dominio (Product, CartItem)
 │    ├── presentation/  --> Lógica de Negocio y ViewModels (ProductListViewModel, CartViewModel, UI States)
 │    └── ui/            --> Vistas en Compose Multiplatform (ProductListScreen, ProductDetailScreen, CartScreen)
 ├── src/androidMain/    --> Implementaciones nativas de Android (BackHandler actual)
 └── src/iosMain/        --> Implementaciones nativas de iOS (ViewController launcher)
```

### Principios de Diseño Aplicados:
1. **MVVM (Model-View-ViewModel):** Las vistas no contienen lógica de red ni de negocio; únicamente observan `StateFlow` expuestos por los `ViewModel`.
2. **Gestión Determinista de Estados:** Uso de `sealed interface ProductUiState` (`Loading`, `Success`, `Error`) para manejar de forma segura los estados de carga y fallas de conexión.
3. **Patrón Expect / Actual:** Abstracción nativa de componentes del sistema operativo como el `BackHandler` para capturar el botón físico "Atrás" en Android sin romper la compilación de iOS.

---

## Características Principales

* **Catálogo de Productos en Grilla (2 Columnas):** Renderizado reactivo de productos con tarjetas de diseño moderno.
* **Búsqueda en Tiempo Real:** Filtrado instantáneo por nombre, marca o categoría sin recargar la red.
* **Carga Asíncrona de Fotos:** Integración de Coil 3 para descargar y almacenar en caché imágenes remotas.
* **Badges de Oferta y "NUEVO":** Identificación automática de productos recién llegados.
* **Detalle de Producto Extendido:** Muestra imagen ampliada, marca, precio, descripción completa y confirmación mediante `Snackbar`.
* **Carrito de Compras Interactivo:**
  * Contador con badge flotante en la TopAppBar.
  * Modificación de cantidades (+ / -) y eliminación de productos.
  * Cálculo dinámico del total acumulado.
  * Diálogo modal para simulación de compra y vaciado automático.
* **Diseño Dark Glassmorphism y Animaciones:** Paleta de colores en tonos oscuros con degradados y transiciones animadas entre pantallas (`AnimatedContent`).
* **Manejo de Errores y Tolerancia a Fallas:** Intercepción de problemas de red (sin internet o caída de servidor) con pantalla descriptiva y botón de "Reintentar".

---

## Orquestación de IA y Bitácora de Prompts

En cumplimiento con el requisito fundamental del challenge, el desarrollo acelerado de este proyecto se potenció utilizando herramientas de Inteligencia Artificial actuando como copilotos de ingeniería:

* **Google Gemini 3.6 Flash / Pro:** Modelo de lenguaje principal para diseño de arquitectura, refactorización y lógica de negocio.
* **Antigravity AI Agent:** Agente especializado para asistencia en pair programming, auditoría de compilación y pruebas unitarias.
* **Android Studio Gemini Bot:** Asistente integrado en el IDE para generación rápida de UI y configuración de Gradle.

### Auditoría Técnica del Ingeniero

El valor agregado como desarrollador radicó en guiar a la IA, auditar su código y resolver problemas de integración que los generadores ignoraron:
1. **Rechazo de Retrofit/Glide en KMP:** Ante sugerencias iniciales de usar librerías exclusivas de Android, se instruyó a la IA a utilizar alternativas 100% multiplataforma como Ktor Client y Coil 3.
2. **Solución del Botón Atrás (UX Android):** La IA generó un cambio de estado simple para la navegación que provocaba que la app se cerrara al presionar "Atrás" en Android. Audité el comportamiento y diseñé la abstracción `expect / actual BackHandler` para solucionar la navegación nativa.
3. **Formateo de Moneda:** Creación de la extensión `Double.formatPrice()` para asegurar dos decimales precisos sin librerías externas pesadas.

---

### Bitácora de Prompts Utilizados

#### Día 1: Setup, Repositorio y Cliente de Red (Ktor)
* **Prompt para Gradle KMP:**
  > "Estoy haciendo un proyecto en Kotlin Multiplatform con UI compartida en Compose. Necesito consumir la FakeStore API. ¿Me podés dar exactamente el bloque de código que tengo que agregar en mi archivo build.gradle.kts (dentro del sourceSets commonMain.dependencies) para incluir Ktor Client (core, content-negotiation y kotlinx-json) y Kotlinx Serialization? Dame solo las dependencias necesarias para KMP."
* **Prompt para Dominio (Product.kt):**
  > "Basado en este JSON de FakeStore: `[{"id":1,"title":"Remera","price":10.0,"category":"ropa","image":"url","rating":{"rate":4.5,"count":10}}]`, generame los 'data class' en Kotlin Multiplatform usando `@Serializable`. Ponelos en un archivo llamado Product.kt."
* **Prompt para Cliente HTTP (FakeStoreHttpClient.kt):**
  > "Ahora generame un FakeStoreHttpClient usando Ktor Client en Kotlin Multiplatform. Necesito que tenga un método suspendido `getProducts(): List<Product>` que le pegue a `https://fakestoreapi.com/products`. Asegurate de configurar el plugin de ContentNegotiation con JSON e ignorar las keys desconocidas (`ignoreUnknownKeys = true`). Incluí manejo de errores básico con try-catch."

#### Día 2: Arquitectura MVVM, Gestión de Estado y Unit Tests
* **Prompt para Estados de UI (ProductUiState.kt):**
  > "En Kotlin Multiplatform, necesito definir una sealed interface llamada `ProductUiState` para representar los estados de la pantalla del catálogo de productos según la arquitectura MVVM: `Loading`, `Success(products: List<Product>)` y `Error(message: String)`. Usá `data object` para el estado de carga."
* **Prompt para ViewModel (ProductListViewModel.kt):**
  > "Generame un `ProductListViewModel` en Kotlin Multiplatform que herede de `androidx.lifecycle.ViewModel`. Debe exponer un `StateFlow<ProductUiState>` reactivo y consumir `FakeStoreHttpClient`. En la función `loadProducts()`, usá `viewModelScope.launch` para actualizar los estados. Agregá un try-catch que clasifique los errores de red (Socket, Host, Conexión) y devuelva un mensaje comprensible como 'Sin conexión a Internet'."
* **Prompt para la Vista Principal (App.kt):**
  > "Actualizá mi componente `@Composable fun App()` en Compose Multiplatform para que consuma el `ProductListViewModel` usando `collectAsState()`. Diseñá una UI con Material 3 que use Scaffold y TopAppBar. Si el estado es Loading, mostrá un `CircularProgressIndicator`. Si es Success, renderizá una `LazyColumn` con tarjetas (`Card`) para cada producto. Si es Error, mostrá un mensaje de error con un botón de 'Reintentar' que vuelva a llamar al ViewModel."
* **Prompt para Pruebas Unitarias (ProductSerializationTest.kt):**
  > "Escribí un test unitario para Kotlin Multiplatform en la carpeta `commonTest` usando `kotlin.test`. El test debe llamarse `ProductSerializationTest` y debe verificar que una cadena JSON de muestra de la API FakeStore se deserialice correctamente en el modelo `ProductResponse` utilizando `kotlinx.serialization`."

#### Día 3: Navegación, Carga de Imágenes y Filtro en Tiempo Real
* **Prompt para Coil 3 y Navegación:**
  > "Estoy desarrollando un proyecto en Kotlin Multiplatform (KMP) con Compose Multiplatform. Actualmente tengo un ProductListViewModel y la data class Product que tiene un campo `image: String?` con URLs de imágenes. Necesito implementar dos funcionalidades: 1. Configurar la librería Coil 3 (`io.coil-kt.coil3`) para Compose Multiplatform en `libs.versions.toml` y `shared/build.gradle.kts` para poder renderizar imágenes desde URLs de la red en commonMain usando `AsyncImage`. 2. Crear un sistema de navegación sencillo en Compose Multiplatform con 2 pantallas: Catálogo (`ProductListScreen`) y Detalle del Producto (`ProductDetailScreen`)."

#### Día 4: Carrito de Compras, Tests Adicionales y Rediseño Dark Glassmorphism
* **Prompt para Carrito y Tests:**
  > "Actualmente tengo 2 pantallas funcionando (Catálogo con buscador e imágenes Coil 3, y Detalle del Producto). Necesito implementar la Pantalla 3: Carrito de Compras y sus tests: 1. Gestión del Carrito (`CartViewModel`): Administrar productos agregados (producto + cantidad), incrementar, decrementar, eliminar y calcular precio total. 2. UI y Navegación (`Screen.Cart`): Ícono de carrito con badge contador en la TopAppBar y `CartScreen.kt` con controles de cantidad y diálogo modal de compra. 3. Pruebas Unitarias para el Carrito en `commonTest` (`CartTest.kt`)."

---

## Pruebas Unitarias (Unit Testing)

Se incluyó una suite completa de pruebas unitarias ejecutables tanto en la máquina local como en pipelines de CI/CD dentro de `shared/src/commonTest/kotlin`:

* **`ProductSerializationTest`**: Valida que la respuesta JSON de la API FakeStore se deserialice correctamente en los modelos `@Serializable` `ProductResponse` y `Product`.
* **`CartTest`**: Valida la lógica del `CartViewModel`:
  * Adición de ítems y actualización de cantidad para productos duplicados.
  * Incremento, decremento y eliminación automática cuando la cantidad llega a 0.
  * Cálculo exacto del precio total acumulado y vaciado del carrito.

### Comprobación de Pruebas:
Todas las pruebas pasaron con éxito total (7 Passed, 0 Failed).

---

## Cómo Compilar y Ejecutar el Proyecto

### Requisitos Previos:
* Android Studio Ladybug / Iguana o superior (con plugins de Kotlin Multiplatform activados).
* JDK 17 o superior.
* Conexión a Internet (para descarga de dependencias y llamadas a la API).

### Comandos de Compilación:

#### Compilar e Instalar la App en Android:
```bash
# Compilar versión Debug
./gradlew :androidApp:assembleDebug

# Compilar e Instalar versión optimizada Release en teléfono físico o emulador
./gradlew :androidApp:installRelease
```

#### Ejecutar la Suite de Pruebas Unitarias:
```bash
./gradlew :shared:testAndroidHostTest
```

---

Desarrollado en Kotlin Multiplatform para AranguriApps.