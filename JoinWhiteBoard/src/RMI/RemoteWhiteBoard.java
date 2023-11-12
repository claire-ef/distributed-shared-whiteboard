/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package RMI;

import WhiteBoard.Shape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard {
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private ArrayList<String> usernames = new ArrayList<String>();
    private ArrayList<String> kickedOutUsers = new ArrayList<String>();
    private ArrayList<String> messages = new ArrayList<String>();
    private String managerName;

    public RemoteWhiteBoard(String managerName) throws RemoteException {
        this.managerName = managerName;
        this.usernames.add(managerName);
    }
    @Override
    public ArrayList<Shape> getShapes() throws RemoteException {
        return this.shapes;
    }
    @Override
    public void addShape(Shape shape) throws RemoteException {
        this.shapes.add(shape);
    }

    public ArrayList<String> getUsernames() throws RemoteException {
        return this.usernames;
    }

    public void addUser(String username) throws RemoteException {
        this.usernames.add(username);
    }

    public void removeUser(String username) throws RemoteException {
        this.usernames.remove(username);
    }

    public boolean checkUsername(String username) throws RemoteException {
        if (this.usernames.contains(username)) {
            return true;
        } else {
            return false;
        }
    }

    public void addMessage(String message) throws RemoteException {
        this.messages.add(message);
    }

    public ArrayList<String> getMessages() throws RemoteException {
        return this.messages;
    }

    public String getManagerName() throws RemoteException {
        return this.managerName;
    }
    public void clearShapes() throws RemoteException {
        this.shapes = new ArrayList<Shape>();
    }
    public void setShapes(ArrayList<Shape> shapes) throws RemoteException {
        this.shapes = shapes;
    }

    public void addToKickOut(String username) throws RemoteException {
        this.kickedOutUsers.add(username);
    }

    public void removeFromKickOut(String username) throws RemoteException {
        this.kickedOutUsers.remove(username);
    }

    public ArrayList<String> getKickedOutUsers() throws RemoteException {
        return this.kickedOutUsers;
    }

    public void clearUsers() throws RemoteException {
        this.usernames = new ArrayList<String>();
    }


}

