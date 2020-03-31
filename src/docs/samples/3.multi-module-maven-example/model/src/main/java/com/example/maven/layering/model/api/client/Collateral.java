package com.example.maven.layering.model.api.client;

/**
 * Collateral Model API.
 */
public interface Collateral {

	long getId();

	String getName();

    CollateralType getType();
}
