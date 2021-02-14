package CLMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Durga Prasad A G (https://durgaprasadag.github.io/dp/)
 */
public class ManageLibrarianAccount extends javax.swing.JInternalFrame {
    
    CLMS.CollegeLibraryManagementSystem lms = new CLMS.CollegeLibraryManagementSystem();
    CLMS.CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    Connection cn;
    Statement st;
    String sql;
    ResultSet rs;
    
    boolean currentPasswordFieldSetNull = true;
    boolean newPasswordFieldSetNull = true;
    boolean confirmPasswordFieldSetNull = true;
    boolean passwordHintTextFIeldSetNull = true;
    
    String userName = lms.userNameTextField.getText();
    String newPassword;
    String passwordHint;

    private void showToast(String s) {
        doneButton.setEnabled(false);
        toastLabel.setVisible(true);
        toastLabel.setText(s);
        timer.invisible(toastLabel);
    }
    
    private void connectToDatabase(String s) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "hr", "superman");
            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(s);
            rs.first();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
    }
    
    private void exeUpdate(String s) {
        try {
            st.executeUpdate(s);
            cn.commit();
            cn.close();
            connectToDatabase("SELECT * FROM LIBRARIAN ORDER BY UNAME");
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Error 1 : " + ex);
        } catch (SQLDataException ex) {
            System.out.println("Error 2 : " + ex);
        } catch (SQLException ex) {
            System.out.println("Error 3 : " + ex);
        }
    }
    
    private void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates new form ManageLibrarian
     */
    public ManageLibrarianAccount() {
        initComponents();
        defaultLookAndFeel();
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
        jPanel2 = lms.new jPanelGradient(255,184, 0, 255,34,0);
        currentPasswordField = new javax.swing.JPasswordField();
        confirmPasswordField = new javax.swing.JPasswordField();
        newPasswordField = new javax.swing.JPasswordField();
        doneButton = new javax.swing.JButton();
        toastLabel = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        passwordHintTextField = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setTitle("Manage Librarian Account");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel2.setBackground(new java.awt.Color(40, 40, 40));
        jPanel2.setToolTipText("");
        jPanel2.setPreferredSize(new java.awt.Dimension(958, 693));

        currentPasswordField.setBackground(new java.awt.Color(255, 137, 0));
        currentPasswordField.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        currentPasswordField.setForeground(new java.awt.Color(255, 255, 255));
        currentPasswordField.setText("Current password");
        currentPasswordField.setToolTipText("Enter Current password to verify it's you.");
        currentPasswordField.setPreferredSize(new java.awt.Dimension(14, 35));
        currentPasswordField.setEchoChar((char) 0);
        currentPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                currentPasswordFieldKeyPressed(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.RoundedBorder c = lms.new RoundedBorder(5);
        currentPasswordField.setBorder(c);

        currentPasswordField.setEchoChar((char) 0);

        confirmPasswordField.setBackground(new java.awt.Color(255, 137, 0));
        confirmPasswordField.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        confirmPasswordField.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswordField.setText("Confirm new password");
        confirmPasswordField.setToolTipText("ReEnter new password");
        confirmPasswordField.setBorder(currentPasswordField.getBorder());
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                confirmPasswordFieldKeyPressed(evt);
            }
        });
        confirmPasswordField.setEchoChar((char) 0);

        newPasswordField.setBackground(new java.awt.Color(255, 137, 0));
        newPasswordField.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        newPasswordField.setForeground(new java.awt.Color(255, 255, 255));
        newPasswordField.setText("New password");
        newPasswordField.setToolTipText("Enter New password");
        newPasswordField.setBorder(currentPasswordField.getBorder());
        newPasswordField.setEchoChar((char) 0);
        newPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newPasswordFieldKeyPressed(evt);
            }
        });
        newPasswordField.setEchoChar((char) 0);

        doneButton.setBackground(new java.awt.Color(218, 94, 12));
        doneButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        doneButton.setForeground(new java.awt.Color(255, 255, 255));
        doneButton.setText("DONE");
        doneButton.setPreferredSize(new java.awt.Dimension(77, 35));
        doneButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doneButtonFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doneButtonFocusLost(evt);
            }
        });
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = lms.new RoundedBorder(10);
        doneButton.setBorder(d);

        toastLabel.setBackground(new java.awt.Color(255, 51, 51));
        toastLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        toastLabel.setForeground(new java.awt.Color(255, 255, 255));
        toastLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        toastLabel.setBorder(doneButton.getBorder());
        toastLabel.setOpaque(true);
        toastLabel.setPreferredSize(new java.awt.Dimension(555, 35));
        toastLabel.setVisible(false);

        jLabel23.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 40)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("CHANGE PASSWORD");

        noteLabel.setBackground(new java.awt.Color(255, 51, 51));
        noteLabel.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        noteLabel.setForeground(new java.awt.Color(255, 255, 255));
        noteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noteLabel.setText("NOTE : New password should have minimum 8 characters.");
        noteLabel.setBorder(doneButton.getBorder());
        noteLabel.setOpaque(true);
        noteLabel.setPreferredSize(new java.awt.Dimension(555, 35));

        passwordHintTextField.setBackground(new java.awt.Color(255, 137, 0));
        passwordHintTextField.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        passwordHintTextField.setForeground(new java.awt.Color(255, 255, 255));
        passwordHintTextField.setText("Password Hint");
        passwordHintTextField.setToolTipText("When you forget your password, It will display the password hint.");
        passwordHintTextField.setBorder(currentPasswordField.getBorder());
        passwordHintTextField.setPreferredSize(new java.awt.Dimension(200, 35));
        passwordHintTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordHintTextFieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(252, 252, 252))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 178, Short.MAX_VALUE)
                .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(377, 377, 377)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(confirmPasswordField)
                    .addComponent(currentPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(doneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newPasswordField)
                    .addComponent(passwordHintTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(toastLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(currentPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordHintTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toastLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, 673, 673, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void currentPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_currentPasswordFieldKeyPressed
        if (currentPasswordFieldSetNull) {
            currentPasswordField.setEchoChar('\u2022');
            currentPasswordField.setText(null);
            currentPasswordFieldSetNull = false;
        }
    }//GEN-LAST:event_currentPasswordFieldKeyPressed

    private void confirmPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordFieldKeyPressed
        if (confirmPasswordFieldSetNull) {
            confirmPasswordField.setEchoChar('\u2022');
            confirmPasswordField.setText(null);
            confirmPasswordFieldSetNull = false;
        }
    }//GEN-LAST:event_confirmPasswordFieldKeyPressed

    private void newPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPasswordFieldKeyPressed
        if (newPasswordFieldSetNull) {
            newPasswordField.setEchoChar('\u2022');
            newPasswordField.setText(null);
            newPasswordFieldSetNull = false;
        }
    }//GEN-LAST:event_newPasswordFieldKeyPressed

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        sql = "UPDATE LIBRARIAN SET PASSWD = '" + newPassword +"', "
                + "PASSWD_HINT = '" + passwordHint +"' WHERE UNAME = 'bitmls1'";
        exeUpdate(sql);
        toastLabel.setVisible(true);
        toastLabel.setText("Your password has been changed successfully!");
        currentPasswordField.setVisible(false);
        newPasswordField.setVisible(false);
        confirmPasswordField.setVisible(false);
        doneButton.setVisible(false);
        passwordHintTextField.setVisible(false);
        noteLabel.setVisible(false);
    }//GEN-LAST:event_doneButtonActionPerformed

    private void doneButtonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doneButtonFocusLost
        doneButton.setEnabled(true);
    }//GEN-LAST:event_doneButtonFocusLost

    private void doneButtonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doneButtonFocusGained
        String currentPassword = currentPasswordField.getText();
        newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        passwordHint = passwordHintTextField.getText();
        String error = null;

        // Handling several exceptions
        try {
            if (currentPassword.equals("Current password")) {
                error = "Please enter current password.";
                throw new Exception();
            } else if (!currentPassword.equals("hello")) {
                error = "Current password is incorrect.";
                throw new Exception();
            } else if (newPassword.equals("New password")
                    || confirmPassword.equals("Confirm new password")) {
                error = "Please fill the password fields.";
                throw new Exception();
            } else if (!newPassword.equals(confirmPassword)) {
                error = "New password & Confirm password didn\'t match.";
                throw new Exception();
            } else if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                error = "Please fill the password fields.";
                throw new Exception();
            } else if (confirmPassword.length() <= 7) {
                error = "Please read the NOTE displayed below.";
                throw new Exception();
            } else if (newPassword.equals(confirmPassword)
                    && confirmPassword.equals(passwordHint)) {
                error = "Please read the NOTE displayed below.";
                throw new Exception();
            } else if (passwordHint.isEmpty() || passwordHint.equals("Password Hint")) {
                error = "Please give an hint of your password.";
                throw new Exception();
            }
        } catch (Exception ex) {
            showToast(error);
        }


    }//GEN-LAST:event_doneButtonFocusGained

    private void passwordHintTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordHintTextFieldKeyPressed
        if (passwordHintTextFIeldSetNull) {
            passwordHintTextField.setText(null);
            passwordHintTextFIeldSetNull = false;
        }
    }//GEN-LAST:event_passwordHintTextFieldKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JPasswordField currentPasswordField;
    private javax.swing.JButton doneButton;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField newPasswordField;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JTextField passwordHintTextField;
    private javax.swing.JLabel toastLabel;
    // End of variables declaration//GEN-END:variables
}