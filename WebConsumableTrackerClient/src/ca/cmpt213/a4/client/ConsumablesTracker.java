package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.MainMenu;

import javax.swing.*;

/**
 * Holds the main method
 */
public class ConsumablesTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}
