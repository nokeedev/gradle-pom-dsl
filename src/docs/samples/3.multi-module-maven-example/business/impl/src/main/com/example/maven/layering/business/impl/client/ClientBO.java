package com.example.maven.layering.business.impl.client;

import java.util.List;

import com.example.maven.layering.model.impl.client.Amount;
import com.example.maven.layering.model.impl.client.Client;
import com.example.maven.layering.model.impl.client.Product;

/**
 * Business Interface for Client.
 */
public interface ClientBO {

    /**
     * Sum of product amounts. Assumes Currency as EURO.
     * @param clientId clientId
     * @return the sum amount
     */
    Amount getClientProductsSum(long clientId);

    /**
     * Compares the new products with existing products and saves changed
     * products.
     * @param clientId clientId
     * @param newProducts
     */
    void saveChangedProducts(long clientId, List<Product> newProducts);

    /**
     * Calculates client product sum and saves it to database.
     * @param client
     */
    void calculateAndSaveClientProductSum(Client client);

}
