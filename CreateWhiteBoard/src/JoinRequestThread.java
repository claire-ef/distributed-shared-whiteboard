/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

import RMI.IRemoteWhiteBoard;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;

public class JoinRequestThread extends Thread {
    private Socket clientSocket;
    private IRemoteWhiteBoard remoteWhiteBoard;
    public JoinRequestThread(Socket clientSocket, IRemoteWhiteBoard remoteWhiteBoard) {
        this.clientSocket = clientSocket;
        this.remoteWhiteBoard = remoteWhiteBoard;
    }
    @Override
    public void run() {
        super.run();
        try {
            //Get the input/output streams for reading/writing data from/to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String username = null;
            while((username = in.readLine()) != null) {
                boolean hasUser = remoteWhiteBoard.checkUsername(username);
                if (! hasUser) {
                    Object[] options = {"approve", "reject"};
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("User " + username + " wants to join the WhiteBoard"));
                    int result = JOptionPane.showOptionDialog(null, panel, "Join request",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, options, null);
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            this.remoteWhiteBoard.addUser(username);
                            out.write("approved\n");
                            out.flush();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(null, "Added " + username + " to the WhiteBoard.");
                    } else {
                        try {
                            out.write("rejected\n");
                            out.flush();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(null, "Rejected " + username + "'s join request.");
                    }
                } else {
                    out.write("userNameAlreadyInUse\n");
                    out.flush();
                }
            }
            //System.out.println("Server closed the client connection!");
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Thread exiting.");
    }

}
