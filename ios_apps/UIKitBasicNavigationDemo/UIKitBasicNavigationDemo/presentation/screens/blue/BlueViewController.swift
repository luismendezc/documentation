//
//  BlueViewController.swift
//  UIKitBasicNavigationDemo
//
//  Created by Luis Enrique Mendez Cantero on 21.01.25.
//

import UIKit

class BlueViewController: UIViewController {

    @IBOutlet weak var mainButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    

    
    @IBAction func goToPink(_ sender: UIButton) {
        let pinkVC: PinkViewController = UIStoryboard.instantiate(from: .pink)
        navigationController?.pushViewController(pinkVC, animated: true)
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
