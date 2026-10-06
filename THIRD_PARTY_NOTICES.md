# Third-party notices

## Gradle Wrapper 8.8

Files: `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`.

Upstream: https://github.com/gradle/gradle/tree/v8.8.0

License: Apache License, Version 2.0 (SPDX: Apache-2.0); the complete text is in `LICENSES/Apache-2.0.txt`.

The wrapper scripts retain upstream's `Copyright © 2015-2021 the original authors.` notice and licensing headers. The binary wrapper is the official Gradle 8.8 wrapper, verified against https://services.gradle.org/distributions/gradle-8.8-wrapper.jar.sha256 . No wrapper classes or script contents have been modified. Wrapper configuration selects Gradle 8.8 and pins the official distribution SHA-256.

## Build dependencies

Forge, ForgeGradle, the Foojay toolchain resolver and downloaded Gradle/Minecraft artifacts are external build dependencies subject to their respective licenses and Minecraft's terms. Their binaries and game files are not part of this source distribution. The mod JAR does not bundle the Gradle wrapper or those dependencies. The original mod's BSD-3-Clause license does not replace any third-party license.
