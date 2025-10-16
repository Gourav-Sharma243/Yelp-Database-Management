package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class UpdateStars {
    private JFrame frame;
    private JLabel businessIdLabel;
    private JLabel nameLabel;
    private JLabel addressLabel;
    private JLabel cityLabel;
    private JLabel postalCodeLabel;
    private JLabel starsLabel;
    private JLabel reviewCountLabel;
    private JButton updateStarsButton;
    private  JTextField userStar;
    static Connection c;
    static String u;
    static String b;
    static String n;
    static String a;
    static String cit;
    static String p;
    static String st;
    static String r;
    public UpdateStars(Connection con,String user, String businessId, String name, String address, String city, String postalCode, String stars, String reviewCount) {
        frame = new JFrame("Business Details");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        c=con;
        u=user;
        b=businessId;
        n=name;
        a=address;
        cit=city;
        p=postalCode;
        st=stars;
        r=reviewCount;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1));

        businessIdLabel = new JLabel("Business ID: " + businessId);
        nameLabel = new JLabel("Name: " + name);
        addressLabel = new JLabel("Address: " + address);
        cityLabel = new JLabel("City: " + city);
        postalCodeLabel = new JLabel("Postal Code: " + postalCode);
        starsLabel = new JLabel("Stars: " + stars);
        reviewCountLabel = new JLabel("Review Count: " + reviewCount);
        
        userStar=new JTextField();
        userStar.setBounds(100, 20, 165, 25);

        
        updateStarsButton = new JButton("SUBMIT REVIEW(1 - 5)");
        updateStarsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	String review=generateReviewID();
            	String s =  "INSERT INTO review(review_id,user_id,business_id,stars) VALUES ('"+review+"','"+u+"','"+b+"',"+st+")";
//            	System.out.println(s);
            	try
    			{
    				Statement statement1 = con.createStatement();
    				
    				statement1.executeUpdate(s);
    				
    				JOptionPane.showMessageDialog(frame, "Review Added! Thanks");
    				
    			} catch (SQLException se)
    				{
    					System.out.println("\nSQL Exception occurred, the state : "+
    									se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
    					return;
    				}
            }
        });

        panel.add(businessIdLabel);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(nameLabel);
        panel.add(new JLabel());
        panel.add(addressLabel);
        panel.add(new JLabel());
        panel.add(cityLabel);
        panel.add(new JLabel());
        panel.add(postalCodeLabel);
        panel.add(new JLabel());
        panel.add(starsLabel);
        panel.add(new JLabel());
        panel.add(reviewCountLabel);
        panel.add(new JLabel());
        panel.add(userStar);
        panel.add(updateStarsButton);

        frame.add(panel);
        frame.setVisible(true);
    }
    private boolean checkReviewId(String id) {
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sSQL = "SELECT * FROM review WHERE review_id = '" + id + "'";    

        try
        {
            pstmt = c.prepareStatement(sSQL);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            return true;
                
        } catch (SQLException se)
            {
                System.out.println("\nSQL Exception occurred, the state : "+
                                se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
                return false;
            }
            
    }
    
    private String generateReviewID() {
        String generatedString;
        while (true) {
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
    
            generatedString = random.ints(leftLimit, rightLimit + 1)
              .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))

              .limit(targetStringLength)
              .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
              .toString();
            
            if (!checkReviewId(generatedString))
                break;
        }
        return generatedString;
    }
}
