package main;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class selectCategory implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> optionsDropdown;
    private JButton performActionButton;
    private static Connection con;
    static SearchUsers searchUser;
    static SearchBussiness searchBussi;
    static String u;
    public selectCategory(Connection C,String user) {
    	u=user;
    	
    	if(C!= null) 
    	{
    		con=C;
    	}
    	
        frame = new JFrame();
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);

        optionsDropdown = new JComboBox<>(new String[]{"Select One","Search Business", "Search User"});
        panel.add(optionsDropdown);

        performActionButton = new JButton("Find");
        panel.add(performActionButton);
        performActionButton.addActionListener(this);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == performActionButton) {
            String selectedOption = (String) optionsDropdown.getSelectedItem();
            if(selectedOption=="Search Business") 
            {
            	frame.dispose();
            	searchBussi= new SearchBussiness(con,u);
            }
            else if(selectedOption=="Search User") 
            {
            frame.dispose();
            searchUser = new SearchUsers(con,u);
            
            }
        }
    }

  
}
