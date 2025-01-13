//
//  ViewController.swift
//  SplitVersionsUIKit
//
//  Created by Luis Enrique Mendez Cantero on 13.01.25.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        // Retrieve values from Info.plist
               if let appEnvironment = Bundle.main.object(forInfoDictionaryKey: "AppEnvironment") as? String {
                   print("App Environment: \(appEnvironment)")
               }

               if let appColor = Bundle.main.object(forInfoDictionaryKey: "AppColor") as? String {
                   print("App Color: \(appColor)")
               }

               if let logLevel = Bundle.main.object(forInfoDictionaryKey: "LogLevel") as? String {
                   print("Log Level: \(logLevel)")
               }
    }


}

