package vrs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemLogs extends javax.swing.JInternalFrame {
    private JTable logsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    private JButton refreshButton;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    private JButton searchButton;

    public SystemLogs() {
        initComponents();
        loadLogs();
    }

    private void initComponents() {
        setTitle("System Logs");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(800, 600);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterCombo = new JComboBox<>(new String[]{"All", "Login", "Logout", "Vehicle", "Rental", "User"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        refreshButton = new JButton("Refresh");

        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterCombo);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);

        // Create table
        String[] columns = {"Timestamp", "User", "Action", "Details", "IP"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logsTable = new JTable(tableModel);
        scrollPane = new JScrollPane(logsTable);

        // Add components to main panel
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        refreshButton.addActionListener(e -> loadLogs());
        searchButton.addActionListener(e -> searchLogs());
        filterCombo.addActionListener(e -> filterLogs());

        // Add main panel to frame
        add(mainPanel);
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        try {
            Connection conn = config.DatabaseConnection.getConnection();
            String query = "SELECT * FROM tbl_user_logs ORDER BY log_timestamp DESC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getTimestamp("log_timestamp"),
                    rs.getString("log_user"),
                    rs.getString("log_action"),
                    rs.getString("log_details"),
                    rs.getString("log_ip")
                };
                tableModel.addRow(row);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading logs: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchLogs() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadLogs();
            return;
        }

        tableModel.setRowCount(0);
        try {
            Connection conn = config.DatabaseConnection.getConnection();
            String query = "SELECT * FROM tbl_user_logs WHERE " +
                          "log_user LIKE ? OR log_action LIKE ? OR log_details LIKE ? OR log_ip LIKE ? " +
                          "ORDER BY log_timestamp DESC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getTimestamp("log_timestamp"),
                    rs.getString("log_user"),
                    rs.getString("log_action"),
                    rs.getString("log_details"),
                    rs.getString("log_ip")
                };
                tableModel.addRow(row);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error searching logs: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterLogs() {
        String filter = (String) filterCombo.getSelectedItem();
        if (filter.equals("All")) {
            loadLogs();
            return;
        }

        tableModel.setRowCount(0);
        try {
            Connection conn = config.DatabaseConnection.getConnection();
            String query = "SELECT * FROM tbl_user_logs WHERE log_action = ? ORDER BY log_timestamp DESC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, filter);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getTimestamp("log_timestamp"),
                    rs.getString("log_user"),
                    rs.getString("log_action"),
                    rs.getString("log_details"),
                    rs.getString("log_ip")
                };
                tableModel.addRow(row);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error filtering logs: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 