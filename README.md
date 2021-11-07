## Installation
1. Add it in your root build.gradle at the end of repositories:

	```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	```

* Add the dependency in your app's build.gradle file

	```groovy
	dependencies {
	        implementation 'com.github.youniwobupa:shortvideo:1.0.0'
	}
	```
  
  ## Usage
  in Activity
  ```kotlin
  
  val videoUrls= listOf("https://mvvideo10.meita.com/618523cb0c2f3ki3frx45.mp4",
    "https://mvvideo10.meitd.com/617d71c68a23b07s660jli19.mp4",
    "https://mvvideo10.meita.com/617e0891ad406q8may298.mp4",)
    
   setContent {
        ShortVideoCompose(this,
        VideoItemsUrl = videoUrls)
   }