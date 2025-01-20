//
//  MorningViewModel.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

class MorningViewModel {
    let onEvent: (MainCoordinatorEvent) -> Void

    init(onEvent: @escaping (MainCoordinatorEvent) -> Void) {
        self.onEvent = onEvent
    }

    func goToEvening() {
        onEvent(.goEvening)
    }

    func showError() {
        onEvent(.showError(message: "Something went wrong!"))
    }
}
