<h1 align="center">🗺️ App_Map - Android Map & Navigation Application</h1>

<p align="center">  
App_Map is a modern Android application for location tracking, place searching, and route navigation. It leverages the latest Android development practices including <strong>Jetpack Compose</strong>, <strong>MVVM architecture</strong>, <strong>Hilt for DI</strong>, <strong>Google Maps SDK</strong>, <strong>Coroutines</strong>, and <strong>Google Directions & Places APIs</strong> to deliver an interactive and real-time mapping experience.
</p>




## 🚀 Features

- 📍 **Current Location Detection**: Automatically detects and displays the user's real-time location using GPS and network providers.
- 🔎 **Place Search & Autocomplete**: Search for places with real-time suggestions using the Google Places API.
- 🗺️ **Interactive Map UI**: Smooth, zoomable Google Map view integrated with Jetpack Compose Maps.
- 🧭 **Turn-by-Turn Directions**: Get directions for driving, walking, or two-wheelers using the Google Directions API with polyline rendering on the map.
- 🧩 **Multi-Transport Mode**: Easily switch between transportation types (Driving, Walking, Two-wheeler) for route planning.
- 📌 **Place Details View**: Tap on a place to view detailed information (name, address, coordinates).
- 🔄 **Live Route Updates**: Updates route in real-time as the current location changes.



## 🧱 Architecture

The app follows **Clean MVVM architecture**, ensuring testability and separation of concerns.

```
Data (Retrofit, PagingSource)
↑
Repository (ResultWrapper, Error Handling)
↑
ViewModel (StateFlow, UI State)
↑
UI (Jetpack Compose)
```


## Tech stack & Open-source libraries

| Layer          | Technology                                                                    |
| -------------- | ----------------------------------------------------------------------------- |
| UI             | Jetpack Compose, Material 3                                                   |
| State Mgmt     | ViewModel, StateFlow                                                          |
| DI             | Hilt                                                                          |
| Networking     | Retrofit, OkHttp                                                              |
| Google Map SDK | Google Maps SDK for Android, Maps Compose Library, Directions API, Places API |
| Async          | Kotlin Coroutines                                                             |


- Minimum SDK level 24.
- [Kotlin](https://kotlinlang.org/) based, utilizing [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- Jetpack Libraries:
    - Jetpack Compose: Android’s modern toolkit for declarative UI development.
    - Lifecycle: Observes Android lifecycles and manages UI states upon lifecycle changes.
    - ViewModel: Manages UI-related data and is lifecycle-aware, ensuring data survival through configuration changes.
    - Material 3: Modern Material Design UI components.
    - SharedPreferences: Used to locally store small pieces of data such as the user's last search query or app settings. Ideal for simple key-value data without the need for a full database.
    - [Hilt](https://dagger.dev/hilt/): Facilitates dependency injection.
- Architecture:
    - MVVM Architecture (View - ViewModel - Model): Facilitates separation of concerns and promotes maintainability.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Constructs REST APIs and facilitates paging network data retrieval.
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization): Kotlin multiplatform / multi-format reflectionless serialization.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API for code generation and analysis.
- Maps Compose Library: Integrates Google Maps with Jetpack Compose UI.
- Directions API: Fetches optimized routes for driving, walking, or two-wheelers; used for drawing polylines on the map.
- Places API: Supports autocomplete suggestions, place details, and location search.
- FusedLocationProviderClient: Provides the most accurate and battery-efficient way to obtain the user’s current location via GPS, WiFi, or mobile networks.

## 🏗️ Project Modules

- **data**: Remote data source, API service, DTOs.
- **domain** *(optional)*: Business logic (can be added for larger apps).
- **presentation**: ViewModels and Compose UI.
- **di**: Hilt module providers.
- **util**: Helper functions, ResultWrapper for error handling.

## 🚀 How to Run the App
1. Clone this repository:
   ```bash
   git clone https://github.com/TraVi-1801/App_Maps.git
   ```
2. Open the project in Android Studio
3. Add your API key to local.properties
- Create or open the local.properties file in the root of your project and add:
```properties
API_KEY = your_api_key_here
```
4. Sync Gradle to download all necessary dependencies.
5. Build and run the app on an emulator or a physical device running Android 8.0 (API level 26) or higher.

