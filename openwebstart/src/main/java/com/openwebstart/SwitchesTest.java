package com.openwebstart;

import com.openwebstart.jvm.ui.widgets.switchbutton.SteelCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SwitchesTest {
    static private JTextArea lastClicked;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Switches");
                SteelCheckBox ts = new SteelCheckBox("activated");

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
                buttonPanel.add(ts);
                frame.add(buttonPanel);

                frame.setSize(300,300);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
