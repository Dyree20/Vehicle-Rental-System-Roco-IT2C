/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Date;

public class rentals extends javax.swing.JInternalFrame {
    
    private DefaultTableModel rentalTableModel;
    private int selectedRentalId = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JLabel lblVehicleImage;
    private JLabel lblVehicleId;
    private JLabel lblStatus;
    
    // Detail panel components
    private JPanel detailPanel;
    private JTextField txtRentalId, txtVehicle, txtPlate, txtClient;
    private JTextField txtPhone, txtEmail, txtLicense;
    private JTextField txtStartDate, txtEndDate, txtAmount, txtStatus, txtCreatedBy;
    
    // UI Components
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JScrollPane jScrollPane1;
    private JTable tblRentals;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReturnVehicle;
    private JButton btnViewDetails;
    private JComboBox<String> cboFilter;
    
    /**
     * Creates new form rentals
     */
    public rentals() {
        initComponents();
        setupAdditionalComponents();
        setupTable();
        setupActionListeners();
        loadRentals("All");
    }
    
    private void initComponents() {
        // Initialize panels
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        
        // Set panel properties
        jPanel1.setBackground(new Color(110, 0, 0));
        jPanel2.setBackground(new Color(110, 0, 0));
        jPanel3.setBackground(Color.WHITE);
        jPanel4.setBackground(new Color(110, 0, 0));
        
        // Set layouts
        jPanel1.setLayout(new BorderLayout());
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel3.setLayout(new BorderLayout());
        jPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        // Initialize components
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Search");
        cboFilter = new JComboBox<>(new String[]{"All", "Active", "Completed"});
        jScrollPane1 = new JScrollPane();
        tblRentals = new JTable();
        btnReturnVehicle = new JButton("RETURN VEHICLE");
        btnViewDetails = new JButton("VIEW DETAILS");
        
        // Add components to panels
        jPanel2.add(txtSearch);
        jPanel2.add(btnSearch);
        jPanel2.add(cboFilter);
        
        jScrollPane1.setViewportView(tblRentals);
        jPanel3.add(jScrollPane1, BorderLayout.CENTER);
        
        jPanel4.add(btnReturnVehicle);
        jPanel4.add(btnViewDetails);
        
        // Add panels to main panel
        jPanel1.add(jPanel2, BorderLayout.NORTH);
        jPanel1.add(jPanel3, BorderLayout.CENTER);
        jPanel1.add(jPanel4, BorderLayout.SOUTH);
        
        // Add main panel to frame
        getContentPane().add(jPanel1, BorderLayout.CENTER);
        
        // Set frame properties
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Rentals Management");
        
        // Pack
        pack();
    }
    
    private void setupAdditionalComponents() {
        // Set filter options
        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Active", "Completed" }));
        
        // Create the detail panel with components
        detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(new java.awt.Color(110, 0, 0));
        
        // Vehicle image label
        lblVehicleImage = new JLabel();
        lblVehicleImage.setPreferredSize(new Dimension(200, 150));
        lblVehicleImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblVehicleImage.setHorizontalAlignment(JLabel.CENTER);
        lblVehicleImage.setText("No Image");
        lblVehicleImage.setForeground(Color.WHITE);
        
        // Hidden label for vehicle ID
        lblVehicleId = new JLabel();
        lblVehicleId.setVisible(false);
        
        // Status label
        lblStatus = new JLabel("No rentals found");
        lblStatus.setForeground(Color.WHITE);
        
        // Create text fields
        txtRentalId = new JTextField(10);
        txtVehicle = new JTextField(20);
        txtPlate = new JTextField(10);
        txtClient = new JTextField(20);
        txtPhone = new JTextField(15);
        txtEmail = new JTextField(20);
        txtLicense = new JTextField(15);
        txtStartDate = new JTextField(10);
        txtEndDate = new JTextField(10);
        txtAmount = new JTextField(10);
        txtStatus = new JTextField(10);
        txtCreatedBy = new JTextField(15);
        
        // Make text fields read-only
        txtRentalId.setEditable(false);
        txtVehicle.setEditable(false);
        txtPlate.setEditable(false);
        txtClient.setEditable(false);
        txtPhone.setEditable(false);
        txtEmail.setEditable(false);
        txtLicense.setEditable(false);
        txtStartDate.setEditable(false);
        txtEndDate.setEditable(false);
        txtAmount.setEditable(false);
        txtStatus.setEditable(false);
        txtCreatedBy.setEditable(false);
        
        // Add components to detail panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add vehicle image
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        detailPanel.add(lblVehicleImage, gbc);
        
        // Reset gridheight
        gbc.gridheight = 1;
        
        // Add labels and text fields
        gbc.gridx = 1;
        gbc.gridy = 0;
        detailPanel.add(new JLabel("Rental ID:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtRentalId, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        detailPanel.add(new JLabel("Vehicle:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtVehicle, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        detailPanel.add(new JLabel("Plate:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtPlate, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        detailPanel.add(new JLabel("Client:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtClient, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 1;
        detailPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtPhone, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 2;
        detailPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtEmail, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        detailPanel.add(new JLabel("License:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtLicense, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 3;
        detailPanel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtStartDate, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        detailPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtEndDate, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 4;
        detailPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtAmount, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        detailPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 2;
        detailPanel.add(txtStatus, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 5;
        detailPanel.add(new JLabel("Created By:"), gbc);
        gbc.gridx = 4;
        detailPanel.add(txtCreatedBy, gbc);
        
        // Set all labels and text fields to white foreground
        Component[] components = detailPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                ((JLabel) component).setForeground(Color.WHITE);
            }
        }
        
        // Add status label to the top panel
        jPanel2.add(lblStatus);
        
        // Replace panel4 content with detail panel
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(detailPanel, BorderLayout.CENTER);
        
        // Add buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new java.awt.Color(110, 0, 0));
        buttonsPanel.add(btnReturnVehicle);
        buttonsPanel.add(btnViewDetails);
        jPanel4.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Hide buttons initially
        btnReturnVehicle.setEnabled(false);
    }
    
    private void setupTable() {
        // Set up the table model with columns
        rentalTableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "ID", "Vehicle", "Client", "Start Date", "End Date", "Amount", "Status", "Created By", "Actions"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only allow editing the Actions column
            }
        };
        
        tblRentals.setModel(rentalTableModel);
        
        // Add a button renderer and editor to the Actions column
        tblRentals.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        tblRentals.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Set column widths
        tblRentals.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tblRentals.getColumnModel().getColumn(1).setPreferredWidth(120); // Vehicle
        tblRentals.getColumnModel().getColumn(2).setPreferredWidth(120); // Client
        tblRentals.getColumnModel().getColumn(3).setPreferredWidth(80);  // Start Date
        tblRentals.getColumnModel().getColumn(4).setPreferredWidth(80);  // End Date
        tblRentals.getColumnModel().getColumn(5).setPreferredWidth(80);  // Amount
        tblRentals.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        tblRentals.getColumnModel().getColumn(7).setPreferredWidth(100); // Created By
        tblRentals.getColumnModel().getColumn(8).setPreferredWidth(80);  // Actions

        // Enable row selection
        tblRentals.setRowSelectionAllowed(true);
        tblRentals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add mouse listener for double-click
        tblRentals.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tblRentals.getSelectedRow();
                    if (row != -1) {
                        selectedRentalId = Integer.parseInt(tblRentals.getValueAt(row, 0).toString());
                        loadRentalDetails(selectedRentalId);
                    }
                }
            }
        });
    }
    
    private void setupActionListeners() {
        // Search button
        btnSearch.addActionListener(e -> {
            String searchText = txtSearch.getText().trim();
            if (!searchText.isEmpty()) {
                loadRentals(cboFilter.getSelectedItem().toString());
            } else {
                JOptionPane.showMessageDialog(this, "Please enter search text", "Search Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Return Vehicle button
        btnReturnVehicle.addActionListener(e -> {
            if (selectedRentalId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a rental first", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            returnVehicle();
        });

        // View Details button
        btnViewDetails.addActionListener(e -> {
            if (selectedRentalId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a rental first", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            viewRentalDetails();
        });

        // Filter combo box
        cboFilter.addActionListener(e -> loadRentals(cboFilter.getSelectedItem().toString()));

        // Add table selection listener
        tblRentals.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblRentals.getSelectedRow();
                if (selectedRow != -1) {
                    selectedRentalId = Integer.parseInt(tblRentals.getValueAt(selectedRow, 0).toString());
                    String status = tblRentals.getValueAt(selectedRow, 6).toString();
                    btnReturnVehicle.setEnabled(status.equalsIgnoreCase("active"));
                    loadRentalDetails(selectedRentalId);
                } else {
                    selectedRentalId = -1;
                    btnReturnVehicle.setEnabled(false);
                    clearDetailPanel();
                }
            }
        });

        // Add enter key listener to search field
        txtSearch.addActionListener(e -> btnSearch.doClick());
    }
    
    private void loadRentals(String filter) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            rentalTableModel.setRowCount(0); // Clear existing rows
            
            con = new dbConnector().getConnection();
            StringBuilder query = new StringBuilder(
                "SELECT r.r_id, v.v_make, v.v_model, c.c_name, r.r_start_date, r.r_end_date, " +
                "r.r_total_amount, r.r_status, r.r_created_by " +
                "FROM tbl_rentals r " +
                "JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id " +
                "JOIN tbl_clients c ON r.r_client_id = c.c_id"
            );
            
            // Apply filter if not "All"
            if (!filter.equals("All")) {
                query.append(" WHERE r.r_status = ?");
            }
            
            // Add search filter if provided
            String searchText = txtSearch.getText().trim();
            if (!searchText.isEmpty()) {
                if (filter.equals("All")) {
                    query.append(" WHERE");
                } else {
                    query.append(" AND");
                }
                query.append(" (v.v_make LIKE ? OR v.v_model LIKE ? OR c.c_name LIKE ? OR r.r_created_by LIKE ?)");
            }
            
            query.append(" ORDER BY r.r_date_created DESC");
            
            pst = con.prepareStatement(query.toString());
            
            int paramIndex = 1;
            if (!filter.equals("All")) {
                pst.setString(paramIndex++, filter.toLowerCase());
            }
            
            if (!searchText.isEmpty()) {
                String pattern = "%" + searchText + "%";
                pst.setString(paramIndex++, pattern);
                pst.setString(paramIndex++, pattern);
                pst.setString(paramIndex++, pattern);
                pst.setString(paramIndex++, pattern);
            }
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                // Format the vehicle and dates
                String vehicle = rs.getString("v_make") + " " + rs.getString("v_model");
                String startDate = dateFormat.format(rs.getDate("r_start_date"));
                String endDate = dateFormat.format(rs.getDate("r_end_date"));
                double amount = rs.getDouble("r_total_amount");
                
                // Add a row to the table
                rentalTableModel.addRow(new Object[] {
                    rs.getInt("r_id"),
                    vehicle,
                    rs.getString("c_name"),
                    startDate,
                    endDate,
                    "₱" + String.format("%.2f", amount),
                    rs.getString("r_status"),
                    rs.getString("r_created_by"),
                    "Return" // Button text
                });
            }
            
            // Update status label
            updateStatusLabel();
            
        } catch (SQLException e) {
            handleDatabaseError("Error loading rentals", e);
        } finally {
            closeResources(con, pst, rs);
        }
    }
    
    private void loadRentalDetails(int rentalId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = new dbConnector().getConnection();
            String query = "SELECT r.*, v.v_make, v.v_model, v.v_year, v.v_plate, v.v_image, " +
                          "c.c_name, c.c_phone, c.c_email, c.c_license_number " +
                          "FROM tbl_rentals r " +
                          "JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id " +
                          "JOIN tbl_clients c ON r.r_client_id = c.c_id " +
                          "WHERE r.r_id = ?";
            
            pst = con.prepareStatement(query);
            pst.setInt(1, rentalId);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                updateDetailPanel(rs);
            } else {
                JOptionPane.showMessageDialog(this, "Rental not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            handleDatabaseError("Error loading rental details", e);
        } finally {
            closeResources(con, pst, rs);
        }
    }
    
    private void updateDetailPanel(ResultSet rs) throws SQLException {
        // Display rental details in the detail panel components
        txtRentalId.setText(String.valueOf(rs.getInt("r_id")));
        txtVehicle.setText(rs.getString("v_make") + " " + rs.getString("v_model") + " (" + rs.getString("v_year") + ")");
        txtPlate.setText(rs.getString("v_plate"));
        txtClient.setText(rs.getString("c_name"));
        txtPhone.setText(rs.getString("c_phone"));
        txtEmail.setText(rs.getString("c_email"));
        txtLicense.setText(rs.getString("c_license_number"));
        txtStartDate.setText(dateFormat.format(rs.getDate("r_start_date")));
        txtEndDate.setText(dateFormat.format(rs.getDate("r_end_date")));
        txtAmount.setText("₱" + String.format("%.2f", rs.getDouble("r_total_amount")));
        txtStatus.setText(rs.getString("r_status"));
        txtCreatedBy.setText(rs.getString("r_created_by"));
        
        // Store the vehicle ID for return processing
        lblVehicleId.setText(String.valueOf(rs.getInt("r_vehicle_id")));
        
        // Display vehicle image
        updateVehicleImage(rs.getBytes("v_image"));
    }
    
    private void updateVehicleImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            try {
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                lblVehicleImage.setIcon(new ImageIcon(image));
                lblVehicleImage.setText("");
            } catch (Exception e) {
                lblVehicleImage.setIcon(null);
                lblVehicleImage.setText("Invalid Image");
            }
        } else {
            lblVehicleImage.setIcon(null);
            lblVehicleImage.setText("No Image");
        }
    }
    
    private void updateStatusLabel() {
        int rowCount = rentalTableModel.getRowCount();
        if (rowCount == 0) {
            lblStatus.setText("No rentals found");
        } else {
            lblStatus.setText(rowCount + " rental" + (rowCount == 1 ? "" : "s") + " found");
        }
    }
    
    private void handleDatabaseError(String message, SQLException e) {
        String errorMessage = message + ": " + e.getMessage();
        System.err.println(errorMessage);
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, errorMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void closeResources(Connection con, PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    private void returnVehicle() {
        if (selectedRentalId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection con = null;
        PreparedStatement statusPst = null;
        PreparedStatement rentalPst = null;
        PreparedStatement vehiclePst = null;
        ResultSet statusRs = null;
        
        try {
            con = new dbConnector().getConnection();
            
            // Get rental status
            String statusQuery = "SELECT r_status, r_vehicle_id FROM tbl_rentals WHERE r_id = ?";
            statusPst = con.prepareStatement(statusQuery);
            statusPst.setInt(1, selectedRentalId);
            statusRs = statusPst.executeQuery();
            
            if (statusRs.next()) {
                String status = statusRs.getString("r_status");
                int vehicleId = statusRs.getInt("r_vehicle_id");
                
                if (!status.equalsIgnoreCase("active")) {
                    JOptionPane.showMessageDialog(this, "This rental is already " + status, "Invalid Status", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Confirm return
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to mark this vehicle as returned?", 
                        "Confirm Return", 
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                
                // Process return
                con.setAutoCommit(false); // Start transaction
                
                try {
                    // 1. Update rental status
                    String rentalQuery = "UPDATE tbl_rentals SET r_status = 'completed', r_date_returned = CURRENT_TIMESTAMP WHERE r_id = ?";
                    rentalPst = con.prepareStatement(rentalQuery);
                    rentalPst.setInt(1, selectedRentalId);
                    rentalPst.executeUpdate();
                    
                    // 2. Update vehicle status
                    String vehicleQuery = "UPDATE tbl_vehicles SET v_status = 'available' WHERE v_id = ?";
                    vehiclePst = con.prepareStatement(vehicleQuery);
                    vehiclePst.setInt(1, vehicleId);
                    vehiclePst.executeUpdate();
                    
                    // Commit transaction
                    con.commit();
                    
                    JOptionPane.showMessageDialog(this, "Vehicle returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the table
                    loadRentals(cboFilter.getSelectedItem().toString());
                    
                    // Reset selection
                    selectedRentalId = -1;
                    btnReturnVehicle.setEnabled(false);
                    
                } catch (SQLException e) {
                    // Rollback transaction on error
                    if (con != null) {
                        try {
                            con.rollback();
                        } catch (SQLException ex) {
                            handleDatabaseError("Error rolling back transaction", ex);
                        }
                    }
                    throw e;
                }
            }
            
        } catch (SQLException e) {
            handleDatabaseError("Error processing return", e);
        } finally {
            closeResources(con, statusPst, statusRs);
            closeResources(null, rentalPst, null);
            closeResources(null, vehiclePst, null);
        }
    }
    
    private void viewRentalDetails() {
        if (selectedRentalId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Instead of showing a message dialog, just load the details in the detail panel
        loadRentalDetails(selectedRentalId);
    }
    
    private void clearDetailPanel() {
        txtRentalId.setText("");
        txtVehicle.setText("");
        txtPlate.setText("");
        txtClient.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtLicense.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtAmount.setText("");
        txtStatus.setText("");
        txtCreatedBy.setText("");
        lblVehicleImage.setIcon(null);
        lblVehicleImage.setText("No Image");
    }
    
    // Button renderer for the Actions column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = table.getValueAt(row, 6).toString();
            if (status.equalsIgnoreCase("active")) {
                setText("Return");
                setEnabled(true);
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            } else {
                setText("Completed");
                setEnabled(false);
                setBackground(table.getBackground());
                setForeground(Color.GRAY);
            }
            return this;
        }
    }
    
    // Button editor for the Actions column
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                if (isPushed) {
                    int row = tblRentals.getSelectedRow();
                    if (row != -1 && label.equals("Return")) {
                        selectedRentalId = Integer.parseInt(tblRentals.getValueAt(row, 0).toString());
                        loadRentalDetails(selectedRentalId);
                        returnVehicle();
                    }
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            String status = table.getValueAt(row, 6).toString();
            if (status.equalsIgnoreCase("active")) {
                label = "Return";
                button.setText(label);
                button.setEnabled(true);
                button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            } else {
                label = "Completed";
                button.setText(label);
                button.setEnabled(false);
                button.setBackground(table.getBackground());
                button.setForeground(Color.GRAY);
            }
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
