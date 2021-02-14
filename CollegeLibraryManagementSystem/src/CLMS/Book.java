package CLMS;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author Durga Prasad A G (https://durgaprasadag.github.io/dp/)
 */
public class Book extends javax.swing.JInternalFrame {

    CollegeLibraryManagementSystem lms = new CollegeLibraryManagementSystem();
    CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    private Connection cn;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    protected String select = "SELECT * FROM BOOK ORDER BY SEM";
    protected ArrayList<Object> text = new ArrayList<>();
    protected JTextField[] textFields;

    /**
     * @see #components()
     */
    protected JButton[] buttons;

    protected JComboBox[] comboBoxs;
    protected boolean fetchBooks = false;
    protected boolean searchByBookName = false;
    protected boolean bookIdAlreadyExists = false;
    protected String branch, book, sql, info;
    protected int sem;

    /**
     * Creates new form Book
     */
    public Book() {
        initComponents();
        connectToDatabase(select);
        components();
        defaultLookAndFeel();
        setHandCursorForButtons();
    }
    
    /**
     * Connects to database.
     *
     * @param s SQL select statement.
     */
    private void connectToDatabase(String s) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/SID", "username", "password");
            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(s);
            rs.first();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
    }

    private void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);

            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
            UIManager.put("ComboBox.disabledForeground", new java.awt.Color(153, 255, 153));
            for (JComboBox comboBox : comboBoxs) {
                comboBox.updateUI();
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showToast(String s) {
        labelToast.setVisible(true);
        labelToast.setText(s);
        timer.invisible(labelToast);
    }
    
    private void components() {
        textFields = new JTextField[]{
            textFieldBookId, textFieldEdition, textFieldAuthor, textFieldIsbnNo
        };

        buttons = new JButton[]{
            buttonSearch, buttonClear, buttonModify, buttonInsert, buttonDone, buttonUpdate
        };

        comboBoxs = new JComboBox[]{
            comboBoxSem, comboBoxBranch, comboBoxBook
        };
    }

    private void setHandCursorForButtons() {
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
    
    private void getTextFieldsData() {
        text.clear();
        text.add(0, Integer.parseInt(textFields[0].getText()));
        text.add(1, Integer.parseInt(comboBoxs[0].getSelectedItem().toString()));
        text.add(2, comboBoxs[1].getSelectedItem().toString());
        text.add(3, comboBoxs[2].getSelectedItem().toString());
        text.add(4, Integer.parseInt(textFields[1].getText()));
        text.add(5, textFields[2].getText());
        text.add(6, Long.parseLong(textFields[3].getText()));
    }

    private void enableComboBoxs(boolean b, boolean c) {
        for (JComboBox box : comboBoxs) {
            box.setEnabled(b);
        }
        textFieldIsbnNo.setEditable(c);
    }

    private void setTextFieldsData() throws SQLException {
        comboBoxBook.removeAllItems();
        textFields[0].setText("" + rs.getInt(1));
        comboBoxs[0].setSelectedItem("" + rs.getInt(2));
        comboBoxs[1].setSelectedItem(rs.getString(3));
        comboBoxs[2].addItem(rs.getString(4));
        textFields[1].setText("" + rs.getInt(5));
        textFields[2].setText(rs.getString(6));
        textFields[3].setText("" + rs.getLong(7));
    }

    private void textFieldsToUppercase() {
        for (JTextField textField : textFields) {
            textField.setText(textField.getText().toUpperCase());
        }
    }

    /**
     *
     * @param b Set Visibility
     * @see #components()
     */
    private void unHideButtons(boolean b) {
        for (int i = 0; i < 3; i++) {
            buttons[i].setVisible(b);
        }
    }

    /**
     * Executes SQL queries like insert, modify and delete.
     *
     * @param s SQL Command
     * @param info Information to be displayed.
     */
    private void exeUpdate(String info) {
        try {
            ps.executeUpdate();
            showToast(info);
            cn.commit();

            /* To Refresh the database. */
            cn.close();
            connectToDatabase(select);
        } catch (SQLIntegrityConstraintViolationException ex) {
            showToast("Book ID already exist in database.");
            bookIdAlreadyExists = true;
        } catch (SQLDataException ex) {
            System.out.println("Error 2 : " + ex);
        } catch (SQLException ex) {
            System.out.println("Error 3 : " + ex);
        }
    }

    private void bookSearch(){
        try {
            int bookId = Integer.parseInt(textFieldBookId.getText());
            
            sql = """
                     SELECT *
                     FROM BOOK
                     WHERE BOOK_ID = ?
                  """;
            ps = cn.prepareStatement(sql);
            ps.setInt(1, (int) bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                setTextFieldsData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
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
        textFieldBookId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textFieldEdition = new javax.swing.JTextField();
        buttonInsert = new javax.swing.JButton();
        buttonModify = new javax.swing.JButton();
        buttonSearch = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        textFieldAuthor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        textFieldIsbnNo = new javax.swing.JTextField();
        labelBook = new javax.swing.JLabel();
        comboBoxBranch = lms.new jCustomCombobox();
        comboBoxBook = lms.new jCustomCombobox();
        comboBoxSem = lms.new jCustomCombobox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        buttonDone = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        labelToast = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Book");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel1.setBackground(new java.awt.Color(45, 45, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(958, 693));

        textFieldBookId.setBackground(new java.awt.Color(35, 35, 35));
        textFieldBookId.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CLMS.CollegeLibraryManagementSystem a = new CLMS.CollegeLibraryManagementSystem();
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = a.new RoundedBorder(5);
        textFieldBookId.setBorder(d);
        textFieldBookId.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("EDITION");
        jLabel4.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("BOOK ID");
        jLabel5.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("AUTHOR");
        jLabel7.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldEdition.setEditable(false);
        textFieldEdition.setBackground(new java.awt.Color(35, 35, 35));
        textFieldEdition.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldEdition.setForeground(new java.awt.Color(153, 255, 153));
        textFieldEdition.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldEdition.setBorder(textFieldBookId.getBorder());
        textFieldEdition.setPreferredSize(new java.awt.Dimension(400, 35));

        buttonInsert.setBackground(new java.awt.Color(61, 80, 255));
        buttonInsert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonInsert.setForeground(new java.awt.Color(255, 255, 255));
        buttonInsert.setText("Add new Book");
        buttonInsert.setPreferredSize(new java.awt.Dimension(125, 35));
        CLMS.CollegeLibraryManagementSystem.RoundedBorder c = a.new RoundedBorder(10);
        buttonInsert.setBorder(c);

        buttonInsert.setMnemonic(java.awt.event.KeyEvent.VK_I);
        buttonInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInsertActionPerformed(evt);
            }
        });

        buttonModify.setBackground(new java.awt.Color(61, 80, 255));
        buttonModify.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonModify.setForeground(new java.awt.Color(255, 255, 255));
        buttonModify.setText("Modify Book Data");
        buttonModify.setPreferredSize(new java.awt.Dimension(125, 35));
        buttonModify.setBorder(c);

        buttonModify.setMnemonic(java.awt.event.KeyEvent.VK_M);

        buttonModify.setVisible(false);
        buttonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyActionPerformed(evt);
            }
        });

        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
        buttonSearch.setForeground(new java.awt.Color(255, 255, 255));
        buttonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/search.png"))); // NOI18N
        buttonSearch.setPreferredSize(new java.awt.Dimension(50, 35));
        buttonSearch.setBorder(c);

        buttonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed(evt);
            }
        });

        buttonClear.setBackground(new java.awt.Color(61, 80, 255));
        buttonClear.setForeground(new java.awt.Color(255, 255, 255));
        buttonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/clear.png"))); // NOI18N
        buttonClear.setPreferredSize(new java.awt.Dimension(50, 35));
        buttonClear.setBorder(c);

        buttonClear.setMnemonic(java.awt.event.KeyEvent.VK_C);
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        textFieldAuthor.setEditable(false);
        textFieldAuthor.setBackground(new java.awt.Color(35, 35, 35));
        textFieldAuthor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldAuthor.setForeground(new java.awt.Color(153, 255, 153));
        textFieldAuthor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldAuthor.setBorder(textFieldBookId.getBorder());
        textFieldAuthor.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("ISBN NO");
        jLabel9.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldIsbnNo.setBackground(new java.awt.Color(35, 35, 35));
        textFieldIsbnNo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIsbnNo.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIsbnNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIsbnNo.setBorder(textFieldBookId.getBorder());
        textFieldIsbnNo.setPreferredSize(new java.awt.Dimension(400, 35));

        labelBook.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelBook.setForeground(new java.awt.Color(255, 255, 255));
        labelBook.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelBook.setText("BOOK");
        labelBook.setPreferredSize(new java.awt.Dimension(175, 35));
        labelBook.setVisible(true);

        comboBoxBranch.setBackground(new java.awt.Color(35, 35, 35));
        comboBoxBranch.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        comboBoxBranch.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxBranch.setMaximumRowCount(9);
        comboBoxBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT BRANCH", "AIML", "CHEMISTRY", "CSE", "CIV", "ECE", "EEE", "ME", "PHYSICS" }));
        comboBoxBranch.setPreferredSize(new java.awt.Dimension(200, 35));
        comboBoxBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxBranchItemStateChanged(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.CustomRenderer customRenderer = lms.new CustomRenderer();
        comboBoxBranch.setRenderer(customRenderer);

        comboBoxBook.setBackground(new java.awt.Color(35, 35, 35));
        comboBoxBook.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        comboBoxBook.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxBook.setMaximumRowCount(10);
        comboBoxBook.setBorder(textFieldBookId.getBorder());
        comboBoxBook.setPreferredSize(new java.awt.Dimension(400, 35));
        comboBoxBook.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxBookItemStateChanged(evt);
            }
        });
        comboBoxBook.setRenderer(comboBoxBranch.getRenderer());
        comboBoxBook.setVisible(true);

        comboBoxSem.setBackground(new java.awt.Color(35, 35, 35));
        comboBoxSem.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        comboBoxSem.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxSem.setMaximumRowCount(9);
        comboBoxSem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT SEM", "1", "2", "3", "4", "5", "6", "7", "8" }));
        comboBoxSem.setToolTipText("");
        comboBoxSem.setPreferredSize(new java.awt.Dimension(190, 35));
        comboBoxSem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxSemItemStateChanged(evt);
            }
        });
        comboBoxSem.setRenderer(comboBoxBranch.getRenderer());

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("SEM");
        jLabel10.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("BRANCH");
        jLabel11.setPreferredSize(new java.awt.Dimension(175, 35));

        buttonDone.setBackground(new java.awt.Color(61, 80, 255));
        buttonDone.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonDone.setForeground(new java.awt.Color(255, 255, 255));
        buttonDone.setText("Done");
        buttonDone.setPreferredSize(new java.awt.Dimension(400, 35));
        buttonDone.setBorder(d);

        buttonDone.setVisible(false);

        buttonDone.setMnemonic(java.awt.event.KeyEvent.VK_D);
        buttonDone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buttonDoneFocusGained(evt);
            }
        });
        buttonDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDoneActionPerformed(evt);
            }
        });

        buttonUpdate.setBackground(new java.awt.Color(61, 80, 255));
        buttonUpdate.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonUpdate.setForeground(new java.awt.Color(255, 255, 255));
        buttonUpdate.setText("Update");
        buttonUpdate.setPreferredSize(new java.awt.Dimension(439, 35));
        buttonUpdate.setBorder(d);

        buttonUpdate.setVisible(false);

        buttonUpdate.setMnemonic(java.awt.event.KeyEvent.VK_U);
        buttonUpdate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buttonUpdateFocusGained(evt);
            }
        });
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateActionPerformed(evt);
            }
        });

        labelToast.setBackground(new java.awt.Color(119, 65, 244));
        labelToast.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelToast.setForeground(new java.awt.Color(255, 255, 255));
        labelToast.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelToast.setBorder(textFieldBookId.getBorder());
        labelToast.setOpaque(true);
        labelToast.setPreferredSize(new java.awt.Dimension(555, 35));
        labelToast.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(193, 193, 193)
                                .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonModify, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textFieldEdition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(textFieldAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(textFieldIsbnNo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonDone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(labelBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(comboBoxBook, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textFieldBookId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonInsert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboBoxBranch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboBoxSem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(labelToast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(210, 210, 210))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(buttonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldBookId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonModify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIsbnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonDone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(labelToast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInsertActionPerformed
        buttonInsert.setBackground(new java.awt.Color(119, 65, 244));
        unHideButtons(false);
        enableComboBoxs(true, true);
        
        buttonDone.setVisible(true);
        buttonInsert.setVisible(true);
        fetchBooks = true;
    }//GEN-LAST:event_buttonInsertActionPerformed

    private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
        buttonModify.setVisible(true);
        buttonSearch.setBackground(java.awt.Color.GREEN);
        textFieldIsbnNo.setEditable(false);
        enableComboBoxs(false, false);
        bookSearch();
    }//GEN-LAST:event_buttonSearchActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        textFieldBookId.setText(null);
        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
    }//GEN-LAST:event_buttonClearActionPerformed

    private void comboBoxBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxBranchItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (fetchBooks) {
                if (comboBoxSem.getSelectedIndex() > 0 && comboBoxBranch.getSelectedIndex() > 0) {
                    try {
                        searchByBookName = false;
                        sem = Integer.parseInt(comboBoxSem.getSelectedItem().toString());
                        branch = comboBoxBranch.getSelectedItem().toString();
                        comboBoxBook.removeAllItems();
                        sql = """
                                 SELECT BOOK_NAME FROM BOOK_INFORMATION
                                 WHERE SEM = ? AND BRANCH = ?
                              """;
                        ps = cn.prepareStatement(sql);
                        ps.setInt(1, (int) sem);
                        ps.setString(2, (String) branch);
                        rs = ps.executeQuery();
                        comboBoxBook.addItem("SELECT A BOOK");
                        while (rs.next()) {
                            comboBoxBook.addItem(rs.getString(1));
                            searchByBookName = !rs.getString(1).isEmpty();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_comboBoxBranchItemStateChanged

    private void buttonDoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buttonDoneFocusGained
        textFieldsToUppercase();
    }//GEN-LAST:event_buttonDoneFocusGained

    private void buttonDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDoneActionPerformed
        getTextFieldsData();
        try {
            sql = """
                     INSERT INTO BOOK
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                  """;
            ps = cn.prepareStatement(sql);
            for (int i = 0; i < text.size(); i++) {
                switch (i) {
                    case 0, 1, 4 ->
                        ps.setInt((i + 1), (int) text.get(i));
                    case 2, 3, 5 ->
                        ps.setString((i + 1), (String) text.get(i));
                    case 6 -> {
                        ps.setLong((i + 1), (long) text.get(i));
                    }
                }
            }
            info = "Book has been added successfully.";
            exeUpdate(info);
            unHideButtons(true);
            buttonDone.setVisible(false);
        } catch (SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDoneActionPerformed

    private void buttonUpdateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buttonUpdateFocusGained

    }//GEN-LAST:event_buttonUpdateFocusGained

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        textFieldIsbnNo.setEditable(true);
        try {
            int bookId = Integer.parseInt(textFieldBookId.getText());
            String isbn = textFieldIsbnNo.getText();
            sql = """
                     UPDATE BOOK
                     SET    ISBN_NO = ?
                     WHERE  BOOK_ID = ?
                  """;
            ps = cn.prepareStatement(sql);
            ps.setString(1, isbn);
            ps.setInt(2, bookId);
            info = "Book Information has been updated successfully.";
            exeUpdate(info);
            buttonModify.setForeground(java.awt.Color.WHITE);
            buttonModify.setBackground(new java.awt.Color(61, 80, 255));
            buttonUpdate.setVisible(false);
            enableComboBoxs(false, false);
            textFieldIsbnNo.setEditable(true);
        } catch (SQLException ex) {
            Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void buttonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyActionPerformed
        buttonUpdate.setVisible(true);
        textFieldIsbnNo.setEditable(false);
        enableComboBoxs(false, true);
        buttonModify.setForeground(java.awt.Color.BLACK);
        buttonModify.setBackground(java.awt.Color.GREEN);
    }//GEN-LAST:event_buttonModifyActionPerformed

    private void comboBoxBookItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxBookItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (searchByBookName) {
                try {
                    String bookName = comboBoxBook.getSelectedItem().toString();
                    sql = """
                             SELECT AUTHOR, BOOK_EDITION
                             FROM   BOOK_INFORMATION
                             WHERE  BOOK_NAME = ?
                          """;
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, (String) bookName);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        textFieldAuthor.setText(rs.getString(1));
                        textFieldEdition.setText("" + rs.getInt(2));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (comboBoxBook.getSelectedIndex() == 0) {
                textFieldAuthor.setText(null);
                textFieldEdition.setText(null);
            }
        }
    }//GEN-LAST:event_comboBoxBookItemStateChanged

    private void comboBoxSemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxSemItemStateChanged
        textFieldAuthor.setText(null);
        textFieldEdition.setText(null);
        comboBoxBook.removeAllItems();
    }//GEN-LAST:event_comboBoxSemItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    public javax.swing.JButton buttonDone;
    private javax.swing.JButton buttonInsert;
    private javax.swing.JButton buttonModify;
    private javax.swing.JButton buttonSearch;
    public javax.swing.JButton buttonUpdate;
    public javax.swing.JComboBox<String> comboBoxBook;
    public javax.swing.JComboBox<String> comboBoxBranch;
    public javax.swing.JComboBox<String> comboBoxSem;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelBook;
    private javax.swing.JLabel labelToast;
    private javax.swing.JTextField textFieldAuthor;
    private javax.swing.JTextField textFieldBookId;
    private javax.swing.JTextField textFieldEdition;
    private javax.swing.JTextField textFieldIsbnNo;
    // End of variables declaration//GEN-END:variables
}
