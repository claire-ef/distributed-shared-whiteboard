/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package WhiteBoard;

import RMI.IRemoteWhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

public class Canvas extends JPanel implements ActionListener {
    public ToolBar toolBar = new ToolBar();
    public SidePanel sidePanel;
    private IRemoteWhiteBoard remoteWhiteBoard;
    private Point startPoint = null, endPoint = null;
    private Point textPoint = null;
    private Timer timer;
    private String username;
    public Canvas(IRemoteWhiteBoard remoteWhiteBoard, String username) throws RemoteException {
        this.username = username;
        this.sidePanel = new SidePanel(username, remoteWhiteBoard);
        setBackground(Color.WHITE);
        this.remoteWhiteBoard = remoteWhiteBoard;
        addMouseListener(new Adapter());
        addMouseMotionListener(new MotionAdapter());
        this.timer = new Timer(100, this);
        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // update the frame every 0.1 second
        if (e.getSource() == timer) {
            try {
                // exit program if the user is kicked out
                if (remoteWhiteBoard.getKickedOutUsers().contains(username)) {
                    JOptionPane.showMessageDialog(this, "You have been kicked out by the manager.");
                    remoteWhiteBoard.removeFromKickOut(username);
                    System.exit(0);
                }

                // exit program if the manager quit
                if (!remoteWhiteBoard.getUsernames().contains(username) &&
                        (!username.equals(remoteWhiteBoard.getManagerName()))) {
                    JOptionPane.showMessageDialog(this, "WhiteBoard is shut down by the manager");
                    System.exit(0);
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            // update userList
            try {
                this.sidePanel.userList.setText(SidePanel.parseUserList(remoteWhiteBoard.getUsernames(), this.username));
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

            // update chatbox
            try {
                this.sidePanel.chatHistory.setText(SidePanel.parseMessages(remoteWhiteBoard.getMessages()));
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

            // update canvas
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw all the shapes that were drawn
        try {
            for (Shape s: remoteWhiteBoard.getShapes()) {
                s.drawShape(g);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // draw the shape that is current being drawn
        if (startPoint != null) {
            Shape currentShape = new Shape(startPoint, endPoint,
                    toolBar.currentShape, toolBar.currentColor,null);
            currentShape.drawShape(g);
        }
    }

    private class Adapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() == 2) {
                textPoint = e.getPoint();
                String content = JOptionPane.showInputDialog("Enter text");
                try {
                    remoteWhiteBoard.addShape(new Shape(textPoint, new Point(0, 0),
                            "text", toolBar.currentColor, content));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                e.getComponent().repaint();
            } else {
                startPoint = e.getPoint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (startPoint != null && endPoint != null && (! startPoint.equals(endPoint))) {
                try {
                    remoteWhiteBoard.addShape(new Shape(startPoint, endPoint,
                            toolBar.currentShape, toolBar.currentColor, null));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            startPoint = null;
        }
    }
    private class MotionAdapter extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            endPoint = e.getPoint();
            e.getComponent().repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            endPoint = e.getPoint();
        }
    }
}

