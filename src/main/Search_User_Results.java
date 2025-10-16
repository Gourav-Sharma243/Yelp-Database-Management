package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Search_User_Results {
    private JFrame resultFrame;
    private JPanel resultPanel;
    private JTable resultTable;
    static String u;
  
    static AddFriend addFriendToDatabase;
    public Search_User_Results(Connection con, String sSQL,String user) {
        this(con, sSQL, user, true);
    }

    public Search_User_Results(Connection con, String sSQL, String user, boolean runInitialQuery) {
        resultFrame = new JFrame();
        resultFrame.setSize(600, 600);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        u=user;
        resultPanel = new JPanel();
        resultFrame.add(resultPanel);

        String[] columnNames = {"User ID", "Name", "Review Count", "Yelping Since", "Useful", "Funny", "Cool", "Fans", "Average Stars"};

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
                    // Customize this based on your table structure
                    String resultUserId = resultTable.getValueAt(selectedRow, 0).toString();
                    String resultName = resultTable.getValueAt(selectedRow, 1).toString();
                    String resultReviewCount = resultTable.getValueAt(selectedRow, 2).toString();
                    String resultAverageStars = resultTable.getValueAt(selectedRow, 8).toString();

                    // Open a new JFrame for adding a friend
                    openAddFriendFrame(con, resultUserId, resultName, resultReviewCount, resultAverageStars);
                }
            }
        });

        resultFrame.setVisible(true);

        if (runInitialQuery) {
            performSearch(con, sSQL, model);
        }
    }

    // Factory to support parameterized queries without executing raw SQL first
    public static Search_User_Results showWithParams(Connection con, String sql, List<Object> params, String user) {
        Search_User_Results inst = new Search_User_Results(con, sql, user, false);
        DefaultTableModel model = (DefaultTableModel) inst.resultTable.getModel();
        inst.performSearch(con, sql, params, model);
        return inst;
    }

    private void performSearch(Connection con, String sSQL, DefaultTableModel model) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sSQL);
            
            while (resultSet.next()) {
                String resultUserId = resultSet.getString("user_id");
                String resultName = resultSet.getString("name");
                String resultReviewCount = resultSet.getString("review_count");
                String resultAverageStars = String.valueOf(resultSet.getDouble("average_stars"));
                String ys = resultSet.getString("yelping_since");
                String useful = resultSet.getString("useful");
                String funny = resultSet.getString("funny");
                String cool = resultSet.getString("cool");
                String fans = resultSet.getString("fans");

                Object[] rowData = {resultUserId, resultName, resultReviewCount, ys, useful, funny, cool, fans, resultAverageStars};
                model.addRow(rowData);
            }

        } catch (SQLException se) {
            System.out.println("Error executing SQL query: " + se.getMessage());
        }
    }

    private void performSearch(Connection con, String sql, List<Object> params, DefaultTableModel model) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int idx = 1;
            for (Object p : params) {
                ps.setObject(idx++, p);
            }
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String resultUserId = resultSet.getString("user_id");
                    String resultName = resultSet.getString("name");
                    String resultReviewCount = resultSet.getString("review_count");
                    String resultAverageStars = String.valueOf(resultSet.getDouble("average_stars"));
                    String ys = resultSet.getString("yelping_since");
                    String useful = resultSet.getString("useful");
                    String funny = resultSet.getString("funny");
                    String cool = resultSet.getString("cool");
                    String fans = resultSet.getString("fans");

                    Object[] rowData = {resultUserId, resultName, resultReviewCount, ys, useful, funny, cool, fans, resultAverageStars};
                    model.addRow(rowData);
                }
            }
        } catch (SQLException se) {
            System.out.println("Error executing SQL query: " + se.getMessage());
        }
    }

    private void openAddFriendFrame(Connection con, String resultUserId, String name, String reviewCount, String averageStars) {
    	JLabel ll=new JLabel("Hello!! Click Below to Add "+name+" As a Add Friend!!");
    	ll.setBounds(10,50,85,30);
        JFrame addFriendFrame = new JFrame("Add Friend");
        addFriendFrame.setSize(300, 300);
        addFriendFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.add(ll);
        JButton addFriendButton = new JButton("Add Friend");
        
        addFriendButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(addFriendFrame, "Do you want to add this user as a friend?", "Add Friend", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Call a method to add the friend to the database
                addFriendToDatabase(con, resultUserId);
                JOptionPane.showMessageDialog(addFriendFrame, "Friend added successfully!");
            }
        });

        panel.add(addFriendButton);
        addFriendFrame.add(panel);
        addFriendFrame.setVisible(true);
    }

    private void addFriendToDatabase(Connection con, String friendId) {
        String insertQuery = "INSERT INTO friendship (user_id, friend) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
            ps.setString(1, u);
            ps.setString(2, friendId);
            ps.executeUpdate();
            System.out.println("Value Inserted into Friendship");
        } catch (SQLException se) {
            System.out.println("\nSQL Exception occurred, the state : "+
                            se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
        }
    }

  
}
