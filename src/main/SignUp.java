 package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignUp implements ActionListener {
    private static JLabel success;
    private static JPanel panel;
    private static JFrame frame;
    private static JButton button;
    private static JPasswordField password_text;
    private static JTextField userText;
    private static JTextField nameText; 
    private static JLabel label;
    private static JLabel passwordLabel;
    private static JLabel nameLabel; 
    private static Connection con;
    static login log;
    
    private static JButton back;
    
    public SignUp(Connection C) {
    	
    	
    	if(C!= null) 
    	{
    		con=C;
    	}
    	
        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(350, 250); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(10, 80, 80, 25);

        label = new JLabel("Username: ");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);
        panel.add(passwordLabel);

        nameLabel = new JLabel("Name: "); 
        nameLabel.setBounds(10, 50, 80, 25); 
        panel.add(nameLabel);

        userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        nameText = new JTextField(); 
        nameText.setBounds(100, 50, 165, 25); 
        panel.add(nameText);

        password_text = new JPasswordField();
        password_text.setBounds(100, 80, 165, 25);
        panel.add(password_text);

        button = new JButton("SignUp");
        button.setBounds(10, 110, 80, 25);
        panel.add(button);
        // Add ActionListener to the button
        button.addActionListener(this);
        
        back = new JButton("Back");
        back.setBounds(100, 110, 80, 25);
        panel.add(back);
        back.addActionListener(this);

        success = new JLabel("");
        success.setBounds(10, 140, 300, 25);
        panel.add(success);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        String username = userText.getText();
        String name = nameText.getText();
        String password = new String(password_text.getPassword());

        if(e.getSource()==back) {
        	frame.dispose();
        	log= new login();
        }
        else {

        if (insertUser(username, name, password)) {
            success.setText("Sign Up Successful");
            
        } else {
            success.setText("Error during Sign Up");
        }
    }}

    private boolean insertUser(String username, String name, String password) {
        String sSQL1 = "INSERT INTO Users (UserID, Password) VALUES (?, ?)";
        String sSQL2 = "INSERT INTO user_yelp (user_id, name) VALUES (?, ?)";
        try (PreparedStatement ps1 = con.prepareStatement(sSQL1);
             PreparedStatement ps2 = con.prepareStatement(sSQL2)) {
            ps1.setString(1, username);
            ps1.setString(2, password);
            ps1.executeUpdate();
            System.out.println("Value Inserted into Users");

            ps2.setString(1, username);
            ps2.setString(2, name);
            ps2.executeUpdate();
            System.out.println("Value Inserted into user_yelp");
            return true;
        } catch (SQLException se) {
            System.out.println("\nSQL Exception occurred, the state : "+
                            se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
        }
        return false;
    }
}