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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VVerticalLayout;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.client.ui.orderedlayout.Slot;

/**
 * DynamicVerticalLayoutWidget extends {@link VVerticalLayout}. It can be used
 * to listen for VVerticalLayout's slot elements resize events and set
 * min-height for them by calculating the required height for that slot's
 * content. Adding min-height this way helps to avoid situations where layout's
 * slots are rendered over each others.
 * 
 * @author Tltv
 * 
 */
public class DynamicVerticalLayoutWidget extends VVerticalLayout {

    private final ElementResizeListener elResizeListener = new ElementResizeListener() {

        @Override
        public void onElementResize(ElementResizeEvent e) {
            int newSlotHeight = e.getElement().getClientHeight();

            Element slotContent = e.getElement().getFirstChildElement();
            int slotContentHeight = slotContent.getClientHeight();
            GWT.log("New slot h: " + newSlotHeight);
            GWT.log("New slot content h: " + slotContentHeight);
            e.getElement().getStyle()
                    .setProperty("minHeight", slotContentHeight + "px");
        }
    };

    public DynamicVerticalLayoutWidget() {
        super();
        addStyleName("d-vertical-layout");

    }

    public void setDynamicMinHeightFor(Widget widget) {
        if (widget == null) {
            return;
        }
        Slot slot = getSlot(widget);
        if (slot == null) {
            return;
        }

        getLayoutManager().addElementResizeListener(slot.getElement(),
                elResizeListener);
    }

    public void removeDynamicMinHeightFor(Widget widget) {
        if (widget == null) {
            return;
        }
        Slot slot = getSlot(widget);
        if (slot == null) {
            return;
        }
        getLayoutManager().removeElementResizeListener(slot.getElement(),
                elResizeListener);
        clearMinWidth(slot.getElement());
    }

    public void clearDynamicMinHeights() {
        for (Widget child : getChildren()) {
            removeDynamicMinHeightFor(child);
        }
    }

    private void clearMinWidth(Element element) {
        if (element == null) {
            return;
        }
        element.getStyle().clearProperty("minHeight");
    }
}