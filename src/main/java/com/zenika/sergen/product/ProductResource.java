package com.zenika.sergen.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Gwennael on 22/02/2015.
 */
@Path("products")
@Slf4j
public class ProductResource {

    @Autowired
    private ProductService productService;

    public ProductResource() {
        log.info("ProductResource Created!");
    }


    /**
     * @return Number of {@link Product} in database
     * @since 1.0
     */
    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Long getCount() {
        return productService.getCount();
    }

}
