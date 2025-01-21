Crear un proyecto para ver las 2 maneras generales de mostrar una pantalla.
push y present.

## 1. Eliminar el Main storyboard para que podamos crear nuestros propias screen y cada una con un storyboard. Borrar en Info.plist
## 2. Crear la estructura del directorio.
		presentation/
			screens/
				blue/
					Blue.storyboard
					BlueViewController.swift
				nativesplash/
					NativeSplash.storyboard
					NativeSplashViewController.swift
				orange/
					Orange.storyboard
					OrangeViewController.swift
				pink/
					Pink.storyboard
					PinkViewController.swift
				red/
					Red.storyboard
					RedViewController.swift
			StoryboardExtensions.swift
		AppDelegate.swift
		Assets.xcassets
		Info.plist
		LaunchScreen.storyboard
		SceneDelegate.swift

## 3. Modificar el color de LaunchScreen.storyboard
## 4. Creación de extensiones
StoryboardExtensions.swift
```swift
**import** UIKit

**enum** Storyboard: String {
    **case** nativeSplash = "NativeSplash"
    **case** orange = "Orange"
    **case** blue = "Blue"
    **case** pink = "Pink"
    **case** red = "Red"
}

**extension** UIStoryboard {
    /// Instantiates a view controller of a specific type from a storyboard.
    ///
    /// The method assumes the view controller's storyboard identifier matches the class name
    /// with the suffix "ViewController" (e.g., `OrangeViewController`).
    ///
    /// - **Parameters**:
    ///   - storyboard: The storyboard to load the view controller from.
    ///   - type: The view controller type to instantiate.
    /// - **Returns**: A fully initialized view controller of the specified type.
    **static** **func** instantiate<T: UIViewController>(from storyboard: Storyboard, as type: T.**Type** = T.**self**) -> T {
        **let** instancedStoryboard = UIStoryboard(name: storyboard.rawValue, bundle: **nil**)
        **let** identifier = String(describing: type) // Use the class name as the identifier
        **guard** **let** viewController = instancedStoryboard.instantiateViewController(withIdentifier: identifier) **as**? T **else** {
            fatalError("ViewController with identifier \(identifier) not found in \(storyboard.rawValue) storyboard.")
        }
        **return** viewController
    }
}
```

## 5. Creación del diseno y conexión 

NativeSplashViewController.swift
```swift
**import** UIKit

**class** NativeSplashViewController: UIViewController {
    **override** **func** viewDidLoad() {
        **super**.viewDidLoad()
        // Simulate splash delay (3 seconds)
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            **self**.transitionToMainFlow()
        }
    }

    **private** **func** transitionToMainFlow() {

        // Initialize the navigation controller with OrangeViewController
        **let** orangeVC: OrangeViewController = UIStoryboard.instantiate(from: .orange)

        **let** navigationController = UINavigationController(rootViewController: orangeVC)

        // Customize the navigation bar appearance to make it transparent and change text color
            **let** appearance = UINavigationBarAppearance()

            appearance.configureWithTransparentBackground() // Keep the navigation bar transparent

            navigationController.navigationBar.tintColor = .white

            navigationController.navigationBar.standardAppearance = appearance

            navigationController.navigationBar.scrollEdgeAppearance = appearance

            navigationController.navigationBar.compactAppearance = appearance // For compact navigation bar (optional)

        // Replace the rootViewController of the current window

            **if** **let** windowScene = UIApplication.shared.connectedScenes.first **as**? UIWindowScene,

                   **let** window = windowScene.windows.first {

                UIView.transition(with: window, duration: 0.3, options: .transitionCurlDown, animations: {

                        window.rootViewController = navigationController

                    })
                }
        }
}
```
![[Pasted image 20250121212724.png]]

BlueViewController.swift
```swift
**import** UIKit

**class* BlueViewController: UIViewController {

  
    **@IBOutlet** **weak** **var** mainButton: UIButton!

    **override** **func** viewDidLoad() {
        **super**.viewDidLoad()

    }

    **@IBAction** **func** goToPink(_ sender: UIButton) {

        **let** pinkVC: PinkViewController = UIStoryboard.instantiate(from: .pink)

        navigationController?.pushViewController(pinkVC, animated: **true**)

    }
}

}
```
![[Pasted image 20250121212311.png]]
OrangeViewController.swift
```swift
**import** UIKit

**class** OrangeViewController: UIViewController {

    **@IBOutlet** **weak** **var** mainButton: UIButton!

    **override** **func** viewDidLoad() {

        **super**.viewDidLoad()
    }


    **@IBAction** **func** goToBlue(_ sender: UIButton) {

        **let** blueVC: BlueViewController = UIStoryboard.instantiate(from: .blue)

        navigationController?.pushViewController(blueVC, animated: **true**)

    }
}
```
![[Pasted image 20250121215803.png]]
PinkViewController.swift
```swift
**import** UIKit

**class** PinkViewController: UIViewController {
    **@IBOutlet** **weak** **var** mainButton: UIButton!

    **override** **func** viewDidLoad() {
        **super**.viewDidLoad()
    }


    **@IBAction** **func** showError(_ sender: UIButton) {
        **let** transition = CATransition()
        transition.duration = 0.3
        transition.type = CATransitionType.push
        transition.subtype = CATransitionSubtype.fromRight
        transition.timingFunction = CAMediaTimingFunction(name:CAMediaTimingFunctionName.default)
        view.window!.layer.add(transition, forKey: kCATransition)
        **let** redVC: RedViewController = UIStoryboard.instantiate(from: .red)
        redVC.modalPresentationStyle = .fullScreen
        present(redVC, animated: **false**, completion: **nil**)
    }
}
```
![[Pasted image 20250121215957.png]]

RedViewController.swift
```swift
**import** UIKit

**class** RedViewController: UIViewController {

    **override** **func** viewDidLoad() {
        **super**.viewDidLoad()

        // Create the button programmatically
                **let** dismissButton = UIButton(type: .system)
                dismissButton.setTitle("Dismiss", for: .normal)
                dismissButton.setTitleColor(.white, for: .normal)
                dismissButton.backgroundColor = .black
                dismissButton.layer.cornerRadius = 10
                dismissButton.translatesAutoresizingMaskIntoConstraints = **false**
  
                // Add the button to the view
                view.addSubview(dismissButton)

                // Set up constraints for the button
                NSLayoutConstraint.activate([
                    dismissButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
                    dismissButton.centerYAnchor.constraint(equalTo: view.centerYAnchor),
                    dismissButton.widthAnchor.constraint(equalToConstant: 150),
                    dismissButton.heightAnchor.constraint(equalToConstant: 50)
                ])

                // Add the action for the button
                dismissButton.addTarget(**self**, action: **#selector**(dismissViewController), for: .touchUpInside)
    }  

    // Action to dismiss the view controller
      **@objc** **private** **func** dismissViewController() {
          dismiss(animated: **true**, completion: **nil**)
      }
}
```
![[Pasted image 20250121220655.png]]

## 6. Configurar Scene delegate
```swift
**import** UIKit

**class** SceneDelegate: UIResponder, UIWindowSceneDelegate {

    **var** window: UIWindow?

    **func** scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        **guard** **let** windowScene = (scene **as**? UIWindowScene) **else** { **return** }
        **let** window = UIWindow(windowScene: windowScene)
        **let** splashVC: NativeSplashViewController = UIStoryboard.instantiate(from: .nativeSplash)
        window.rootViewController = splashVC
        **self**.window = window
        window.makeKeyAndVisible()
    }

    **func** sceneDidDisconnect(_ scene: UIScene) {
    }

    **func** sceneDidBecomeActive(_ scene: UIScene) {
    }

    **func** sceneWillResignActive(_ scene: UIScene) {
    }

    **func** sceneWillEnterForeground(_ scene: UIScene) {
    }

    **func** sceneDidEnterBackground(_ scene: UIScene) {
    }
}
```