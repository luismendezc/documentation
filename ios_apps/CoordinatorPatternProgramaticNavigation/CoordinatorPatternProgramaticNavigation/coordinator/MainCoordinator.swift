//
//  MainCoordinator.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class MainCoordinator: Coordinator {
    
    typealias Event = MainCoordinatorEvent
    
    var childCoordinators: [any Coordinator] = []
    let window: UIWindow
    var mainNavigationController: CoordinatorNavigationController
    var errorNavigationController: CoordinatorNavigationController?
    var errorCoordinator: ErrorCoordinator?

    init(window: UIWindow) {
        self.window = window
        self.mainNavigationController = CoordinatorNavigationController()
        self.mainNavigationController.parentCoordinator = self
    }

    func start() {
        let currentTime = Calendar.current.component(.hour, from: Date())
        if currentTime >= 17 {
            showEveningScreen()
        } else {
            showMorningScreen()
        }
        window.rootViewController = mainNavigationController
        window.makeKeyAndVisible()
    }

    func showMorningScreen() {
        let morningVC: MorningViewController = UIStoryboard.instantiateViewController(
            from: "Morning",
            ofType: MorningViewController.self
        )
        let viewModel = MorningViewModel(onEvent: { [weak self] event in
            self?.onEvent(event)
        })
        morningVC.viewModel = viewModel
        mainNavigationController.setViewControllers([morningVC], animated: true)
    }

    func showEveningScreen() {
        let eveningVC: EveningViewController = UIStoryboard.instantiateViewController(
            from: "Evening",
            ofType: EveningViewController.self
        )
        let viewModel = EveningViewModel(onEvent: { [weak self] event in
            self?.onEvent(event)
        })
        eveningVC.viewModel = viewModel
        mainNavigationController.setViewControllers([eveningVC], animated: true)
    }

    func showErrorScreen(message: String) {
        // Create a new ErrorNavigationController
        let errorNavigationController = CoordinatorNavigationController()
        
        // Set MainCoordinator as the parentCoordinator for the errorNavigationController
        errorNavigationController.parentCoordinator = self

        // Create the ErrorCoordinator
        let errorCoordinator = ErrorCoordinator(navigationController: errorNavigationController)
        childCoordinators.append(errorCoordinator)
        
        // Start the error flow
        errorCoordinator.start(with: message)
        
        self.errorNavigationController = errorNavigationController
        self.errorCoordinator = errorCoordinator

        // Switch the rootViewController to the ErrorNavigationController
        window.rootViewController = errorNavigationController
    }


    func returnToMainFlow() {
        // Switch back to the main navigation controller
        UIView.transition(
            with: window,
            duration: 0.3,
            options: .transitionCrossDissolve,
            animations: { [weak self] in
                self?.window.rootViewController = self?.mainNavigationController
            },
            completion: nil
        )

        // Clean up the error coordinator
        errorCoordinator = nil
        errorNavigationController = nil
    }


    func onEvent(_ event: MainCoordinatorEvent) {
        switch event {
        case .goMorning:
            showMorningScreen()
        case .goEvening:
            showEveningScreen()
        case .showError(let message):
            showErrorScreen(message: message)
        }
    }
}
