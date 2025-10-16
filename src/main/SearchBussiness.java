package main;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class SearchBussiness implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField;
    private JTextField cityField;
    private JTextField minStarsField;
    private JComboBox<String> orderByDropdown;
    private JButton performActionButton;
    private static Connection con;
    static Search_Business_Results Busi_Result;
    static String u;
    public SearchBussiness(Connection C,String user) {
    	
    	if(C!= null) 
    	{
    		con=C;
    	}
    	u=user;
    	
        frame = new JFrame();
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();



        JLabel nameLabel = new JLabel("Name:");
        JLabel cityLabel = new JLabel("City:");
        JLabel minStarsLabel = new JLabel("Minimum Stars:");
        JLabel orderByLabel = new JLabel("Order By:");

        
        nameField = new JTextField(15);
        cityField = new JTextField(15);
        minStarsField = new JTextField(5);
        
       


        orderByDropdown = new JComboBox<>(new String[]{"Select One","Name", "City", "Stars"});


        performActionButton = new JButton("SEARCH");
        performActionButton.addActionListener(this);


        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(cityLabel);
        panel.add(cityField);
        panel.add(minStarsLabel);
        panel.add(minStarsField);
        panel.add(orderByLabel);
        panel.add(orderByDropdown);
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
            String city = cityField.getText();
            String minStars = minStarsField.getText();
            String orderBy = (String) orderByDropdown.getSelectedItem();

            StringBuilder sql = new StringBuilder("SELECT * FROM business b");
            boolean hasWhere = false;
            java.util.List<Object> params = new java.util.ArrayList<>();

            if (!name.isEmpty()) {
                sql.append(hasWhere ? " AND" : " WHERE");
                sql.append(" b.name LIKE ?");
                params.add("%" + name + "%");
                hasWhere = true;
            }
            if (!city.isEmpty()) {
                sql.append(hasWhere ? " AND" : " WHERE");
                sql.append(" b.city LIKE ?");
                params.add("%" + city + "%");
                hasWhere = true;
            }
            if (!minStars.isEmpty()) {
                try {
                    int stars = Integer.parseInt(minStars);
                    sql.append(hasWhere ? " AND" : " WHERE");
                    sql.append(" b.stars >= ?");
                    params.add(stars);
                    hasWhere = true;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Minimum Stars must be a number.");
                    return;
                }
            }

            if ("Name".equals(orderBy)) {
                sql.append(" ORDER BY b.name");
            } else if ("City".equals(orderBy)) {
                sql.append(" ORDER BY b.city");
            } else if ("Stars".equals(orderBy)) {
                sql.append(" ORDER BY b.stars");
            }

            Busi_Result = Search_Business_Results.showWithParams(con, sql.toString(), params, u);
            
       }
            
            
            
            
          
        }
        
        
    }



