package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddFriend {
    private JFrame frame;
    private JButton addFriendButton;

    public AddFriend(Connection con, String userId, String friendId) {
        frame = new JFrame("Add Friend");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        addFriendButton = new JButton("Add Friend");

        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	
                int option = JOptionPane.showConfirmDialog(frame, "Do you want to add this user as a friend?", "Add Friend", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Call a method to add the friend to the database
                    addFriendToDatabase(con, userId, friendId);
                    JOptionPane.showMessageDialog(frame, "Friend added successfully!");
                }
            }
        });

        panel.add(addFriendButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void addFriendToDatabase(Connection con, String userId, String friendId) {
        // Implement the logic to add the friend to the database
        String insertQuery = "INSERT INTO friendship (user_id, friend) VALUES ("+userId+","+ friendId + ")";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, friendId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
}
