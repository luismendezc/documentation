//
//  ErrorViewModel.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

class ErrorViewModel {
    let message: String
    let closeErrorFlow: () -> Void

    init(message: String, closeErrorFlow: @escaping () -> Void) {
        self.message = message
        self.closeErrorFlow = closeErrorFlow
    }
}

