/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

import RMI.IRemoteWhiteBoard;
import RMI.RemoteWhiteBoard;
import WhiteBoard.WhiteBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CreateWhiteBoard {

    private static String ip;
    private static int port;
    private static String username;

    public static void main(String[] args) {
        try {
            ip = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];

            IRemoteWhiteBoard remoteWhiteBoard = new RemoteWhiteBoard(username);

            //Publish the remote object's stub in the registry under the name "Compute"
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("WhiteBoardCompute", remoteWhiteBoard);
            System.out.println("WhiteBoard server ready");
            WhiteBoard whiteBoard = new WhiteBoard(remoteWhiteBoard, username, true);
            whiteBoard.setVisible(true);

            // Create a server socket to listen for join requests
            ServerSocket listenSocket = null;
            try {
                listenSocket = new ServerSocket(port + 1);
                listenSocket.setReuseAddress(true);

                //Listen for incoming connections forever
                while (true) {
                    //Accept an incoming client connection request and create a new thread for serving the client
                    Socket clientSocket = listenSocket.accept();
                    JoinRequestThread t = new JoinRequestThread(clientSocket, remoteWhiteBoard);
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (listenSocket != null) {
                    try {
                        // close the server socket
                        listenSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            UnicastRemoteObject.unexportObject(remoteWhiteBoard, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

