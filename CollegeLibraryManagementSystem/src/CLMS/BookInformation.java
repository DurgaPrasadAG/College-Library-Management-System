package CLMS;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Durga Prasad A G (https://durgaprasadag.github.io/dp/)
 */
public class BookInformation extends javax.swing.JInternalFrame {

    CollegeLibraryManagementSystem lms = new CollegeLibraryManagementSystem();
    CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    private String sql, info, branch;
    protected int sem;
    private Connection cn;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    protected String select = "SELECT * FROM BOOK_INFORMATION ORDER BY SEM";
    protected boolean bookAlreadyExists = false;
    protected boolean isDeleteStatement = false;
    protected boolean searchByBookName = false;
    protected ArrayList<Object> text = new ArrayList<>();
    protected ArrayList<Object> bookNameArrayList = new ArrayList<>();
    protected JButton[] buttons;
    protected JTextField[] textFields;

    private void textFieldsAndButtons() {
        buttons = new JButton[]{
            // 0              1             2             3             4           5         
            buttonClear, buttonSearch, buttonModify, buttonInsert, buttonUpdate, buttonDone
        };

        textFields = new JTextField[]{
            //   0               1               2                   3                   4
            textFieldSem, textFieldBranch, textFieldBook, textFieldAuthor, textFieldEdition,
            //         5                     6                         7               8 
            textFieldPublisher, textFieldPublishYear, textFieldPrice, textFieldNumOfCopies,
            //    9
            textFieldRackNo
        };
    }

    /**
     * Gets text data of all the text fields.
     */
    private void getTextFieldsData() {
        text.clear();
        for (int i = 0; i < textFields.length; i++) {
            text.add(i, textFields[i].getText());
            switch (i) {
                case 0, 4, 6, 7, 8, 9 ->
                    text.set(i, Integer.parseInt(String.valueOf(text.get(i))));
            }
        }
    }

    /**
     * Connects to database.
     *
     * @param s SQL select statement.
     */
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

    /**
     * Show a toast for 5 seconds. This is similar to Snack bar.
     *
     * @param s Information to be displayed.
     */
    private void showToast(String s) {
        labelToast.setVisible(true);
        labelToast.setText(s);
        timer.invisible(labelToast);
    }

    /* Search a book in the database by sem & branch. */
    private void searchBook() {
        try {
            sem = Integer.parseInt(textFieldSem.getText());
            branch = textFieldBranch.getText();
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
            }
            searchByBookName = true;
        } catch (SQLException ex) {
            Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayBookInfo() throws SQLException, ParseException {
        for (int i = 3; i <= 10; i++) {
            switch (i) {
                case 4, 7, 8, 9 ->
                    textFields[i].setText("" + rs.getInt((i - 2)));
                case 3, 5, 6 ->
                    textFields[i].setText(rs.getString((i - 2)));
            }
        }
    }

    /**
     * Clears all the text field data.
     */
    private void clearBookTextFields() {
        for (JTextField textField : textFields) {
            textField.setText(null);
        }
    }

    /**
     * Unhide all the buttons.
     *
     * @param b if true then unhide all the buttons. If false then hide all the buttons.
     */
    private void unHideButtons(boolean b) {
        for (int i = 0; i < 5; i++) {
            buttons[i].setVisible(b);
        }
    }

    private void editableTextFields(boolean b) {
        for (int i = 2; i < textFields.length; i++) {
            textFields[i].setEditable(b);
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

            /* Refreshing the database. */
            cn.close();
            connectToDatabase(select);
        } catch (SQLIntegrityConstraintViolationException ex) {
            showToast("Book with same name already exist in database.");
            bookAlreadyExists = true;
        } catch (SQLDataException ex) {
            System.out.println("Error 2 : " + ex);
        } catch (SQLException ex) {
            System.out.println("Error 3 : " + ex);
        }
    }

    private void onInsertVisibility(boolean b) {
        buttonClear.setVisible(b);
        bookNameVisbility(true, false, b);
    }

    private void bookNameVisbility(boolean a, boolean b, boolean d) {
        textFieldBook.setVisible(a);
        labelBook.setVisible(a);

        comboBoxBook.setVisible(b);
        labelBook1.setVisible(b);

        comboBoxSem.setVisible(d);
        combBoxBranch.setVisible(d);
    }

    private void onSearchVisbility() {
        comboBoxSem.setVisible(false);
        combBoxBranch.setVisible(false);
        bookNameVisbility(false, true, false);
    }

    private void textFieldsToUppercase() {
        for (int i = 2; i < 7; i++) {
            switch (i) {
                case 2, 3, 5, 6 ->
                    textFields[i].setText(textFields[i].getText().toUpperCase());
            }
        }
    }

    /**
     * Creates new form BookInfo
     */
    public BookInformation() {
        initComponents();
        textFieldsAndButtons();
        setHandCursorForButtons();
        connectToDatabase(select);
        defaultLookAndFeel();
    }

    private void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel6 = new javax.swing.JLabel();
        labelBook = new javax.swing.JLabel();
        textFieldBook = new javax.swing.JTextField();
        textFieldNumOfCopies = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textFieldAuthor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        textFieldPublishYear = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textFieldPublisher = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textFieldPrice = new javax.swing.JTextField();
        buttonInsert = new javax.swing.JButton();
        buttonModify = new javax.swing.JButton();
        buttonDone = new javax.swing.JButton();
        buttonSearch = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        textFieldEdition = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        textFieldRackNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        comboBoxBook = lms.new jCustomCombobox();
        labelBook1 = new javax.swing.JLabel();
        buttonUpdate = new javax.swing.JButton();
        labelToast = new javax.swing.JLabel();
        combBoxBranch = lms.new jCustomCombobox();
        comboBoxSem = lms.new jCustomCombobox();
        textFieldBranch = new javax.swing.JTextField();
        textFieldSem = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Book Information");
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel1.setBackground(new java.awt.Color(45, 45, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(958, 693));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("NUMBER OF COPIES");
        jLabel6.setPreferredSize(new java.awt.Dimension(175, 35));

        labelBook.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelBook.setForeground(new java.awt.Color(255, 255, 255));
        labelBook.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelBook.setText("BOOK");
        labelBook.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldBook.setBackground(new java.awt.Color(35, 35, 35));
        textFieldBook.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBook.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBook.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBook.setPreferredSize(new java.awt.Dimension(400, 35));
        CLMS.CollegeLibraryManagementSystem a = new CLMS.CollegeLibraryManagementSystem();
        CLMS.CollegeLibraryManagementSystem.RoundedBorder c = a.new RoundedBorder(5);
        textFieldBook.setBorder(c);

        textFieldNumOfCopies.setBackground(new java.awt.Color(35, 35, 35));
        textFieldNumOfCopies.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldNumOfCopies.setForeground(new java.awt.Color(153, 255, 153));
        textFieldNumOfCopies.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldNumOfCopies.setBorder(textFieldBook.getBorder());
        textFieldNumOfCopies.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("AUTHOR");
        jLabel3.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldAuthor.setBackground(new java.awt.Color(35, 35, 35));
        textFieldAuthor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldAuthor.setForeground(new java.awt.Color(153, 255, 153));
        textFieldAuthor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldAuthor.setBorder(textFieldBook.getBorder());
        textFieldAuthor.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("PUBLISH YEAR");
        jLabel4.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldPublishYear.setBackground(new java.awt.Color(35, 35, 35));
        textFieldPublishYear.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldPublishYear.setForeground(new java.awt.Color(153, 255, 153));
        textFieldPublishYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldPublishYear.setBorder(textFieldBook.getBorder());
        textFieldPublishYear.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("PUBLISHER");
        jLabel5.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldPublisher.setBackground(new java.awt.Color(35, 35, 35));
        textFieldPublisher.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldPublisher.setForeground(new java.awt.Color(153, 255, 153));
        textFieldPublisher.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldPublisher.setBorder(textFieldBook.getBorder());
        textFieldPublisher.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("PRICE");
        jLabel7.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldPrice.setBackground(new java.awt.Color(35, 35, 35));
        textFieldPrice.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldPrice.setForeground(new java.awt.Color(153, 255, 153));
        textFieldPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldPrice.setBorder(textFieldBook.getBorder());
        textFieldPrice.setPreferredSize(new java.awt.Dimension(400, 35));

        buttonInsert.setBackground(new java.awt.Color(61, 80, 255));
        buttonInsert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonInsert.setForeground(new java.awt.Color(255, 255, 255));
        buttonInsert.setText("Add new Book Information");
        buttonInsert.setPreferredSize(new java.awt.Dimension(125, 35));
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = a.new RoundedBorder(10);
        buttonInsert.setBorder(d);

        buttonInsert.setMnemonic(java.awt.event.KeyEvent.VK_I);
        buttonInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInsertActionPerformed(evt);
            }
        });

        buttonModify.setBackground(new java.awt.Color(61, 80, 255));
        buttonModify.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        buttonModify.setForeground(new java.awt.Color(255, 255, 255));
        buttonModify.setText("Modify Book Details");
        buttonModify.setPreferredSize(new java.awt.Dimension(125, 35));
        buttonModify.setBorder(d);

        buttonModify.setMnemonic(java.awt.event.KeyEvent.VK_M);
        buttonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyActionPerformed(evt);
            }
        });
        buttonModify.setVisible(false);

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

        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
        buttonSearch.setForeground(new java.awt.Color(255, 255, 255));
        buttonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/search.png"))); // NOI18N
        buttonSearch.setPreferredSize(new java.awt.Dimension(50, 35));
        buttonSearch.setBorder(d);

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
        buttonClear.setBorder(d);

        buttonClear.setMnemonic(java.awt.event.KeyEvent.VK_C);
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });
        buttonClear.setVisible(false);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("EDITION");
        jLabel8.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldEdition.setBackground(new java.awt.Color(35, 35, 35));
        textFieldEdition.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldEdition.setForeground(new java.awt.Color(153, 255, 153));
        textFieldEdition.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldEdition.setPreferredSize(new java.awt.Dimension(400, 35));
        textFieldEdition.setBorder(c);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("RACK NO");
        jLabel9.setPreferredSize(new java.awt.Dimension(175, 35));

        textFieldRackNo.setBackground(new java.awt.Color(35, 35, 35));
        textFieldRackNo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldRackNo.setForeground(new java.awt.Color(153, 255, 153));
        textFieldRackNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldRackNo.setBorder(textFieldBook.getBorder());
        textFieldRackNo.setPreferredSize(new java.awt.Dimension(400, 35));

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

        comboBoxBook.setBackground(new java.awt.Color(35, 35, 35));
        comboBoxBook.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        comboBoxBook.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxBook.setMaximumRowCount(10);
        comboBoxBook.setFocusable(false);
        comboBoxBook.setPreferredSize(new java.awt.Dimension(400, 35));
        comboBoxBook.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxBookItemStateChanged(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.CustomRenderer cutomRenderer = lms.new CustomRenderer();
        comboBoxBook.setRenderer(cutomRenderer);
        comboBoxBook.setVisible(false);

        labelBook1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelBook1.setForeground(new java.awt.Color(255, 255, 255));
        labelBook1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelBook1.setText("BOOK");
        labelBook1.setPreferredSize(new java.awt.Dimension(175, 35));
        labelBook1.setVisible(false);

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
        labelToast.setBorder(textFieldBook.getBorder());
        labelToast.setOpaque(true);
        labelToast.setPreferredSize(new java.awt.Dimension(555, 35));
        labelToast.setVisible(false);

        combBoxBranch.setBackground(new java.awt.Color(35, 35, 35));
        combBoxBranch.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combBoxBranch.setForeground(new java.awt.Color(255, 255, 255));
        combBoxBranch.setMaximumRowCount(9);
        combBoxBranch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT BRANCH", "AIML", "CHEMISTRY", "CSE", "CIV", "ECE", "EEE", "ME", "PHYSICS", " " }));
        combBoxBranch.setFocusable(false);
        combBoxBranch.setPreferredSize(new java.awt.Dimension(200, 35));
        combBoxBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combBoxBranchItemStateChanged(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem.CustomRenderer customRenderer = lms.new CustomRenderer();
        combBoxBranch.setRenderer(customRenderer);
        combBoxBranch.setVisible(false);

        comboBoxSem.setBackground(new java.awt.Color(35, 35, 35));
        comboBoxSem.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        comboBoxSem.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxSem.setMaximumRowCount(10);
        comboBoxSem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT SEM", "1", "2", "3", "4", "5", "6", "7", "8" }));
        comboBoxSem.setFocusable(false);
        comboBoxSem.setPreferredSize(new java.awt.Dimension(200, 35));
        comboBoxSem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxSemItemStateChanged(evt);
            }
        });
        comboBoxSem.setRenderer(combBoxBranch.getRenderer());
        comboBoxSem.setVisible(false);

        textFieldBranch.setEditable(false);
        textFieldBranch.setBackground(new java.awt.Color(35, 35, 35));
        textFieldBranch.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBranch.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBranch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBranch.setBorder(textFieldBook.getBorder());
        textFieldBranch.setPreferredSize(new java.awt.Dimension(400, 35));

        textFieldSem.setEditable(false);
        textFieldSem.setBackground(new java.awt.Color(35, 35, 35));
        textFieldSem.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldSem.setForeground(new java.awt.Color(153, 255, 153));
        textFieldSem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldSem.setBorder(textFieldBook.getBorder());
        textFieldSem.setPreferredSize(new java.awt.Dimension(400, 35));
        textFieldSem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                textFieldSemMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelBook, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelBook1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxBook, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldPublishYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldNumOfCopies, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBook, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldRackNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonModify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(buttonInsert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textFieldSem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textFieldBranch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combBoxBranch, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxSem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelToast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(202, 202, 202))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(buttonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combBoxBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonModify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelBook1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldEdition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldPublishYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldNumOfCopies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldRackNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelToast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 948, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setHandCursorForButtons() {
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    private void buttonInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInsertActionPerformed
        unHideButtons(false);

        buttonInsert.setVisible(true);
        buttonInsert.setBackground(new java.awt.Color(119, 65, 244));
        buttonInsert.setFocusable(false);

        editableTextFields(true);
        onInsertVisibility(true);

        buttonDone.setVisible(true);
    }//GEN-LAST:event_buttonInsertActionPerformed

    private void buttonDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDoneActionPerformed
        getTextFieldsData();
        try {
            sql = """
                     INSERT INTO BOOK_INFORMATION
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                  """;
            ps = cn.prepareStatement(sql);
            for (int i = 0; i < text.size(); i++) {
                switch (i) {
                    case 0, 4, 6, 7, 8, 9 ->
                        ps.setInt((i + 1), (int) text.get(i));
                    case 1, 2, 3, 5 ->
                        ps.setString((i + 1), (String) text.get(i));
                }
            }
            info = "Book Information has been added successfully.";
            exeUpdate(info);
            onInsertVisibility(false);
            unHideButtons(true);

            buttonInsert.setFocusable(true);
            buttonDone.setVisible(false);
        } catch (SQLException ex) {
        }
    }//GEN-LAST:event_buttonDoneActionPerformed

    private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
        searchByBookName = false;
        comboBoxBook.removeAllItems();

        editableTextFields(false);
        onSearchVisbility();
        searchBook();
        buttonSearch.setBackground(java.awt.Color.GREEN);
    }//GEN-LAST:event_buttonSearchActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        clearBookTextFields();
        textFieldBook.setText(null);
        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
    }//GEN-LAST:event_buttonClearActionPerformed

    private void comboBoxBookItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxBookItemStateChanged
        if (searchByBookName) {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                try {
                    sem = Integer.parseInt(textFieldSem.getText());
                    branch = textFieldBranch.getText();
                    String bookName = comboBoxBook.getSelectedItem().toString();
                    sql = """
                             SELECT AUTHOR, BOOK_EDITION, PUBLISHER, 
                                    PUBLISH_YEAR, PRICE, NUM_OF_COPIES, RACK_NO 
                             FROM   BOOK_INFORMATION
                             WHERE  SEM = ? AND BRANCH = ? AND BOOK_NAME = ?
                          """;
                    ps = cn.prepareStatement(sql);
                    ps.setInt(1, (int) sem);
                    ps.setString(2, (String) branch);
                    ps.setString(3, (String) bookName);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        displayBookInfo();
                    }
                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (comboBoxBook.getSelectedIndex() == 0) {
            buttonModify.setVisible(false);
            for (int i = 3; i < textFields.length; i++) {
                textFields[i].setText(null);
            }
        } else {
            buttonModify.setForeground(java.awt.Color.WHITE);
            buttonModify.setBackground(new java.awt.Color(61, 80, 255));
            buttonModify.setVisible(true);
        }
    }//GEN-LAST:event_comboBoxBookItemStateChanged

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        try {
            getTextFieldsData();
            sql = """
                     UPDATE BOOK_INFORMATION
                     SET    SEM = ?, BRANCH = ?, AUTHOR = ?, BOOK_EDITION = ?, PUBLISHER = ?,
                            PUBLISH_YEAR = ?, PRICE = ?, NUM_OF_COPIES = ?, RACK_NO = ?
                     WHERE  BOOK_NAME = ?
                  """;
            ps = cn.prepareStatement(sql);

            int j = 1;
            for (int i = 0; i < text.size(); i++, j++) {
                switch (i) {
                    case 0 ->
                        ps.setInt((i + 1), (int) text.get(i));
                    case 1 ->
                        ps.setString((i + 1), (String) text.get(i));
                    case 2, 4 ->
                        ps.setString((i + 1), (String) text.get(j));
                    case 3, 5, 6, 7, 8 ->
                        ps.setInt((i + 1), (int) text.get(j));
                    case 9 ->
                        ps.setString(10, comboBoxBook.getSelectedItem().toString());
                }
            }
            info = "Book Information has been updated successfully.";
            exeUpdate(info);
            buttonModify.setForeground(java.awt.Color.WHITE);
            buttonModify.setBackground(new java.awt.Color(61, 80, 255));
            buttonUpdate.setVisible(false);
            editableTextFields(false);
        } catch (SQLException ex) {
            Logger.getLogger(BookInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void comboBoxSemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxSemItemStateChanged
        textFieldSem.setText(comboBoxSem.getSelectedItem().toString());
        if (comboBoxSem.getSelectedItem().toString().equals("SELECT SEM")) {
            textFieldSem.setText(null);
        }
    }//GEN-LAST:event_comboBoxSemItemStateChanged

    private void combBoxBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combBoxBranchItemStateChanged
        textFieldBranch.setText(combBoxBranch.getSelectedItem().toString());
        if (combBoxBranch.getSelectedItem().toString().equals("SELECT BRANCH")) {
            textFieldBranch.setText(null);
        }
    }//GEN-LAST:event_combBoxBranchItemStateChanged

    private void buttonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyActionPerformed
        buttonModify.setForeground(java.awt.Color.BLACK);
        buttonModify.setBackground(java.awt.Color.GREEN);
        buttonUpdate.setVisible(true);
        editableTextFields(true);
    }//GEN-LAST:event_buttonModifyActionPerformed

    private void buttonDoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buttonDoneFocusGained
        textFieldsToUppercase();
    }//GEN-LAST:event_buttonDoneFocusGained

    private void textFieldSemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textFieldSemMouseEntered
        comboBoxSem.setVisible(true);
        combBoxBranch.setVisible(true);
    }//GEN-LAST:event_textFieldSemMouseEntered

    private void buttonUpdateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buttonUpdateFocusGained

    }//GEN-LAST:event_buttonUpdateFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    public javax.swing.JButton buttonDone;
    private javax.swing.JButton buttonInsert;
    private javax.swing.JButton buttonModify;
    private javax.swing.JButton buttonSearch;
    public javax.swing.JButton buttonUpdate;
    public javax.swing.JComboBox<String> combBoxBranch;
    public javax.swing.JComboBox<String> comboBoxBook;
    public javax.swing.JComboBox<String> comboBoxSem;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelBook;
    private javax.swing.JLabel labelBook1;
    private javax.swing.JLabel labelToast;
    private javax.swing.JTextField textFieldAuthor;
    private javax.swing.JTextField textFieldBook;
    private javax.swing.JTextField textFieldBranch;
    private javax.swing.JTextField textFieldEdition;
    private javax.swing.JTextField textFieldNumOfCopies;
    private javax.swing.JTextField textFieldPrice;
    private javax.swing.JTextField textFieldPublishYear;
    private javax.swing.JTextField textFieldPublisher;
    private javax.swing.JTextField textFieldRackNo;
    private javax.swing.JTextField textFieldSem;
    // End of variables declaration//GEN-END:variables
}
