package com.zenika.sergen.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Gwennael on 22/02/2015.
 */
@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    /**
     * @return Number of {@link Product} in database
     * @since 1.0
     */
    public Long getCount() {
        return productDAO.count();
    }
}
