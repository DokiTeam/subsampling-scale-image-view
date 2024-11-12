Subsampling Scale Image View (Kotatsu fork)
===========================

> A custom image view for Android, designed for photo galleries and displaying huge images (e.g. maps and building
> plans) without `OutOfMemoryError`s. Includes pinch to zoom, panning, rotation and animation support, and allows easy
> extension so you can add your own overlays and touch event detection.

# Differences from [upstream](https://github.com/davemorrissey/subsampling-scale-image-view)

- Fully rewritten in Kotlin and using Coroutines for image loading (using from Java code is supported as well)
- Loading images from zip files (`ImageSource.zipEntry`)
- Support for ColorFilter
- Automatically stores and restores state (zoom and center)
- Supports Interpolator for animation
- Handle mouse and keyboard/dpad events for scaling and panning
- Supports downsampling

---

### Usage

1. Add it in your root build.gradle at the end of repositories:

   ```groovy
   allprojects {
	   repositories {
		   ...
		   maven { url 'https://jitpack.io' }
	   }
   }
   ```

2. Add the dependency

    ```groovy
    dependencies {
        implementation("com.github.KotatsuApp:subsampling-scale-image-view:$version")
    }
    ```

   See for versions at [JitPack](https://jitpack.io/#KotatsuApp/subsampling-scale-image-view)
