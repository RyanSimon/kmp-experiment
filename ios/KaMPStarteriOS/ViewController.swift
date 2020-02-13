//
//  ViewController.swift
//  KaMPStarteriOS
//
//  Created by Kevin Schildhorn on 12/18/19.
//  Copyright © 2019 Touchlab. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    

    @IBOutlet weak var businessNameTableView: UITableView!
    var businessInfo: NSArray = NSArray()
    
    private var getBusinessesAndTopReviewsBySearch: GetBusinessesAndTopReviewsBySearch?

    override func viewDidLoad() {
        super.viewDidLoad()
        businessNameTableView.delegate = self
        businessNameTableView.dataSource = self
        
        getBusinessesAndTopReviewsBySearch = GetBusinessesAndTopReviewsBySearch()
        
        getBusinessesAndTopReviewsBySearch!.invoke(params: GetBusinessesAndTopReviewsBySearch.Params(searchTerm: "chocolate", location: "San Francisco, CA", numResults: 20, numResultsToSkip: 0), onResult: { (either: Either) in either.either(fnL: { _ in
                
            }) { (success: Any?) -> Any in
                self.businessInfo = success as! NSMutableArray as NSArray
                return self.businessNameTableView.reloadData()
            }
        })
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.getBusinessesAndTopReviewsBySearch?.cancel()
        self.getBusinessesAndTopReviewsBySearch = nil
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return businessInfo.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BusinessNameCell", for: indexPath)
        if let businessNameCell = cell as? BusinessNameCell {
            let businessInfo = self.businessInfo[indexPath.row] as Any
            businessNameCell.bind(businessInfo: businessInfo as! (Business, BusinessReview))
        }
        return cell
    }
}

class BusinessNameCell: UITableViewCell {
    
    @IBOutlet weak var businessNameLabel: UILabel!
    
    var business: Business?
    var businessReview: BusinessReview?
    
    func bind(businessInfo: (Business, BusinessReview)){
        self.business = businessInfo.0
        self.businessReview = businessInfo.1
        businessNameLabel.text = business?.name
    }
}
