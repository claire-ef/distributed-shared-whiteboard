/**
 * This code is implemented by Qingyang Feng (980940) for COMP90015 Assignment 2.
 */

package WhiteBoard;

import RMI.IRemoteWhiteBoard;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuBar extends JMenuBar {
    private String userToRemove = null;
    private String savedPath = null;
    public MenuBar(IRemoteWhiteBoard remoteWhiteBoard) {
        JMenu menu;
        JMenuItem menuItem;
        /**
         * The File menu
         */
        menu = new JMenu("File");
        this.add(menu);

        // open a new file
        menuItem = new JMenuItem("new");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Do you want to save the current file?");
                if (result == JOptionPane.YES_OPTION) {
                    // get current shapes
                    ArrayList<Shape> currentShapes = null;
                    try {
                        currentShapes = remoteWhiteBoard.getShapes();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    // save current shapes
                    save(currentShapes);
                    // clear shapes
                    try {
                        remoteWhiteBoard.clearShapes();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    savedPath = null;

                } else if (result == JOptionPane.NO_OPTION) {
                    try {
                        remoteWhiteBoard.clearShapes();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    savedPath = null;
                }
            }
        });
        menu.add(menuItem);

        // open a saved file
        menuItem = new JMenuItem("open");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Do you want to save the current file?");
                if (result == JOptionPane.YES_OPTION) {
                    // get current shapes
                    ArrayList<Shape> currentShapes = null;
                    try {
                        currentShapes = remoteWhiteBoard.getShapes();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    // save current shapes
                    save(currentShapes);

                    // open file
                    open(remoteWhiteBoard);
                } else if (result == JOptionPane.NO_OPTION) {
                    // open file
                    open(remoteWhiteBoard);
                }
            }
        });
        menu.add(menuItem);

        // save a file
        menuItem = new JMenuItem("save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Shape> currentShapes = null;
                try {
                    currentShapes = remoteWhiteBoard.getShapes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                save(currentShapes);
            }
        });
        menu.add(menuItem);

        // saveAs a file
        menuItem = new JMenuItem("saveAs");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // convert shapes to string
                ArrayList<Shape> currentShapes = null;
                try {
                    currentShapes = remoteWhiteBoard.getShapes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                saveAs(currentShapes);
            }
        });
        menu.add(menuItem);

        /**
         * The User Management menu
         */
        menu = new JMenu("User Management");
        this.add(menu);

        // Kick a user out
        menuItem = new JMenuItem("kick a user out");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kickOut(remoteWhiteBoard);
            }
        });
        menu.add(menuItem);
    }

    private void kickOut(IRemoteWhiteBoard remoteWhiteBoard) {
        ArrayList<String> usernames = null;
        try {
            usernames = remoteWhiteBoard.getUsernames();
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Choose the user you want to kick out", JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(label);
        JComboBox userCombo = new JComboBox();
        if (usernames.size() > 1) {
            userCombo = new JComboBox(usernames.subList(1, usernames.size()).toArray());
        } else {
        }
        userCombo.setSelectedIndex(-1);
        userCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JComboBox cb = (JComboBox) ev.getSource();
                userToRemove = (String) cb.getSelectedItem();
            }
        });
        panel.add(userCombo);
        int result = JOptionPane.showOptionDialog(null, panel, "Kick a user out",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,null, null);
        if (result==JOptionPane.OK_OPTION) {
            try {
                remoteWhiteBoard.removeUser(userToRemove);
                remoteWhiteBoard.addToKickOut(userToRemove);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void saveAs(ArrayList<Shape> currentShapes) {
        Gson gson = new Gson();
        String shapesJson = gson.toJson(currentShapes);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("saveAs");
        int selection = fileChooser.showSaveDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave != null) {
                if (!fileToSave.getName().toLowerCase().endsWith(".json")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".json");
                }
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(fileToSave);
                    fileWriter.write(shapesJson);
                    fileWriter.close();
                    this.savedPath = fileToSave.getPath();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void save(ArrayList<Shape> currentShapes) {
        if (this.savedPath != null) {
            Gson gson = new Gson();
            String shapesJson = gson.toJson(currentShapes);
            FileWriter file = null;
            try {
                file = new FileWriter(this.savedPath);
                file.write(shapesJson);
                file.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            saveAs(currentShapes);
        }
    }

    private void open(IRemoteWhiteBoard remoteWhiteBoard) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("open");
        int selection = fileChooser.showOpenDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            if (fileToOpen != null) {
                if (! fileToOpen.getName().toLowerCase().endsWith(".json")) {
                    JOptionPane.showMessageDialog(null, "You can only open a json file.",
                            "Invalid File", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Gson gson = new Gson();
                        JsonReader reader = new JsonReader(new FileReader(fileToOpen.getPath()));
                        ArrayList<Shape> openedShapes = new ArrayList<> (Arrays.asList(gson.fromJson(reader, Shape[].class)));
                        remoteWhiteBoard.setShapes(openedShapes);
                        this.savedPath = fileToOpen.getPath();
                    } catch (JsonSyntaxException e) {
                        JOptionPane.showMessageDialog(null, "The json file is invalid to open.",
                                "Invalid Json File", JOptionPane.ERROR_MESSAGE);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, "The file is not found.",
                                "File Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

}
