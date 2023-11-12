/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package WhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar {

    private String[] SHAPE_EXAMPLES = {"line", "circle", "oval", "rectangle"};
    private String DEFAULT_SHAPE = SHAPE_EXAMPLES[0];
    public String currentShape = DEFAULT_SHAPE;

    private Color DEFAULT_COLOR = Color.BLACK;
    public Color currentColor = DEFAULT_COLOR;

    public ToolBar() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        // choose shape
        JComboBox shapeCombo = new JComboBox(SHAPE_EXAMPLES);
        shapeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String newShape = (String) cb.getSelectedItem();
                currentShape = newShape;
            }
        });

        // choose color
        JButton colorBtn = new JButton("  ");
        colorBtn.setBackground(currentColor);
        colorBtn.setOpaque(true);
        colorBtn.setBorderPainted(false);
        colorBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(colorBtn, "Choose Color", currentColor);
                if ((newColor != null) && (currentColor != newColor)) {
                    colorBtn.setBackground(newColor);
                    colorBtn.setOpaque(true);
                    colorBtn.setBorderPainted(false);
                    currentColor = newColor;
                }
            }
        });

        // layout the components in the toolbar
        // shape chooser components
        JLabel shapeLabel = new JLabel("Shape:");
        this.add(shapeLabel);
        this.add(shapeCombo);
        // color chooser components
        JLabel colorLabel = new JLabel("Color:");
        this.add(colorLabel);
        this.add(colorBtn);
        this.add(Box.createHorizontalStrut(25));
    }

}

