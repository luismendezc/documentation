//
//  ErrorViewController.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class ErrorViewController: UIViewController {
    var viewModel: ErrorViewModel?

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .red
        title = "Error"

        let closeButton = UIButton(type: .system)
        closeButton.setTitle("Close", for: .normal)
        closeButton.addTarget(self, action: #selector(didTapClose), for: .touchUpInside)

        view.addSubview(closeButton)
        closeButton.translatesAutoresizingMaskIntoConstraints = false

        NSLayoutConstraint.activate([
            closeButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            closeButton.centerYAnchor.constraint(equalTo: view.centerYAnchor),
        ])
    }

    @objc private func didTapClose() {
        viewModel?.closeErrorFlow()
    }
}

