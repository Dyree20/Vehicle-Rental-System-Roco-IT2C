/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import config.Logger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author ROCO
 */
public class add_clients extends javax.swing.JInternalFrame {
    
    private javax.swing.JTextField txtClientEmail;
    private int selectedVehicleId = -1;
    private JPanel selectedCardPanel = null;
    private Color originalCardColor = new Color(128, 0, 0); // Dark red background
    private Color selectedCardColor = new Color(200, 100, 0); // Orange-ish highlight
    
    /**
     * Creates new form add_clients
     */
    public add_clients() {
        initComponents();
        setupComponents();
        // Remove border and title bar for borderless internal frame
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));   
        javax.swing.plaf.basic.BasicInternalFrameUI bi = (javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI();
        bi.setNorthPane(null);
        
        // Initialize the vehicle cards container
        if (vehicleCardsContainer != null) {
            vehicleCardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            vehicleCardsContainer.setBackground(new Color(180, 180, 180));
        }
        
        // Initialize the filter combo box
        if (cboFilter != null) {
            cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Truck", "Motorcycle", "AUV", "SUV" }));
        }
        
        // Initialize buttons and labels
        if (btnRentVehicle != null) btnRentVehicle.setEnabled(false);
        if (btnViewDetails != null) btnViewDetails.setEnabled(false);
        if (lblSelectedVehicle != null) lblSelectedVehicle.setText("Selected Vehicle: None");
        
        // Load vehicle cards
        loadVehicleCards();
        
        // Add listeners for full search functionality
        if (btnSearch != null) btnSearch.addActionListener(this::btnSearchActionPerformed);
        if (cboFilter != null) cboFilter.addActionListener(e -> btnSearchActionPerformed(null));
        if (txtSearch != null) txtSearch.addActionListener(e -> btnSearchActionPerformed(null));
    }
    
    private void setupComponents() {
        // Set FlowLayout for the vehicle cards container
        vehicleCardsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        // Update filter combo box items
        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "All", "Truck", "Motorcycle", "AUV", "SUV" 
        }));
        
        // Set the selected vehicle label text
        lblSelectedVehicle.setText("Selected Vehicle: None");
    }

    private void loadVehicleCards() {
        try {
            vehicleCardsContainer.removeAll();
            Connection con = new dbConnector().getConnection();
            StringBuilder query = new StringBuilder("SELECT * FROM tbl_vehicles");
            boolean hasFilter = !cboFilter.getSelectedItem().toString().equals("All");
            String searchText = txtSearch.getText().trim().toLowerCase();
            boolean hasSearch = !searchText.isEmpty();
            if (hasFilter || hasSearch) {
                query.append(" WHERE ");
                if (hasFilter) {
                    query.append("v_type = ?");
                }
                if (hasSearch) {
                    if (hasFilter) query.append(" AND ");
                    query.append("(LOWER(v_make) LIKE ? OR LOWER(v_model) LIKE ? OR LOWER(v_type) LIKE ?)");
                }
            }
            PreparedStatement pst = con.prepareStatement(query.toString());
            int paramIndex = 1;
            if (hasFilter) {
                pst.setString(paramIndex++, cboFilter.getSelectedItem().toString());
            }
            if (hasSearch) {
                String likeText = "%" + searchText + "%";
                pst.setString(paramIndex++, likeText);
                pst.setString(paramIndex++, likeText);
                pst.setString(paramIndex++, likeText);
            }
            ResultSet rs = pst.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                final int vehicleId = rs.getInt("v_id");
                final String make = rs.getString("v_make");
                final String model = rs.getString("v_model");
                final String year = rs.getString("v_year");
                final String plate = rs.getString("v_plate");
                final String rate = rs.getString("v_rate");
                final String status = rs.getString("v_status");
                final String vType = rs.getString("v_type");
                byte[] imageData = rs.getBytes("v_image");
                
                final JPanel cardPanel = createVehicleCard(make, model, year, plate, 
                    rate, status, vType, imageData, originalCardColor, new Dimension(300, 150));
                
                cardPanel.putClientProperty("vehicleId", vehicleId);
                cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        selectVehicleCard(cardPanel, vehicleId, make + " " + model);
                    }
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        if (cardPanel != selectedCardPanel) {
                            cardPanel.setBackground(new Color(150, 30, 30));
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        if (cardPanel != selectedCardPanel) {
                            cardPanel.setBackground(originalCardColor);
                        }
                    }
                });
                
                vehicleCardsContainer.add(cardPanel);
            }
            
            if (count == 0) {
                JLabel noResultsLabel = new JLabel("No vehicles found");
                noResultsLabel.setForeground(Color.WHITE);
                noResultsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
                vehicleCardsContainer.add(noResultsLabel);
            }
            
            vehicleCardsContainer.revalidate();
            vehicleCardsContainer.repaint();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading vehicles: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createVehicleCard(String make, String model, String year, String plate, 
                                   String rate, String status, String vType, byte[] imageData, 
                                   Color bgColor, Dimension cardSize) {
        JPanel cardPanel = new JPanel(new BorderLayout(10, 0));
        cardPanel.setPreferredSize(cardSize);
        cardPanel.setBackground(bgColor);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Image label
        JLabel imageLabel = new JLabel("No Image");
        imageLabel.setPreferredSize(new Dimension(120, 100));
        imageLabel.setBackground(Color.BLACK);
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        if (imageData != null && imageData.length > 0) {
            ImageIcon icon = new ImageIcon(imageData);
            Image img = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText(""); // Clear the text when setting an image
        }
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        // Labels
        JLabel nameLabel = new JLabel("Name: " + make + " " + model);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        JLabel typeYearLabel = new JLabel("Type: " + vType + " | Year: " + year + " | Plate: " + plate);
        typeYearLabel.setForeground(Color.WHITE);
        typeYearLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        
        JLabel priceLabel = new JLabel("Price: ₱" + rate + " per week");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        
        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setForeground(status.equals("available") ? Color.GREEN : Color.RED);
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        // Add components
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(typeYearLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(priceLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(statusLabel);
        
        cardPanel.add(imageLabel, BorderLayout.WEST);
        cardPanel.add(textPanel, BorderLayout.CENTER);
        
        return cardPanel;
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
        String searchText = txtSearch.getText().trim();
        String filterType = cboFilter.getSelectedItem() != null ? cboFilter.getSelectedItem().toString() : "All";
        // Log search action
        try {
            String userIp = "Unknown";
            try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("SEARCH VEHICLE", "Search text: '" + searchText + "', Type: '" + filterType + "'", username, userIp);
        } catch (Exception e) { e.printStackTrace(); }
        loadVehicleCards();
    }
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
        txtSearch.setText("");
        loadVehicleCards();
        // Log clear action
        try {
            String userIp = "Unknown";
            try { userIp = java.net.InetAddress.getLocalHost().getHostAddress(); } catch (java.net.UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("CLEAR CLIENT SEARCH", "Cleared client search/filter", username, userIp);
        } catch (Exception e) { e.printStackTrace(); }
    }


    private void cboFilterActionPerformed(java.awt.event.ActionEvent evt) {
        loadVehicleCards();
        // Log filter action
        try {
            String userIp = "Unknown";
            try { userIp = java.net.InetAddress.getLocalHost().getHostAddress(); } catch (java.net.UnknownHostException e) {}
            String username = System.getProperty("user.name");
            String filterType = cboFilter.getSelectedItem() != null ? cboFilter.getSelectedItem().toString() : "All";
            Logger.log("FILTER CLIENTS", "Filter selected: '" + filterType + "'", username, userIp);
        } catch (Exception e) { e.printStackTrace(); }
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
        edit_client = new javax.swing.JButton();

        jPanel1.setLayout(new BorderLayout());

        searchPanel.setBackground(new java.awt.Color(110, 0, 0));

        btnSearch.setText("SEARCH");

        btnClear.setText("CLEAR");

        cboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Truck", "Motorcycle", "AUV", "SUV" }));

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

        jPanel1.add(searchPanel, BorderLayout.NORTH);

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

        jPanel1.add(vehicleDisplayPanel, BorderLayout.CENTER);

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

        edit_client.setText("EDIT");

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(actionPanelLayout.createSequentialGroup()
                        .addComponent(btnRentVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(edit_client, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 262, Short.MAX_VALUE)
                .addComponent(lblSelectedVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edit_client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRentVehicle, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addComponent(lblSelectedVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel1.add(actionPanel, BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRentVehicleActionPerformed(java.awt.event.ActionEvent evt) {                                               
        
    if (selectedVehicleId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a vehicle first.");
        return;
    }
    
    // Create rental dialog
    JDialog rentalDialog = new JDialog();
    rentalDialog.setTitle("Rent Vehicle");
    rentalDialog.setSize(400, 400); // Increased height for date pickers
    rentalDialog.setLocationRelativeTo(this);
    rentalDialog.setModal(true);
    rentalDialog.setLayout(new BorderLayout());
    
    // Create rental form panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Client name field
    JPanel namePanel = new JPanel(new BorderLayout(5, 0));
    namePanel.add(new JLabel("Client Name:"), BorderLayout.WEST);
    JTextField txtClientName = new JTextField(20);
    namePanel.add(txtClientName, BorderLayout.CENTER);
    formPanel.add(namePanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Client phone field
    JPanel phonePanel = new JPanel(new BorderLayout(5, 0));
    phonePanel.add(new JLabel("Phone Number:"), BorderLayout.WEST);
    JTextField txtClientPhone = new JTextField(20);
    phonePanel.add(txtClientPhone, BorderLayout.CENTER);
    formPanel.add(phonePanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Client email field
    JPanel emailPanel = new JPanel(new BorderLayout(5, 0));
    emailPanel.add(new JLabel("Email Address:"), BorderLayout.WEST);
    txtClientEmail = new JTextField(20);
    emailPanel.add(txtClientEmail, BorderLayout.CENTER);
    formPanel.add(emailPanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Client license field
    JPanel licensePanel = new JPanel(new BorderLayout(5, 0));
    licensePanel.add(new JLabel("License Number:"), BorderLayout.WEST);
    JTextField txtClientLicense = new JTextField(20);
    licensePanel.add(txtClientLicense, BorderLayout.CENTER);
    formPanel.add(licensePanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Client address field
    JPanel addressPanel = new JPanel(new BorderLayout(5, 0));
    addressPanel.add(new JLabel("Address:"), BorderLayout.WEST);
    JTextField txtClientAddress = new JTextField(20);
    addressPanel.add(txtClientAddress, BorderLayout.CENTER);
    formPanel.add(addressPanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Start date field (use JDateChooser from JCalendar library or com.toedter.calendar)
    JPanel startDatePanel = new JPanel(new BorderLayout(5, 0));
    startDatePanel.add(new JLabel("Start Date:"), BorderLayout.WEST);
    
    // For simplicity, let's use a formatted text field instead of JDateChooser
    JTextField txtStartDate = new JTextField(20);
    txtStartDate.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    startDatePanel.add(txtStartDate, BorderLayout.CENTER);
    
    formPanel.add(startDatePanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // End date field
    JPanel endDatePanel = new JPanel(new BorderLayout(5, 0));
    endDatePanel.add(new JLabel("End Date:"), BorderLayout.WEST);
    
    // For simplicity, let's use a formatted text field
    JTextField txtEndDate = new JTextField(20);
    // Set default end date to one week later
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.add(java.util.Calendar.WEEK_OF_YEAR, 1);
    txtEndDate.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
    
    endDatePanel.add(txtEndDate, BorderLayout.CENTER);
    formPanel.add(endDatePanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Number of weeks (calculated field)
    JPanel weeksPanel = new JPanel(new BorderLayout(5, 0));
    weeksPanel.add(new JLabel("Number of Weeks:"), BorderLayout.WEST);
    JTextField txtWeeks = new JTextField(20);
    txtWeeks.setText("1"); // Default 1 week
    txtWeeks.setEditable(false); // This is calculated
    weeksPanel.add(txtWeeks, BorderLayout.CENTER);
    formPanel.add(weeksPanel);
    formPanel.add(Box.createVerticalStrut(10));
    
    // Add listeners to update weeks when dates change
    txtStartDate.addActionListener(e -> updateWeekCalculation(txtStartDate, txtEndDate, txtWeeks));
    txtEndDate.addActionListener(e -> updateWeekCalculation(txtStartDate, txtEndDate, txtWeeks));
    
    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton rentButton = new JButton("Rent");
    JButton cancelButton = new JButton("Cancel");
    
    rentButton.addActionListener(e -> {
        String clientName = txtClientName.getText().trim();
        String clientPhone = txtClientPhone.getText().trim();
        String clientEmail = txtClientEmail.getText().trim();
        String clientLicense = txtClientLicense.getText().trim();
        String clientAddress = txtClientAddress.getText().trim();
        String startDate = txtStartDate.getText().trim();
        String endDate = txtEndDate.getText().trim();
        
        // Validate inputs
        if (clientName.isEmpty() || clientPhone.isEmpty() || clientEmail.isEmpty() || 
            clientLicense.isEmpty() || clientAddress.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(rentalDialog, "Please fill in all fields.");
            return;
        }
        
        // Email validation
        if (!clientEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(rentalDialog, "Please enter a valid email address.");
            return;
        }
        
        // Date validation
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            java.util.Date start = dateFormat.parse(startDate);
            java.util.Date end = dateFormat.parse(endDate);
            
            if (end.before(start)) {
                JOptionPane.showMessageDialog(rentalDialog, "End date cannot be before start date.");
                return;
            }
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(rentalDialog, "Please enter valid dates in yyyy-MM-dd format.");
            return;
        }
        
        // Process rental with custom start and end dates
        if (processRentalWithCustomDates(selectedVehicleId, clientName, clientPhone, clientEmail, clientLicense, clientAddress, startDate, endDate)) {
            rentalDialog.dispose();
            loadVehicleCards(); // Refresh vehicle list
            resetSelection();   // Reset vehicle selection
        }
    });
    
    cancelButton.addActionListener(e -> rentalDialog.dispose());
    
    buttonPanel.add(rentButton);
    buttonPanel.add(cancelButton);
    
    rentalDialog.add(formPanel, BorderLayout.CENTER);
    rentalDialog.add(buttonPanel, BorderLayout.SOUTH);
    rentalDialog.setVisible(true);
}
// Method to update weeks calculation when dates change
private void updateWeekCalculation(JTextField startDateField, JTextField endDateField, JTextField weeksField) {
    try {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate = dateFormat.parse(startDateField.getText());
        java.util.Date endDate = dateFormat.parse(endDateField.getText());
        
        // Calculate difference in milliseconds
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        // Convert to days
        long diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
        // Calculate weeks (rounded up)
        int weeks = (int) Math.ceil(diffInDays / 7.0);
        
        weeksField.setText(String.valueOf(weeks));
    } catch (java.text.ParseException ex) {
        weeksField.setText("Error");
    }
}
// Modified process rental method with custom dates
private boolean processRentalWithCustomDates(int vehicleId, String name, String phone, String email, String license, String address, String startDate, String endDate) {
    try {
        Connection con = new dbConnector().getConnection();
        con.setAutoCommit(false); // Start transaction
        
        // Insert client if new, or get existing client ID
        String clientQuery = "INSERT INTO tbl_clients (c_name, c_phone, c_email, c_license_number, c_address) VALUES (?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE c_phone=?, c_email=?, c_license_number=?, c_address=?";
        
        PreparedStatement clientStmt = con.prepareStatement(clientQuery, Statement.RETURN_GENERATED_KEYS);
        clientStmt.setString(1, name);
        clientStmt.setString(2, phone);
        clientStmt.setString(3, email);
        clientStmt.setString(4, license);
        clientStmt.setString(5, address);
        clientStmt.setString(6, phone);
        clientStmt.setString(7, email);
        clientStmt.setString(8, license);
        clientStmt.setString(9, address);
        int clientResult = clientStmt.executeUpdate();
        
        // Get client ID
        int clientId;
        ResultSet rs = clientStmt.getGeneratedKeys();
        if (rs.next()) {
            clientId = rs.getInt(1);
        } else {
            // Get client ID if already exists
            String getClientQuery = "SELECT c_id FROM tbl_clients WHERE c_name = ? AND c_phone = ?";
            PreparedStatement getClientStmt = con.prepareStatement(getClientQuery);
            getClientStmt.setString(1, name);
            getClientStmt.setString(2, phone);
            ResultSet clientRs = getClientStmt.executeQuery();
            
            if (clientRs.next()) {
                clientId = clientRs.getInt("c_id");
                clientRs.close();
                getClientStmt.close();
            } else {
                throw new SQLException("Could not get client ID");
            }
        }
        rs.close();
        clientStmt.close();
        
        // Log client add if new
        if (clientResult > 0) {
            String userIp = "Unknown";
            try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("Add/Update Client", name + " (" + phone + ", " + email + ") added/updated.", username, userIp);
        }
        
        // Get vehicle rate
        String rateQuery = "SELECT v_rate FROM tbl_vehicles WHERE v_id = ?";
        PreparedStatement rateStmt = con.prepareStatement(rateQuery);
        rateStmt.setInt(1, vehicleId);
        ResultSet rateRs = rateStmt.executeQuery();
        
        double rate = 0;
        if (rateRs.next()) {
            rate = rateRs.getDouble("v_rate");
        }
        rateRs.close();
        rateStmt.close();
        
        // Calculate weeks and total amount
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start = dateFormat.parse(startDate);
        java.util.Date end = dateFormat.parse(endDate);
        
        // Calculate difference in milliseconds
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        // Convert to days
        long diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
        // Calculate weeks (rounded up)
        int weeks = (int) Math.ceil(diffInDays / 7.0);
        
        // Calculate total amount (weekly rate * number of weeks)
        double totalAmount = rate * weeks;
        
        // Create rental record with custom dates
        String rentalQuery = "INSERT INTO tbl_rentals (r_vehicle_id, r_client_id, r_start_date, r_end_date, " +
                           "r_total_amount, r_status, r_created_by) VALUES " +
                           "(?, ?, ?, ?, ?, 'active', ?)";
        
        PreparedStatement rentalStmt = con.prepareStatement(rentalQuery);
        rentalStmt.setInt(1, vehicleId);
        rentalStmt.setInt(2, clientId);
        rentalStmt.setString(3, startDate); // Use the provided start date
        rentalStmt.setString(4, endDate);   // Use the provided end date
        rentalStmt.setDouble(5, totalAmount);
        rentalStmt.setString(6, "system"); // Replace with actual user if available
        
        int rentalResult = rentalStmt.executeUpdate();
        rentalStmt.close();
        
        // Log rental add
        if (rentalResult > 0) {
            String userIp = "Unknown";
            try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("Add Rental", "Vehicle ID: " + vehicleId + ", Client: " + name + ", Dates: " + startDate + " to " + endDate, username, userIp);
        }
        
        // Update vehicle status
        String updateQuery = "UPDATE tbl_vehicles SET v_status = 'rented' WHERE v_id = ?";
        PreparedStatement updateStmt = con.prepareStatement(updateQuery);
        updateStmt.setInt(1, vehicleId);
        updateStmt.executeUpdate();
        updateStmt.close();
        
        con.commit();
        con.close();
        
        JOptionPane.showMessageDialog(this, "Vehicle rented successfully for " + weeks + " weeks!\nTotal Amount: ₱" + totalAmount);
        return true;
        
    } catch (SQLException | java.text.ParseException ex) {
        JOptionPane.showMessageDialog(this, "Error processing rental: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

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
                StringBuilder details = new StringBuilder();
                details.append("VEHICLE DETAILS\n\n");
                details.append("ID: ").append(rs.getInt("v_id")).append("\n");
                details.append("Make: ").append(rs.getString("v_make")).append("\n");
                details.append("Model: ").append(rs.getString("v_model")).append("\n");
                details.append("Year: ").append(rs.getString("v_year")).append("\n");
                details.append("Plate Number: ").append(rs.getString("v_plate")).append("\n");
                details.append("Weekly Rate: ₱").append(rs.getString("v_rate")).append("\n");
                details.append("Status: ").append(rs.getString("v_status")).append("\n");
                String status = rs.getString("v_status");
                if (status.equalsIgnoreCase("rented")) {
                    // Query rental and client info for dialog display
                    StringBuilder extraInfo = new StringBuilder();
                    try {
                        Connection con2 = new dbConnector().getConnection();
                        String rentalQuery = "SELECT r.r_start_date, r.r_end_date, r.r_total_amount, c.c_name, c.c_phone, c.c_email, c.c_address FROM tbl_rentals r JOIN tbl_clients c ON r.r_client_id = c.c_id WHERE r.r_vehicle_id = ? AND r.r_status = 'active'";
                        PreparedStatement pst2 = con2.prepareStatement(rentalQuery);
                        pst2.setInt(1, selectedVehicleId);
                        ResultSet rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            extraInfo.append("\n--- RENTAL RECEIPT ---\n");
                            extraInfo.append("Client Name: ").append(rs2.getString("c_name")).append("\n");
                            extraInfo.append("Phone: ").append(rs2.getString("c_phone")).append("\n");
                            extraInfo.append("Email: ").append(rs2.getString("c_email")).append("\n");
                            extraInfo.append("Address: ").append(rs2.getString("c_address")).append("\n");
                            extraInfo.append("Start Date: ").append(rs2.getString("r_start_date")).append("\n");
                            extraInfo.append("End Date: ").append(rs2.getString("r_end_date")).append("\n");
                            extraInfo.append("Total Amount: ₱").append(String.format("%.2f", rs2.getDouble("r_total_amount"))).append("\n");
                        }
                        rs2.close();
                        pst2.close();
                        con2.close();
                    } catch (Exception ex) {
                        extraInfo.append("\n[Error loading rental/client info]");
                    }
                    // Show custom dialog with Print Receipt button
                    JTextArea textArea = new JTextArea(details.toString() + extraInfo.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JButton printButton = new JButton("Print Receipt");
                    printButton.addActionListener(e -> {
                        try {
                            // Query rental and client info for printing (same as before)
                            Connection con2 = new dbConnector().getConnection();
                            String rentalQuery = "SELECT r.r_start_date, r.r_end_date, r.r_total_amount, c.c_name, c.c_phone, c.c_email, c.c_address FROM tbl_rentals r JOIN tbl_clients c ON r.r_client_id = c.c_id WHERE r.r_vehicle_id = ? AND r.r_status = 'active'";
                            PreparedStatement pst2 = con2.prepareStatement(rentalQuery);
                            pst2.setInt(1, selectedVehicleId);
                            ResultSet rs2 = pst2.executeQuery();
                            StringBuilder receipt = new StringBuilder();
                            receipt.append(details.toString());
                            if (rs2.next()) {
                                receipt.append("\n--- RENTAL RECEIPT ---\n");
                                receipt.append("Client Name: ").append(rs2.getString("c_name")).append("\n");
                                receipt.append("Phone: ").append(rs2.getString("c_phone")).append("\n");
                                receipt.append("Email: ").append(rs2.getString("c_email")).append("\n");
                                receipt.append("Address: ").append(rs2.getString("c_address")).append("\n");
                                receipt.append("Start Date: ").append(rs2.getString("r_start_date")).append("\n");
                                receipt.append("End Date: ").append(rs2.getString("r_end_date")).append("\n");
                                receipt.append("Total Amount: ₱").append(String.format("%.2f", rs2.getDouble("r_total_amount"))).append("\n");
                            } else {
                                receipt.append("\nNo active rental found for this vehicle.");
                            }
                            rs2.close();
                            pst2.close();
                            con2.close();
                            // Print the receipt
                            JTextArea printArea = new JTextArea(receipt.toString());
                            boolean complete = printArea.print();
                            if (complete) {
                                JOptionPane.showMessageDialog(this, "Receipt sent to printer.", "Print", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Printing was cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error printing receipt: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.add(scrollPane, BorderLayout.CENTER);
                    panel.add(printButton, BorderLayout.SOUTH);
                    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Vehicle Details", Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.getContentPane().add(panel);
                    dialog.setSize(350, 350);
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                } else {
                    // Show normal details dialog
                    JOptionPane.showMessageDialog(this, details.toString(), "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving vehicle details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnViewDetailsActionPerformed

    private void edit_clientActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedVehicleId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Add your edit functionality here
        JOptionPane.showMessageDialog(this, "Edit functionality for vehicle ID: " + selectedVehicleId, "Edit Vehicle", JOptionPane.INFORMATION_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRentVehicle;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JComboBox<String> cboFilter;
    private javax.swing.JButton edit_client;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSelectedVehicle;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JPanel vehicleCardsContainer;
    private javax.swing.JPanel vehicleDisplayPanel;
    // End of variables declaration//GEN-END:variables
}
