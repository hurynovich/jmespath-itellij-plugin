<!-- Plugin logo / Header -->
<p align="center">
  <img src="src/main/resources/META-INF/pluginIcon.svg" width="80" height="80" alt="Plugin Icon" />
</p>

# JMESPath Highlighter for IntelliJ Platform

[![Version](https://img.shields.io/badge/version-1.0.0--SNAPSHOT-blue.svg)](gradle.properties)
[![Compatibility](https://img.shields.io/badge/IntelliJ%20Platform-2025.3%2B-brightgreen.svg)](build.gradle.kts)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

A high-performance IntelliJ Platform plugin that provides syntax highlighting and tooling support for the **JMESPath** query language.

---

## Features

- 🎨 **Syntax Highlighting:** Full syntax highlighting for JMESPath expressions, identifiers, functions, literals, filters, and operators.
- 🎛️ **Color Scheme Customization:** Easily adjust colors and fonts for all JMESPath syntax elements to match your preferred theme.
- 🛠️ **Dedicated Tool Window:** Quick access to JMESPath tools and interactive utilities directly within the IDE.
- ⚡ **Cross-IDE Compatibility:** Seamlessly integrates across JetBrains IDEs (IntelliJ IDEA, PyCharm, WebStorm, GoLand, etc.).

---

## Visual Demo

> *Placeholder: Add screenshots or animated GIFs demonstrating JMESPath highlighting and the tool window in action.*
<!-- ![JMESPath Highlighting Demo](docs/assets/demo.png) -->

---

## Installation

### From JetBrains Marketplace
1. Open your IDE and navigate to **Settings** (or **Preferences** on macOS) | **Plugins**.
2. Select the **Marketplace** tab.
3. Search for **Demo-highlight-plugin** or **JMESPath Highlighter**.
4. Click **Install** and restart the IDE if prompted.

### Manual Installation (.zip)
1. Download the latest plugin release `.zip` from the releases page or build it locally.
2. In the IDE, go to **Settings** (or **Preferences**) | **Plugins**.
3. Click the gear icon (**⚙️**) in the top right corner and select **Install Plugin from Disk...**.
4. Choose the downloaded `.zip` file and restart the IDE.

---

## Usage & Settings

### Customizing Colors
To configure custom syntax colors for JMESPath:
1. Open **Settings** (or **Preferences**) | **Editor** | **Color Scheme**.
2. Select **JMESPath** (or general language highlights) to customize keywords, identifiers, strings, and operators.

### Accessing the Tool Window
Open the plugin's dedicated tool window via:
- **View | Tool Windows | My Tool Window**
- Or click the **My Tool Window** icon in the IDE tool window stripe.

---

## Compatibility

- **Supported IDEs:** Compatible with IntelliJ Platform products including:
  - IntelliJ IDEA (Ultimate & Community)
  - PyCharm (Professional & Community)
  - WebStorm
  - GoLand
  - CLion
  - PhpStorm
  - Rider
  - DataGrip
- **Minimum IDE Version:** `2025.3` or higher

---

## Building from Source

This project uses Gradle with the IntelliJ Platform Gradle Plugin (`org.jetbrains.intellij.platform`).

### Prerequisites
- JDK 21+

### Useful Gradle Tasks
- **Run sandbox IDE with plugin installed:**
  ```bash
  ./gradlew runIde
  ```
- **Execute unit tests:**
  ```bash
  ./gradlew test
  ```
- **Build plugin distribution archive:**
  ```bash
  ./gradlew buildPlugin
  ```
  The packaged distribution will be located in `build/distributions/`.

---

## Contributing & Issues

Contributions, suggestions, and bug reports are welcome!
- **Issues & Feature Requests:** Please open an issue in the issue tracker.
- **Pull Requests:** Fork the repository, create a feature branch, and submit a PR with clear description and tests.

---

## License

This project is licensed under the [Apache License 2.0](LICENSE).
