//
//  ViewController.swift
//  KaMPStarteriOS
//
//  Copyright © 2020 Ryan Simon. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate {
    

    @IBOutlet weak var businessNameTableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    var businessInfo: Array<KotlinPair<Business, BusinessReview>> = []
    
    private var getBusinessesAndTopReviewsBySearch: GetBusinessesAndTopReviewsBySearch?

    override func viewDidLoad() {
        super.viewDidLoad()
        businessNameTableView.delegate = self
        businessNameTableView.dataSource = self
        searchBar.delegate = self
        
        getBusinessesAndTopReviewsBySearch = GetBusinessesAndTopReviewsBySearch()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.getBusinessesAndTopReviewsBySearch?.cancel()
        self.getBusinessesAndTopReviewsBySearch = nil
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return businessInfo.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BusinessNameCell", for: indexPath)
        if let businessNameCell = cell as? BusinessNameCell {
            let businessInfo = self.businessInfo[indexPath.row]
            businessNameCell.bind(businessInfo: businessInfo)
        }
        return cell
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if let searchText = searchBar.text {
            getBusinessesAndTopReviewsBySearch?.invoke(params: GetBusinessesAndTopReviewsBySearch.Params(searchTerm: searchText, location: "San Francisco, CA", numResults: 20, numResultsToSkip: 0), onResult: { (either: Either) in either.either(fnL: { _ in
                    
                }) { (success: Any) -> Any in
                    self.businessInfo = success as! NSArray as! Array<KotlinPair<Business, BusinessReview>>
                    return self.businessNameTableView.reloadData()
                }
            })
        }
    }
}

class BusinessNameCell: UITableViewCell {
    
    @IBOutlet weak var businessNameLabel: UILabel!
    
    var business: Business?
    var businessReview: BusinessReview?
    
    func bind(businessInfo: KotlinPair<Business, BusinessReview>){
        self.business = businessInfo.first
        self.businessReview = businessInfo.second
        businessNameLabel.text = business?.name
    }
}
