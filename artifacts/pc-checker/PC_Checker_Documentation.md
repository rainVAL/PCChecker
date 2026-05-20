# PC Checker - Technical Documentation & Summary Report

## 1. Executive Summary
**PC Checker** is a professional-grade Android application designed to assist users in building, optimizing, and procuring high-performance personal computers. It bridges the gap between digital planning and physical hardware acquisition by combining real-time compatibility logic with live location-based store discovery.

## 2. Core Features

### 2.1 Intelligent PC Builder
* **Dynamic Slot System**: Users can select components across seven major categories: CPU, GPU, RAM, Motherboard, Storage, PSU, and Case.
* **Real-time Validation**: As parts are added, the app instantly validates technical compatibility (e.g., socket types, memory standards, physical clearance).
* **Automated Sync Meter**: Visualizes the "Performance Balance" of the current build, helping users avoid bottlenecks.

### 2.2 Built-in Knowledge Base
* **Extra-Detailed Descriptions**: Every component in the catalog features deep technical insights, architectural details, and specific use-case advice.
* **Expert Pairing Logic**: Provides "Pro-Tips" in the detail view, suggesting complementary hardware based on the selected part's performance tier.

### 2.3 Proximity-Based Shop Locator
* **Google Maps Integration**: Live map view centered on the user's current GPS coordinates.
* **Dynamic Geofencing**: Users can adjust a search radius (1km to 20km) via a slider to cover specific neighborhoods or entire towns.
* **Verified Places Data**: Utilizes the Google Places API to fetch real, active computer and electronics retailers within the defined perimeter.

### 2.4 Build Management
* **Room Database Integration**: A high-performance, 100% offline local database for permanent storage of custom PC configurations.
* **Gallery View**: A dedicated library to preview, edit, or delete previously saved builds.

## 3. Technical Architecture

### 3.1 Frontend (UI/UX)
* **Design Language**: Modern "Tailwind-inspired" Indigo and Slate palette.
* **Key Components**:
    * `CollapsingToolbarLayout` with parallax image effects.
    * Floating Action Buttons (FAB) for primary workflows.
    * Circular status badges for instant technical feedback.
    * Responsive `ConstraintLayout` for multi-device support.

### 3.2 Backend & Data
* **Database (Room)**: Uses SQLite via the Room Persistence Library for robust, offline-first data management.
* **JSON Processing**: Integrated `Gson` for complex object-to-string conversions within the database layer.
* **Image Loading**: Leverages the `Glide` library for optimized, high-speed loading of local and remote hardware assets.
* **API Integration**: Powered by Google Maps SDK and Google Places SDK for Android.

## 4. Design Choices
* **User-Centric Navigation**: Implemented a standardized Bottom Navigation Bar for rapid switching between core app modules.
* **Accessibility**: Full utilization of `strings.xml` for complete localization capability and improved screen-reader support.
* **Branding**: Custom-designed tech-motif vector logo and unified iconography.

## 5. Deployment Information
* **Minimum SDK**: 24 (Android 7.0 Nougat)
* **Target SDK**: 34 (Android 14)
* **Build System**: Gradle 8.5
* **Repository**: [PCChecker on GitHub](https://github.com/rainVAL/PCChecker.git)

---
*Report Generated: May 2026*
