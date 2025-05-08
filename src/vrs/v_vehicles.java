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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author ROCO
 */
public class v_vehicles extends javax.swing.JInternalFrame {

    /**
     * Creates new form v_vehicles
     */
    public v_vehicles() {
        initComponents();
        
        loadVehicleCards();
        
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
                    bi.setNorthPane(null);
    }

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
    loadVehicleCards(); // Load vehicles when the form opens
}
    
    
private void loadVehicleCards() {
    try {
        // Get the container panel inside the scroll pane
        JPanel containerPanel = (JPanel) jScrollPane1.getViewport().getView(); // Change scrollPane to jScrollPane1
        containerPanel.removeAll();
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Left-aligned with spacing
        
        // Connect to database
        Connection con = new dbConnector().getConnection();
        String query = "SELECT * FROM tbl_vehicles";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
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
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BorderLayout(10, 0)); // Small gap between image and text
            cardPanel.setPreferredSize(new Dimension(300, 150));
            cardPanel.setBackground(new Color(128, 0, 0)); // Dark red background
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            // Image panel (left side)
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(120, 100));
            if (imageData != null && imageData.length > 0) {
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } else {
                imageLabel.setText("No Image");
                imageLabel.setForeground(Color.WHITE);
            }
            
            // Text panel (right side)
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false); // Make it transparent to show the card's background
            
            // Create text labels
            JLabel nameLabel = new JLabel("Name: " + make + " " + model);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
            
            JLabel typeYearLabel = new JLabel("Type: Year: " + year + " Plate: " + plate);
            typeYearLabel.setForeground(Color.WHITE);
            typeYearLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
            
            JLabel priceLabel = new JLabel("Price: " + rate + " per day");
            priceLabel.setForeground(Color.WHITE);
            priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
            
            // Add text labels to the text panel
            textPanel.add(Box.createVerticalStrut(20)); // Add some top space
            textPanel.add(nameLabel);
            textPanel.add(Box.createVerticalStrut(10)); // Add space between labels
            textPanel.add(typeYearLabel);
            textPanel.add(Box.createVerticalStrut(10)); // Add space between labels
            textPanel.add(priceLabel);
            
            // Add image and text panels to the card
            cardPanel.add(imageLabel, BorderLayout.WEST);
            cardPanel.add(textPanel, BorderLayout.CENTER);
            
            // Add the card to the container
            containerPanel.add(cardPanel);
        }
        
        // Refresh the container
        containerPanel.revalidate();
        containerPanel.repaint();
        
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        vehicleCardsPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jScrollPane1.setBackground(new java.awt.Color(102, 0, 0));
        jScrollPane1.setViewportView(vehicleCardsPanel);

        jPanel1.setBackground(new java.awt.Color(110, 50, 50));

        jButton1.setText("Search");

        jButton2.setText("Clear");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(596, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel vehicleCardsPanel;
    // End of variables declaration//GEN-END:variables
}
