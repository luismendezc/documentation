//
//  Extensions.swift
//  CoordinatorPatternProgramaticNavigation
//
//  Created by Luis Enrique Mendez Cantero on 19.01.25.
//

import UIKit

extension UIStoryboard {
    static func instantiateViewController<T: UIViewController>(
        from storyboardName: String,
        ofType type: T.Type
    ) -> T {
        let storyboard = UIStoryboard(name: storyboardName, bundle: nil)
        return storyboard.instantiateViewController(withIdentifier: String(describing: type)) as! T
    }
}

