# Loyalty Points Service – How to Build and Run Tests

This project shows a small **Loyalty Points Quote Service** API built with Vert.x and tested with **Java (JUnit)**, and outlines how you would run **Android Espresso** and **iOS XCUITest (XCUI)** tests.

***

## 1. Prerequisites

Before you start, please make sure you have:

- **Java** 17 or later  
- **Maven** 3.8 or later  
- **Git**

For the mobile sections (optional, if you want to run UI tests):

- **Android Studio** + Android SDK (for Espresso tests)  
- **Xcode** + iOS Simulator (for XCUITest)

***

## 2. Build the project and run API tests (Maven)

All Java API tests are run with Maven.

### 2.1 Clean and build

```bash
mvn clean install
```

This command:

- Cleans previous builds.  
- Compiles the code.  
- Runs all tests (including `LoyaltyPointsServiceAlternativeTest`).

### 2.2 Run tests only

```bash
mvn test
```

This:

- Starts the Vert.x HTTP server in test mode.  
- Sends real HTTP requests to `POST /v1/points/quote` using Vert.x WebClient.  
- Verifies:
  - HTTP status codes  
  - JSON response fields (basePoints, tierBonus, promoBonus, totalPoints, effectiveFxRate)  
  - Validation errors (e.g. missing `fareAmount`)  

***

## 3. Run the API service locally

You can run the Loyalty Points service as a standalone HTTP API.

### 3.1 Package the project

```bash
mvn clean package
```

### 3.2 Start the service

```bash
java -cp target/*:. com.emirates.loyalty.LoyaltyPointsService
```

By default the service listens on:

- **URL:** `http://localhost:8080/v1/points/quote`  
- **Method:** `POST`  
- **Content-Type:** `application/json`

### 3.3 Example request payload

```json
{
  "fareAmount": 1234.50,
  "currency": "USD",
  "cabinClass": "ECONOMY",
  "customerTier": "SILVER",
  "promoCode": "SUMMER25"
}
```

You can send this using any REST client (Postman, curl, etc.) and you should receive a JSON response with calculated loyalty points.

***

## 4. Android UI tests – Espresso (outline)

> This section explains how Android UI tests would be run, assuming there is an Android app module with Espresso tests.

### 4.1 Typical project layout

- Android app module, for example:  
  `android-app/app/`
- Espresso test classes under:  
  `android-app/app/src/androidTest/java/...`

An example test file might be: `LoginEspressoTest.kt`.

### 4.2 Run Espresso tests from Android Studio

1. Open the Android module (e.g. `android-app/`) in **Android Studio**.  
2. Connect a device or start an Android Emulator.  
3. In the Project view, right‑click the test class (for example `LoginEspressoTest`) and choose **Run**.  

Android Studio will:

- Build the app.  
- Install it on the device/emulator.  
- Run all Espresso tests in the `androidTest` package.

### 4.3 Run Espresso tests from the command line

From the Android module root (where `gradlew` is located):

```bash
./gradlew connectedAndroidTest
```

This will:

- Build the app in debug mode.  
- Deploy it to the connected device/emulator.  
- Run all instrumented tests (Espresso).

***

## 5. iOS UI tests – XCUITest (XCUI) (outline)

> This section explains how iOS UI tests would be run, assuming there is an Xcode project with a UITest target.

### 5.1 Typical project layout

- Xcode project: `ios-app/YourApp.xcodeproj`  
- UI test target: `YourAppUITests`  
- Test file example: `YourAppUITests/LoginUITests.swift`

### 5.2 Run XCUITests from Xcode

1. Open the Xcode project (for example `ios-app/YourApp.xcodeproj`) in **Xcode**.  
2. In the scheme selector, choose the **UITests** scheme (e.g. `YourAppUITests`).  
3. Choose an iOS Simulator (for example, iPhone 15).  
4. Press **⌘ + U** (Command + U) or go to **Product → Test**.

Xcode will:

- Build the main app and the UITest target.  
- Launch the simulator.  
- Run all XCUITest UI tests.

### 5.3 Run XCUITests from the command line

From the `ios-app` directory:

```bash
xcodebuild \
  -scheme YourAppUITests \
  -destination 'platform=iOS Simulator,name=iPhone 15,OS=latest' \
  test
```

This does the same as running tests from within Xcode, but via the terminal (useful for CI/CD pipelines).

***

## 6. Summary

- Use **`mvn test`** to run Java API/component tests for the Loyalty Points service.  
- Use **`java -cp ... LoyaltyPointsService`** to start the API locally on port 8080.  
- Use **Android Studio / `./gradlew connectedAndroidTest`** to run Espresso tests on Android.  
- Use **Xcode / `xcodebuild ... test`** to run XCUITest UI tests on iOS.