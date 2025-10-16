package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Search_Business_Results {
    private JFrame resultFrame;
    private JPanel resultPanel;
    private JTable resultTable;
    static UpdateStars US;
    static String u;

    public Search_Business_Results(Connection con, String sSQL,String user) {
        u=user;
    	resultFrame = new JFrame();
        resultFrame.setSize(600, 400);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resultPanel = new JPanel();
        resultFrame.add(resultPanel);

        // Define column names
        String[] columnNames = {"Business ID", "Name", "Address", "City", "Postal Code", "Stars", "Review Count"};

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        resultTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        resultPanel.add(scrollPane);

        // Add MouseAdapter to listen for mouse events on the JTable
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) {
                	
                    String businessId = resultTable.getValueAt(selectedRow, 0).toString();
                    String name = resultTable.getValueAt(selectedRow, 1).toString();
                    String address = resultTable.getValueAt(selectedRow, 2).toString();
                    String city = resultTable.getValueAt(selectedRow, 3).toString();
                    String postalCode = resultTable.getValueAt(selectedRow, 4).toString();
                    String stars = resultTable.getValueAt(selectedRow, 5).toString();
                    String reviewCount = resultTable.getValueAt(selectedRow, 6).toString();


                    US = new UpdateStars(con,u , businessId, name, address, city, postalCode, stars, reviewCount);
                }
            }
        });

        resultFrame.setVisible(true);

        performSearch(con, sSQL, model);
    }

    private void performSearch(Connection con, String sSQL, DefaultTableModel model) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sSQL);


            while (resultSet.next()) {

                String resultBusinessId = resultSet.getString("business_id");
                String resultName = resultSet.getString("name");
                String resultAddress = resultSet.getString("address");
                String resultCity = resultSet.getString("city");
                String resultPostalCode = resultSet.getString("postal_code");
                String resultStars = String.valueOf(resultSet.getInt("stars"));
                String resultReviewCount = resultSet.getString("review_count");

         
                Object[] rowData = {resultBusinessId, resultName, resultAddress, resultCity, resultPostalCode, resultStars, resultReviewCount};
                model.addRow(rowData);
            }

        } catch (SQLException se) {
            System.out.println("Error executing SQL query: " + se.getMessage());
        }
    }
}
