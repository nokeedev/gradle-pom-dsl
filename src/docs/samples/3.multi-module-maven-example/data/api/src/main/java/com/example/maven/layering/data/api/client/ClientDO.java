package com.example.maven.layering.data.api.client;

import com.example.maven.layering.model.api.client.Client;

/**
 * Data Object for the Client entity.
 */
public interface ClientDO {

    /**
     * Retrieves details of the client.
     *
     * @param clientId the client id to search for.
     */
    Client getClientDetails(long clientId);

	void saveClient(Client client);
}
