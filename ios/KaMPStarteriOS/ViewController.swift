//
//  ViewController.swift
//  KaMPStarteriOS
//
//  Copyright © 2020 Ryan Simon. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UISearchBarDelegate {
    
    
    @IBOutlet weak var businessCollectionView: UICollectionView!
    var businessInfo: Array<KotlinPair<Business, BusinessReview>> = []
    
    private var getBusinessesAndTopReviewsBySearch: GetBusinessesAndTopReviewsBySearch?

    override func viewDidLoad() {
        super.viewDidLoad()

        getBusinessesAndTopReviewsBySearch = GetBusinessesAndTopReviewsBySearch()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.getBusinessesAndTopReviewsBySearch?.cancel()
        self.getBusinessesAndTopReviewsBySearch = nil
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return businessInfo.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell: BusinessCell = collectionView.dequeueReusableCell(withReuseIdentifier: "BusinessCell", for: indexPath) as! BusinessCell
        
        let businessInfo = self.businessInfo[indexPath.row]
        cell.bind(businessInfo: businessInfo)
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        let searchView: UICollectionReusableView = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: "searchBar", for: indexPath)
        
        return searchView
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if let searchText = searchBar.text {
            getBusinessesAndTopReviewsBySearch?.invoke(params: GetBusinessesAndTopReviewsBySearch.Params(searchTerm: searchText, location: "San Francisco, CA", numResults: 20, numResultsToSkip: 0), onResult: { (either: Either) in either.either(fnL: { _ in
                    
                }) { (success: Any) -> Any in
                    self.businessInfo = success as! NSArray as! Array<KotlinPair<Business, BusinessReview>>
                    return self.businessCollectionView.reloadData()
                }
            })
        }
    }
}
