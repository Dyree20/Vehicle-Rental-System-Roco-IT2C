/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import net.proteanit.sql.DbUtils;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author SCC-COLLEGE
 */
public class Approval extends javax.swing.JInternalFrame {

    /**
     * Creates new form Approval
     */
    public Approval() {
        initComponents();
         displayData();
        
        
     this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));   
     BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
     bi.setNorthPane(null);
        
        
     
     
    }

    public void displayData(){
        
     try{
     
     dbConnector db = new dbConnector();
     ResultSet rs = db.getData("SELECT * FROM tbl_approval");
     employ.setModel(DbUtils.resultSetToTableModel(rs));
     rs.close();
     
     }catch(SQLException ex){
     
         System.out.println("Errors:"+ex.getMessage());
     
     }
     
       
        
        
        
    }
   private void approveUser() {
    int selectedRow = employ.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to approve.");
        return;
    }

    String username = employ.getValueAt(selectedRow, 1).toString(); // Adjust index based on your table

    dbConnector db = new dbConnector();

    try (Connection conn = db.getConnection()) { // Use the new method
        // Retrieve user details from tbl_approval
        String selectQuery = "SELECT * FROM tbl_approval WHERE u_username = ?";
        PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
        selectStmt.setString(1, username);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            String fullName = rs.getString("u_name");
            String email = rs.getString("u_email");
            String pass = rs.getString("u_password");
            String phone = rs.getString("u_phone");
            String role = rs.getString("u_role");

            // Insert into users table
            String insertQuery = "INSERT INTO users (u_name, u_username, u_email, u_password, u_phone, u_role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, fullName);
            insertStmt.setString(2, username);
            insertStmt.setString(3, email);
            insertStmt.setString(4, pass);
            insertStmt.setString(5, phone);
            insertStmt.setString(6, role);

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                // Delete from tbl_approval after successful insertion
                String deleteQuery = "DELETE FROM tbl_approval WHERE u_username = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, username);
                deleteStmt.executeUpdate();
                deleteStmt.close();

                JOptionPane.showMessageDialog(this, "User Approved and Moved to Users Section!");
                displayData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Approval Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            insertStmt.close();
        } else {
            JOptionPane.showMessageDialog(this, "User Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        selectStmt.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}






private void editUser() {
    int selectedRow = employ.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        return;
    }

    String userId = employ.getValueAt(selectedRow, 0).toString();
    String newEmail = JOptionPane.showInputDialog(this, "Enter new email:");
    String newPhone = JOptionPane.showInputDialog(this, "Enter new phone:");

    if (newEmail == null || newPhone == null || newEmail.isEmpty() || newPhone.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Invalid input. No changes made.");
        return;
    }

    dbConnector db = new dbConnector();
    db.updateData("UPDATE tbl_approval SET u_email = ?, u_phone = ? WHERE id = ?", newEmail, newPhone, userId);

    JOptionPane.showMessageDialog(this, "User updated successfully.");
    displayData(); // Refresh table
}
   


private void deleteUser() {
    int selectedRow = employ.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        return;
    }

    String userId = employ.getValueAt(selectedRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    dbConnector db = new dbConnector();
    db.updateData("DELETE FROM tbl_approval WHERE id = ?", userId);

    JOptionPane.showMessageDialog(this, "User deleted successfully.");
    displayData(); // Refresh table
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
        employ = new javax.swing.JTable();
        u_approve = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        edit = new javax.swing.JPanel();
        u_edit = new javax.swing.JLabel();
        u_delete = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        employ.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(employ);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 466));

        u_approve.setBackground(new java.awt.Color(255, 102, 51));
        u_approve.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        u_approve.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                u_approveMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel2.setText("APPROVE");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout u_approveLayout = new javax.swing.GroupLayout(u_approve);
        u_approve.setLayout(u_approveLayout);
        u_approveLayout.setHorizontalGroup(
            u_approveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(u_approveLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel2)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        u_approveLayout.setVerticalGroup(
            u_approveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        getContentPane().add(u_approve, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, -1, 40));

        edit.setBackground(new java.awt.Color(255, 102, 51));
        edit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
        });

        u_edit.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        u_edit.setText("EDIT");

        javax.swing.GroupLayout editLayout = new javax.swing.GroupLayout(edit);
        edit.setLayout(editLayout);
        editLayout.setHorizontalGroup(
            editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(u_edit)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        editLayout.setVerticalGroup(
            editLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(u_edit, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        getContentPane().add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 480, 160, 40));

        u_delete.setBackground(new java.awt.Color(255, 102, 51));
        u_delete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        u_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                u_deleteMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 14)); // NOI18N
        jLabel3.setText("DELETE");

        javax.swing.GroupLayout u_deleteLayout = new javax.swing.GroupLayout(u_delete);
        u_delete.setLayout(u_deleteLayout);
        u_deleteLayout.setHorizontalGroup(
            u_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(u_deleteLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel3)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        u_deleteLayout.setVerticalGroup(
            u_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        getContentPane().add(u_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 480, 170, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void u_approveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_u_approveMouseClicked
        approveUser();
    }//GEN-LAST:event_u_approveMouseClicked

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
        editUser();
    }//GEN-LAST:event_editMouseClicked

    private void u_deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_u_deleteMouseClicked
        deleteUser();
    }//GEN-LAST:event_u_deleteMouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel edit;
    private javax.swing.JTable employ;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel u_approve;
    private javax.swing.JPanel u_delete;
    private javax.swing.JLabel u_edit;
    // End of variables declaration//GEN-END:variables
}
