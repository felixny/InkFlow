# 🎨 InkFlow

[![Maven Central](https://img.shields.io/maven-central/v/com.felixny/inkflow.svg)](https://search.maven.org/search?q=g:com.felixny%20AND%20a:inkflow)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?logo=kotlin)](https://kotlinlang.org)

> A beautiful Jetpack Compose UI library for creating fluid motion effects and animations.

## 📖 Description

InkFlow is a modern Android library built with Jetpack Compose that provides elegant and performant fluid motion effects for your Android applications. Designed with AGSL (Android Graphics Shading Language) support, InkFlow uses advanced shaders on API 33+ (Android 13+) while gracefully falling back to alpha fade on older devices.

## ✨ Features

- 🎨 Beautiful fluid motion effects
- 🚀 Built with Jetpack Compose
- ⚡ Optimized for performance
- 🎯 Easy to use and integrate
- 📱 Modern Android development

## 🏗️ Installation

### Gradle

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.felixny:inkflow:VERSION")
}
```

## 📝 Usage

### Library Entry Point

The `InkFlow` object provides access to library information and utilities:

```kotlin
import com.felixny.inkflow.InkFlow

// Check if device supports AGSL shaders
if (InkFlow.isAgslSupported()) {
    // Device supports full ink bleed effect
} else {
    // Will use alpha fade fallback
}

// Get default configuration
val defaultConfig = InkFlow.defaultConfig

// Create optimized config for tablets
val config = InkFlow.createOptimizedConfig(isTablet = true)

// Access version information
println("InkFlow version: ${InkFlow.VERSION_NAME}")
```

### Basic Usage

Apply the `inkReveal` modifier to any composable to create an organic ink bleed reveal effect:

```kotlin
import com.felixny.inkflow.inkReveal
import com.felixny.inkflow.InkFlowConfig

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
val config = InkFlowConfig(
    noiseScale = 3.0f,           // Controls noise detail (higher = more detail)
    distortionStrength = 0.15f,   // How much noise distorts the spread (0.0 to 1.0)
    edgeSoftness = 0.15f         // Softness of the ink edge (0.0 to 1.0)
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

### Adaptive Tablet Support

Automatically adjust noise scale for tablets to prevent stretching:

```kotlin
@Composable
fun AdaptiveReveal() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    val noiseScale = if (isTablet) 5.0f else 3.0f
    val config = remember(noiseScale) {
        InkFlowConfig(noiseScale = noiseScale)
    }
    
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

### Use Cases & Examples

The sample app includes 7 different use cases demonstrating various ways to use InkFlow:

#### 1. **Profile Card Reveal**
Reveal profile information with ink bleed effect. Perfect for user profiles, cards, and information displays.

```kotlin
Card(
    modifier = Modifier.inkReveal(progress = progress, config = config)
) {
    // Profile content
}
```

#### 2. **Image Reveal**
Animate image appearance with organic spread. Great for photo galleries, artwork, and visual content.

```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .inkReveal(progress = progress, config = config)
) {
    // Image content
}
```

#### 3. **Text Animation**
Reveal text elements sequentially with staggered ink spread for dramatic effect.

```kotlin
texts.forEachIndexed { index, text ->
    var progress by remember { mutableFloatStateOf(0f) }
    
    Card(
        modifier = Modifier.inkReveal(progress = progress, config = config)
    ) {
        Text(text = text)
    }
}
```

#### 4. **Button Press Effects**
Add organic ink effects to button interactions for enhanced user feedback.

```kotlin
Box(
    modifier = Modifier.inkReveal(progress = progress, config = config)
) {
    Button(onClick = { /* trigger animation */ }) {
        Text("Click Me")
    }
}
```

#### 5. **Loading State**
Show loading progress with organic ink reveal animation.

```kotlin
Card(
    modifier = Modifier.inkReveal(progress = loadingProgress, config = config)
) {
    CircularProgressIndicator()
    Text("Loading...")
}
```

#### 6. **Card Stack**
Reveal stacked cards sequentially for a dramatic layered effect.

```kotlin
cards.forEachIndexed { index, card ->
    Card(
        modifier = Modifier
            .offset(y = (index * 20).dp)
            .inkReveal(progress = progress[index], config = config)
    ) {
        // Card content
    }
}
```

#### 7. **Modal Dialog**
Animate modal dialogs and bottom sheets with organic ink reveal.

```kotlin
if (showModal) {
    Card(
        modifier = Modifier.inkReveal(progress = progress, config = config)
    ) {
        // Modal content
    }
}
```

### Complete Example

```kotlin
@Composable
fun ProfileCardScreen() {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Tablet detection for adaptive scaling
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val noiseScale = if (isTablet) 5.0f else 3.0f
    val config = remember(noiseScale) {
        InkFlowConfig(noiseScale = noiseScale)
    }
    
    // Sync animatable with state
    LaunchedEffect(animatableProgress.value) {
        progress = animatableProgress.value
    }
    
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .inkReveal(progress = progress, config = config)
        ) {
            // Your card content
        }
        
        Slider(
            value = progress,
            onValueChange = { newValue ->
                progress = newValue
                coroutineScope.launch {
                    animatableProgress.snapTo(newValue)
                }
            }
        )
        
        Button(
            onClick = {
                coroutineScope.launch {
                    animatableProgress.animateTo(1f, tween(2000))
                }
            }
        ) {
            Text("Animate")
        }
    }
}
```

## 🔧 API Compatibility

- **API 33+ (Android 13+)**: Uses AGSL shaders for the full organic ink bleed effect
- **API 24-32**: Automatically falls back to a simple alpha fade effect
- The library handles API detection automatically - no additional code needed!

## 📋 Parameters

### `inkReveal` Modifier

- `progress: Float` - Reveal progress from 0.0 to 1.0 (automatically clamped)
- `config: InkFlowConfig` - Optional configuration (defaults provided)

### `InkFlowConfig`

- `noiseScale: Float` - Controls the scale of the noise pattern (default: `3.0f`)
  - Higher values = more detail, better for larger screens/tablets
  - Lower values = smoother, better for smaller screens
- `distortionStrength: Float` - How much noise distorts the ink spread (default: `0.15f`, range: 0.0-1.0)
- `edgeSoftness: Float` - Softness of the ink edge (default: `0.15f`, range: 0.0-1.0)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

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
