/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package RMI;

import WhiteBoard.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteWhiteBoard extends Remote {
    public ArrayList<Shape> getShapes() throws RemoteException;
    public void setShapes(ArrayList<Shape> shapes) throws RemoteException;
    public void clearShapes() throws RemoteException;
    public void addShape(Shape shape) throws RemoteException;

    public ArrayList<String> getUsernames() throws RemoteException;
    public boolean checkUsername(String username) throws RemoteException;
    public void removeUser(String username) throws RemoteException;
    public void clearUsers() throws RemoteException;
    public void addUser(String username) throws RemoteException;
    public void addMessage(String message) throws RemoteException;
    public ArrayList<String> getMessages() throws RemoteException;

    public String getManagerName() throws RemoteException;
    public void addToKickOut(String username) throws RemoteException;
    public void removeFromKickOut(String username) throws RemoteException;
    public ArrayList<String> getKickedOutUsers() throws RemoteException;
}