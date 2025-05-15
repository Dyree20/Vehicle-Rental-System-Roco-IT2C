/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import config.Logger;

/**
 *
 * @author ROCO
 */
public class passwordResetRequests extends javax.swing.JInternalFrame {
private int selectedRequestId = -1;
private String selectedUsername = null;
    /**
     * Creates new form passwordResetRequests
     */
    public passwordResetRequests() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
    bi.setNorthPane(null);
    
    loadRequests(); // Load initial data
    
    requestsTable.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && requestsTable.getSelectedRow() != -1) {
        int row = requestsTable.getSelectedRow();
        selectedRequestId = Integer.parseInt(requestsTable.getModel().getValueAt(row, 0).toString());
        selectedUsername = requestsTable.getModel().getValueAt(row, 1).toString();
        System.out.println("Selected: " + selectedRequestId + ", " + selectedUsername);
    }
});
approveBtn.addActionListener(e -> {
    System.out.println("Approve clicked for ID: " + selectedRequestId);
    approveRequest();
});
denyBtn.addActionListener(e -> {
    System.out.println("Deny clicked for ID: " + selectedRequestId);
    denyRequest();
});
setPasswordBtn.addActionListener(e -> {
    System.out.println("Set password clicked for ID: " + selectedRequestId);
    setNewPassword();
});
    
    }
private void loadRequests() {
    DefaultTableModel model = (DefaultTableModel) requestsTable.getModel();
    model.setRowCount(0);
    
    try {
        Connection conn = new dbConnector().getConnection();
        String query = "SELECT * FROM tbl_password_reset ORDER BY pr_date DESC";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("pr_id"),
                rs.getString("pr_username"),
                rs.getTimestamp("pr_date"),
                rs.getString("pr_status")
            });
        }
        
        rs.close();
        pst.close();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
                "Error loading requests: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void requestsTableMouseClicked(java.awt.event.MouseEvent evt) {
    int row = requestsTable.getSelectedRow();
    System.out.println("Selected row: " + row);
    
    if (row != -1) {
        try {
            Object idObj = requestsTable.getValueAt(row, 0);
            Object usernameObj = requestsTable.getValueAt(row, 1);
            
            System.out.println("ID Object: " + idObj + ", Username Object: " + usernameObj);
            
            if (idObj != null) {
                selectedRequestId = Integer.parseInt(idObj.toString());
                selectedUsername = (usernameObj != null) ? usernameObj.toString() : "";
                
                System.out.println("Set selected ID: " + selectedRequestId);
                System.out.println("Set selected username: " + selectedUsername);
            }
        } catch (Exception e) {
            System.out.println("Error in table selection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


private void approveBtnActionPerformed(java.awt.event.ActionEvent evt) {
    approveRequest();
}
private void denyBtnActionPerformed(java.awt.event.ActionEvent evt) {
    denyRequest();
}
private void setPasswordBtnActionPerformed(java.awt.event.ActionEvent evt) {
    setNewPassword();
}

private void approveRequest() {
    System.out.println("approveRequest method called, selectedRequestId=" + selectedRequestId);
    if (selectedRequestId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a request first.");
        return;
    }
    
    try {
        Connection conn = new dbConnector().getConnection();
        String query = "UPDATE tbl_password_reset SET pr_status = 'Approved', pr_approved_date = NOW() WHERE pr_id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, selectedRequestId);
        
        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Request approved. Please set a new password for the user.");
            // Logging
            String userIp = "Unknown";
            try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("Approve Password Reset", "Request ID: " + selectedRequestId + ", User: " + selectedUsername, username, userIp);
            loadRequests();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to approve request.");
        }
        
        pst.close();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void denyRequest() {
    if (selectedRequestId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a request first.");
        return;
    }
    
    try {
        Connection conn = new dbConnector().getConnection();
        String query = "UPDATE tbl_password_reset SET pr_status = 'Denied', pr_approved_date = NOW() WHERE pr_id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, selectedRequestId);
        
        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Request denied.");
            // Logging
            String userIp = "Unknown";
            try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
            String username = System.getProperty("user.name");
            Logger.log("Deny Password Reset", "Request ID: " + selectedRequestId + ", User: " + selectedUsername, username, userIp);
            loadRequests();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to deny request.");
        }
        
        pst.close();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void setNewPassword() {
    if (selectedRequestId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a request first.");
        return;
    }
    
    try {
        Connection conn = new dbConnector().getConnection();
        String checkQuery = "SELECT pr_status FROM tbl_password_reset WHERE pr_id = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setInt(1, selectedRequestId);
        
        ResultSet rs = checkPst.executeQuery();
        if (rs.next()) {
            String status = rs.getString("pr_status");
            
            if (!"Approved".equals(status)) {
                JOptionPane.showMessageDialog(this, "This request must be approved before setting a new password.");
                rs.close();
                checkPst.close();
                conn.close();
                return;
            }
            
            String newPassword = JOptionPane.showInputDialog(this, "Enter new password for user: " + selectedUsername);
            if (newPassword != null && !newPassword.isEmpty()) {
                String hashPassword = hashPassword(newPassword);
                
                String updateUserQuery = "UPDATE tbl_users SET u_password = ? WHERE u_username = ?";
                PreparedStatement updateUserPst = conn.prepareStatement(updateUserQuery);
                updateUserPst.setString(1, hashPassword);
                updateUserPst.setString(2, selectedUsername);
                
                int userResult = updateUserPst.executeUpdate();
                
                String updateRequestQuery = "UPDATE tbl_password_reset SET pr_status = 'Completed', pr_new_password = ? WHERE pr_id = ?";
                PreparedStatement updateRequestPst = conn.prepareStatement(updateRequestQuery);
                updateRequestPst.setString(1, hashPassword);
                updateRequestPst.setInt(2, selectedRequestId);
                
                int requestResult = updateRequestPst.executeUpdate();
                
                if (userResult > 0 && requestResult > 0) {
                    JOptionPane.showMessageDialog(this, "Password has been reset successfully.");
                    // Logging
                    String userIp = "Unknown";
                    try { userIp = InetAddress.getLocalHost().getHostAddress(); } catch (UnknownHostException e) {}
                    String username = System.getProperty("user.name");
                    Logger.log("Set Password", "Request ID: " + selectedRequestId + ", User: " + selectedUsername, username, userIp);
                    loadRequests();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reset password.");
                }
                
                updateUserPst.close();
                updateRequestPst.close();
            }
        }
        
        rs.close();
        checkPst.close();
        conn.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Password hashing not available", e);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        requestsTable = new javax.swing.JTable();
        approveBtn = new javax.swing.JButton();
        denyBtn = new javax.swing.JButton();
        setPasswordBtn = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(110, 0, 0));

        requestsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Request ID", "User", "Request Date", "Status"
            }
        ));
        jScrollPane1.setViewportView(requestsTable);

        approveBtn.setFont(new java.awt.Font("Microsoft YaHei", 3, 11)); // NOI18N
        approveBtn.setText("APPROVE");

        denyBtn.setFont(new java.awt.Font("Microsoft YaHei", 3, 11)); // NOI18N
        denyBtn.setText("DENY");

        setPasswordBtn.setFont(new java.awt.Font("Microsoft YaHei", 3, 11)); // NOI18N
        setPasswordBtn.setText("CHANGE PASSWORD");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(approveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(denyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(setPasswordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(approveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(denyBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(setPasswordBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -30, 930, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveBtn;
    private javax.swing.JButton denyBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable requestsTable;
    private javax.swing.JButton setPasswordBtn;
    // End of variables declaration//GEN-END:variables
}
