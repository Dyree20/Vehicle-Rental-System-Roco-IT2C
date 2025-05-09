/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;

import config.dbConnector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author SCC11
 */
public class user_logs extends javax.swing.JInternalFrame {

private javax.swing.JLabel jLabel2;
private javax.swing.JLabel jLabel3;
private javax.swing.JLabel jLabel4;
   private void loadLogs() {
    try {
        Connection con = new dbConnector().getConnection();
        String query = "SELECT * FROM tbl_user_logs ORDER BY log_timestamp DESC";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        
        // Create table model
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Log ID", "Action", "Details", "User", "Timestamp", "IP Address"}
        ) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data to the model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("log_id"),
                rs.getString("log_action"),
                rs.getString("log_details"),
                rs.getString("log_user"),
                rs.getTimestamp("log_timestamp"),
                rs.getString("log_ip")
            });
        }
        
        // Set the model to the table
        logTable.setModel(model);
        
        // Close resources
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading logs: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
// Method to add panel configuration and setup to make it look better
private void setupPanels() {
    // Set panel background colors
    jPanel1.setBackground(new java.awt.Color(110, 0, 0)); // Dark red for top panel
    jPanel2.setBackground(new java.awt.Color(180, 180, 180)); // Light gray for middle panel
    jPanel3.setBackground(new java.awt.Color(110, 0, 0)); // Dark red for bottom panel
    
    // Set label text and styling
    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // Bold, size 18
    jLabel1.setForeground(java.awt.Color.WHITE);
    jLabel1.setText("User Activity Logs");
    
    // Style search components
    jLabel2.setForeground(java.awt.Color.WHITE);
    jLabel2.setText("Search:");
    
    // Style filter components
    jLabel3.setForeground(java.awt.Color.WHITE);
    jLabel3.setText("Action:");
    
    jLabel4.setForeground(java.awt.Color.WHITE);
    jLabel4.setText("User:");
    
    // Button styles
    btnSearch.setText("Search");
    btnClear.setText("Clear");
    btnRefresh.setText("Refresh");
    btnExport.setText("Export to CSV");
    
    // Set table styles
    logTable.setRowHeight(25);
    logTable.getTableHeader().setFont(new java.awt.Font("Tahoma", 1, 12)); // Bold headers
    
    // Fill combo boxes
    populateFilters();
}
// Method to populate action and user filters
private void populateFilters() {
    try {
        Connection con = new dbConnector().getConnection();
        
        // Populate action filter
        cboAction.removeAllItems();
        cboAction.addItem("All Actions");
        
        String actionQuery = "SELECT DISTINCT log_action FROM tbl_user_logs ORDER BY log_action";
        PreparedStatement actionStmt = con.prepareStatement(actionQuery);
        ResultSet actionRs = actionStmt.executeQuery();
        
        while (actionRs.next()) {
            String action = actionRs.getString("log_action");
            if (action != null && !action.isEmpty()) {
                cboAction.addItem(action);
            }
        }
        
        // Populate user filter
        cboUser.removeAllItems();
        cboUser.addItem("All Users");
        
        String userQuery = "SELECT DISTINCT log_user FROM tbl_user_logs ORDER BY log_user";
        PreparedStatement userStmt = con.prepareStatement(userQuery);
        ResultSet userRs = userStmt.executeQuery();
        
        while (userRs.next()) {
            String user = userRs.getString("log_user");
            if (user != null && !user.isEmpty()) {
                cboUser.addItem(user);
            }
        }
        
        // Close resources
        actionRs.close();
        actionStmt.close();
        userRs.close();
        userStmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error populating filters: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
// Method to handle the search button action
private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
    String searchText = txtSearch.getText().trim();
    String actionFilter = cboAction.getSelectedItem().toString();
    String userFilter = cboUser.getSelectedItem().toString();
    
    // Build query with filters
    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tbl_user_logs WHERE 1=1");
    
    if (!searchText.isEmpty()) {
        queryBuilder.append(" AND (log_action LIKE ? OR log_details LIKE ? OR log_user LIKE ?)");
    }
    
    if (!actionFilter.equals("All Actions")) {
        queryBuilder.append(" AND log_action = ?");
    }
    
    if (!userFilter.equals("All Users")) {
        queryBuilder.append(" AND log_user = ?");
    }
    
    queryBuilder.append(" ORDER BY log_timestamp DESC");
    
    try {
        Connection con = new dbConnector().getConnection();
        PreparedStatement pst = con.prepareStatement(queryBuilder.toString());
        
        int paramIndex = 1;
        
        if (!searchText.isEmpty()) {
            String searchPattern = "%" + searchText + "%";
            pst.setString(paramIndex++, searchPattern);
            pst.setString(paramIndex++, searchPattern);
            pst.setString(paramIndex++, searchPattern);
        }
        
        if (!actionFilter.equals("All Actions")) {
            pst.setString(paramIndex++, actionFilter);
        }
        
        if (!userFilter.equals("All Users")) {
            pst.setString(paramIndex++, userFilter);
        }
        
        ResultSet rs = pst.executeQuery();
        
        // Create table model with same structure as loadLogs()
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Log ID", "Action", "Details", "User", "Timestamp", "IP Address"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data to the model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("log_id"),
                rs.getString("log_action"),
                rs.getString("log_details"),
                rs.getString("log_user"),
                rs.getTimestamp("log_timestamp"),
                rs.getString("log_ip")
            });
        }
        
        // Set the model to the table
        logTable.setModel(model);
        
        // Close resources
        rs.close();
        pst.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error searching logs: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
    txtSearch.setText("");
    cboAction.setSelectedItem("All Actions");
    cboUser.setSelectedItem("All Users");
    loadLogs();
}
private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
    loadLogs();
    populateFilters();
}
private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Export Logs");
    
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        
        // Add .csv extension if needed
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }
        
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            // Write header
            bw.write("Log ID,Action,Details,User,Timestamp,IP Address");
            bw.newLine();
            
            // Get data from table
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) logTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    String cellValue = (value != null) ? value.toString() : "";
                    
                    // Escape quotes and commas for CSV
                    if (cellValue.contains(",") || cellValue.contains("\"")) {
                        cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
                    }
                    
                    sb.append(cellValue);
                    
                    if (j < model.getColumnCount() - 1) {
                        sb.append(",");
                    }
                }
                
                bw.write(sb.toString());
                bw.newLine();
            }
            
            JOptionPane.showMessageDialog(this, "Logs exported successfully to: " + file.getAbsolutePath(), 
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error exporting logs: " + ex.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
// Make sure to call these methods in your constructor after initComponents()
public user_logs() {
    initComponents();
    
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    
    setupPanels();
    loadLogs();
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
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        cboAction = new javax.swing.JComboBox<>();
        btnClear = new javax.swing.JButton();
        cboUser = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(110, 0, 50));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("USER ACTIVITY LOGS");

        btnSearch.setText("Search");

        cboAction.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnClear.setText("Clear");

        cboUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(500, Short.MAX_VALUE)
                .addComponent(cboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(btnSearch)
                    .addComponent(cboAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear)
                    .addComponent(cboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 918, -1));

        jPanel2.setBackground(new java.awt.Color(110, 0, 0));

        logTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(logTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 920, 410));

        jPanel3.setBackground(new java.awt.Color(110, 50, 0));

        btnRefresh.setText("Refresh");

        btnExport.setText("Export");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(711, Short.MAX_VALUE)
                .addComponent(btnExport)
                .addGap(43, 43, 43)
                .addComponent(btnRefresh)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(btnExport))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 920, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cboAction;
    private javax.swing.JComboBox<String> cboUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable logTable;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
