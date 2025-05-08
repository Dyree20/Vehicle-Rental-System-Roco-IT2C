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
    }
    
    private void setupActionListeners() {
        // Add listeners to buttons
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        
        btnReturnVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnVehicleActionPerformed(evt);
            }
        });
        
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });
        
        // Add table selection listener
        tblRentals.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblRentals.getSelectedRow();
                if (selectedRow != -1) {
                    selectedRentalId = Integer.parseInt(tblRentals.getValueAt(selectedRow, 0).toString());
                    String status = tblRentals.getValueAt(selectedRow, 6).toString();
                    
                    // Enable/disable return button based on status
                    btnReturnVehicle.setEnabled(status.equalsIgnoreCase("active"));
                    
                    // Load rental details
                    loadRentalDetails(selectedRentalId);
                }
            }
        });
    }
    
    private void loadRentals(String filter) {
        try {
            rentalTableModel.setRowCount(0); // Clear existing rows
            
            Connection con = new dbConnector().getConnection();
            String query = "SELECT r.r_id, v.v_make, v.v_model, c.c_name, r.r_start_date, r.r_end_date, " +
                          "r.r_total_amount, r.r_status, r.r_created_by " +
                          "FROM tbl_rentals r " +
                          "JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id " +
                          "JOIN tbl_clients c ON r.r_client_id = c.c_id";
            
            // Apply filter if not "All"
            if (!filter.equals("All")) {
                query += " WHERE r.r_status = ?";
            }
            
            // Add search filter if provided
            String searchText = txtSearch.getText().trim();
            if (!searchText.isEmpty()) {
                if (filter.equals("All")) {
                    query += " WHERE";
                } else {
                    query += " AND";
                }
                query += " (v.v_make LIKE ? OR v.v_model LIKE ? OR c.c_name LIKE ? OR r.r_created_by LIKE ?)";
            }
            
            query += " ORDER BY r.r_date_created DESC";
            
            PreparedStatement pst = con.prepareStatement(query);
            
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
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                // Format the vehicle and dates
                String vehicle = rs.getString("v_make") + " " + rs.getString("v_model");
                String startDate = dateFormat.format(rs.getDate("r_start_date"));
                String endDate = dateFormat.format(rs.getDate("r_end_date"));
                
                // Add a row to the table
                rentalTableModel.addRow(new Object[] {
                    rs.getInt("r_id"),
                    vehicle,
                    rs.getString("c_name"),
                    startDate,
                    endDate,
                    "$" + rs.getDouble("r_total_amount"),
                    rs.getString("r_status"),
                    rs.getString("r_created_by"),
                    "Return" // Button text
                });
            }
            
            rs.close();
            pst.close();
            con.close();
            
            // Show message if no rentals found
            if (rentalTableModel.getRowCount() == 0) {
                lblStatus.setText("No rentals found");
            } else {
                lblStatus.setText(rentalTableModel.getRowCount() + " rentals found");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rentals: " + e.getMessage());
        }
    }
    
    private void loadRentalDetails(int rentalId) {
        try {
            Connection con = new dbConnector().getConnection();
            String query = "SELECT r.*, v.v_make, v.v_model, v.v_year, v.v_plate, v.v_image, " +
                          "c.c_name, c.c_phone, c.c_email, c.c_license_number " +
                          "FROM tbl_rentals r " +
                          "JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id " +
                          "JOIN tbl_clients c ON r.r_client_id = c.c_id " +
                          "WHERE r.r_id = ?";
            
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, rentalId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
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
                txtAmount.setText("$" + rs.getDouble("r_total_amount"));
                txtStatus.setText(rs.getString("r_status"));
                txtCreatedBy.setText(rs.getString("r_created_by"));
                
                // Store the vehicle ID for return processing
                lblVehicleId.setText(String.valueOf(rs.getInt("r_vehicle_id")));
                
                // Display vehicle image
                byte[] imageData = rs.getBytes("v_image");
                if (imageData != null && imageData.length > 0) {
                    ImageIcon imageIcon = new ImageIcon(imageData);
                    Image image = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    lblVehicleImage.setIcon(new ImageIcon(image));
                    lblVehicleImage.setText("");
                } else {
                    lblVehicleImage.setIcon(null);
                    lblVehicleImage.setText("No Image");
                }
            }
            
            rs.close();
            pst.close();
            con.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rental details: " + e.getMessage());
        }
    }
    
   private void returnVehicle() {
    if (selectedRentalId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a rental first");
        return;
    }
    
    try {
        // Get rental status
        Connection con = new dbConnector().getConnection();
        String statusQuery = "SELECT r_status, r_vehicle_id FROM tbl_rentals WHERE r_id = ?";
        PreparedStatement statusPst = con.prepareStatement(statusQuery);
        statusPst.setInt(1, selectedRentalId);
        ResultSet statusRs = statusPst.executeQuery();
        
        if (statusRs.next()) {
            String status = statusRs.getString("r_status");
            int vehicleId = statusRs.getInt("r_vehicle_id");
            
            if (!status.equalsIgnoreCase("active")) {
                JOptionPane.showMessageDialog(this, "This rental is already " + status);
                statusRs.close();
                statusPst.close();
                con.close();
                return;
            }
            
            // Confirm return
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to mark this vehicle as returned?", 
                    "Confirm Return", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                statusRs.close();
                statusPst.close();
                con.close();
                return;
            }
            
            // Process return
            con.setAutoCommit(false); // Start transaction
            
            // 1. Update rental status
            String rentalQuery = "UPDATE tbl_rentals SET r_status = 'completed' WHERE r_id = ?";
            PreparedStatement rentalPst = con.prepareStatement(rentalQuery);
            rentalPst.setInt(1, selectedRentalId);
            rentalPst.executeUpdate();
            
            // 2. Update vehicle status
            String vehicleQuery = "UPDATE tbl_vehicles SET v_status = 'available' WHERE v_id = ?";
            PreparedStatement vehiclePst = con.prepareStatement(vehicleQuery);
            vehiclePst.setInt(1, vehicleId);
            vehiclePst.executeUpdate();
            
            // Commit transaction
            con.commit();
            
            JOptionPane.showMessageDialog(this, "Vehicle returned successfully!");
            
            // Refresh the table
            loadRentals(cboFilter.getSelectedItem().toString());
            
            // Reset selection
            selectedRentalId = -1;
            btnReturnVehicle.setEnabled(false);
            
            rentalPst.close();
            vehiclePst.close();
        }
        
        statusRs.close();
        statusPst.close();
        con.close();
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error processing return: " + e.getMessage());
    }
}
    private void viewRentalDetails() {
    if (selectedRentalId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a rental first");
        return;
    }
    
    try {
        Connection con = new dbConnector().getConnection();
        String query = "SELECT r.*, v.v_make, v.v_model, v.v_year, v.v_plate, " +
                      "c.c_name, c.c_phone, c.c_email, c.c_license_number " +
                      "FROM tbl_rentals r " +
                      "JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id " +
                      "JOIN tbl_clients c ON r.r_client_id = c.c_id " +
                      "WHERE r.r_id = ?";
        
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, selectedRentalId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            // Create message with rental details
            StringBuilder details = new StringBuilder();
            details.append("RENTAL DETAILS\n\n");
            details.append("Rental ID: ").append(rs.getInt("r_id")).append("\n");
            details.append("Vehicle: ").append(rs.getString("v_make")).append(" ");
            details.append(rs.getString("v_model")).append(" (").append(rs.getString("v_year")).append(")\n");
            details.append("Plate Number: ").append(rs.getString("v_plate")).append("\n");
            details.append("Client: ").append(rs.getString("c_name")).append("\n");
            details.append("Phone: ").append(rs.getString("c_phone")).append("\n");
            details.append("Email: ").append(rs.getString("c_email")).append("\n");
            details.append("License: ").append(rs.getString("c_license_number")).append("\n");
            details.append("Start Date: ").append(dateFormat.format(rs.getDate("r_start_date"))).append("\n");
            details.append("End Date: ").append(dateFormat.format(rs.getDate("r_end_date"))).append("\n");
            details.append("Amount: $").append(rs.getDouble("r_total_amount")).append("\n");
            details.append("Status: ").append(rs.getString("r_status")).append("\n");
            details.append("Created By: ").append(rs.getString("r_created_by")).append("\n");
            
            // Show the details in a message dialog
            JOptionPane.showMessageDialog(this, details.toString(), "Rental Details", JOptionPane.INFORMATION_MESSAGE);
        }
        
        rs.close();
        pst.close();
        con.close();
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading rental details: " + e.getMessage());
    }
}
    // Button renderer for the Actions column
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
        } else {
            setText("Completed");
            setEnabled(false);
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
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            String status = table.getValueAt(row, 6).toString();
            if (status.equalsIgnoreCase("active")) {
                label = "Return";
                button.setText(label);
                button.setEnabled(true);
            } else {
                label = "Completed";
                button.setText(label);
                button.setEnabled(false);
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
        
        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
            if (isPushed) {
                // Get the selected row and rental ID
                int row = tblRentals.getSelectedRow();
                if (row != -1 && label.equals("Return")) {
                    selectedRentalId = Integer.parseInt(tblRentals.getValueAt(row, 0).toString());
                    loadRentalDetails(selectedRentalId);
                    returnVehicle();
                }
            }
        }
    }
    
    
    
    // Event handler methods
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        loadRentals(cboFilter.getSelectedItem().toString());
    }
    
    private void btnReturnVehicleActionPerformed(java.awt.event.ActionEvent evt) {
        returnVehicle();
    }
    
    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedRentalId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental first");
            return;
        }
        
        // Create a formatted message with rental details
        StringBuilder details = new StringBuilder();
        details.append("RENTAL DETAILS\n\n");
        details.append("Rental ID: ").append(txtRentalId.getText()).append("\n");
        details.append("Vehicle: ").append(txtVehicle.getText()).append("\n");
        details.append("Plate Number: ").append(txtPlate.getText()).append("\n");
        details.append("Client: ").append(txtClient.getText()).append("\n");
        details.append("Phone: ").append(txtPhone.getText()).append("\n");
        details.append("Email: ").append(txtEmail.getText()).append("\n");
        details.append("License: ").append(txtLicense.getText()).append("\n");
        details.append("Start Date: ").append(txtStartDate.getText()).append("\n");
        details.append("End Date: ").append(txtEndDate.getText()).append("\n");
        details.append("Amount: ").append(txtAmount.getText()).append("\n");
        details.append("Status: ").append(txtStatus.getText()).append("\n");
        details.append("Created By: ").append(txtCreatedBy.getText()).append("\n");
        
        // Show the details in a message dialog
        JOptionPane.showMessageDialog(this, details.toString(), "Rental Details", JOptionPane.INFORMATION_MESSAGE);
    }

    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cboFilter = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRentals = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnReturnVehicle = new javax.swing.JButton();
        btnViewDetails = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(110, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(110, 0, 0));

        btnSearch.setText("Search");

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch)
                .addGap(27, 27, 27)
                .addComponent(cboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(623, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(cboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, 70));

        tblRentals.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblRentals);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 920, 430));

        jPanel4.setBackground(new java.awt.Color(110, 0, 0));

        btnReturnVehicle.setText("RETURN VEHICLE");

        btnViewDetails.setText("VIEW DETAILS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnReturnVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(604, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReturnVehicle)
                    .addComponent(btnViewDetails))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 920, -1));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFilterActionPerformed
        loadRentals(cboFilter.getSelectedItem().toString());

    }//GEN-LAST:event_cboFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReturnVehicle;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblRentals;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
