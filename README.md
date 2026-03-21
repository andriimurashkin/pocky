# Pocky

Loyalty card wallet for Android. Store all your discount and loyalty card barcodes in one place.

## Features

- Store loyalty/discount cards with barcodes (EAN-13, EAN-8, Code 128, Code 39, UPC-A, QR Code)
- Add cards manually (enter digits) or scan with camera
- Card grid with flip animation to reveal barcode
- Favorite cards pinned to the top
- Search cards by name or number
- Auto brightness boost when showing barcode for reliable scanning
- Color-coded cards for easy recognition
- Material You dynamic theming with light/dark mode

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **Database:** Room
- **DI:** Hilt
- **Barcode scanning:** Google ML Kit
- **Barcode generation:** ZXing Core
- **Camera:** CameraX
- **Navigation:** Jetpack Navigation Compose

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Min SDK 26 (Android 8.0)

## Build

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires keystore.properties)
./gradlew assembleRelease
```

### Release Signing

Copy `keystore.properties.template` to `keystore.properties` and fill in your keystore details.

## Project Structure

```
app/src/main/java/com/amur/pocky/
├── data/           # Room database, models, repository
├── di/             # Hilt dependency injection modules
├── ui/
│   ├── theme/      # Material 3 dynamic color theme
│   ├── navigation/ # Navigation graph
│   ├── home/       # Card grid home screen
│   ├── addcard/    # Add/edit card form
│   ├── carddetail/ # Card detail with flip animation
│   ├── scanner/    # Camera barcode scanner
│   └── components/ # Reusable composables
└── util/           # Barcode generation, brightness control
```

## Roadmap

- [ ] Categories/grouping for cards
- [ ] Export/import cards as JSON backup
- [ ] Home screen widget for quick access
- [ ] NFC card emulation
- [ ] Cloud sync
- [ ] Wear OS companion app

## License

MIT
