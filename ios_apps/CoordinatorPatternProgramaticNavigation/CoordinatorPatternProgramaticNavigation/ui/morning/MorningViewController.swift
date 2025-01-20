//
//  MorningViewController.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

class MorningViewController: UIViewController {
    var viewModel: MorningViewModel?

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        title = "Good Morning"

        let button = UIButton(type: .system)
        button.setTitle("Go to Evening", for: .normal)
        button.addTarget(self, action: #selector(didTapEvening), for: .touchUpInside)

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

    @objc private func didTapEvening() {
        viewModel?.goToEvening()
    }

    @objc private func didTapError() {
        viewModel?.showError()
    }
}

