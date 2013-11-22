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

package org.vaadin.tltv.layout;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.tltv.layout.client.DynamicVerticalLayoutClientRpc;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * DynamicVerticalLayout extends {@link VerticalLayout}. It can be used to
 * listen for VerticalLayout's slot elements resize events and set min-height
 * for them by calculating the required height for that slot's content. Adding
 * min-height this way helps to avoid situations where layout's slots are
 * rendered over each others.
 * 
 * @author Tltv
 * 
 */
public class DynamicVerticalLayout extends VerticalLayout {

    private final Set<Component> dynamicMinHeights = new HashSet<Component>();

    public DynamicVerticalLayout() {
    }

    public void setDynamicMinHeightFor(Component target) {
        if (target == null || isDynamicMinHeight(target)) {
            return;
        }
        dynamicMinHeights.add(target);

        getRpcProxy(DynamicVerticalLayoutClientRpc.class)
                .setDynamicMinHeightFor(target);
    }

    public void removeDynamicMinHeightFor(Component target) {
        if (target == null || !isDynamicMinHeight(target)) {
            return;
        }
        dynamicMinHeights.remove(target);

        getRpcProxy(DynamicVerticalLayoutClientRpc.class)
                .removeDynamicMinHeightFor(target);
    }

    public boolean isDynamicMinHeight(Component target) {
        return dynamicMinHeights.contains(target);
    }
}
