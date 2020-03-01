//
//  UIImageViewExt.swift
//  KaMPStarteriOS
//
//  Created by Ryan Simon on 2/29/20.
//  Copyright © 2020 Touchlab. All rights reserved.
//

import UIKit

extension UIImageView {
    func setImage(from url: URL, withPlaceholder placeholder: UIImage? = nil) {
        self.image = placeholder
        URLSession.shared.dataTask(with: url) { data, _, _ in
            if let data = data {
                let image = UIImage(data: data)
                DispatchQueue.main.async {
                    self.image = image
                }
            }
        }.resume()
    }
}
