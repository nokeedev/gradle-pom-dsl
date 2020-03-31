package com.example.maven.layering.model.impl.client;

/**
 * Collateral Model API.
 */
public interface Collateral {

	long getId();

	String getName();

    CollateralType getType();
}
