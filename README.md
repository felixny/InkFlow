# 🎨 InkFlow

[![](https://jitpack.io/v/felixny/InkFlow.svg)](https://jitpack.io/#felixny/InkFlow)
[![Maven Central](https://img.shields.io/maven-central/v/com.felixny/inkflow.svg)](https://search.maven.org/search?q=g:com.felixny%20AND%20a:inkflow)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![CI](https://github.com/felixny/InkFlow/workflows/Build/badge.svg)](https://github.com/felixny/InkFlow/actions)

> A beautiful Jetpack Compose UI library for creating fluid motion effects and animations with organic ink bleed reveals.

## 📖 Description

InkFlow is a modern Android library built with Jetpack Compose that provides elegant and performant fluid motion effects for your Android applications. Designed with AGSL (Android Graphics Shading Language) support, InkFlow uses advanced procedural noise shaders on API 33+ (Android 13+) to create organic, non-uniform ink spread patterns while gracefully falling back to alpha fade on older devices.

## ✨ Features

- 🎨 **Organic Ink Bleed Effects** - Procedural noise-based shaders create natural, fluid motion
- 🚀 **Built with Jetpack Compose** - Modern declarative UI framework
- ⚡ **Optimized Performance** - Shader caching and efficient rendering
- 🎯 **Easy to Use** - Simple modifier API, works out of the box
- 📱 **Cross-Platform Compatible** - API 24+ support with automatic fallbacks
- 🎛️ **Highly Customizable** - Control speed, origin, noise, and edge properties
- 📐 **Adaptive Design** - Automatic tablet optimization

## 🏗️ Installation

**Note:** Requires JitPack and JDK 17+.

### JitPack (Recommended)

Add JitPack repository to your `settings.gradle.kts` (or `settings.gradle`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or if using `settings.gradle` (Groovy):

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Then add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.felixny:InkFlow:1.0.0")
}
```

Or in `build.gradle` (Groovy):

```groovy
dependencies {
    implementation 'com.github.felixny:InkFlow:1.0.0'
}
```

**Note:** You can also use specific commit hashes or branch names:
- `implementation("com.github.felixny:InkFlow:main-SNAPSHOT")` - Latest from main branch
- `implementation("com.github.felixny:InkFlow:abc1234")` - Specific commit hash

## 📝 Usage

### Basic Usage

Apply the `inkReveal` modifier to any composable to create an organic ink bleed reveal effect:

```kotlin
import com.felixny.inkflow.inkReveal

@Composable
fun MyCard() {
    var progress by remember { mutableFloatStateOf(0f) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .inkReveal(progress = progress)
    ) {
        // Your content here
    }
}
```

### With Configuration

Customize the ink effect using `InkFlowConfig`:

```kotlin
import com.felixny.inkflow.InkFlowConfig

val config = InkFlowConfig(
    noiseScale = 3.0f,           // Controls noise detail (higher = more detail)
    distortionStrength = 0.15f,   // How much noise distorts the spread (0.0 to 1.0)
    edgeSoftness = 0.15f,         // Softness of the ink edge (0.0 to 1.0)
    centerX = 0.5f,               // X origin (0.0 = left, 0.5 = center, 1.0 = right)
    centerY = 0.5f,               // Y origin (0.0 = top, 0.5 = center, 1.0 = bottom)
    speedMultiplier = 1.0f        // Speed of spread (1.0 = normal, >1.0 = faster)
)

Card(
    modifier = Modifier.inkReveal(
        progress = progress,
        config = config
    )
) {
    // Content
}
```

### With Animation

Animate the reveal effect using `Animatable`:

```kotlin
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch

@Composable
fun AnimatedReveal() {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Sync animatable with state
    LaunchedEffect(animatableProgress.value) {
        progress = animatableProgress.value
    }
    
    Column {
        Card(
            modifier = Modifier.inkReveal(progress = progress)
        ) {
            // Content
        }
        
        Button(
            onClick = {
                coroutineScope.launch {
                    animatableProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(2000)
                    )
                }
            }
        ) {
            Text("Reveal")
        }
    }
}
```

### Predefined Positions

Use convenience constants for common origin positions:

```kotlin
// Start from center (default)
val config = InkFlowConfig.CENTER

// Start from top center (like a dropdown)
val config = InkFlowConfig.TOP_CENTER

// Start from bottom center (like a bottom sheet)
val config = InkFlowConfig.BOTTOM_CENTER

// Start from corners
val config = InkFlowConfig.TOP_LEFT
val config = InkFlowConfig.BOTTOM_RIGHT

// Apply with custom speed
val config = InkFlowConfig.BOTTOM_CENTER.copy(speedMultiplier = 2.0f)
```

### Adaptive Tablet Support

Automatically adjust noise scale for tablets to prevent stretching:

```kotlin
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun AdaptiveReveal() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    val config = InkFlow.createOptimizedConfig(isTablet = isTablet)
    
    Card(
        modifier = Modifier.inkReveal(
            progress = 0.5f,
            config = config
        )
    ) {
        // Content
    }
}
```

## 🔬 Technical Details

### AGSL Noise Approach

InkFlow uses **procedural noise algorithms** implemented in AGSL (Android Graphics Shading Language) to create organic, non-uniform ink spread patterns. The implementation includes:

#### Simplex Noise
- A gradient noise function that produces smoother, more natural-looking patterns than Perlin noise
- Provides better computational performance and visual quality
- Used as the base for creating organic distortions

#### Fractal Brownian Motion (FBM)
- Combines multiple octaves of Simplex noise at different frequencies
- Creates complex, natural-looking patterns with varying levels of detail
- 4 octaves are used for optimal balance between detail and performance

#### Distance-Based Distortion
- Base distance calculation from the origin point
- Noise-based distortion applied to create organic, non-circular spread
- Configurable distortion strength allows fine-tuning of the effect

#### Shader Implementation

The shader works by:
1. **Calculating base distance** from the configurable origin point
2. **Applying noise distortion** to create organic variation
3. **Normalizing distance** to create a 0-1 progress range
4. **Applying smooth falloff** with configurable edge softness
5. **Masking alpha** based on progress to reveal/hide content

This approach creates a fluid, organic ink spread that looks natural and avoids the mechanical appearance of simple geometric shapes.

### Performance Optimizations

- **Shader Caching**: RuntimeShader instances are cached using `remember()` to avoid recompilation
- **Uniform Updates**: Only uniform values are updated during recomposition, not the shader code
- **Efficient Rendering**: Uses Compose's graphicsLayer with RenderEffect for hardware acceleration
- **Automatic Fallback**: API < 33 uses simple alpha fade (no shader overhead)

## 📋 API Reference

### `Modifier.inkReveal()`

Applies an ink bleed reveal effect to a composable.

**Parameters:**
- `progress: Float` - Reveal progress from 0.0 to 1.0 (automatically clamped)
- `config: InkFlowConfig` - Optional configuration (defaults provided)

**Returns:** A modifier that applies the ink bleed effect

### `InkFlowConfig`

Configuration class for customizing the ink reveal effect.

**Properties:**
- `noiseScale: Float` - Controls the scale of the noise pattern (default: `3.0f`)
  - Higher values = more detail, better for larger screens/tablets
  - Lower values = smoother, better for smaller screens
- `distortionStrength: Float` - How much noise distorts the ink spread (default: `0.15f`, range: 0.0-1.0)
- `edgeSoftness: Float` - Softness of the ink edge (default: `0.15f`, range: 0.0-1.0)
- `centerX: Float` - X coordinate of flow origin (default: `0.5f`, range: 0.0-1.0)
- `centerY: Float` - Y coordinate of flow origin (default: `0.5f`, range: 0.0-1.0)
- `speedMultiplier: Float` - Speed of ink spread (default: `1.0f`, must be > 0)

**Predefined Positions:**
- `InkFlowConfig.CENTER` - Center (0.5, 0.5)
- `InkFlowConfig.TOP_CENTER` - Top center (0.5, 0.0)
- `InkFlowConfig.BOTTOM_CENTER` - Bottom center (0.5, 1.0)
- `InkFlowConfig.LEFT_CENTER` - Left center (0.0, 0.5)
- `InkFlowConfig.RIGHT_CENTER` - Right center (1.0, 0.5)
- `InkFlowConfig.TOP_LEFT` - Top left (0.0, 0.0)
- `InkFlowConfig.TOP_RIGHT` - Top right (1.0, 0.0)
- `InkFlowConfig.BOTTOM_LEFT` - Bottom left (0.0, 1.0)
- `InkFlowConfig.BOTTOM_RIGHT` - Bottom right (1.0, 1.0)

### `InkFlow` Object

Main entry point for the library.

**Properties:**
- `VERSION_NAME: String` - Library version name
- `VERSION_CODE: Int` - Library version code
- `MIN_AGSL_API: Int` - Minimum API level for AGSL support (33)
- `defaultConfig: InkFlowConfig` - Default configuration instance

**Functions:**
- `isAgslSupported(): Boolean` - Checks if device supports AGSL shaders
- `createOptimizedConfig(isTablet: Boolean): InkFlowConfig` - Creates optimized config for device type

## 🔧 API Compatibility

- **API 33+ (Android 13+)**: Uses AGSL shaders for the full organic ink bleed effect
- **API 24-32**: Automatically falls back to a simple alpha fade effect
- The library handles API detection automatically - no additional code needed!

## 📱 Sample App

The repository includes a comprehensive sample app (`:app` module) with 8 different use cases:

1. **Profile Card** - Reveal profile information with ink bleed
2. **Image Reveal** - Animate image appearance with organic spread
3. **Text Animation** - Sequential text reveal with staggered effects
4. **Button Press** - Interactive button with ink effect
5. **Loading State** - Show loading progress with ink reveal
6. **Card Stack** - Reveal stacked cards sequentially
7. **Modal Dialog** - Animate modal appearance
8. **Flow Control** - Test speed and origin position settings

Run the sample app to see all examples in action!

## 🤝 Contributing

Contributions are welcome and greatly appreciated! Here's how you can help:

### Getting Started

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** and ensure code follows the project style
4. **Add tests** if applicable
5. **Commit your changes**: `git commit -m 'Add amazing feature'`
6. **Push to the branch**: `git push origin feature/amazing-feature`
7. **Open a Pull Request**

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc documentation for public APIs
- Ensure all public functions are properly documented
- Write tests for new features

### Reporting Issues

If you find a bug or have a feature request, please open an issue on GitHub with:
- Clear description of the problem/feature
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Android version and device information (if applicable)

### Pull Request Guidelines

- Keep PRs focused and small when possible
- Include tests for new functionality
- Update documentation as needed
- Ensure CI checks pass

## 📄 License

```
Copyright 2024 Felix Ny

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 👤 Author

**Felix Ny**

## 📚 Resources

- [AGSL Documentation](https://developer.android.com/develop/ui/views/graphics/agsl)
- [Jetpack Compose Graphics](https://developer.android.com/jetpack/compose/graphics)
- [Android Graphics Shading Language](https://developer.android.com/develop/ui/views/graphics/agsl)
