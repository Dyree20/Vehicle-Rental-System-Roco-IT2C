/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import config.dbConnector;
import java.util.ArrayList;

/**
 *
 * @author ROCO
 */
public class v_clients extends javax.swing.JInternalFrame {

    /**
     * Creates new form v_clients
     */
    public v_clients() {
        initComponents();
        // Set up search and refresh actions
        btnSearch.addActionListener(e -> loadClientCards(txtSearch.getText()));
        txtSearch.addActionListener(e -> loadClientCards(txtSearch.getText()));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadClientCards("");
        });
        // Load cards on startup
        loadClientCards("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        middlePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        clientsContainer = new javax.swing.JPanel();
        bottomPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topPanel.setBackground(new java.awt.Color(110, 0, 0));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CLIENTS HISTORY");

        btnSearch.setText("Search");

        btnRefresh.setText("Refresh");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addGap(79, 79, 79))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(topPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 0, 930, 90));

        middlePanel.setBackground(new java.awt.Color(110, 20, 0));

        clientsContainer.setBackground(new java.awt.Color(110, 5, 0));

        javax.swing.GroupLayout clientsContainerLayout = new javax.swing.GroupLayout(clientsContainer);
        clientsContainer.setLayout(clientsContainerLayout);
        clientsContainerLayout.setHorizontalGroup(
            clientsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 928, Short.MAX_VALUE)
        );
        clientsContainerLayout.setVerticalGroup(
            clientsContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 308, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(clientsContainer);

        javax.swing.GroupLayout middlePanelLayout = new javax.swing.GroupLayout(middlePanel);
        middlePanel.setLayout(middlePanelLayout);
        middlePanelLayout.setHorizontalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        middlePanelLayout.setVerticalGroup(
            middlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        mainPanel.add(middlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 930, 310));

        bottomPanel.setBackground(new java.awt.Color(110, 0, 20));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NAME:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PHONE NUMBER:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ADDRESS");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("EMAIL ADDRESS:");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Rented Vehicle:");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Amount:");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Start Date:");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("End Date:");

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bottomPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(bottomPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(194, Short.MAX_VALUE))
            .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bottomPanelLayout.createSequentialGroup()
                    .addGap(300, 300, 300)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(492, Short.MAX_VALUE)))
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addGap(5, 5, 5)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9))
                        .addGap(3, 3, 3)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10))
                        .addGap(4, 4, 4)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 61, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomPanelLayout.createSequentialGroup()
                    .addContainerGap(242, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addGap(28, 28, 28)))
        );

        mainPanel.add(bottomPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 930, 270));

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadClientCards(String searchText) {
        System.out.println("Loading client cards...");
        clientsContainer.removeAll();
        clientsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        int rowCount = 0;
        try {
            Connection con = new dbConnector().getConnection();
            String query = "SELECT c.*, r.r_start_date, r.r_end_date, r.r_total_amount, v.v_make, v.v_model, v.v_plate, v.v_type, v.v_year " +
                           "FROM tbl_clients c " +
                           "LEFT JOIN tbl_rentals r ON c.c_id = r.r_client_id " +
                           "LEFT JOIN tbl_vehicles v ON r.r_vehicle_id = v.v_id";
            if (searchText != null && !searchText.trim().isEmpty()) {
                query += " WHERE c.c_name LIKE ? OR c.c_email LIKE ? OR c.c_phone LIKE ?";
            }
            PreparedStatement pst = con.prepareStatement(query);
            if (searchText != null && !searchText.trim().isEmpty()) {
                String likeText = "%" + searchText.trim() + "%";
                pst.setString(1, likeText);
                pst.setString(2, likeText);
                pst.setString(3, likeText);
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rowCount++;
                System.out.println("Loaded client: " + rs.getString("c_name"));
                // Card style similar to add_vehicles
                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(420, 120));
                card.setBackground(new Color(110, 0, 20));
                card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                card.setLayout(null);

                // Client image
                JLabel imgLabel = new JLabel();
                imgLabel.setBounds(10, 10, 100, 100);
                byte[] imageBytes = rs.getBytes("c_image");
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    imgLabel.setIcon(null);
                }
                card.add(imgLabel);

                // Info (use HTML for styling)
                String name = rs.getString("c_name");
                String phone = rs.getString("c_phone");
                String address = rs.getString("c_address");
                String email = rs.getString("c_email");
                String rentedVehicle = rs.getString("v_model");
                String amount = rs.getString("r_total_amount");
                String startDate = rs.getString("r_start_date");
                String endDate = rs.getString("r_end_date");
                String info = "<html>"
                    + "<b>Name:</b> " + (name != null ? name : "") + "<br>"
                    + "<b>Phone:</b> " + (phone != null ? phone : "") + "<br>"
                    + "<b>Address:</b> " + (address != null ? address : "") + "<br>"
                    + "<b>Email:</b> " + (email != null ? email : "") + "<br>"
                    + "<b>Rented Vehicle:</b> " + (rentedVehicle != null ? rentedVehicle : "") + "<br>"
                    + "<b>Amount:</b> " + (amount != null ? amount : "") + "<br>"
                    + "<b>Start:</b> " + (startDate != null ? startDate : "") + " <b>End:</b> " + (endDate != null ? endDate : "")
                    + "</html>";
                JLabel infoLabel = new JLabel(info);
                infoLabel.setForeground(Color.WHITE);
                infoLabel.setBounds(120, 10, 280, 100);
                card.add(infoLabel);

                // Card click: fill bottom panel
                final String fName = name;
                final String fPhone = phone;
                final String fAddress = address;
                final String fEmail = email;
                final String fRentedVehicle = rentedVehicle;
                final String fAmount = amount;
                final String fStartDate = startDate;
                final String fEndDate = endDate;
                final byte[] fImageBytes = imageBytes;
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTextField1.setText(fName);
                        jTextField3.setText(fPhone);
                        jTextField2.setText(fAddress);
                        jTextField4.setText(fEmail);
                        jTextField5.setText(fRentedVehicle);
                        jTextField6.setText(fAmount);
                        jTextField7.setText(fStartDate);
                        jTextField8.setText(fEndDate);
                        // Set client image if available
                        if (fImageBytes != null && fImageBytes.length > 0) {
                            ImageIcon icon = new ImageIcon(fImageBytes);
                            Image img = icon.getImage().getScaledInstance(jLabel2.getWidth(), jLabel2.getHeight(), Image.SCALE_SMOOTH);
                            jLabel2.setIcon(new ImageIcon(img));
                        } else {
                            jLabel2.setIcon(null);
                        }
                        // Visual selection effect
                        if (selectedCard != null) {
                            selectedCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                            selectedCard.setBackground(new Color(110, 0, 20));
                        }
                        card.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 0), 4)); // orange border
                        card.setBackground(new Color(140, 40, 40)); // slightly lighter shade
                        selectedCard = card;
                    }
                });

                clientsContainer.add(card);
            }
            System.out.println("Total clients loaded: " + rowCount);
            clientsContainer.setPreferredSize(new Dimension(900, rowCount * 130 + 20));
            clientsContainer.revalidate();
            clientsContainer.repaint();
            jScrollPane1.revalidate();
            jScrollPane1.repaint();
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading clients: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel makeLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Tahoma", Font.BOLD, 12));
        JLabel v = new JLabel(value != null ? value : "");
        v.setForeground(Color.WHITE);
        v.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panel.add(l, BorderLayout.NORTH);
        panel.add(v, BorderLayout.CENTER);
        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel clientsContainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtSearch;
    private JPanel selectedCard = null;
    // End of variables declaration//GEN-END:variables
}
