//
//  ErrorCoordinator.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class ErrorCoordinator: Coordinator {
    typealias Event = ErrorCoordinatorEvent

    var childCoordinators: [any Coordinator] = []
    let navigationController: CoordinatorNavigationController

    init(navigationController: CoordinatorNavigationController) {
        self.navigationController = navigationController
    }

    func start() {
        // No-op
    }

    func start(with message: String) {
        let errorVC: ErrorViewController = UIStoryboard.instantiateViewController(
            from: "Error",
            ofType: ErrorViewController.self
        )
        let viewModel = ErrorViewModel(message: message, closeErrorFlow: { [weak self] in
            self?.handleClose()
        })
        errorVC.viewModel = viewModel
        navigationController.setViewControllers([errorVC], animated: true)
    }

    private func handleClose() {
        // Notify the parent coordinator (MainCoordinator) to switch back
        (navigationController.parentCoordinator as? MainCoordinator)?.returnToMainFlow()
    }

    func onEvent(_ event: ErrorCoordinatorEvent) {
        // Handle events specific to the error flow
    }
}




