package main;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class SearchUsers implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField;
    private JTextField minReviewCountField;
    private JTextField maxAvgStarsField;
    private JButton performActionButton;
    private static Connection con;
    static Search_User_Results SUR;
    static String u;
    public SearchUsers(Connection C, String user) {
    	
    	if(C!= null) 
    	{
    		con=C;
    	}
    	u=user;
        frame = new JFrame();
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
     

        JLabel nameLabel = new JLabel("UserName: ");
        JLabel minReviewCountLabel = new JLabel("Minimum Review Count: ");
        JLabel maxAvgStarsLabel = new JLabel("Maximum Average Stars: ");

        nameField = new JTextField();
        nameField.setBounds(10, 50, 80, 25);
        minReviewCountField = new JTextField();
        minReviewCountField.setBounds(10, 70, 80, 25);
        maxAvgStarsField = new JTextField();
        maxAvgStarsField.setBounds(10, 90, 80, 25);

        performActionButton = new JButton("SEARCH");
        performActionButton.addActionListener(this);
        
        panel.setLayout(new GridLayout(4, 2));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(minReviewCountLabel);
        panel.add(minReviewCountField);
        panel.add(maxAvgStarsLabel);
        panel.add(maxAvgStarsField);
        panel.add(performActionButton);
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == performActionButton) {
            String name = nameField.getText();
            String minReviewCount = minReviewCountField.getText();
            String maxAvgStars = maxAvgStarsField.getText();
           

            StringBuilder sql = new StringBuilder("SELECT * FROM user_yelp u");
            boolean hasWhere = false;
            java.util.List<Object> params = new java.util.ArrayList<>();

            if (!name.isEmpty()) {
                sql.append(hasWhere ? " AND" : " WHERE");
                sql.append(" u.name LIKE ?");
                params.add("%" + name + "%");
                hasWhere = true;
            }

            if (!minReviewCount.isEmpty()) {
                try {
                    int minRC = Integer.parseInt(minReviewCount);
                    sql.append(hasWhere ? " AND" : " WHERE");
                    sql.append(" u.review_count >= ?");
                    params.add(minRC);
                    hasWhere = true;
                } catch (NumberFormatException nfe) {
                    javax.swing.JOptionPane.showMessageDialog(frame, "Minimum Review Count must be a number.");
                    return;
                }
            }

            if (!maxAvgStars.isEmpty()) {
                try {
                    double maxAS = Double.parseDouble(maxAvgStars);
                    sql.append(hasWhere ? " AND" : " WHERE");
                    sql.append(" u.average_stars <= ?");
                    params.add(maxAS);
                    hasWhere = true;
                } catch (NumberFormatException nfe) {
                    javax.swing.JOptionPane.showMessageDialog(frame, "Maximum Average Stars must be a number.");
                    return;
                }
            }

            SUR = Search_User_Results.showWithParams(con, sql.toString(), params, u);
        }

    }
}
