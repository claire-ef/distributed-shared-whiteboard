/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

import RMI.IRemoteWhiteBoard;
import WhiteBoard.WhiteBoard;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {

    private static String ip;
    private static int port;
    private static String username;

    public static void main(String[] args) {
        try {
            ip = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

            Socket socket = null;
            try {
                // Create a stream socket and connect it to the server socket
                socket = new Socket(ip, port + 1);

                // Get the input/output streams for reading/writing data from/to the socket
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

                // Send the username to the server by writing to the socket output stream
                out.write(username + "\n");
                out.flush();

                boolean approved = false;
                while (! approved) {
                    String received = in.readLine();
                    if (received.equals("approved")) {
                        approved = true;
                        break;
                    } else if (received.equals("userNameAlreadyInUse")) {
                        Object[] options = {"enter", "cancel"};
                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.add(new JLabel("The username you entered has already been used."));
                        panel.add(new JLabel("Please enter a different username:"));
                        JTextField textField = new JTextField(5);
                        panel.add(textField);
                        int result = JOptionPane.showOptionDialog(null, panel, "Enter a different username",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, options, null);
                        if (result == JOptionPane.YES_OPTION) {
                            String newUsername = textField.getText();
                            username = newUsername;
                            System.out.println("newname "+ newUsername);
                            out.write(newUsername + "\n");
                            out.flush();
                        } else {
                            System.exit(0);
                        }
                    } else if (received.equals("rejected")){
                        JOptionPane.showMessageDialog(null, "Your connection to the WhiteBoard is rejected by the manager.");
                        System.out.println("Connection to the WhiteBoard is rejected by the manager.");
                        System.exit(0);
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                // Close the socket
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e)
                    { e.printStackTrace();
                    }
                }
            }

            //Connect to the rmiregistry that is running on localhost
            Registry registry = LocateRegistry.getRegistry(port);

            //Retrieve the stub/proxy for the remote math object from the registry
            IRemoteWhiteBoard remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("WhiteBoardCompute");

            WhiteBoard whiteBoard = new WhiteBoard(remoteWhiteBoard, username, false);
            whiteBoard.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
