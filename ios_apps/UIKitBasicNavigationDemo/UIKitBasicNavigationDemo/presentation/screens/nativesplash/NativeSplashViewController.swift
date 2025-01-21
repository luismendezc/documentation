//
//  NativeSplashViewController.swift
//  UIKitBasicNavigationDemo
//
//  Created by Luis Enrique Mendez Cantero on 21.01.25.
//

import UIKit

class NativeSplashViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
            self.transitionToMainFlow()
        }
    }
    
    private func transitionToMainFlow() {
        let orangeVC: OrangeViewController = UIStoryboard.instantiate(from: .orange)
        let navigationController = UINavigationController(rootViewController: orangeVC)
        
        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()
        navigationController.navigationBar.tintColor = .white
        navigationController.navigationBar.standardAppearance = appearance
        navigationController.navigationBar.standardAppearance = appearance
        navigationController.navigationBar.scrollEdgeAppearance = appearance
        navigationController.navigationBar.compactAppearance = appearance
        
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first{
            UIView.transition(with: window, duration: 0.3, options:.transitionCurlDown){
                window.rootViewController = navigationController
            }
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
