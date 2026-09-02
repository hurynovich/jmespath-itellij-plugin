---
name: generate-intellij-plugin-readme
description: Interactively analyze conversation context and IntelliJ Platform plugin repository to generate or update a comprehensive, standardized README.md file.
---

# Generate IntelliJ Plugin README.md

## Objective
Generate or update a clean, professional, and standard `../../../README.md` tailored for IntelliJ Platform plugins using conversation context, interactive user clarification, and codebase inspection.

## Interactive Workflow & Decision Logic

When invoked, the agent must execute the following sequential steps:

### Step 1: Conversation Context Assessment
- Evaluate the ongoing conversation context to check if sufficient plugin details are already provided (e.g., plugin name, key features, target audience, usage instructions, configurations, repository links).
- Determine what information is missing (e.g., specific features, settings paths, license, demo assets, supported IDE versions).

### Step 2: Interactive Clarification or Codebase Inspection Prompt
If the conversation context does **not** contain sufficient details:
1. **Interactive Prompting:** Ask the user targeted questions to fill in missing details (e.g., plugin purpose, key user-facing features, screenshots/GIFs, specific configuration requirements).
2. **Codebase Inspection Option:** Offer / ask permission to automatically inspect repository files (`plugin.xml`, `../../../build.gradle.kts`, `../../../gradle.properties`, source code) to discover and extract missing information.

### Step 3: Codebase Analysis (When Available / Approved)
Inspect the following project sources to extract or verify details:
- `../../../src/main/resources/META-INF/plugin.xml` (extracts `<id>`, `<name>`, `<description>`, `<change-notes>`, `<vendor>`, extensions, actions).
- `../../../build.gradle.kts` / `../../../gradle.properties` / `libs.versions.toml` (extracts version, target platform/IDE version, dependencies).
- Core feature source files (to identify key functionality, tool windows, actions, languages, or file types supported).
- `LICENSE` file for licensing details.

### Step 4: README Generation
Synthesize conversation context and codebase findings into the standardized IntelliJ plugin `../../../README.md` structure.

---

## Standard README Structure

Generate the following standard sections in order:

1. **Header & Badges:** Title, icon/logo (if available), badges (version, downloads, rating, license), and a concise one-line elevator pitch.
2. **Features List:** Clear bullet points highlighting primary capabilities (e.g. syntax highlighting, tool windows, code inspections, completions, intentions, custom actions).
3. **Visual Demo Placeholder:** Reference screenshots or demo GIFs if present under `doc/` or `assets/` (or ask user for URLs).
4. **Installation:**
   - Standard JetBrains Marketplace installation steps (**Settings | Plugins | Marketplace**).
   - Manual `.zip` installation flow (**Settings | Plugins | ⚙️ | Install Plugin from Disk...**).
5. **Usage / Keymaps / Settings:** Step-by-step navigation paths to access plugin settings, tool windows, actions, or color scheme configurations (e.g., **Settings | Editor | Color Scheme**, **View | Tool Windows**).
6. **Compatibility:** Supported IDE list (IntelliJ IDEA, PyCharm, WebStorm, etc.) and version requirements.
7. **Building from Source:** Standard Gradle commands using the wrapper (`./gradlew runIde`, `./gradlew test`, `./gradlew buildPlugin`).
8. **Contributing & Issues:** Contribution guidelines and issue tracker links.
9. **License:** Extracted from the repository's `LICENSE` file or plugin descriptor.

---

## Style & Formatting Guidelines
- Keep wording concise, developer-friendly, and actionable.
- Use Markdown tables, lists, and code blocks appropriately.
- Format IDE navigation paths in bold (e.g., **Settings | Tools | <Plugin>**).
- Maintain an interactive, helpful tone when requesting additional user input.
