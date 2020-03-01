//
//  BusinessCell.swift
//  KaMPStarteriOS
//
//  Created by Ryan Simon on 2/29/20.
//  Copyright © 2020 Touchlab. All rights reserved.
//

import UIKit
import shared

class BusinessCell: UICollectionViewCell {

    @IBOutlet weak var businessNameLabel: UILabel!
    @IBOutlet weak var businessImageView: UIImageView!
    
    var business: Business?
    var businessReview: BusinessReview?
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        // add shadow on cell
        layer.masksToBounds = false
        layer.shadowOpacity = 0.23
        layer.shadowRadius = 2
        layer.shadowOffset = CGSize(width: 0, height: 0)
        layer.shadowColor = UIColor.black.cgColor

        // add corner radius on `contentView`
        contentView.layer.cornerRadius = 4
        contentView.layer.masksToBounds = true
    }
    
    func bind(businessInfo: KotlinPair<Business, BusinessReview>){
        self.business = businessInfo.first
        self.businessReview = businessInfo.second
        businessNameLabel.text = business?.name
        if let url = URL(string: business?.imageUrl ?? "") {
            businessImageView.setImage(from: url)
        }
    }
}
