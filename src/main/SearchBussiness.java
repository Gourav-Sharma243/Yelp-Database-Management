package main;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
            
            String sSQL1 = "SELECT * FROM business b WHERE";
            
            int count=0;
            
            if(!name.isEmpty()) 
            {
            	sSQL1+=" b.name LIKE '%"+name+"%'" ;
            	count++;
            }
            if(count!=0 && !city.isEmpty()) 
            {
            	sSQL1+=" AND '%"+city+"%' ";
            	count++;
            }
            
            if(count==0) 
            {
            	sSQL1+=" b.city LIKE '%"+city+"%' ";
            	count++;
            }
            if(count!=0 && !minStars.isEmpty()) 
            {
            	sSQL1+=" AND '%"+minStars+"%' ";
            	count++;
            }
            if(count==0) 
            {
            	sSQL1+=" b.city LIKE '%"+minStars+"%' ";
            	count++;
            }
            
            if(count==0) 
            {
            	sSQL1="SELECT * FROM business b ";
            }
            
            
            if(orderBy.equals("Name")) 
            {
            	sSQL1+="ORDER BY b.name";
            }
            else if(orderBy.equals("City")) 
            {
            	sSQL1+="ORDER BY b.city";
            }
            else if(orderBy.equals("Stars")) 
            {
            	sSQL1+="ORDER BY b.stars";
            }
            sSQL1+=";";
            
            
            Busi_Result=new Search_Business_Results(con,sSQL1,u);
            
       }
            
            
            
            
          
        }
        
        
    }



