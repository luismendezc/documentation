//
//  EveningViewController.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class EveningViewController: UIViewController {
    var viewModel: EveningViewModel?

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .orange
        title = "Good Evening"

        let button = UIButton(type: .system)
        button.setTitle("Go to Morning", for: .normal)
        button.addTarget(self, action: #selector(didTapMorning), for: .touchUpInside)

        let errorButton = UIButton(type: .system)
        errorButton.setTitle("Show Error", for: .normal)
        errorButton.addTarget(self, action: #selector(didTapError), for: .touchUpInside)

        let stackView = UIStackView(arrangedSubviews: [button, errorButton])
        stackView.axis = .vertical
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            stackView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
        ])
    }

    @objc private func didTapMorning() {
        viewModel?.goToMorning()
    }

    @objc private func didTapError() {
        viewModel?.showError()
    }
}

