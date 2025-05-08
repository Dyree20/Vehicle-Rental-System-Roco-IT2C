/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ROCO
 */
public class add_clients extends javax.swing.JInternalFrame {

    private int selectedVehicleId = -1;
private JPanel selectedCardPanel = null;
private Color originalCardColor = new Color(128, 0, 0); // Dark red background
private Color selectedCardColor = new Color(200, 100, 0); // Orange-ish highlight
    
    /**
     * Creates new form add_clients
     */
    public add_clients() {
        initComponents();
        vehicleCardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    vehicleCardsContainer.setBackground(new Color(180, 180, 180)); // Light grey background
    
    // Set up filter
    cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Vehicles", "Available Only", "Luxury", "Economy" }));
    
    // Initialize buttons
    btnRentVehicle.setEnabled(false);
    btnViewDetails.setEnabled(false);
    
    // Set default selected vehicle text
    lblSelectedVehicle.setText("Selected Vehicle: None");
    
    // Load vehicles
    loadVehicleCards();
}
    
    private void setupComponents() {
    // Set FlowLayout for the vehicle cards container
    vehicleCardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    
    // Update filter combo box items
    cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
        "All Vehicles", "Available Only", "Luxury", "Economy" 
    }));
    
    // Set the selected vehicle label text
    lblSelectedVehicle.setText("Selected Vehicle: None");
}
    private void loadVehicleCards() {
    try {
        // Clear existing cards
        vehicleCardsContainer.removeAll();
        
        // Connect to database
        Connection con = new dbConnector().getConnection();
        String query = "SELECT * FROM tbl_vehicles";
        
        // Apply filter if selected
        if (cboFilter.getSelectedItem().toString().equals("Available Only")) {
            query += " WHERE v_status = 'available'";
        } else if (cboFilter.getSelectedItem().toString().equals("Luxury")) {
            query += " WHERE v_rate > 500";  // Adjust threshold as needed
        } else if (cboFilter.getSelectedItem().toString().equals("Economy")) {
            query += " WHERE v_rate <= 500";  // Adjust threshold as needed
        }
        
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        
        int count = 0;
        
        while (rs.next()) {
            count++;
            // Get vehicle data
            final int vehicleId = rs.getInt("v_id");
            final String make = rs.getString("v_make");
            final String model = rs.getString("v_model");
            final String year = rs.getString("v_year");
            final String plate = rs.getString("v_plate");
            final String rate = rs.getString("v_rate");
            final String status = rs.getString("v_status");
            byte[] imageData = rs.getBytes("v_image");
            
            // Create a card panel for this vehicle
            final JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BorderLayout(10, 0));
            cardPanel.setPreferredSize(new Dimension(300, 150));
            cardPanel.setBackground(originalCardColor); // Dark red background
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            // Store the vehicle ID as a property of the panel
            cardPanel.putClientProperty("vehicleId", vehicleId);
            
            // Make the card interactive
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Call selection method
                    selectVehicleCard(cardPanel, vehicleId, make + " " + model);
                }
                
                // Add hover effect (optional)
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (cardPanel != selectedCardPanel) {
                        cardPanel.setBackground(new Color(150, 30, 30)); // Slightly lighter on hover
                    }
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (cardPanel != selectedCardPanel) {
                        cardPanel.setBackground(originalCardColor); // Back to original when not hovered
                    }
                }
            });
            
            // Image panel (left side)
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(120, 100));
            imageLabel.setBackground(Color.BLACK);
            imageLabel.setForeground(Color.WHITE);
            
            if (imageData != null && imageData.length > 0) {
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } else {
                imageLabel.setText("No Image");
            }
            
            // Text panel (right side)
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            
            // Create text labels
            JLabel nameLabel = new JLabel("Name: " + make + " " + model);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
            
            JLabel typeYearLabel = new JLabel("Type: Year: " + year + " Plate: " + plate);
            typeYearLabel.setForeground(Color.WHITE);
            typeYearLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
            
            JLabel priceLabel = new JLabel("Price: " + rate + " per week");
            priceLabel.setForeground(Color.WHITE);
            priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
            
            JLabel statusLabel = new JLabel("Status: " + status);
            statusLabel.setForeground(Color.WHITE);
            if (status.equals("available")) {
                statusLabel.setForeground(Color.GREEN);
            } else {
                statusLabel.setForeground(Color.RED);
            }
            statusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
            
            // Add text labels to the text panel
            textPanel.add(Box.createVerticalStrut(20));
            textPanel.add(nameLabel);
            textPanel.add(Box.createVerticalStrut(10));
            textPanel.add(typeYearLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(priceLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(statusLabel);
            
            // Add image and text panels to the card
            cardPanel.add(imageLabel, BorderLayout.WEST);
            cardPanel.add(textPanel, BorderLayout.CENTER);
            
            // Check if this is the previously selected vehicle and highlight it
            if (vehicleId == selectedVehicleId) {
                cardPanel.setBackground(selectedCardColor);
                selectedCardPanel = cardPanel;
            }
            
            // Add the card to the container
            vehicleCardsContainer.add(cardPanel);
        }
        
        // Add a message if no vehicles found
        if (count == 0) {
            JLabel noVehiclesLabel = new JLabel("No vehicles found matching your criteria.");
            noVehiclesLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
            noVehiclesLabel.setForeground(Color.WHITE);
            vehicleCardsContainer.add(noVehiclesLabel);
        }
        
        // Refresh the container
        vehicleCardsContainer.revalidate();
        vehicleCardsContainer.repaint();
        
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
    }
}
private void selectVehicleCard(JPanel cardPanel, int vehicleId, String vehicleName) {
    // If there was a previously selected card, reset its color
    if (selectedCardPanel != null) {
        selectedCardPanel.setBackground(originalCardColor);
    }
    
    // Update the selected vehicle and card
    selectedVehicleId = vehicleId;
    selectedCardPanel = cardPanel;
    
    // Change the selected card's color
    cardPanel.setBackground(selectedCardColor);
    
    // Update the selected vehicle label
    lblSelectedVehicle.setText("Selected Vehicle: " + vehicleName);
    
    // Enable the rent and view buttons
    btnRentVehicle.setEnabled(true);
    btnViewDetails.setEnabled(true);
    
    // Repaint to show the changes
    vehicleCardsContainer.repaint();
}
// Additional reset method (add this too)
private void resetSelection() {
    selectedVehicleId = -1;
    selectedCardPanel = null;
    lblSelectedVehicle.setText("Selected Vehicle: None");
    btnRentVehicle.setEnabled(false);
    btnViewDetails.setEnabled(false);
}
// Add this variable declaration at the class leve    
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
    // Search implementation
}
private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
    txtSearch.setText("");
    loadVehicleCards();
}


private void cboFilterActionPerformed(java.awt.event.ActionEvent evt) {
    loadVehicleCards();
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
        searchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        cboFilter = new javax.swing.JComboBox<>();
        vehicleDisplayPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vehicleCardsContainer = new javax.swing.JPanel();
        actionPanel = new javax.swing.JPanel();
        btnRentVehicle = new javax.swing.JButton();
        btnViewDetails = new javax.swing.JButton();
        lblSelectedVehicle = new javax.swing.JLabel();

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchPanel.setBackground(new java.awt.Color(110, 0, 0));

        btnSearch.setText("SEARCH");

        btnClear.setText("CLEAR");

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClear)
                .addGap(18, 18, 18)
                .addComponent(cboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(541, Short.MAX_VALUE))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnClear)
                    .addComponent(cboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.add(searchPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 60));

        vehicleDisplayPanel.setBackground(new java.awt.Color(110, 50, 50));

        jScrollPane1.setViewportView(vehicleCardsContainer);

        javax.swing.GroupLayout vehicleDisplayPanelLayout = new javax.swing.GroupLayout(vehicleDisplayPanel);
        vehicleDisplayPanel.setLayout(vehicleDisplayPanelLayout);
        vehicleDisplayPanelLayout.setHorizontalGroup(
            vehicleDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
        );
        vehicleDisplayPanelLayout.setVerticalGroup(
            vehicleDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );

        jPanel1.add(vehicleDisplayPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 930, 400));

        actionPanel.setBackground(new java.awt.Color(80, 50, 50));

        btnRentVehicle.setText("RENT VEHICLE");
        btnRentVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentVehicleActionPerformed(evt);
            }
        });

        btnViewDetails.setText("VIEW DETAILS");
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });

        lblSelectedVehicle.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRentVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 416, Short.MAX_VALUE)
                .addComponent(lblSelectedVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRentVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addComponent(lblSelectedVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel1.add(actionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 930, 140));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRentVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentVehicleActionPerformed
        if (selectedVehicleId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a vehicle first");
        return;
    }
    
    // Check if the vehicle is available
    try {
        Connection con = new dbConnector().getConnection();
        String query = "SELECT v_status FROM tbl_vehicles WHERE v_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, selectedVehicleId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String status = rs.getString("v_status");
            
            if (!status.equalsIgnoreCase("available")) {
                JOptionPane.showMessageDialog(this, "This vehicle is not available for rent");
                rs.close();
                pst.close();
                con.close();
                return;
            }
            
            // Get client information
            String clientName = JOptionPane.showInputDialog(this, "Enter client name:");
            if (clientName == null || clientName.trim().isEmpty()) {
                rs.close();
                pst.close();
                con.close();
                return; // User cancelled
            }
            
            String clientPhone = JOptionPane.showInputDialog(this, "Enter client phone:");
            if (clientPhone == null || clientPhone.trim().isEmpty()) {
                rs.close();
                pst.close();
                con.close();
                return; // User cancelled
            }
            
            String licenseNumber = JOptionPane.showInputDialog(this, "Enter license number:");
            if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
                rs.close();
                pst.close();
                con.close();
                return; // User cancelled
            }
            
            // Create new client
            String insertClientSQL = "INSERT INTO tbl_clients (c_name, c_phone, c_license_number) VALUES (?, ?, ?)";
            PreparedStatement insertClientStmt = con.prepareStatement(insertClientSQL, Statement.RETURN_GENERATED_KEYS);
            insertClientStmt.setString(1, clientName);
            insertClientStmt.setString(2, clientPhone);
            insertClientStmt.setString(3, licenseNumber);
            insertClientStmt.executeUpdate();
            
            // Get new client ID
            ResultSet generatedKeys = insertClientStmt.getGeneratedKeys();
            int clientId = -1;
            if (generatedKeys.next()) {
                clientId = generatedKeys.getInt(1);
            }
            generatedKeys.close();
            insertClientStmt.close();
            
            if (clientId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to create client record");
                rs.close();
                pst.close();
                con.close();
                return;
            }
            
            // Create rental with fixed 7-day period
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date startDate = new java.sql.Date(currentDate.getTime());
            
            // Calculate end date (7 days later)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(java.util.Calendar.DATE, 7);
            java.util.Date utilDate = cal.getTime();
            java.sql.Date endDate = new java.sql.Date(utilDate.getTime());
            
            // Get vehicle rate
            String rateQuery = "SELECT v_rate FROM tbl_vehicles WHERE v_id = ?";
            PreparedStatement rateStmt = con.prepareStatement(rateQuery);
            rateStmt.setInt(1, selectedVehicleId);
            ResultSet rateRs = rateStmt.executeQuery();
            
            double totalAmount = 0;
            if (rateRs.next()) {
                totalAmount = rateRs.getDouble("v_rate"); // Weekly rate
            }
            
            rateRs.close();
            rateStmt.close();
            
            // Begin transaction
            con.setAutoCommit(false);
            
            try {
                // 1. Create rental record
                String rentalSQL = "INSERT INTO tbl_rentals (r_vehicle_id, r_client_id, r_start_date, r_end_date, r_total_amount) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement rentalStmt = con.prepareStatement(rentalSQL);
                rentalStmt.setInt(1, selectedVehicleId);
                rentalStmt.setInt(2, clientId);
                rentalStmt.setDate(3, startDate);
                rentalStmt.setDate(4, endDate);
                rentalStmt.setDouble(5, totalAmount);
                rentalStmt.executeUpdate();
                rentalStmt.close();
                
                // 2. Update vehicle status
                String updateSQL = "UPDATE tbl_vehicles SET v_status = 'rented' WHERE v_id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateSQL);
                updateStmt.setInt(1, selectedVehicleId);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                // Commit transaction
                con.commit();
                
                JOptionPane.showMessageDialog(this, 
                    "Vehicle rented successfully!\n" +
                    "Client: " + clientName + "\n" +
                    "Start Date: " + startDate + "\n" +
                    "End Date: " + endDate + "\n" +
                    "Total Amount: $" + totalAmount);
                
                // Refresh vehicle list
                loadVehicleCards();
                resetSelection();
                
            } catch (SQLException e) {
                // Rollback transaction on error
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Error creating rental: " + e.getMessage());
            }
        }
        
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnRentVehicleActionPerformed

    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        if (selectedVehicleId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a vehicle first", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        Connection con = new dbConnector().getConnection();
        String query = "SELECT * FROM tbl_vehicles WHERE v_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, selectedVehicleId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            // Create a formatted message with vehicle details
            StringBuilder details = new StringBuilder();
            details.append("VEHICLE DETAILS\n\n");
            details.append("ID: ").append(rs.getInt("v_id")).append("\n");
            details.append("Make: ").append(rs.getString("v_make")).append("\n");
            details.append("Model: ").append(rs.getString("v_model")).append("\n");
            details.append("Year: ").append(rs.getString("v_year")).append("\n");
            details.append("Plate Number: ").append(rs.getString("v_plate")).append("\n");
            details.append("Weekly Rate: $").append(rs.getString("v_rate")).append("\n");
            details.append("Status: ").append(rs.getString("v_status")).append("\n");
            
            // Show the details in a message dialog
            JOptionPane.showMessageDialog(this, details.toString(), "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
        }
        
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error retrieving vehicle details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btnViewDetailsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRentVehicle;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSelectedVehicle;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JPanel vehicleCardsContainer;
    private javax.swing.JPanel vehicleDisplayPanel;
    // End of variables declaration//GEN-END:variables
}
