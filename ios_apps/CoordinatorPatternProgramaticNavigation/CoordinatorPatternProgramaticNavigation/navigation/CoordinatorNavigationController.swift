//
//  CoordinatorNavigationController.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class CoordinatorNavigationController: UINavigationController {
    weak var parentCoordinator: (any Coordinator)?
}

