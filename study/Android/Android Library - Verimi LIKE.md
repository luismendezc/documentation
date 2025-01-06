




Best way to deliver:
JIT
Github

Ways to deliver the app in a fast way no online repo or something:
### 1) Building an **AAR** File and Copying It into Another Project

This is the **simplest** approach if you just want an **AAR** artifact you can drop into another project.

1. **Build the AAR** from your root project (where `app` and `designlemc` co-exist). In a terminal, run:
		```
		./gradlew :designlemc:assembleRelease
		```
    
    - This will generate an **AAR** file (Android Archive) at something like:
        ```
        designlemc/build/outputs/aar/designlemc-release.aar
        ```
        
    - The exact path may vary slightly depending on your setup.
      
2. **Copy the AAR** into your **new project**. Typically, you create a folder named `libs/` inside your new project’s **module** (or the root). For example:
    
    `myNewProject/  
    ├─ app/  
    │   ├─ src/  
    │   ├─ libs/  
    │   │   └─ designlemc-release.aar  
    │   └─ build.gradle  
    └─ build.gradle`
    
3. **Reference the AAR** in your new project’s `app/build.gradle` (or wherever you want to use it). In the `dependencies { ... }` block, add:
```
dependencies {     
    implementation files("libs/designlemc-release.aar")    
     // ... other dependencies ... 
}
```
    
    
4. **Sync** your new project. You can now import your library classes (e.g., `com.oceloti.lemc.designlemc.LemcSDK`) in your new project’s code.
    

**That’s it!** Each time you update the library, you’ll need to **re-build** the AAR (`assembleRelease`) and **copy** the new file over.


### 2) Using a **Local Maven Repository** (Optional Alternative)

If you plan to **reuse** your library across multiple local projects often—and prefer a more “Gradle-friendly” approach—consider publishing to a **local Maven repository** on your machine. Then each consuming project can just do `implementation "groupId:artifactId:1.0.0"`.

**Steps**:

1. In your `designlemc/build.gradle`, make sure you apply `maven-publish` plugin:
```groovy
plugins { 
	id("com.android.library") 
	id("maven-publish") 
	// ... other plugins 
} 
// ... 
  
afterEvaluate {  
	publishing {  
	    publications {  
		      create("release", MavenPublication::class.java) {  
				from(components["release"])  
			    groupId = "com.oceloti.lemc"  
		        artifactId = "designlemc"  
		        version = "0.0.1"  
		    }  
	    }  
	}  
}
```

2. **Publish locally** by running:
```bash
./gradlew :designlemc:publishReleasePublicationToMavenLocal
```

- This places your AAR in `~/.m2/repository/com/oceloti/lemc/designlemc/1.0.0/` (the local Maven repo).
 **Where Is My Local Maven Repository on Windows?**
By **default**, the local Maven repository on Windows is located at:
```
C:\Users\<YourUserName>\.m2\repository
```

1. In your **new project**, add `mavenLocal()` to your top-level `repositories`:
```groovy
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    // ...
}
```

And in your module’s `build.gradle`, do:
```groovy
dependencies {
    implementation "com.oceloti.lemc:designlemc:1.0.0"
    // ...
}
```

4. Sync. Gradle will pull the AAR from your local Maven repository.

**Note:** Every time you update the library, you must **re-publish** it to mavenLocal. Then your other project will pick up the new version.

