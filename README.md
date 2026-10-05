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
5. [Orquestación de IA y Criterio de Ingeniería](#orquestación-de-ia-y-criterio-de-ingeniería)
6. [Pruebas Unitarias (Unit Testing)](#pruebas-unitarias-unit-testing)
7. [Cómo Compilar y Ejecutar el Proyecto](#cómo-compilar-y-ejecutar-el-proyecto)

---

## Descripción del Proyecto

MiStoreApp es una aplicación móvil completa orientada a e-commerce que permite explorar un catálogo de productos, buscar artículos en tiempo real, visualizar detalles con imágenes en alta resolución, gestionar un carrito de compras dinámico y simular el proceso de pago.

El proyecto está diseñado desde cero aplicando Clean Architecture y el patrón de diseño MVVM, garantizando un código desacoplado, testeable y extensible para Android e iOS compartiendo más del 95% del código base.

---

## Stack Tecnológico y Librerías

* **Lenguaje:** Kotlin 2.4.20 (Multiplatform)
* **UI Declarativa:** Compose Multiplatform (Material 3)
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
* **Manejo de Errores y Tolerancia a Fallas:** Intercepción de problemas de red (sin internet o caída de servidor) con pantalla descriptiva y botón de "Reintentar".

---

## Orquestación de IA y Criterio de Ingeniería

En cumplimiento con las consignas del challenge, el desarrollo acelerado de este proyecto se potenció utilizando asistentes de Inteligencia Artificial (Gemini en Android Studio & Antigravity) actuando como copilotos.

### Auditoría Técnica y Decisiones tomadas sobre las sugerencias de la IA:

1. **Rechazo de Retrofit en KMP:** Ante sugerencias iniciales de usar librerías exclusivas de Android (como Retrofit o Glide), se instruyó a la IA a utilizar alternativas 100% multiplataforma como Ktor Client y Coil 3.
2. **Solución del Botón Atrás (UX Android):** La IA generó un cambio de estado simple para la navegación que provocaba que la app se cerrara al presionar "Atrás" en Android. Audité el comportamiento y diseñé la abstracción `expect / actual BackHandler` para solucionar la navegación nativa.
3. **Formateo de Moneda sin librerías pesadas:** En lugar de agregar librerías de terceros para formatear precios, creé una extensión personalizada `Double.formatPrice()` para asegurar dos decimales precisos.

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