//
//  EveningViewModel.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

class EveningViewModel {
    let onEvent: (MainCoordinatorEvent) -> Void

    init(onEvent: @escaping (MainCoordinatorEvent) -> Void) {
        self.onEvent = onEvent
    }

    func goToMorning() {
        onEvent(.goMorning)
    }

    func showError() {
        onEvent(.showError(message: "An error occurred in the evening screen."))
    }
}
