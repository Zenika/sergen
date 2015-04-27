package com.zenika.sergen.product;

import lombok.Data;

import java.util.Random;

/**
 * Created by Gwennael on 22/02/2015.
 */
@Data
public class Product {
    private Long _id;
    private Long SKU;
    private String name;

    /**
     * Default constructor.
     * Generate a null _id
     */
    public Product() {
        this._id = Long.valueOf(10);
        this.SKU = (long) 124;
        this.name = "Marcel";
    }

    /**
     * Generate a random _id
     *
     * @param sku  SKU for this new {@link Product}
     * @param name Name for this new {@link Product}
     */
    public Product(Long sku, String name) {
        this._id = new Random().nextLong();
        this.SKU = sku;
        this.name = name;
    }
}
