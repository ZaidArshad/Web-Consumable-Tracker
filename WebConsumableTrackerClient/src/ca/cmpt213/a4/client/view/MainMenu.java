package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ControlMenu;
import ca.cmpt213.a4.client.control.ServerRequest;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Controls the tabs of different consumables by expiry
 * Has buttons to add / remove items
 */
public class MainMenu {
    private JPanel panelWindow;
    private JPanel panelMain;
    private JPanel panelTitle;
    private JLabel labelTitle;
    private JPanel panelNavigation;

    private JButton btnShowAll;
    private JButton btnShowExpired;
    private JButton btnShowNotExpired;
    private JButton btnShowExpire7Days;
    private JScrollPane scrollPaneItems;
    private JLabel templateLabelItem;
    private JButton btnAdd;

    public MainMenu() {
        JFrame mainFrame = new JFrame("App");
        mainFrame.setResizable(false);
        mainFrame.setContentPane(panelWindow);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);

        setControls();

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ServerRequest.requestGET("exit");
            }
        });
    }

    private void setControls() {

        ControlMenu controlMenu = new ControlMenu(templateLabelItem, scrollPaneItems, ControlMenu.SHOW_ALL);
        controlMenu.show(ControlMenu.SHOW_ALL);

        AddMenu addMenu = new AddMenu(templateLabelItem);

        btnShowAll.addActionListener(e -> controlMenu.show(ControlMenu.SHOW_ALL));

        btnShowExpired.addActionListener(e -> controlMenu.show(ControlMenu.SHOW_EXPIRED));

        btnShowNotExpired.addActionListener(e -> controlMenu.show(ControlMenu.SHOW_NON_EXPIRED));

        btnShowExpire7Days.addActionListener(e -> controlMenu.show(ControlMenu.SHOW_EXPIRES_IN_7_DAYS));

        btnAdd.addActionListener(e-> addMenu.showMenu(controlMenu));
    }

}
