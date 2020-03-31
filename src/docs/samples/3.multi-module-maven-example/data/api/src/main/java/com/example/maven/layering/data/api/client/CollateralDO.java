package com.example.maven.layering.data.api.client;

import java.util.List;

import com.example.maven.layering.model.api.client.Collateral;

/**
 * Data Object for the Client entity.
 */
public interface CollateralDO {

    /**
     * Retrieve collaterals for the client.
     *
     * @param clientId client id.
     */
    List<Collateral> getAllCollaterals(long clientId);
}
