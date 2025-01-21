//
//  StoryboardExtensions.swift
//  UIKitBasicNavigationDemo
//
//  Created by Luis Enrique Mendez Cantero on 21.01.25.
//

import Foundation
import UIKit

enum Storyboard: String {
    case nativeSplash =  "NativeSplash"
    case orange = "Orange"
    case blue = "Blue"
    case pink = "Pink"
    case red = "Red"
}

extension UIStoryboard {
    
    static func instantiate<T: UIViewController>(from storyboard: Storyboard, as type: T.Type =  T.self) -> T {
        let instancedStoryboard = UIStoryboard(name: storyboard.rawValue, bundle: nil)
        let identifier = String(describing: type)
        guard let viewController = instancedStoryboard.instantiateViewController(withIdentifier: identifier) as? T else {
            fatalError("ViewController with identifier \(identifier) not found in \(storyboard.rawValue) storyboard")
        }
        return viewController
    }
        
}
