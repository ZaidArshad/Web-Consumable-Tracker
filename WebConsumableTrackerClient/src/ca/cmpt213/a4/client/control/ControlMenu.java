package ca.cmpt213.a4.client.control;


import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;

/**
 * Shows the different tabs to the screen and items associated with the model
 */
public class ControlMenu {
    private final JLabel templateLabelItem;
    private final JScrollPane scrollPane;
    private Integer showState;
    private ArrayList<Consumable> consumableItems;
    private boolean hasItems;

    public final static int SHOW_ALL = 0;
    public final static int SHOW_EXPIRED = 1;
    public final static int SHOW_NON_EXPIRED = 2;
    public final static int SHOW_EXPIRES_IN_7_DAYS = 3;

    private final String[] EMPTY_STATES_STRINGS = new String[] {
            "No items to show.",
            "No expired items to show.",
            "No non-expired items to show.",
            "No items expiring in 7 days to show."
    };

    public ControlMenu(JLabel templateLabelItem,JScrollPane scrollPane, Integer showState) {
        this.templateLabelItem = templateLabelItem;
        this.scrollPane = scrollPane;
        this.showState = showState;
        this.consumableItems = ConsumableFileManager.loadConsumables();
        this.hasItems = false;
    }

    public void show(Integer condition) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(mainPanel);
        showState = condition;
        hasItems = false;

        /* Condition 1: Show all
           Condition 2: Show expired
           Condition 3: Show non-expired
           Condition 4: Show expires in 7 days
         */
        switch (condition) {
            case SHOW_ALL -> consumableItems = ConsumableFileManager.parseConsumableList("listAll");
            case SHOW_EXPIRED -> consumableItems = ConsumableFileManager.parseConsumableList("listExpired");
            case SHOW_NON_EXPIRED -> consumableItems = ConsumableFileManager.parseConsumableList("listNonExpired");
            case SHOW_EXPIRES_IN_7_DAYS -> consumableItems = ConsumableFileManager.parseConsumableList("listExpiringIn7Days");
        }

        int number = 0;
        for (Consumable consumableItem : consumableItems) {
            createItemPanel(consumableItem, mainPanel, number);
            number++;
        }

        if (!hasItems) {
            showEmptyState(mainPanel);
        }
    }

    private void showEmptyState(JPanel mainPanel) {
        JLabel labelNoItems = new JLabel(EMPTY_STATES_STRINGS[showState]);
        labelNoItems.setBorder(new EmptyBorder(10,10,10,10));
        labelNoItems.setFont(templateLabelItem.getFont());
        mainPanel.add(labelNoItems);
    }

    public void createItemPanel(Consumable consumable, JPanel mainPanel, int number) {
        hasItems = true;

        // Create Main window
        JPanel panelItem = new JPanel();
        panelItem.setLayout(new BoxLayout(panelItem, BoxLayout.X_AXIS));
        panelItem.setBorder(new CompoundBorder(
                new EmptyBorder(10,10,10,10),
                new BevelBorder(BevelBorder.RAISED)
        ));

        // Setting text
        JPanel panelText = new JPanel();
        panelText.setBorder(new EmptyBorder(10,10,10,10));
        panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));

        String title = "Item #" + (number + 1);
        if (consumable.getType() == Consumable.FOOD) title += " (Food)";
        else title += " (Drink)";
        JLabel labelHeader = new JLabel(title);

        JLabel labelDescription = new JLabel(consumable.toString());
        labelHeader.setFont(templateLabelItem.getFont());
        panelText.add(labelHeader);
        panelText.add(labelDescription);

        // Setting remove button
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new BoxLayout(panelButton,BoxLayout.Y_AXIS));
        panelButton.setBorder(new EmptyBorder(120,10,10,10));
        JButton btnRemove = new JButton("Remove");
        panelButton.add(btnRemove);

        btnRemove.addActionListener(e-> {
            ServerRequest.requestPOST("removeItem", consumableItems.indexOf(consumable));
            show(showState);
        });

        // Adding panels together
        panelItem.add(panelText);
        panelItem.add(panelButton);
        mainPanel.add(panelItem);
    }
}
