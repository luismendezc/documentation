https://developer.apple.com/documentation/xcode/customizing-the-build-schemes-for-a-project

### Step 1: Project Setup

1. **Create a New Xcode Project:**
    
    - Open Xcode, select "App" under "iOS" > "App Template," and name your project `SplitVersionsUIKit`.
    - Choose "Storyboard" as the interface and "Swift" as the language.
2. **Directory Structure:**
    
    - Keep your files organized by creating folders (groups in Xcode) for each variant. For example:
        
        css
        
        Copy code
        
        `SplitVersionsUIKit/ 
        ├── Configurations/ 
        ├── Flavors/ 
        ├── AppDelegate.swift 
        ├── SceneDelegate.swift 
        ├── LaunchScreen.storyboard 
        ├── Main.storyboard`

![[Pasted image 20250113161042.png]]
### Step 2: Configure Build Settings

1. **Define Flavors and Variants:**
    
    - Open the Xcode project, go to the **Project Navigator**, select the project file (root node), and switch to the **Info** tab.
    - Add configurations under "Configurations":
        - Click the "+" at the bottom and select "Duplicate Debug Configuration."
        - Rename it (e.g., `Int2RedDebug`).
        - Repeat this for all combinations: `Int2RedDebug`, `Int2BlueDebug`, `ProdBlueRelease`, etc.
![[Pasted image 20250113161235.png]]
1. **Edit Build Settings:**
    
    - Under the **Build Settings** tab, define preprocessor macros to differentiate between configurations:
        - Search for `Swift Compiler - Custom Flags`.
        - Add custom flags under "Active Compilation Conditions" for each configuration. Example:
            - `DEBUG_INT2_RED` for `Int2RedDebug`.
            - `PROD_BLUE_RELEASE` for `ProdBlueRelease`.
![[Pasted image 20250113161449.png]]
1. **Define Custom Schemes:**
    
    - Open the Scheme menu (next to the Run/Stop buttons).
    - Click "Manage Schemes," and duplicate the default scheme for each variant.
    - Name them according to your variants (e.g., `Int2RedDebug`, `ProdBlueRelease`).
4. **Add Custom Plist Files:**
    
    - Duplicate the existing `Info.plist` file for each flavor.
    - Modify specific keys (like display name or bundle identifier) for each variant.

1. **Understand `DEBUG` and `$(inherited)`**:
    
    - The `DEBUG` condition is automatically added to all debug configurations in Xcode. It's used to include debug-specific code, such as `#if DEBUG` blocks.
    - `$(inherited)` ensures that custom conditions are appended to the existing ones. This allows flexibility to add new flags without overwriting defaults.
2. **Customize Compilation Conditions Without Breaking Defaults**:
    
    - **Keep `DEBUG`**: It's a good idea to retain `DEBUG` for general debug-related logic.
    - **Retain `$(inherited)`**: This ensures that any conditions inherited from parent or base configurations remain intact.
    - **Add Your Custom Flags**:
        - Modify the `Active Compilation Conditions` for each configuration to add your custom flags **in addition** to `DEBUG` and `$(inherited)`.
3. **Example**:
    
    - For `Int2RedDebug` configuration:
        
        javascript
        
        Copy code
        
        `DEBUG_INT2_RED DEBUG $(inherited)`
        
    - For `ProdBlueRelease` configuration:
        
        javascript
        
        Copy code
        
        `PROD_BLUE_RELEASE $(inherited)`
        
![[Pasted image 20250113161530.png]]
---

### Debug and Release Configurations Still Exist

Even after adding custom configurations (e.g., `Int2RedDebug`, `ProdBlueRelease`), **Debug** and **Release** remain the base configurations in Xcode. This is normal behavior, and you can handle it as follows:

1. **Keep Debug and Release as Baseline**:
    
    - You don't need to remove them. Instead, treat them as templates for your custom configurations.
    - For example:
        - `Int2RedDebug` can inherit from `Debug`.
        - `ProdBlueRelease` can inherit from `Release`.
2. **Verify and Update Custom Configurations**:
    
    - Check the "Based on Configuration File" field in your custom configurations (under **Info** tab in the project settings). Ensure they're based on `Debug.xcconfig` or `Release.xcconfig` if needed.
3. **Override Where Necessary**:
    
    - If you need a unique behavior for a specific configuration (e.g., different flags or settings for `Int2RedDebug`), you can override the defaults in that specific configuration.


Create a Layered Builds:
### Step 1: Create Layered `.xcconfig` Files

We’ll organize `.xcconfig` files into three levels:

1. **Environment** (`Int1`, `Int4`, `Prod`)
2. **App Color** (`Red`, `Blue`)
3. **Build Type** (`Debug`, `Release`)

#### File Structure

Organize your files like this:

mathematica

Copy code

`Configurations/ ├── Environments/ │   ├── Int1.xcconfig │   ├── Int4.xcconfig │   └── Prod.xcconfig ├── Colors/ │   ├── Red.xcconfig │   └── Blue.xcconfig ├── BuildTypes/ │   ├── Debug.xcconfig │   └── Release.xcconfig └── Combined/     ├── Int1RedDebug.xcconfig     ├── Int1BlueRelease.xcconfig     ├── Int4RedDebug.xcconfig     └── ProdBlueRelease.xcconfig`

---

### Step 2: Define Variables in Each `.xcconfig`

#### 1. Environment `.xcconfig`

Define variables specific to environments (e.g., API URLs):

arduino

Copy code

`// Int1.xcconfig 
BASE_API_URL = https://api.int1.example.com APP_ENVIRONMENT = Int1`

arduino

Copy code

`// Int4.xcconfig 
BASE_API_URL = https://api.int4.example.com 
APP_ENVIRONMENT = Int4`

arduino

Copy code

`// Prod.xcconfig 
BASE_API_URL = https://api.prod.example.com 
APP_ENVIRONMENT = Prod`

#### 2. App Color `.xcconfig`

Define theme-related variables:

arduino

Copy code

`// Red.xcconfig 
APP_COLOR = red`

arduino

Copy code

`// Blue.xcconfig 
APP_COLOR = blue`

#### 3. Build Type `.xcconfig`

Define logging or build-specific variables:

java

Copy code

`// Debug.xcconfig 
LOG_LEVEL = verbose 
ENABLE_DEBUG_MODE = YES`

go

Copy code

`// Release.xcconfig 
LOG_LEVEL = error 
ENABLE_DEBUG_MODE = NO`

#### 4. Combined `.xcconfig`

Combine all layers for specific configurations:

arduino

Copy code

`// Int1RedDebug.xcconfig #include "../Environments/Int1.xcconfig" #include "../Colors/Red.xcconfig" #include "../BuildTypes/Debug.xcconfig"`

arduino

Copy code

`// ProdBlueRelease.xcconfig 
#include "../Environments/Prod.xcconfig" 
#include "../Colors/Blue.xcconfig" 
#include "../BuildTypes/Release.xcconfig"`

Each combined file inherits the variables from its included files, creating a precise and modular configuration.

![[Pasted image 20250113161603.png]]

![[Pasted image 20250113161616.png]]
---

### Step 3: Assign `.xcconfig` Files to Build Configurations

1. **Link Combined Files**:
    
    - Go to the **Info** tab of your project in Xcode.
    - Assign each combined `.xcconfig` file to its corresponding build configuration.
2. **Verify Inheritance**:
    
    - Open the Build Settings in Xcode.
    - Look for the inherited values from the `.xcconfig` files.

![[Pasted image 20250113161639.png]]
---

### Step 4: Access Configuration Variables in Code

1. **Use `Info.plist`**: Add references to the combined `.xcconfig` variables in `Info.plist`:
    
    xml
    
    Copy code
    
    `<key>AppEnvironment</key> <string>$(APP_ENVIRONMENT)</string> <key>AppColor</key> <string>$(APP_COLOR)</string>`
    ![[Pasted image 20250113161715.png]]
2. **Access Variables**: Retrieve the variables in your code:
    
    swift
    
    Copy code
    
    `let environment = Bundle.main.object(forInfoDictionaryKey: "AppEnvironment") as? String let appColor = Bundle.main.object(forInfoDictionaryKey: "AppColor") as? String`
    

---

### Step 5: Conditional Code Based on Build Settings

Use `#if` directives to conditionally include code:

swift

Copy code

`#if DEBUG print("Log level: verbose") #else print("Log level: error") #endif  #if ENABLE_DEBUG_MODE print("Debug mode enabled") #endif`

---

### Step 6: Test Each Configuration

1. **Select a Scheme**:
    
    - In Xcode, select a scheme that corresponds to a combined `.xcconfig` file (e.g., `Int1RedDebug`).
2. **Verify Behavior**:
    
    - Check logs, API URLs, and UI elements (e.g., app color).


### 1. **How to Access Configuration in the `ViewController`**

To see and use the configuration values in your `ViewController`, follow these steps:

#### Access Configuration from `Info.plist`

Assuming you’ve added the variables to your `Info.plist` file via `.xcconfig` (e.g., `AppEnvironment`, `AppColor`), you can access them using the `Bundle` API.

Update your `ViewController` like this:

swift

Copy code

```swift
import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Retrieve values from Info.plist
        if let appEnvironment = Bundle.main.object(forInfoDictionaryKey: "AppEnvironment") as? String {
            print("App Environment: \(appEnvironment)")
        }

        if let appColor = Bundle.main.object(forInfoDictionaryKey: "AppColor") as? String {
            print("App Color: \(appColor)")
        }

        if let logLevel = Bundle.main.object(forInfoDictionaryKey: "LogLevel") as? String {
            print("Log Level: \(logLevel)")
        }
    }
}

```


Make sure the keys (`AppEnvironment`, `AppColor`, etc.) exist in your `Info.plist` and are properly populated by the `.xcconfig` files.

#### Debugging Tip

Run the app for different build variants and verify the printed values in Xcode's **Debug Console**.

---

### 2. **How to Build a Specific Variant**

#### a) **Select a Scheme**

Xcode uses schemes to determine which build configuration to use. Here’s how to build a specific variant:

1. Open the scheme menu in the toolbar (next to the Run/Stop buttons).
2. Select the scheme corresponding to the variant you want to build (e.g., `Int1RedDebug` or `ProdBlueRelease`).
    - If you don’t see a scheme for your variant, add it manually.

#### b) **Create a New Scheme (If Missing)**

1. **Go to "Manage Schemes":**
    
    - Click the **Scheme Menu** > **Manage Schemes**.
2. **Duplicate an Existing Scheme:**
    
    - Select an existing scheme (e.g., `Debug`) and click **Duplicate Scheme**.
3. **Rename the Scheme:**
    
    - Rename it to match the specific variant, such as `Int1RedDebug`.
4. **Assign the Configuration:**
    
    - Select your new scheme, click **Edit Scheme**, and assign the appropriate **Build Configuration** for each action:
        - Run: `Int1RedDebug`
        - Test: `Int1RedDebug`
        - Profile: `Int1RedDebug`

#### c) **Build the Selected Variant**

Once the scheme is set:

1. Select the desired scheme.
2. Click **Run** (or use `Cmd + R`) to build and run the app for that variant.

---

### Summary

1. **Accessing Configurations**: Use `Bundle.main.object(forInfoDictionaryKey:)` in your `ViewController`.
2. **Building Variants**:
    - Select or create a scheme for the specific variant.
    - Assign the corresponding `.xcconfig`-based build configuration.
    - Build and run the app for the selected scheme.

Would you like me to guide you further in automating the scheme creation or testing the configurations?

![[Pasted image 20250113161747.png]]
![[Pasted image 20250113161803.png]]
![[Pasted image 20250113161835.png]]
![[Pasted image 20250113161846.png]]
![[Pasted image 20250113161905.png]]
![[Pasted image 20250113161910.png]]
![[Pasted image 20250113161918.png]]



### Step 1: Customize the Bundle Identifier

1. **Open Xcode Project Settings**:
    
    - Select your project in the **Project Navigator**.
    - Go to the **Info** tab.
2. **Modify the Bundle Identifier for Each Configuration**:
    
    - For each build configuration, add a unique suffix to the base bundle identifier:
        - Base: `com.yourcompany.SplitVersionsUIKit`
        - Suffixes:
            - `Int1RedDebug`: `com.yourcompany.SplitVersionsUIKit.int1red.debug`
            - `Int4BlueRelease`: `com.yourcompany.SplitVersionsUIKit.int4blue.release`
            - `ProdBlueDebug`: `com.yourcompany.SplitVersionsUIKit.prodblue.debug`
3. **Use `.xcconfig` to Dynamically Set the Bundle Identifier**:
    
    - Add the bundle identifier in each `.xcconfig` file:
        
        text
        
        Copy code
        
        `// Int1RedDebug.xcconfig PRODUCT_BUNDLE_IDENTIFIER = com.yourcompany.SplitVersionsUIKit.int1red.debug`
        
    - Repeat for all configurations.
        
4. **Verify in Build Settings**:
    
    - Go to the **Build Settings** tab.
    - Search for `PRODUCT_BUNDLE_IDENTIFIER`.
    - Ensure each configuration has the correct bundle identifier.

![[Pasted image 20250113160514.png]]

---

### Step 2: Add Display Name Customization (Optional)

If you want to differentiate the app names on the home screen:

1. **Add a `CFBundleDisplayName` Key in `Info.plist`**:
    
    - Open `Info.plist`.
    - Add a key for `CFBundleDisplayName` with a placeholder:
        
        xml
        
        Copy code
        
        `<key>CFBundleDisplayName</key> <string>$(APP_DISPLAY_NAME)</string>`
        
2. **Define `APP_DISPLAY_NAME` in `.xcconfig` Files**:
    
    - Example for `Int1RedDebug.xcconfig`:
        
        text
        
        Copy code
        
        `APP_DISPLAY_NAME = Split Int1 Red Debug`
        
    - Example for `ProdBlueRelease.xcconfig`:
        
        text
        
        Copy code
        
        `APP_DISPLAY_NAME = Split Prod Blue Release`
        

---

### Step 3: Test the Setup

1. **Clean and Build**:
    
    - Clean the project (`Cmd + Shift + K`).
    - Build and run each configuration (select appropriate schemes).
2. **Install Different Variants**:
    
    - Verify that installing a new variant doesn’t override the previous one.
    - Check that the app names and bundle identifiers are correct.

---

### Step 4: Automate with Fastlane (Optional)

To simplify testing and distribution of multiple variants, you can use **Fastlane**:

1. Add lanes in your `Fastfile` for each configuration.
2. Automate the building, signing, and distribution process for each variant.


![[Pasted image 20250113160848.png]]

![[Pasted image 20250113162045.png]]