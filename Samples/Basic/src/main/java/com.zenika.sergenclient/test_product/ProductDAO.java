package com.zenika.sergenclient.test_product;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gwennael on 23/02/2015.
 *
 * @author Gwennael Buchet
 * @since 1.0
 */
@Component
public class ProductDAO {

    private ArrayList<Product> productsList = new ArrayList<>();

    public ProductDAO() {
        for (int i = 0; i < 10; i++) {
            Product p = new Product(new Random().nextLong(), "Marcel");
            this.insert(p);
        }
    }

    /**
     * Insert a Product
     *
     * @param product {@link Product} to insert
     * @return ID for the inserted {@link Product}
     * @since 1.0
     */
    public Long insert(Product product) {

        if (null == product.get_id()) {
            product.set_id(new Random().nextLong());
        }

        this.productsList.add(product);

        return product.get_id();
    }

    /**
     * @return Number of {@link Product} in database
     * @since 1.0
     */
    public Long count() {
        //todo Use database driver instead
        return (long) productsList.size();
    }


}
