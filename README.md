# DynamicVerticalLayout component for Vaadin 7

DynamicVerticalLayout is an UI component for Vaadin 7.

Component extends VerticalLayout and adds few new methods in the API:
* setDynamicMinHeightFor(Component)
* removeDynamicMinHeightFor(Component)
* isDynamicMinHeight(Component)

With these methods, you can enable or disable automatic min-height calculation for a individual component 
in the layout. min-height will be the actual height of the component, which means the 'total height 
required for the content in the component's element'. min-height CSS attribute is attached into the 
target component's wrapper element (slot). Resizing the layout will update the min-height automatically.

This helps for example, when expand ratios are used, and slots overlaps with each others and you want the 
minimum height to be the slot content's required height.

Works with Vaadin 7.x. Tested up to Vaadin 7.1.8.

 
## Building and running demo

* git clone <url of the DynamicVerticalLayout repository>
* mvn clean install
* cd demo
* mvn jetty:run

To see the demo, navigate to http://localhost:8080/d-vertical-layout-demo



## License & Author

Distributed under Apache License 2.0. For license terms, see LICENSE.txt.

DynamicVerticalLayout is written by Tomi Virtanen