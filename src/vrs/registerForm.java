/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ROCO
 */
public class registerForm extends javax.swing.JFrame {

    /**
     * Creates new form registerForm
     */
    public registerForm() {
        initComponents();
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
        car = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        u_email = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        register = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        u_user = new javax.swing.JTextField();
        b_page = new javax.swing.JButton();
        u_role = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        u_name = new javax.swing.JTextField();
        c_pass = new javax.swing.JPasswordField();
        u_pass = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        u_phone = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        car.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2025-Chevy-Equinox-15_1_-removebg-preview.png"))); // NOI18N
        jPanel1.add(car, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 350, 370, 160));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Dutch-Tilt_1_-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 360, 260));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Range-Rover-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-420, 260, 670, 280));

        jPanel2.setBackground(new java.awt.Color(255, 102, 51));

        jLabel1.setFont(new java.awt.Font("Verdana", 3, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("WELCOME TO VEHICLE RENTAL SYSTEM");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Right-Front-Diagonal-2_1_-removebg-preview.png"))); // NOI18N
        jLabel5.setText("jLabel5");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-400, 130, 610, 290));

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Confirm Password");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 290, 140, 70));

        u_email.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        u_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_emailActionPerformed(evt);
            }
        });
        jPanel1.add(u_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, 210, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("REGISTER");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 190, 60));

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Email");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 190, 30));

        register.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        register.setText("Register");
        register.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerActionPerformed(evt);
            }
        });
        jPanel1.add(register, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 440, 90, 30));

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Password");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 100, 40));

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Phone Number");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, 190, 30));

        u_user.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        u_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_userActionPerformed(evt);
            }
        });
        jPanel1.add(u_user, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 210, 30));

        b_page.setBackground(new java.awt.Color(255, 153, 153));
        b_page.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        b_page.setText("<--BACK");
        b_page.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        b_page.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pageActionPerformed(evt);
            }
        });
        jPanel1.add(b_page, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 50));

        u_role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Employee" }));
        u_role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_roleActionPerformed(evt);
            }
        });
        jPanel1.add(u_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 440, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Full Name");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 190, 30));

        u_name.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        u_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_nameActionPerformed(evt);
            }
        });
        jPanel1.add(u_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 210, 30));

        c_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_passActionPerformed(evt);
            }
        });
        jPanel1.add(c_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 340, 210, 30));

        u_pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_passActionPerformed(evt);
            }
        });
        jPanel1.add(u_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 210, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Username");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 190, 30));

        u_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                u_phoneActionPerformed(evt);
            }
        });
        jPanel1.add(u_phone, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 400, 210, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void u_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_u_emailActionPerformed

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
                                       
                                        
    String url = "jdbc:mysql://localhost:3306/vrs";
    String user = "root";  // Change this if needed
    String password = "";  // Change this if needed

    String fullName = u_name.getText();
    String username = u_user.getText();
    String emailText = u_email.getText();
    String pass = new String(u_pass.getPassword());
    String confirmPass = new String(c_pass.getPassword());
    String phone = u_phone.getText();
    String role = u_role.getSelectedItem().toString();

    if (fullName.isEmpty() || username.isEmpty() || emailText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!pass.equals(confirmPass)) {
        JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!phone.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Phone number must be numeric!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        Connection con = DriverManager.getConnection(url, user, password);

        // Check if username or email already exists
        String checkQuery = "SELECT COUNT(*) FROM tbl_approval WHERE u_username = ? OR u_email = ?";
        PreparedStatement checkStmt = con.prepareStatement(checkQuery);
        checkStmt.setString(1, username);
        checkStmt.setString(2, emailText);
        
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "Username or Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            rs.close();
            checkStmt.close();
            con.close();
            return;
        }

        rs.close();
        checkStmt.close();

        // If Admin, insert directly into users, otherwise insert into approval
        if (role.equals("Admin")) {
            String sql = "INSERT INTO tbl_users (u_name, u_username, u_email, u_password, u_phone, u_role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fullName);
            pst.setString(2, username);
            pst.setString(3, emailText);
            pst.setString(4, pass);
            pst.setString(5, phone);
            pst.setString(6, role);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Admin Registered Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pst.close();
        } else {
            String sql = "INSERT INTO tbl_approval (u_name, u_username, u_email, u_password, u_phone, u_role, u_status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fullName);
            pst.setString(2, username);
            pst.setString(3, emailText);
            pst.setString(4, pass);
            pst.setString(5, phone);
            pst.setString(6, role);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Employee Registered Successfully! Pending Approval.");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pst.close();
        }

        con.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_registerActionPerformed

    private void u_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_u_userActionPerformed

    private void b_pageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pageActionPerformed
       loginForm log = new loginForm();
       log.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_b_pageActionPerformed

    private void u_roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_roleActionPerformed
        // Create the Role JComboBox
String[] roles = {"Admin", "Employee"};
JComboBox<String> roleComboBox = new JComboBox<>(roles);
roleComboBox.setBounds(150, 220, 150, 30); // Adjust position & size
add(roleComboBox);

    }//GEN-LAST:event_u_roleActionPerformed

    private void u_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_u_nameActionPerformed

    private void u_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_passActionPerformed
                                      
    char[] password = u_pass.getPassword(); 
    
 
    String passwordString = new String(password);
    System.out.println("Password entered: " + passwordString);

    }//GEN-LAST:event_u_passActionPerformed

    private void c_passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_passActionPerformed
       
    char[] password = u_pass.getPassword(); 
    
    
    String passwordString = new String(password);
    System.out.println("Password entered: " + passwordString);
    }//GEN-LAST:event_c_passActionPerformed

    private void u_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_u_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_u_phoneActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(registerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(registerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(registerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(registerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new registerForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_page;
    private javax.swing.JPasswordField c_pass;
    private javax.swing.JLabel car;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton register;
    private javax.swing.JTextField u_email;
    private javax.swing.JTextField u_name;
    private javax.swing.JPasswordField u_pass;
    private javax.swing.JTextField u_phone;
    private javax.swing.JComboBox<String> u_role;
    private javax.swing.JTextField u_user;
    // End of variables declaration//GEN-END:variables
}
