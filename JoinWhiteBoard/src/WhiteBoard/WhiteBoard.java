/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package WhiteBoard;

import RMI.IRemoteWhiteBoard;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class WhiteBoard extends JFrame {
    public WhiteBoard(IRemoteWhiteBoard remoteWhiteBoard, String username, boolean isManager) throws RemoteException {
        Canvas canvas = new Canvas(remoteWhiteBoard, username);
        String framename = null;
        if (isManager) {
            framename = "WhiteBoard Manager - " + username;
        } else {
            framename = "WhiteBoard User - " + username;
        }

        this.setTitle(framename);
        this.setSize(900, 600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (isManager) {
                    e.getWindow().dispose();
                    try {
                        remoteWhiteBoard.clearUsers();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    Timer timer = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                } else {
                    e.getWindow().dispose();
                    try {
                        remoteWhiteBoard.removeUser(username);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }
            }
        });
        this.setBackground(Color.LIGHT_GRAY);

        if (isManager) {
            JMenuBar menuBar = (new MenuBar(remoteWhiteBoard));
            menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
            this.setJMenuBar(menuBar);
        }

        this.getContentPane().add(canvas.toolBar, BorderLayout.NORTH);
        this.add(canvas);
        this.add(canvas.sidePanel, BorderLayout.EAST);
    }
}
