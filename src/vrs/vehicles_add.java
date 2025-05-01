/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author ROCO
 */
public class vehicles_add extends javax.swing.JInternalFrame {

    private String imagePath = null;
    private File selectedFile = null;
    
    /**
     * Creates new form vehicles_add
     */
    public vehicles_add() {
        initComponents();
        
        displayVehicles();
        
        // Remove the frame border
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
        bi.setNorthPane(null);
    }
    
    // Method to display all vehicles in the table
    private void displayVehicles() {
        try {
            dbConnector db = new dbConnector();
            Connection conn = db.getConnection();
            String query = "SELECT v_id, v_make, v_model, v_year, v_plate, v_rate, v_status FROM tbl_vehicles";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            vehiclesTable.setModel(DbUtils.resultSetToTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading vehicles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to clear input fields
    private void clearFields() {
        v_make.setText("");
        v_model.setText("");
        v_year.setText("");
        v_plate.setText("");
        v_rate.setText("");
        v_status.setSelectedIndex(0);
        imagePath = null;
        selectedFile = null;
        vehicleImage.setIcon(null);
        vehicleImage.setText("No Image Selected");
        v_id.setText("");
        addBtn.setEnabled(true);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }
    
    // Method to add a new vehicle
    private void addVehicle() {
        String make = v_make.getText().trim();
        String model = v_model.getText().trim();
        String year = v_year.getText().trim();
        String plate = v_plate.getText().trim();
        String rate = v_rate.getText().trim();
        String status = v_status.getSelectedItem().toString();
        
        // Validate input
        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || plate.isEmpty() || rate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            dbConnector db = new dbConnector();
            Connection conn = db.getConnection();
            
            // SQL query with or without image
            String sql;
            if (selectedFile != null) {
                sql = "INSERT INTO tbl_vehicles (v_make, v_model, v_year, v_plate, v_rate, v_status, v_image) VALUES (?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO tbl_vehicles (v_make, v_model, v_year, v_plate, v_rate, v_status) VALUES (?, ?, ?, ?, ?, ?)";
            }
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, make);
            pst.setString(2, model);
            pst.setString(3, year);
            pst.setString(4, plate);
            pst.setString(5, rate);
            pst.setString(6, status);
            
            // Add image if available
            if (selectedFile != null) {
                try {
                    InputStream is = new FileInputStream(selectedFile);
                    pst.setBlob(7, is);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error with image file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!");
                clearFields();
                displayVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pst.close();
            conn.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to update a vehicle
    private void updateVehicle() {
        if (v_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = v_id.getText().trim();
        String make = v_make.getText().trim();
        String model = v_model.getText().trim();
        String year = v_year.getText().trim();
        String plate = v_plate.getText().trim();
        String rate = v_rate.getText().trim();
        String status = v_status.getSelectedItem().toString();
        
        // Validate input
        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || plate.isEmpty() || rate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            dbConnector db = new dbConnector();
            Connection conn = db.getConnection();
            
            // SQL query with or without image
            String sql;
            if (selectedFile != null) {
                sql = "UPDATE tbl_vehicles SET v_make=?, v_model=?, v_year=?, v_plate=?, v_rate=?, v_status=?, v_image=? WHERE v_id=?";
            } else {
                sql = "UPDATE tbl_vehicles SET v_make=?, v_model=?, v_year=?, v_plate=?, v_rate=?, v_status=? WHERE v_id=?";
            }
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, make);
            pst.setString(2, model);
            pst.setString(3, year);
            pst.setString(4, plate);
            pst.setString(5, rate);
            pst.setString(6, status);
            
            // Add image if available
            if (selectedFile != null) {
                try {
                    InputStream is = new FileInputStream(selectedFile);
                    pst.setBlob(7, is);
                    pst.setString(8, id);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error with image file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                pst.setString(7, id);
            }
            
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!");
                clearFields();
                displayVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update vehicle", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pst.close();
            conn.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to delete a vehicle
    private void deleteVehicle() {
        if (v_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = v_id.getText().trim();
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this vehicle?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            dbConnector db = new dbConnector();
            Connection conn = db.getConnection();
            
            String sql = "DELETE FROM tbl_vehicles WHERE v_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully!");
                clearFields();
                displayVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete vehicle", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pst.close();
            conn.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to load vehicle image
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Vehicle Image");
        
        // Set file filter to show only images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            
            // Display image preview
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(vehicleImage.getWidth(), vehicleImage.getHeight(), Image.SCALE_SMOOTH);
            vehicleImage.setIcon(new ImageIcon(img));
            vehicleImage.setText("");
        }
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vehiclesTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        v_make = new javax.swing.JTextField();
        v_model = new javax.swing.JTextField();
        v_year = new javax.swing.JTextField();
        v_plate = new javax.swing.JTextField();
        v_rate = new javax.swing.JTextField();
        v_status = new javax.swing.JComboBox<>();
        vehicleImage = new javax.swing.JLabel();
        browseBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        v_id = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(933, 630));

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VEHICLE MANAGEMENT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 933, -1));

        vehiclesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Make", "Model", "Year", "Plate No.", "Rate/Day", "Status"
            }
        ));
        vehiclesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vehiclesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(vehiclesTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 913, 270));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Make:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Model:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Year:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Plate No.:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Rate/Day:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Status:");

        v_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Rented", "Maintenance", "Reserved" }));

        vehicleImage.setBackground(new java.awt.Color(255, 255, 255));
        vehicleImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        vehicleImage.setText("No Image Selected");
        vehicleImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        vehicleImage.setOpaque(true);

        browseBtn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        browseBtn.setText("Browse Image");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });

        addBtn.setBackground(new java.awt.Color(0, 204, 0));
        addBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        addBtn.setText("ADD");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        updateBtn.setBackground(new java.awt.Color(0, 153, 255));
        updateBtn.setFont(new java.