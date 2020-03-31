package com.example.maven.layering.model.impl.client;

import java.math.BigDecimal;
import java.util.List;

/**
 * Client Model API.
 */
public interface Client {

	long getId();

	String getName();

	Enum<?> getType();

	List<Collateral> getCollaterals();

	List<Product> getProducts();

	void setProductAmount(BigDecimal productAmount);

	BigDecimal getProductAmount();

}
