//
//  PinkViewController.swift
//  UIKitBasicNavigationDemo
//
//  Created by Luis Enrique Mendez Cantero on 21.01.25.
//

import UIKit

class PinkViewController: UIViewController {

    @IBOutlet weak var mainButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    

    @IBAction func showErrorModally(_ sender: UIButton) {
        let transition = CATransition()
        transition.duration = 0.3
        transition.type = .push
        transition.subtype = .fromRight
        transition.timingFunction = CAMediaTimingFunction(name: .easeInEaseOut)
        view.window!.layer.add(transition, forKey: kCATransition)
        let redVC: RedViewController = UIStoryboard.instantiate(from: .red)
        redVC.modalPresentationStyle = .fullScreen
        present(redVC, animated: false, completion: nil)
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
