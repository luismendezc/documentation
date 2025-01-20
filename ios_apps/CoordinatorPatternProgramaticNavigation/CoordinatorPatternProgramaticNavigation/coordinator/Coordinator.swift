//
//  Coordinator.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

protocol Coordinator: AnyObject {
    associatedtype Event: AppEvent
    var childCoordinators: [any Coordinator] { get set }
    func start()
    func onEvent(_ event: Event)
}


