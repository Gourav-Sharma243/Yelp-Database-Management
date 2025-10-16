package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login implements ActionListener {

    JLabel  success;
    private  JPanel panel;
    private  JFrame frame;
    
    private  JPasswordField password_text;
    private  JTextField userText;
    private  JLabel label;
    private JLabel passwordLabel;
    private static Connection con;
    private JButton button;
	private JButton signUp;
    
	static SignUp signuppage; 
	static selectCategory selectCatPage;
	
    public login() 
    {
    	 panel = new JPanel();
         frame = new JFrame();
         frame.setSize(350, 200);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.add(panel);

         panel.setLayout(null);

         passwordLabel = new JLabel("Password: ");
         passwordLabel.setBounds(10, 50, 80, 25);

         label = new JLabel("Username: ");
         label.setBounds(10, 20, 80, 25);
         panel.add(label);
         panel.add(passwordLabel);

         userText = new JTextField();
         userText.setBounds(100, 20, 165, 25);
         panel.add(userText);

         password_text = new JPasswordField();
         password_text.setBounds(100, 50, 165, 25);
         panel.add(password_text);

         button = new JButton("Login");
         button.setBounds(10, 80, 80, 25);
         panel.add(button);
      
         button.addActionListener(this);
         
         signUp= new JButton("Sign Up");
         signUp.setBounds(100, 80, 80, 25);
         panel.add(signUp);
      
         signUp.addActionListener(this);
         
         
         success = new JLabel("");
         success.setBounds(10, 110, 300, 25);
         panel.add(success);

         frame.setVisible(true);
    }
    

    public static void main(String[] args) {      
        con = Db.getConnection();
        new login();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    	if (e.getSource() == button) {
	        String user = userText.getText();
	        String password = new String(password_text.getPassword());
	        if(loginCheck(user,password)) 
	        {
	        	frame.dispose();
	        	selectCatPage= new selectCategory(con,user);
	        }
	        
    	
    	}
    	
    	else if (e.getSource() == signUp) {
    		signuppage = new SignUp(con);
    	}
    }
    
public static boolean loginCheck(String id, String password) {
        ResultSet rs = null;
        String sSQL = "SELECT Password FROM Users WHERE UserID = ?";

        JFrame frame2=new JFrame();
        try (PreparedStatement pstmt = con.prepareStatement(sSQL)) {
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getString("Password").equals(password)) {
                    JOptionPane.showMessageDialog(frame2,"Wrong Password");
                    return false;
                } else {
                    System.out.println("Correct Password");
                    return true;
                }
            }
            JOptionPane.showMessageDialog(frame2,"User not found");
            return false;
        } catch (SQLException se) {
            System.out.println("\nSQL Exception occurred, the state : "+
                            se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
            return false;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
        }
}

    // Connection provided by Db utility now.
    
    
}
