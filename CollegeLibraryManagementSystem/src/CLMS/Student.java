package CLMS;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Durga Prasad A G (https://durgaprasadag.github.io/dp/)
 */
public class Student extends javax.swing.JInternalFrame {

    CollegeLibraryManagementSystem lms = new CollegeLibraryManagementSystem();
    CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    private Connection cn;
    private Statement st;
    private String sql;
    private ResultSet rs;
    private PreparedStatement ps;
    protected String select = "SELECT * FROM STUDENT ORDER BY USN";
    protected boolean usnAlreadyExists = false;
    protected boolean usnNotFound = false;
    protected boolean setDefaultIndex = false;
    protected boolean studentAdded = false;
    protected String info, usn, branch, name, eligible;
    protected int sem;

    private void connectToDatabase(String s) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "hr", "superman");
            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(s);
            rs.first();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showToast(String s) {
        toastLabel.setVisible(true);
        toastLabel.setText(s);
        timer.invisible(toastLabel);
    }

    private void popStudentTextFields() throws SQLException {
        nameTextField.setText(rs.getString(2));
        branchTextField.setText(rs.getString(3));
        semTextField.setText(rs.getString(4));
        eligibleTextField.setText(rs.getString(5));
    }

    private void searchStudent() {
        try {
            String usn = usnTextField.getText();
            sql = "SELECT * FROM STUDENT WHERE USN = '" + usn + "'";
            rs = st.executeQuery(sql);
            rs.first();
            if (usn.equals(rs.getString(1))) {
                popStudentTextFields();
                buttonsDeleteModifyVisibility(true);
            } else {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            buttonsDeleteModifyVisibility(false);
            showToast("USN not found in database.");
            usnNotFound = true;
            searchButton.setBackground(java.awt.Color.RED);
        }
    }

    private void getTextFieldsData() {
        usn = usnTextField.getText();
        name = nameTextField.getText();
        branch = branchTextField.getText();
        sem = Integer.parseInt(semTextField.getText());
        eligible = eligibleTextField.getText();
    }

    private void unEditableAllTextFields() {
        unEditableStudentTextFields();
        nameTextField.setEditable(false);
    }

    private void editableTextFields() {
        nameTextField.setEditable(true);
    }

    private void clearStudentTextFields() {
        usnTextField.setText("3BR");
        nameTextField.setText(null);
        branchTextField.setText(null);
        semTextField.setText(null);
        eligibleTextField.setText(null);
    }

    private void unHidejComboBox() {
        branchCombBox.setVisible(true);
        semComboBox.setVisible(true);
        eligibleComboBox.setVisible(true);

        if (setDefaultIndex) {
            branchCombBox.setSelectedIndex(0);
            semComboBox.setSelectedIndex(0);
            eligibleComboBox.setSelectedIndex(1);
            setDefaultIndex = false;
        }
    }

    private void hidejComboBox() {
        branchCombBox.setVisible(false);
        semComboBox.setVisible(false);
        eligibleComboBox.setVisible(false);
    }

    private void unEditableStudentTextFields() {
        branchTextField.setEditable(false);
        semTextField.setEditable(false);
        eligibleTextField.setEditable(false);
    }

    private void unHideButtons(boolean b) {
        clearButton.setVisible(b);
        insertButton.setVisible(b);
        searchButton.setVisible(b);
        deleteButton.setVisible(b);
        modifyButton.setVisible(b);
        goToIssueBooksButton.setVisible(b);
    }

    private void buttonsDeleteModifyVisibility(boolean b) {
        deleteButton.setVisible(b);
        modifyButton.setVisible(b);
    }

    private void exeUpdate(String s, String info) {
        try {
            st.executeUpdate(s);
            showToast(info);
            cn.commit();
            cn.close();
            connectToDatabase(select);
            studentAdded = true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            showToast("USN already exist in database!");
            usnAlreadyExists = true;
        } catch (SQLDataException ex) {
            System.out.println("Error 2 : " + ex);
        } catch (SQLException ex) {
            System.out.println("Error 3 : " + ex);
        }
    }

    private void exeQuery() {
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(1, (String) usn);
            for (int i = 2; i <= 13; i++) {
                if (i % 2 == 0) {
                    ps.setNull(i, java.sql.Types.INTEGER);
                } else {
                    ps.setNull(i, java.sql.Types.DATE);
                }
            }
            rs = ps.executeQuery();
            cn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void unHideConfirmIssueBooksButtons(boolean b) {
        toastLabel.setVisible(b);
        yesButton1.setVisible(b);
        noButton1.setVisible(b);
    }

    /**
     * Creates new form Student
     */
    public Student() {
        initComponents();
        connectToDatabase("SELECT * FROM STUDENT ORDER BY USN");
        hidejComboBox();
        defaultLookAndFeel();
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
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        usnTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        semTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        branchTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        eligibleTextField = new javax.swing.JTextField();
        goToIssueBooksButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        insertButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        modifyButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        toastLabel = new javax.swing.JLabel();
        branchCombBox = lms.new jCustomCombobox();
        semComboBox = lms.new jCustomCombobox();
        eligibleComboBox = lms.new jCustomCombobox();
        updateButton = new javax.swing.JButton();
        yesButton = new javax.swing.JButton();
        noButton = new javax.swing.JButton();
        yesButton1 = new javax.swing.JButton();
        noButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Student");
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel1.setBackground(new java.awt.Color(45, 45, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(958, 693));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("USN");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 35));

        usnTextField.setBackground(new java.awt.Color(35, 35, 35));
        usnTextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        usnTextField.setForeground(new java.awt.Color(153, 255, 153));
        usnTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usnTextField.setText("3BR");
        usnTextField.setPreferredSize(new java.awt.Dimension(400, 35));
        usnTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usnTextFieldKeyReleased(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem a = new CLMS.CollegeLibraryManagementSystem();
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = a.new RoundedBorder(5);
        usnTextField.setBorder(d);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("NAME");
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 35));

        nameTextField.setBackground(new java.awt.Color(35, 35, 35));
        nameTextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        nameTextField.setForeground(new java.awt.Color(153, 255, 153));
        nameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameTextField.setBorder(usnTextField.getBorder());
        nameTextField.setPreferredSize(new java.awt.Dimension(400, 35));
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("SEM");
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 35));

        semTextField.setBackground(new java.awt.Color(35, 35, 35));
        semTextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        semTextField.setForeground(new java.awt.Color(153, 255, 153));
        semTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        semTextField.setBorder(usnTextField.getBorder());
        semTextField.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("BRANCH");
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 35));

        branchTextField.setBackground(new java.awt.Color(35, 35, 35));
        branchTextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        branchTextField.setForeground(new java.awt.Color(153, 255, 153));
        branchTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        branchTextField.setBorder(usnTextField.getBorder());
        branchTextField.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("ELIGIBLE");
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 35));

        eligibleTextField.setBackground(new java.awt.Color(35, 35, 35));
        eligibleTextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        eligibleTextField.setForeground(new java.awt.Color(153, 255, 153));
        eligibleTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        eligibleTextField.setBorder(usnTextField.getBorder());
        eligibleTextField.setPreferredSize(new java.awt.Dimension(300, 35));

        goToIssueBooksButton.setBackground(new java.awt.Color(61, 80, 255));
        goToIssueBooksButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        goToIssueBooksButton.setForeground(new java.awt.Color(255, 255, 255));
        goToIssueBooksButton.setText("Go To Issue Books");
        goToIssueBooksButton.setPreferredSize(new java.awt.Dimension(400, 35));
        CLMS.CollegeLibraryManagementSystem.RoundedBorder c = a.new RoundedBorder(10);
        goToIssueBooksButton.setBorder(c);

        goToIssueBooksButton.setMnemonic(java.awt.event.KeyEvent.VK_I);
        goToIssueBooksButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                goToIssueBooksButtonFocusGained(evt);
            }
        });
        goToIssueBooksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToIssueBooksButtonActionPerformed(evt);
            }
        });
        goToIssueBooksButton.setVisible(false);

        searchButton.setBackground(new java.awt.Color(61, 80, 255));
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/search.png"))); // NOI18N
        searchButton.setPreferredSize(new java.awt.Dimension(50, 35));
        searchButton.setBorder(c);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchButton.setMnemonic(java.awt.event.KeyEvent.VK_S);

        clearButton.setBackground(new java.awt.Color(61, 80, 255));
        clearButton.setForeground(new java.awt.Color(255, 255, 255));
        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/clear.png"))); // NOI18N
        clearButton.setPreferredSize(new java.awt.Dimension(50, 35));
        clearButton.setBorder(c);

        clearButton.setMnemonic(java.awt.event.KeyEvent.VK_C);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        insertButton.setBackground(new java.awt.Color(61, 80, 255));
        insertButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        insertButton.setForeground(new java.awt.Color(255, 255, 255));
        insertButton.setText("Add new Student");
        insertButton.setBorder(goToIssueBooksButton.getBorder());
        insertButton.setMnemonic(java.awt.event.KeyEvent.VK_N);
        insertButton.setPreferredSize(new java.awt.Dimension(125, 35));
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(61, 80, 255));
        deleteButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.setBorder(goToIssueBooksButton.getBorder());
        deleteButton.setMnemonic(java.awt.event.KeyEvent.VK_D);
        deleteButton.setPreferredSize(new java.awt.Dimension(124, 35));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        deleteButton.setVisible(false);

        modifyButton.setBackground(new java.awt.Color(61, 80, 255));
        modifyButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        modifyButton.setForeground(new java.awt.Color(255, 255, 255));
        modifyButton.setText("Modify");
        modifyButton.setBorder(goToIssueBooksButton.getBorder());
        modifyButton.setMnemonic(java.awt.event.KeyEvent.VK_M);
        modifyButton.setPreferredSize(new java.awt.Dimension(125, 35));
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });
        modifyButton.setVisible(false);

        doneButton.setBackground(new java.awt.Color(61, 80, 255));
        doneButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        doneButton.setForeground(new java.awt.Color(255, 255, 255));
        doneButton.setText("Done");
        doneButton.setBorder(goToIssueBooksButton.getBorder());
        doneButton.setPreferredSize(new java.awt.Dimension(400, 35));
        doneButton.setBorder(c);

        doneButton.setMnemonic(java.awt.event.KeyEvent.VK_D);
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
        doneButton.setVisible(false);

        toastLabel.setBackground(new java.awt.Color(119, 65, 244));
        toastLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        toastLabel.setForeground(new java.awt.Color(255, 255, 255));
        toastLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        toastLabel.setBorder(usnTextField.getBorder());
        toastLabel.setOpaque(true);
        toastLabel.setPreferredSize(new java.awt.Dimension(555, 35));
        toastLabel.setVisible(false);

        branchCombBox.setBackground(new java.awt.Color(51, 51, 51));
        branchCombBox.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        branchCombBox.setForeground(new java.awt.Color(255, 255, 255));
        branchCombBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT BRANCH", "CSE", "CIV", "ECE", "EEE", "ME", "AIML" }));
        branchCombBox.setBorder(usnTextField.getBorder());
        branchCombBox.setFocusable(false);
        branchCombBox.setPreferredSize(new java.awt.Dimension(65, 35));
        branchCombBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                branchCombBoxItemStateChanged(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.CustomRenderer customRenderer = lms.new CustomRenderer();
        branchCombBox.setRenderer(customRenderer);

        semComboBox.setBackground(new java.awt.Color(51, 51, 51));
        semComboBox.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        semComboBox.setForeground(new java.awt.Color(255, 255, 255));
        semComboBox.setMaximumRowCount(10);
        semComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT SEM", "1", "2", "3", "4", "5", "6", "7", "8" }));
        semComboBox.setBorder(usnTextField.getBorder());
        semComboBox.setFocusable(false);
        semComboBox.setPreferredSize(new java.awt.Dimension(65, 35));
        semComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                semComboBoxItemStateChanged(evt);
            }
        });
        semComboBox.setRenderer(branchCombBox.getRenderer());

        eligibleComboBox.setBackground(new java.awt.Color(51, 51, 51));
        eligibleComboBox.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        eligibleComboBox.setForeground(new java.awt.Color(255, 255, 255));
        eligibleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ELIGIBLE?", "YES", "NO" }));
        eligibleComboBox.setSelectedIndex(1);
        eligibleComboBox.setBorder(usnTextField.getBorder());
        eligibleComboBox.setFocusable(false);
        eligibleComboBox.setPreferredSize(new java.awt.Dimension(65, 35));
        eligibleComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                eligibleComboBoxItemStateChanged(evt);
            }
        });
        eligibleComboBox.setRenderer(branchCombBox.getRenderer());

        updateButton.setBackground(new java.awt.Color(61, 80, 255));
        updateButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setText("Update");
        updateButton.setBorder(goToIssueBooksButton.getBorder());
        updateButton.setPreferredSize(new java.awt.Dimension(400, 35));
        updateButton.setBorder(c);

        updateButton.setMnemonic(java.awt.event.KeyEvent.VK_U);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        updateButton.setVisible(false);

        yesButton.setBackground(new java.awt.Color(61, 80, 255));
        yesButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        yesButton.setForeground(new java.awt.Color(255, 255, 255));
        yesButton.setText("YES");
        yesButton.setPreferredSize(new java.awt.Dimension(70, 35));
        yesButton.setBorder(c);

        yesButton.setMnemonic(java.awt.event.KeyEvent.VK_Y);
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });
        yesButton.setVisible(false);

        noButton.setBackground(new java.awt.Color(61, 80, 255));
        noButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        noButton.setForeground(new java.awt.Color(255, 255, 255));
        noButton.setText("NO");
        noButton.setPreferredSize(new java.awt.Dimension(70, 35));
        noButton.setBorder(c);

        noButton.setMnemonic(java.awt.event.KeyEvent.VK_N);
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });
        noButton.setVisible(false);

        yesButton1.setBackground(new java.awt.Color(61, 80, 255));
        yesButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        yesButton1.setForeground(new java.awt.Color(255, 255, 255));
        yesButton1.setText("YES");
        yesButton1.setPreferredSize(new java.awt.Dimension(70, 35));
        yesButton1.setBorder(c);
        yesButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButton1ActionPerformed(evt);
            }
        });
        yesButton1.setVisible(false);

        noButton1.setBackground(new java.awt.Color(61, 80, 255));
        noButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        noButton1.setForeground(new java.awt.Color(255, 255, 255));
        noButton1.setText("NO");
        noButton1.setPreferredSize(new java.awt.Dimension(70, 35));
        noButton1.setBorder(c);
        noButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButton1ActionPerformed(evt);
            }
        });
        noButton1.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(toastLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(194, 194, 194))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(167, 167, 167)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(semTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(branchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(usnTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(goToIssueBooksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(eligibleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(285, 285, 285)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(doneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGap(0, 0, 0)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(branchCombBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(eligibleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(semComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(456, 456, 456)
                        .addComponent(yesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yesButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(usnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(branchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(branchCombBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eligibleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eligibleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goToIssueBooksButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(doneButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(toastLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yesButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goToIssueBooksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToIssueBooksButtonActionPerformed
        IssueBooks iB = new IssueBooks();
        javax.swing.JDesktopPane dp = getDesktopPane();
        dp.add(iB);
        iB.show();
        String usn = usnTextField.getText();
        iB.textFieldUsn.setText(usn);
    }//GEN-LAST:event_goToIssueBooksButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        searchButton.setBackground(java.awt.Color.GREEN);
        searchStudent();
        unEditableAllTextFields();
        goToIssueBooksButton.setVisible(true);

        if (usnNotFound) {
            goToIssueBooksButton.setVisible(false);
            usnNotFound = false;
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearStudentTextFields();
        searchButton.setBackground(new java.awt.Color(61, 80, 255));
        buttonsDeleteModifyVisibility(false);
        goToIssueBooksButton.setVisible(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void usnTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usnTextFieldKeyReleased
        usnTextField.setText(usnTextField.getText().toUpperCase());
    }//GEN-LAST:event_usnTextFieldKeyReleased

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        getTextFieldsData();

        sql = "INSERT INTO STUDENT VALUES ('" + usn + "', '" + name + "', '" + branch + "', "
                + sem + ", '" + eligible + "')";
        info = "Student data has been added successfully.";
        exeUpdate(sql, info);

        if (studentAdded) {
            sql = """
                     INSERT INTO ISSUE_BOOKS
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                  """;
            exeQuery();
        }

        hidejComboBox(); // Hide the jCombo box
        unHideButtons(true); // Unhide the Buttons
        doneButton.setVisible(false); // Hide Done button

        /* Basically if usn is already taken by some other student then 
           usnAlreadyExits becomes true in catch block
         */
        if (usnAlreadyExists) {
            unHidejComboBox();
            unHideButtons(false); // Hide the Buttons
            doneButton.setVisible(true); // Make the Done button visible again.
            usnAlreadyExists = false;
        }
    }//GEN-LAST:event_doneButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        unHideButtons(false);
        toastLabel.setVisible(true);
        toastLabel.setText("Are you sure? Do you want to delete this student related data?");
        yesButton.setVisible(true);
        noButton.setVisible(true);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        clearStudentTextFields(); // CLear all the text fields
        setDefaultIndex = true;
        unHidejComboBox(); // Unhide the jCombo box
        unEditableStudentTextFields(); // Make Text fields of SEM, BRANCH & ELIGIBLE uneditable
        editableTextFields();
        eligibleTextField.setText("YES"); // Set ELigible Text field to YES by default
        doneButton.setVisible(true); // Make Done button visible
        unHideButtons(false); // Hide all the buttons
    }//GEN-LAST:event_insertButtonActionPerformed

    private void branchCombBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_branchCombBoxItemStateChanged
        branchTextField.setText(branchCombBox.getSelectedItem().toString());
        if (branchCombBox.getSelectedItem().toString().equals("SELECT BRANCH")) {
            branchTextField.setText(null);
        }
    }//GEN-LAST:event_branchCombBoxItemStateChanged

    private void semComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_semComboBoxItemStateChanged
        semTextField.setText(semComboBox.getSelectedItem().toString());
        if (semComboBox.getSelectedItem().toString().equals("SELECT SEM")) {
            semTextField.setText(null);
        }
    }//GEN-LAST:event_semComboBoxItemStateChanged

    private void eligibleComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_eligibleComboBoxItemStateChanged
        eligibleTextField.setText(eligibleComboBox.getSelectedItem().toString());
        if (eligibleComboBox.getSelectedItem().toString().equals("ELIGIBLE?")) {
            eligibleTextField.setText(null);
        }
    }//GEN-LAST:event_eligibleComboBoxItemStateChanged

    private void nameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyReleased
        nameTextField.setText(nameTextField.getText().toUpperCase());
    }//GEN-LAST:event_nameTextFieldKeyReleased

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        getTextFieldsData();

        sql = "UPDATE STUDENT SET STUDENT_NAME = '" + name + "', BRANCH = '" + branch
                + "', SEM = " + sem + ", ELIGIBLE = '" + eligible
                + "' WHERE USN = '" + usn + "'";
        info = "Student data modified successfully.";
        exeUpdate(sql, info);

        updateButton.setVisible(false);
        unHideButtons(true);
        hidejComboBox();
        usnTextField.setEditable(true);
        nameTextField.setEditable(false);
    }//GEN-LAST:event_updateButtonActionPerformed

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        updateButton.setVisible(true);
        usnTextField.setEditable(false);
        unHidejComboBox();
        unHideButtons(false);
        unEditableStudentTextFields();
        editableTextFields();
    }//GEN-LAST:event_modifyButtonActionPerformed

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        usn = "" + usnTextField.getText();
        info = "Student data deleted successfully.";
        unHideButtons(true);
        yesButton.setVisible(false);
        noButton.setVisible(false);
        clearStudentTextFields();
        buttonsDeleteModifyVisibility(false);
        sql = "DELETE FROM STUDENT WHERE USN = '" + usn + "'";
        exeUpdate(sql, info);
    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        unHideButtons(true);
        yesButton.setVisible(false);
        noButton.setVisible(false);
        toastLabel.setVisible(false);
    }//GEN-LAST:event_noButtonActionPerformed

    private void doneButtonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doneButtonFocusGained
        if (usnTextField.getText().equals("3BR") || usnTextField.getText().isEmpty()
                || nameTextField.getText().isEmpty() || branchTextField.getText().isEmpty()
                || semTextField.getText().isEmpty() || eligibleTextField.getText().isEmpty()) {
            doneButton.setEnabled(false);
            showToast("Please fill the Text fields.");
        } else if (usnTextField.getText().length() > 10) {
            doneButton.setEnabled(false);
            showToast("USN length is more than 10 characters.");
        } else if (usnTextField.getText().length() < 10) {
            doneButton.setEnabled(false);
            showToast("USN length is less than 10 characters.");
        }
    }//GEN-LAST:event_doneButtonFocusGained

    private void doneButtonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doneButtonFocusLost
        doneButton.setEnabled(true);
    }//GEN-LAST:event_doneButtonFocusLost

    private void yesButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButton1ActionPerformed
        unHideConfirmIssueBooksButtons(false);
        goToIssueBooksButtonActionPerformed(evt);
        goToIssueBooksButton.setEnabled(true);
    }//GEN-LAST:event_yesButton1ActionPerformed

    private void goToIssueBooksButtonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_goToIssueBooksButtonFocusGained
        eligible = eligibleTextField.getText();
        if (eligible.equals("NO")) {
            goToIssueBooksButton.setEnabled(false);
            unHideConfirmIssueBooksButtons(true);
            toastLabel.setText("This student is not eligible. Do you want to continue?");
        }
    }//GEN-LAST:event_goToIssueBooksButtonFocusGained

    private void noButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButton1ActionPerformed
        unHideButtons(true);
        unHideConfirmIssueBooksButtons(false);
        goToIssueBooksButton.setEnabled(true);
    }//GEN-LAST:event_noButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> branchCombBox;
    private javax.swing.JTextField branchTextField;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    public javax.swing.JButton doneButton;
    public javax.swing.JComboBox<String> eligibleComboBox;
    private javax.swing.JTextField eligibleTextField;
    private javax.swing.JButton goToIssueBooksButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton modifyButton;
    private javax.swing.JTextField nameTextField;
    public javax.swing.JButton noButton;
    public javax.swing.JButton noButton1;
    private javax.swing.JButton searchButton;
    public javax.swing.JComboBox<String> semComboBox;
    private javax.swing.JTextField semTextField;
    private javax.swing.JLabel toastLabel;
    public javax.swing.JButton updateButton;
    private javax.swing.JTextField usnTextField;
    public javax.swing.JButton yesButton;
    public javax.swing.JButton yesButton1;
    // End of variables declaration//GEN-END:variables
}
