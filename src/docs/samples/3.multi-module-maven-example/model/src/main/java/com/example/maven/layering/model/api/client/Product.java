package com.example.maven.layering.model.api.client;

/**
 * Product Model API.
 */
public interface Product {

	long getId();

    String getName();

    ProductType getType();

    Amount getAmount();
}
