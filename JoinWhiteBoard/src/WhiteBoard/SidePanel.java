package WhiteBoard;

import RMI.IRemoteWhiteBoard;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SidePanel extends JPanel {
    public JTextArea userList;
    public JTextArea chatHistory;
    public SidePanel(String username, IRemoteWhiteBoard remoteWhiteBoard) throws RemoteException {
        this.setBorder(new LineBorder(Color.LIGHT_GRAY,2));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // show list of user
        JLabel userLabel = new JLabel("You are sharing the WhiteBoard with", JLabel.CENTER);
        userLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.userList = new JTextArea(30, 10);
        this.userList.setText(parseUserList(remoteWhiteBoard.getUsernames(), username));
        this.userList.setEditable(false);
        this.userList.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollUser = new JScrollPane(this.userList);
        scrollUser.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        DefaultCaret caret = (DefaultCaret) this.userList.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        // show chat history
        JLabel chatLabel = new JLabel("ChatBox", JLabel.CENTER);
        chatLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.chatHistory = new JTextArea(100, 10);
        this.chatHistory.setText(parseMessages(remoteWhiteBoard.getMessages()));
        this.chatHistory.setEditable(false);
        this.chatHistory.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollChat = new JScrollPane(chatHistory);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        caret = (DefaultCaret) this.chatHistory.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        // input box
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(1,2));
        JButton sendBtn = new JButton("send");
        JTextField textBox = new JTextField(10);
        input.add(textBox);
        input.add(sendBtn);
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textBox.getText();
                //chatHistory.append(username + ": "+ text);
                try {
                    remoteWhiteBoard.addMessage(username + ": "+ text);
                    textBox.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(userLabel);
        this.add(scrollUser);
        this.add(chatLabel);
        this.add(scrollChat);
        this.add(input);
    }

    public static String parseUserList(ArrayList<String> userList, String thisUser) {
        String result = "";
        for (String user: userList) {
            if (! user.equals(thisUser)) {
                result += user + "\n";
            }
        }
        return result;
    }
    public static String parseMessages(ArrayList<String> messages) {
        String result = "";
        for (String message: messages) {
            result += message + "\n";
        }
        return result;
    }
}

