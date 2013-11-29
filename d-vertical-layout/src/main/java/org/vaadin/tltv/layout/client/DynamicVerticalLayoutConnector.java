/*
 * Copyright 2013 Tomi Virtanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.tltv.layout.client;

import org.vaadin.tltv.layout.DynamicVerticalLayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * Connector for DynamicVerticalLayout.
 * 
 * @author Tltv
 * 
 */
@Connect(value = DynamicVerticalLayout.class, loadStyle = LoadStyle.EAGER)
public class DynamicVerticalLayoutConnector extends
        AbstractOrderedLayoutConnector {

    public DynamicVerticalLayoutConnector() {

        registerRpc(DynamicVerticalLayoutClientRpc.class,
                new DynamicVerticalLayoutClientRpc() {
                    @Override
                    public void setDynamicMinHeightFor(Connector target) {
                        if (target == null) {
                            return;
                        }
                        getWidget().setDynamicMinHeightFor(
                                ((ComponentConnector) target).getWidget());
                    }

                    @Override
                    public void removeDynamicMinHeightFor(Connector target) {
                        getWidget().removeDynamicMinHeightFor(
                                ((ComponentConnector) target).getWidget());
                    }
                });

    }

    @Override
    protected Widget createWidget() {
        return GWT.create(DynamicVerticalLayoutWidget.class);
    }

    @Override
    public DynamicVerticalLayoutWidget getWidget() {
        return (DynamicVerticalLayoutWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public void onUnregister() {
        getWidget().clearDynamicMinHeights();
        super.onUnregister();
    }

}
