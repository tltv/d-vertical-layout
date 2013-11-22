package org.vaadin.tltv.layout.client;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ClientRpc;

public interface DynamicVerticalLayoutClientRpc extends ClientRpc {

    public void setDynamicMinHeightFor(Connector target);

    public void removeDynamicMinHeightFor(Connector target);

}