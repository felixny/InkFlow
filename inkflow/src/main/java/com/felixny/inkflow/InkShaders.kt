package com.felixny.inkflow

/**
 * AGSL shader code for the Ink Bleed effect.
 * Uses procedural noise to create an organic ink spread pattern.
 */
internal object InkShaders {
    
    /**
     * AGSL shader that creates an organic ink bleed effect using procedural noise.
     * 
     * @param inputImage The underlying UI content to be revealed
     * @param progress The bleed progress from 0.0 to 1.0
     * @return The shader code as a string
     */
    val inkBleedShader = """
        uniform shader inputImage;
        uniform float progress;
        uniform float noiseScale;
        uniform float distortionStrength;
        uniform float edgeSoftness;
        
        // Simplex noise hash function
        float hash(vec2 p) {
            return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
        }
        
        // 2D Simplex noise
        float noise(vec2 p) {
            const float K1 = 0.366025404; // (sqrt(3)-1)/2
            const float K2 = 0.211324865; // (3-sqrt(3))/6
            
            vec2 i = floor(p + (p.x + p.y) * K1);
            vec2 a = p - i + (i.x + i.y) * K2;
            vec2 o = (a.x < a.y) ? vec2(0.0, 1.0) : vec2(1.0, 0.0);
            vec2 b = a - o + K2;
            vec2 c = a - 1.0 + 2.0 * K2;
            
            vec3 h = max(0.5 - vec3(dot(a, a), dot(b, b), dot(c, c)), 0.0);
            vec3 n = h * h * h * h * vec3(
                dot(a, vec2(hash(i + vec2(0.0)), hash(i + vec2(1.0, 0.0)))),
                dot(b, vec2(hash(i + o + vec2(0.0, 1.0)), hash(i + o))),
                dot(c, vec2(hash(i + vec2(1.0)), hash(i + vec2(1.0, 1.0))))
            );
            
            return dot(n, vec3(70.0));
        }
        
        // Fractal Brownian Motion for organic variation
        float fbm(vec2 p) {
            float value = 0.0;
            float amplitude = 0.5;
            float frequency = 1.0;
            
            for (int i = 0; i < 4; i++) {
                value += amplitude * noise(p * frequency);
                frequency *= 2.0;
                amplitude *= 0.5;
            }
            
            return value;
        }
        
        half4 main(vec2 coord) {
            // Get the size of the content from the input image
            // In AGSL, we can sample to get dimensions, but for simplicity,
            // we'll use a relative coordinate system
            vec2 size = vec2(1000.0, 1000.0); // Default size, will be overridden by actual content
            vec2 center = size * 0.5; // Center of the effect
            float maxRadius = length(size) * 0.7; // Maximum spread radius (diagonal)
            
            // Use relative coordinates for noise (0-1 range)
            vec2 uv = coord / max(size.x, size.y);
            
            // Calculate noise-based distortion using configurable noiseScale
            float noiseValue = fbm(uv * noiseScale);
            
            // Calculate distance from center
            float baseDist = distance(coord, center);
            
            // Apply organic distortion using noise with configurable strength
            float distortion = noiseValue * (maxRadius * distortionStrength);
            float inkDist = baseDist - distortion;
            
            // Normalize distance to 0..1 range
            float normalizedDist = inkDist / maxRadius;
            
            // Create smooth falloff with progress using configurable edge softness
            // As progress increases from 0 to 1, the ink consumes more pixels
            float threshold = progress * 1.1; // Slight overshoot for smoother edge
            float softness = edgeSoftness;
            float alpha = 1.0 - smoothstep(threshold - softness, threshold, normalizedDist);
            
            // Clamp alpha to valid range
            alpha = clamp(alpha, 0.0, 1.0);
            
            // Sample the input image
            half4 color = inputImage.eval(coord);
            
            // Apply alpha based on ink spread - pixels are revealed as ink consumes them
            return half4(color.rgb, color.a * alpha);
        }
    """.trimIndent()
}
