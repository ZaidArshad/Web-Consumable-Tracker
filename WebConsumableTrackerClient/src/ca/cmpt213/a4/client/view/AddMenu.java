package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ControlMenu;
import ca.cmpt213.a4.client.control.ServerRequest;
import ca.cmpt213.a4.client.model.*;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Creates a dialog to add items to the saved file
 */
public class AddMenu extends JDialog {
    private final JLabel templateLabelItem;
    private JComboBox<String> fieldType;
    private JTextField fieldName;
    private JTextField fieldNotes;
    private JTextField fieldPrice;
    private JTextField fieldQuantity;
    private DatePicker fieldExpiry;
    private JLabel labelQuantity;

    public AddMenu(JLabel templateLabelItem) {
        this.templateLabelItem = templateLabelItem;
    }

    public void showMenu(ControlMenu controlMenu) {

        // Main panel that holds other panels vertically
        JPanel panelAdd = new JPanel();
        panelAdd.setBorder(new EmptyBorder(10,10,10,10));
        panelAdd.setLayout(new BoxLayout(panelAdd, BoxLayout.Y_AXIS));
        JLabel labelTitle = new JLabel("Add Item");
        labelTitle.setBorder(new EmptyBorder(0,0,10,0));
        labelTitle.setFont(templateLabelItem.getFont());
        panelAdd.add(labelTitle);

        // Panel that holds the 2 panels that hold labels and field horizontally
        JPanel panelAttributes = new JPanel();
        panelAttributes.setLayout(new BoxLayout(panelAttributes, BoxLayout.X_AXIS));
        panelAdd.add(panelAttributes);

        createAttributeFields(panelAttributes);
        createButtonPanel(panelAdd, controlMenu);

        this.setTitle("Add Item");
        this.setResizable(false);
        this.setContentPane(panelAdd);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }


    private void createButtonPanel(JPanel parent, ControlMenu controlMenu) {
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.X_AXIS));
        panelButton.setBorder(new EmptyBorder(10,10,10,10));

        JButton buttonCancel = new JButton("Cancel");
        JButton buttonCreate = new JButton("Create");

        buttonCreate.addActionListener(e-> {
            if (checkEmptyFields()) {
                addItem(controlMenu);
                closeWindow();
            }
        });

        buttonCancel.addActionListener(e-> closeWindow());

        panelButton.add(buttonCancel);
        panelButton.add(buttonCreate);
        parent.add(panelButton);

    }

    private void addItem(ControlMenu controlMenu) {
        Consumable newItem;

        int type;
        if (fieldType.getSelectedItem() == "Food") type = Consumable.FOOD;
        else type = Consumable.DRINK;

        String name = formatSpace(fieldName.getText());
        String notes = formatSpace(fieldNotes.getText());
        double price = Double.parseDouble(removeWhiteSpace(fieldPrice.getText()));
        double quantity = Double.parseDouble(removeWhiteSpace(fieldQuantity.getText()));
        LocalDateTime expiry = LocalDateTime.of(fieldExpiry.getDate(), LocalTime.MIDNIGHT);

        ConsumableFactory consumableFactory = new ConsumableFactory();
        newItem = consumableFactory.getInstance(type,name,notes,price,quantity,expiry);

        if (newItem.getType() == Consumable.FOOD) ServerRequest.requestPOST("addItem/food", newItem);
        else ServerRequest.requestPOST("addItem/drink", newItem);

        controlMenu.show(ControlMenu.SHOW_ALL);
    }

    private String removeWhiteSpace(String string) {
        return string.replaceAll("\\s","");
    }

    private String formatSpace(String string) {
        return string.replaceAll("\\s","_");
    }

    private void createAttributeFields(JPanel parent) {
        // Fields to gather data
        fieldType = new JComboBox<>();
        fieldType.addItem("Food");
        fieldType.addItem("Drink");
        fieldName = new JTextField(30);
        fieldNotes = new JTextField(30);
        fieldPrice = new JTextField(30);
        fieldQuantity = new JTextField(30);
        fieldExpiry = new DatePicker();

        JComponent[] attributeComponents = new JComponent[] {
                fieldType, fieldName, fieldNotes, fieldPrice, fieldQuantity, fieldExpiry
        };

        labelQuantity = new JLabel("Weight");
        JLabel[] attributeLabels = new JLabel[] {
                new JLabel("Type"), new JLabel("Name"), new JLabel("Notes    "),
                new JLabel("Price"), labelQuantity, new JLabel("Expiry")
        };

        fieldType.addActionListener(e -> {
            if (fieldType.getSelectedItem() == "Food") {
                labelQuantity.setText("Weight");
            }
            else {
                labelQuantity.setText("Volume");
            }
        });

        createAttributePanel(parent, attributeLabels, attributeComponents);
    }

    private boolean checkEmptyFields() {
        String emptyFields = "";
        if (removeWhiteSpace(fieldName.getText()).isEmpty()) emptyFields += "<br/>Name";
        if (removeWhiteSpace(fieldPrice.getText()).isEmpty()) emptyFields += "<br/>Price";
        if (removeWhiteSpace(fieldQuantity.getText()).isEmpty()) emptyFields += "<br/>"+labelQuantity.getText();
        if (removeWhiteSpace(fieldExpiry.getText()).isEmpty()) emptyFields += "<br/>Expiry Date";

        if (!emptyFields.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "<html>Please fill in a value for:<br/>"+emptyFields +"<html>",
                    "Empty Fields", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void createAttributePanel(JPanel parent, JLabel[] attributeLabels, JComponent[] attributeComponents) {

        // Panel for labels on left side
        JPanel panelAttributeLabels = new JPanel();
        panelAttributeLabels.setBorder(new EmptyBorder(0,0,0,5));
        panelAttributeLabels.setLayout(new BoxLayout(panelAttributeLabels, BoxLayout.Y_AXIS));
        for (JLabel attributeLabel : attributeLabels) {
            attributeLabel.setBorder(new EmptyBorder(5,5,5,5));
            panelAttributeLabels.add(attributeLabel);
        }

        // Panel for fields on right side
        JPanel panelAttributeFields = new JPanel();
        panelAttributeFields.setLayout(new BoxLayout(panelAttributeFields, BoxLayout.Y_AXIS));
        panelAttributeFields.setBorder(new EmptyBorder(0,0,0,5));
        for (JComponent attributeComponent: attributeComponents) {
            panelAttributeFields.add(attributeComponent);
        }

        parent.add(panelAttributeLabels);
        parent.add(panelAttributeFields);
    }

    private void closeWindow() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
