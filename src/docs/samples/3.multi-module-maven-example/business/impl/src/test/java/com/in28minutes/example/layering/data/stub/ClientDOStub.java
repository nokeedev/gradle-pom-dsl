package com.example.maven.layering.data.stub;

import org.springframework.stereotype.Component;

import com.example.maven.layering.data.api.client.ClientDO;
import com.example.maven.layering.model.api.client.Client;

@Component
public class ClientDOStub implements ClientDO {

	@Override
	public Client getClientDetails(long clientId) {
		return null;
	}

	@Override
	public void saveClient(Client client) {

	}
}
