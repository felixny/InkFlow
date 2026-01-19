@file:JvmName("InkFlow")

package com.felixny.inkflow

import androidx.compose.runtime.Stable

/**
 * InkFlow - A Jetpack Compose UI library for fluid motion effects.
 * 
 * This object serves as the main entry point for the InkFlow library.
 * Use it to access library information, default configurations, and utility functions.
 */
object InkFlow {
    
    /**
     * Library version name (e.g., "1.0.0").
     */
    const val VERSION_NAME = "1.0.0"
    
    /**
     * Library version code (incremental integer).
     */
    const val VERSION_CODE = 1
    
    /**
     * Minimum Android API level required for AGSL shader support.
     * Devices below this API level will use the alpha fade fallback.
     */
    const val MIN_AGSL_API = 33
    
    /**
     * Default configuration for ink reveal effects.
     * Use this as a starting point or customize with [InkFlowConfig].
     */
    val defaultConfig: InkFlowConfig = InkFlowConfig()
    
    /**
     * Checks if the current device supports AGSL shaders (API 33+).
     * 
     * @return true if AGSL is supported, false otherwise (will use alpha fade fallback)
     */
    fun isAgslSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= MIN_AGSL_API
    }
    
    /**
     * Creates a default configuration optimized for the current device.
     * Automatically adjusts noise scale for tablets.
     * 
     * @param isTablet Whether the device is a tablet (screen width >= 600dp)
     * @return An optimized [InkFlowConfig] instance
     */
    fun createOptimizedConfig(isTablet: Boolean = false): InkFlowConfig {
        return InkFlowConfig(
            noiseScale = if (isTablet) 5.0f else 3.0f,
            distortionStrength = defaultConfig.distortionStrength,
            edgeSoftness = defaultConfig.edgeSoftness
        )
    }
}

/**
 * Configuration class for InkFlow library.
 * 
 * @param noiseScale Controls the scale of the noise pattern (higher = more detail)
 * @param distortionStrength Controls how much the noise distorts the ink spread (0.0 to 1.0)
 * @param edgeSoftness Controls the softness of the ink edge (0.0 to 1.0)
 * @param centerX The X coordinate of the ink flow origin (0.0 to 1.0, where 0.5 is center)
 * @param centerY The Y coordinate of the ink flow origin (0.0 to 1.0, where 0.5 is center)
 * @param speedMultiplier Controls the speed of ink spread (1.0 = normal, >1.0 = faster, <1.0 = slower)
 */
@Stable
data class InkFlowConfig(
    val noiseScale: Float = 3.0f,
    val distortionStrength: Float = 0.15f,
    val edgeSoftness: Float = 0.15f,
    val centerX: Float = 0.5f, // Center by default
    val centerY: Float = 0.5f, // Center by default
    val speedMultiplier: Float = 1.0f // Normal speed by default
) {
    init {
        require(noiseScale > 0f) { "noiseScale must be positive" }
        require(distortionStrength in 0f..1f) { "distortionStrength must be between 0 and 1" }
        require(edgeSoftness in 0f..1f) { "edgeSoftness must be between 0 and 1" }
        require(centerX in 0f..1f) { "centerX must be between 0 and 1" }
        require(centerY in 0f..1f) { "centerY must be between 0 and 1" }
        require(speedMultiplier > 0f) { "speedMultiplier must be positive" }
    }
    
    companion object {
        /**
         * Predefined configuration with center origin (0.5, 0.5).
         * Use for standard reveal effects that start from the center.
         */
        val CENTER = InkFlowConfig(centerX = 0.5f, centerY = 0.5f)
        
        /**
         * Predefined configuration with top center origin (0.5, 0.0).
         * Ideal for dropdown menus and top-down reveals.
         */
        val TOP_CENTER = InkFlowConfig(centerX = 0.5f, centerY = 0.0f)
        
        /**
         * Predefined configuration with bottom center origin (0.5, 1.0).
         * Perfect for bottom sheets and bottom-up reveals.
         */
        val BOTTOM_CENTER = InkFlowConfig(centerX = 0.5f, centerY = 1.0f)
        
        /**
         * Predefined configuration with left center origin (0.0, 0.5).
         * Use for left-to-right reveal effects.
         */
        val LEFT_CENTER = InkFlowConfig(centerX = 0.0f, centerY = 0.5f)
        
        /**
         * Predefined configuration with right center origin (1.0, 0.5).
         * Use for right-to-left reveal effects.
         */
        val RIGHT_CENTER = InkFlowConfig(centerX = 1.0f, centerY = 0.5f)
        
        /**
         * Predefined configuration with top left origin (0.0, 0.0).
         * Use for corner-based reveal effects starting from top-left.
         */
        val TOP_LEFT = InkFlowConfig(centerX = 0.0f, centerY = 0.0f)
        
        /**
         * Predefined configuration with top right origin (1.0, 0.0).
         * Use for corner-based reveal effects starting from top-right.
         */
        val TOP_RIGHT = InkFlowConfig(centerX = 1.0f, centerY = 0.0f)
        
        /**
         * Predefined configuration with bottom left origin (0.0, 1.0).
         * Use for corner-based reveal effects starting from bottom-left.
         */
        val BOTTOM_LEFT = InkFlowConfig(centerX = 0.0f, centerY = 1.0f)
        
        /**
         * Predefined configuration with bottom right origin (1.0, 1.0).
         * Use for corner-based reveal effects starting from bottom-right.
         */
        val BOTTOM_RIGHT = InkFlowConfig(centerX = 1.0f, centerY = 1.0f)
    }
}
