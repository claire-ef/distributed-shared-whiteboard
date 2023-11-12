/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package WhiteBoard;

import java.awt.*;
import java.io.Serializable;

public class Shape implements Serializable {
    private Point startPoint, endPoint;
    private String type;
    private Color color;
    private String text;
    public Shape(Point startPoint, Point endPoint, String type, Color color, String text) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.type = type;
        this.color = color;
        this.text = text;
    }
    public void drawShape(Graphics g) {
        g.setColor(color);
        int leftCornerX = Math.min(startPoint.x, endPoint.x);
        int leftCornerY = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);
        if (type.equals("line")) {
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        } else if (type.equals("oval")) {
            g.drawOval(leftCornerX, leftCornerY, width, height);
        } else if (type.equals("circle")) {
            int radius = Math.max(width, height);
            g.drawOval(leftCornerX, leftCornerY, radius, radius);
        } else if (type.equals("rectangle")) {
            g.drawRect(leftCornerX, leftCornerY, width, height);
        } else if (type.equals("text")) {
            if (text!=null) {
                g.drawString(text, startPoint.x, startPoint.y);
            }
        }
    }
}

