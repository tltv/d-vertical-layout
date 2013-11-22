package org.vaadin.tltv.layout.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.vaadin.tltv.layout.DynamicVerticalLayout;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("demo")
@Title("DynamicVerticalLayout Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    private enum Type {
        Label,
        VerticalLayout,
        DynamicVerticalLayout,
        PanelWithVerticalLayout,
        PanelWithDynamicVerticalLayout,
    }

    private final LayoutClickListener layoutClickListener = new LayoutClickListener() {

        @Override
        public void layoutClick(LayoutClickEvent event) {
            if (event.getButton() != MouseButton.LEFT) {
                return;
            }

            if (event.getChildComponent() == null) {
                return;
            }
            showLayoutEditor((AbstractOrderedLayout) event.getComponent(),
                    event.getChildComponent(), event.getClientX(),
                    event.getClientY());
        }
    };

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout main = new VerticalLayout();

        main.addComponent(createToolbar());

        Window win = createWindow("VerticalLayout");
        win.setContent(createNormalVerticalLayout());
        addWin(win);

        win = createWindow("DynamicVerticalLayout");
        win.setContent(createDynamicVerticalLayout());
        addWin(win);

        setContent(main);
    }

    private Component createToolbar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setCaption("You can drag elements from here:");

        addDragBoxes(layout);

        return layout;
    }

    private void addDragBoxes(HorizontalLayout layout) {
        Component c;
        for (Type type : Type.values()) {
            c = createDragBox(type);
            layout.addComponent(c);
            layout.setComponentAlignment(c, Alignment.MIDDLE_LEFT);
        }
    }

    private DragAndDropWrapper createDragBox(Type type) {
        Label dragElement = new Label("<a href='javascript:void'>"
                + type.name() + "</a>", ContentMode.HTML);
        dragElement.setHeight(50, Unit.PIXELS);
        dragElement.setWidth(100, Unit.PERCENTAGE);
        dragElement.setData(type);
        DragAndDropWrapper wrap = new DragAndDropWrapper(dragElement);
        wrap.setDragStartMode(DragStartMode.COMPONENT);
        return wrap;
    }

    private void addWin(Window w) {
        addWindow(w);
        int p = 100 / getWindows().size();
        int width = new BigDecimal(p).divide(new BigDecimal(100.0))
                .multiply(new BigDecimal(getPage().getBrowserWindowWidth()))
                .intValue();
        int x = 0;
        for (Window win : getWindows()) {
            win.setWidth(p - 1, Unit.PERCENTAGE);
            win.setPositionX(x + 5);
            x += width;
        }
    }

    private Window createWindow(String caption) {
        Window window = new Window(caption);
        window.addStyleName("win");
        window.setWidth(70, Unit.PERCENTAGE);
        window.setHeight(70, Unit.PERCENTAGE);
        window.setPositionY(50);
        window.setClosable(false);
        return window;
    }

    private Layout createDynamicVerticalLayout() {
        final DynamicVerticalLayout layout = createEditableDynamicVerticalLayout();
        fillLayout(layout);

        layout.setExpandRatio(layout.getComponent(1), 1);
        layout.setDynamicMinHeightFor(layout.getComponent(1));
        layout.getComponent(1).addStyleName("dynamic");
        return layout;
    }

    private Layout createNormalVerticalLayout() {
        VerticalLayout layout = createEditableVerticalLayout();
        fillLayout(layout);

        layout.setExpandRatio(layout.getComponent(1), 1);
        return layout;
    }

    private VerticalLayout createEditableVerticalLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addLayoutClickListener(layoutClickListener);
        return layout;
    }

    private DynamicVerticalLayout createEditableDynamicVerticalLayout() {
        DynamicVerticalLayout layout = new DynamicVerticalLayout();
        layout.setSizeFull();

        layout.addLayoutClickListener(layoutClickListener);
        return layout;
    }

    private Panel createPanel(Layout content) {
        Panel p = new Panel();
        p.setSizeFull();
        p.setContent(content);
        return p;
    }

    private void fillLayout(AbstractOrderedLayout layout) {
        layout.addComponent(createDropTarget(layout,
                createComponent(Type.Label)));
        layout.addComponent(createDropTarget(layout,
                createComponent(Type.Label)));
        layout.addComponent(createDropTarget(layout,
                createComponent(Type.Label)));
    }

    private DragAndDropWrapper createDropTarget(
            final AbstractOrderedLayout layout, Component component) {
        DragAndDropWrapper wrap = new DragAndDropWrapper(component);
        wrap.setData("" + component.getClass().getSimpleName());
        wrap.setDropHandler(new DropHandler() {

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return AcceptAll.get();
            }

            @Override
            public void drop(DragAndDropEvent event) {

                WrapperTransferable t = (WrapperTransferable) event
                        .getTransferable();

                Type type = (Type) ((AbstractComponent) t.getDraggedComponent())
                        .getData();
                layout.addComponent(
                        createDropTarget(layout, createComponent(type)), layout
                                .getComponentIndex(event.getTargetDetails()
                                        .getTarget()));
            }
        });
        return wrap;
    }

    private Component createComponent(Type type) {
        if (type == Type.VerticalLayout) {
            AbstractOrderedLayout l = createEditableVerticalLayout();
            l.addStyleName("normal-layout");
            l.addStyleName("added");
            l.addComponent(createDropTarget(l, createComponent(Type.Label)));
            return l;

        } else if (type == Type.DynamicVerticalLayout) {
            AbstractOrderedLayout l = createEditableDynamicVerticalLayout();
            l.addStyleName("dynamic-layout");
            l.addStyleName("added");
            l.addStyleName("dynamic");
            l.addComponent(createDropTarget(l, createComponent(Type.Label)));
            return l;

        } else if (type == Type.PanelWithVerticalLayout) {
            Panel p = createPanel(createEditableVerticalLayout());
            p.getContent().setHeight(null);
            p.addStyleName("panel");
            p.addStyleName("added");
            ((Layout) p.getContent()).addComponent(createDropTarget(
                    (AbstractOrderedLayout) p.getContent(),
                    createComponent(Type.Label)));
            return p;

        } else if (type == Type.PanelWithDynamicVerticalLayout) {
            Panel p = createPanel(createEditableDynamicVerticalLayout());
            p.getContent().setHeight(null);
            p.addStyleName("panel");
            p.addStyleName("added");
            p.addStyleName("dynamic");
            ((Layout) p.getContent()).addComponent(createDropTarget(
                    (AbstractOrderedLayout) p.getContent(),
                    createComponent(Type.Label)));
            return p;
        }

        return createLoremIpsumLabel();
    }

    private Label createLoremIpsumLabel() {
        Label l = new Label(
                "Lorem ipsum dolor sit amet, te sint viris tation quo, vim noster fierent no. Cu sea solum voluptua urbanitas, solet malorum ocurreret et quo. Pro nonumy conceptam ea, hinc debet noster in vel. Ut nec causae adipisci intellegat, vix et euripidis tincidunt vituperatoribus. Aliquip vituperatoribus id usu, quo ne ferri rationibus.");
        return l;
    }

    private void showLayoutEditor(AbstractOrderedLayout layout,
            Component component, int x, int y) {
        if (component == null) {
            return;
        }

        if (!(component instanceof DragAndDropWrapper)) {
            component = component.getParent();
            if (!(component instanceof DragAndDropWrapper)) {
                return;
            }
        }

        Window w = new Window("Layout Editor: "
                + ((DragAndDropWrapper) component).getData());
        w.setModal(true);
        w.setPositionX(x);
        w.setPositionY(y);
        try {
            VerticalLayout content = createLayoutEditor(layout, component);
            w.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        w.setCloseShortcut(KeyCode.ESCAPE);
        addWindow(w);
    }

    private VerticalLayout createLayoutEditor(
            final AbstractOrderedLayout layout, final Component component) {
        final VerticalLayout main = new VerticalLayout();
        main.setMargin(true);
        main.setSizeUndefined();

        FormLayout l = new FormLayout();

        fillExpandRatioEditor(layout, component, l);

        if (layout instanceof DynamicVerticalLayout) {
            fillDynamicMinHeightEditor(layout, component, l);
        }

        if (component instanceof DragAndDropWrapper) {
            Component c = ((DragAndDropWrapper) component).iterator().next();
            if (c instanceof Panel) {
                fillPanelEditors(l, (Panel) c);

            } else if (c instanceof Label) {
                fillLabelEditors(l, (Label) c);
            }
        }

        Button close = new Button("Close", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window r = null;
                for (Window w : getWindows()) {
                    if (w.getContent() == main) {
                        r = w;
                        break;
                    }
                }
                if (r != null) {
                    removeWindow(r);
                }
            }
        });
        main.addComponent(l);
        main.addComponent(close);
        main.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);
        main.setExpandRatio(l, 1);

        close.focus();
        return main;
    }

    private void fillExpandRatioEditor(final AbstractOrderedLayout layout,
            final Component component, FormLayout l) {
        TextField expandRatioField = createExpandRatioEditor(layout, component);
        l.addComponent(expandRatioField);
    }

    private void fillDynamicMinHeightEditor(final AbstractOrderedLayout layout,
            final Component component, FormLayout l) {
        CheckBox dynamicMinHeightCheckBox = createDynamicMinHeightCheckBox(
                layout, component);
        l.addComponent(dynamicMinHeightCheckBox);
    }

    private void fillLabelEditors(FormLayout l, Label c) {
        l.addComponent(createTextEditor(c));
    }

    private void fillPanelEditors(FormLayout l, Panel c) {
        HorizontalLayout heightAndUnit = new HorizontalLayout(
                createHeightEditor(c), createHeightUnitEditor(c));
        heightAndUnit.setCaption("Height");
        HorizontalLayout widthAndUnit = new HorizontalLayout(
                createWidthEditor(c), createWidthUnitEditor(c));
        widthAndUnit.setCaption("Width");

        HorizontalLayout contentHeightAndUnit = new HorizontalLayout(
                createHeightEditor(c.getContent()),
                createHeightUnitEditor(c.getContent()));
        contentHeightAndUnit.setCaption("Content Height");
        HorizontalLayout contentWidthAndUnit = new HorizontalLayout(
                createWidthEditor(c.getContent()),
                createWidthUnitEditor(c.getContent()));
        contentWidthAndUnit.setCaption("Content Width");

        l.addComponent(heightAndUnit);
        l.addComponent(widthAndUnit);
        l.addComponent(contentHeightAndUnit);
        l.addComponent(contentWidthAndUnit);
    }

    private CheckBox createDynamicMinHeightCheckBox(
            final AbstractOrderedLayout layout, final Component component) {
        CheckBox dynamicMinHeightCheckBox = new CheckBox(
                "Enable dynamic min-height");
        dynamicMinHeightCheckBox.setValue(((DynamicVerticalLayout) layout)
                .isDynamicMinHeight(component));
        dynamicMinHeightCheckBox
                .addValueChangeListener(new ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Boolean value = (Boolean) event.getProperty()
                                .getValue();
                        if (value) {
                            ((DynamicVerticalLayout) layout)
                                    .setDynamicMinHeightFor(component);
                            component.addStyleName("dynamic");
                        } else {
                            ((DynamicVerticalLayout) layout)
                                    .removeDynamicMinHeightFor(component);
                            component.removeStyleName("dynamic");
                        }
                    }
                });
        return dynamicMinHeightCheckBox;
    }

    private TextField createExpandRatioEditor(
            final AbstractOrderedLayout layout, final Component component) {
        return createNumberEditor("Expand ratio",
                layout.getExpandRatio(component), component,
                new NumberValueChange() {

                    @Override
                    public void onValueChange(float number) {
                        layout.setExpandRatio(component, number);
                    }
                });
    }

    private TextField createHeightEditor(final Component component) {
        return createNumberEditor("Height", component.getHeight(), component,
                new NumberValueChange() {

                    @Override
                    public void onValueChange(float number) {
                        component.setHeight(number, Unit.PERCENTAGE);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private TextField createTextEditor(final AbstractComponent component) {
        return createTextEditor("Value",
                ((Property<String>) component).getValue(), component,
                new TextValueChange() {

                    @Override
                    public void onValueChange(String value) {
                        ((Property<String>) component).setValue(value);
                    }
                });
    }

    private TextField createWidthEditor(final Component component) {
        return createNumberEditor("Width", component.getWidth(), component,
                new NumberValueChange() {

                    @Override
                    public void onValueChange(float number) {
                        component.setWidth(number, Unit.PERCENTAGE);
                    }
                });
    }

    private TextField createNumberEditor(String caption, float value,
            final Component component, final NumberValueChange valueChange) {
        TextField expandRatioField = new TextField(caption);
        expandRatioField.setValue("" + value);
        expandRatioField.setImmediate(true);
        expandRatioField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object v = event.getProperty().getValue();
                try {
                    float f = Float.parseFloat("" + v);
                    valueChange.onValueChange(f);
                } catch (NumberFormatException e) {
                    Notification
                            .show("Invalid floating number! Format is 123.345");
                }
            }
        });
        return expandRatioField;
    }

    private TextField createTextEditor(String caption, String value,
            final Component component, final TextValueChange valueChange) {
        TextField expandRatioField = new TextField(caption);
        expandRatioField.setValue("" + value);
        expandRatioField.setImmediate(true);
        expandRatioField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object v = event.getProperty().getValue();
                valueChange.onValueChange(String.valueOf(v));
            }
        });
        return expandRatioField;
    }

    private NativeSelect createHeightUnitEditor(final Component component) {
        return createNativeSelectEditor("Height Unit",
                component.getHeightUnits(), Arrays.asList(Unit.values()),
                new SelectValueChange() {

                    @Override
                    public void onValueChange(Object unit) {
                        component.setHeight(component.getHeight(), (Unit) unit);
                    }
                });
    }

    private NativeSelect createWidthUnitEditor(final Component component) {
        return createNativeSelectEditor("Width Unit",
                component.getWidthUnits(), Arrays.asList(Unit.values()),
                new SelectValueChange() {

                    @Override
                    public void onValueChange(Object unit) {
                        component.setWidth(component.getWidth(), (Unit) unit);
                    }
                });
    }

    private NativeSelect createNativeSelectEditor(String caption, Object value,
            Collection<?> items, final SelectValueChange valueChange) {
        NativeSelect s = new NativeSelect(caption);
        for (Object i : items) {
            s.addItem(i);
            s.setItemCaption(i, String.valueOf(i));
        }
        s.setNullSelectionAllowed(false);
        s.setValue(value);
        s.setImmediate(true);
        s.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                valueChange.onValueChange(event.getProperty().getValue());
            }
        });
        return s;
    }

    interface TextValueChange {
        void onValueChange(String value);
    }

    interface NumberValueChange {
        void onValueChange(float number);
    }

    interface SelectValueChange {
        void onValueChange(Object value);
    }
}
