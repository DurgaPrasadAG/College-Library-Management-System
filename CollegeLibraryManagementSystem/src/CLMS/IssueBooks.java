package CLMS;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Durga Prasad A G (https://durgaprasadag.github.io/dp/)
 */
public class IssueBooks extends javax.swing.JInternalFrame {

    CollegeLibraryManagementSystem lms = new CollegeLibraryManagementSystem();
    CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    private Connection cn;
    private Statement st;
    private String sql, error;
    private ResultSet rs;
    private PreparedStatement ps;
    protected String select = "SELECT * FROM ISSUE_BOOKS ORDER BY USN";
    protected JTextField[] textFields;
    protected ArrayList<Object> text = new ArrayList<>();
    String getDate, formattedDate;
    Date unFormattedDate;

    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String todayDate = "" + formatter.format(date);

    /**
     * Creates new form IssueBooks
     */
    public IssueBooks() {
        initComponents();
        defaultLookAndFeel();
        connectToDatabase(select);
        addTextFields();
    }

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

    private void showToast(String s) {
        toastLabel.setVisible(true);
        toastLabel.setText(s);
        timer.invisible(toastLabel);
    }

    private void addTextFields() {
        textFields = new JTextField[]{
            textFieldBookId1, textFieldIssueDate1, textFieldBookId2, textFieldIssueDate2,
            textFieldBookId3, textFieldIssueDate3, textFieldBookId4, textFieldIssueDate4,
            textFieldBookId5, textFieldIssueDate5, textFieldBookId6, textFieldIssueDate6
        };
    }

    /* Search usn in the database and fetch issued books. */
    private void display() throws SQLException, ParseException {
        for (int i = 0; i < textFields.length; i++) {
            if (i % 2 == 0) {
                textFields[i].setText(rs.getString((i + 2)));
                if (textFields[i].getText().length() > 0) {
                    textFields[i].setEditable(false);
                    textFields[i].setFocusable(false);
                    textFields[i + 1].setEditable(false);
                }
            } else {
                getDate = rs.getString((i + 2));
                if (getDate == null) {
                    textFields[i].setText(rs.getString((i + 2)));
                } else {
                    unFormattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
                            .parse(getDate);
                    formattedDate = new SimpleDateFormat("dd-MMM-yyyy")
                            .format(unFormattedDate);
                    textFields[i].setText(formattedDate);
                }
            }
        }
    }

    private void getIssuedBooks() {
        try {
            String usn = (textFieldUsn.getText());
            sql = """
                     SELECT * FROM ISSUE_BOOKS
                     WHERE USN = ?
                  """;
            ps = cn.prepareStatement(sql);
            ps.setString(1, (String) usn);
            rs = ps.executeQuery();
            while (rs.next()) {
                display();
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(BookInformation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(IssueBooks.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editableTextFields() {
        for (JTextField textField : textFields) {
                textField.setEditable(true);
                textField.setFocusable(true);
            }
            buttonIssueBooks.setVisible(false);
            buttonReVerify.setVisible(true);
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
        jLabel3 = new javax.swing.JLabel();
        textFieldUsn = new javax.swing.JTextField();
        textFieldBookId1 = new javax.swing.JTextField();
        textFieldIssueDate1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textFieldBookId2 = new javax.swing.JTextField();
        textFieldIssueDate2 = new javax.swing.JTextField();
        textFieldBookId3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textFieldIssueDate3 = new javax.swing.JTextField();
        textFieldIssueDate4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textFieldBookId4 = new javax.swing.JTextField();
        textFieldIssueDate5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        textFieldBookId5 = new javax.swing.JTextField();
        textFieldIssueDate6 = new javax.swing.JTextField();
        textFieldBookId6 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        buttonIssueBooks = new javax.swing.JButton();
        buttonVerify = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        toastLabel = new javax.swing.JLabel();
        buttonReVerify = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Issue Books");
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel1.setBackground(new java.awt.Color(45, 45, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(958, 693));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("USN");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 35));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Book ID");
        jLabel3.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldUsn.setEditable(false);
        textFieldUsn.setBackground(new java.awt.Color(25, 25, 25));
        textFieldUsn.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldUsn.setForeground(new java.awt.Color(153, 255, 153));
        textFieldUsn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldUsn.setPreferredSize(new java.awt.Dimension(300, 35));
        textFieldUsn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldUsnFocusGained(evt);
            }
        });
        CLMS.CollegeLibraryManagementSystem a = new CLMS.CollegeLibraryManagementSystem();
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = a.new RoundedBorder(5);
        textFieldUsn.setBorder(d);

        textFieldBookId1.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId1.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId1.setBorder(textFieldUsn.getBorder());
        textFieldBookId1.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId1KeyTyped(evt);
            }
        });

        textFieldIssueDate1.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate1.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate1.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate1.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldIssueDate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldIssueDate1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("2");
        jLabel5.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldBookId2.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId2.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId2.setBorder(textFieldUsn.getBorder());
        textFieldBookId2.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId2KeyTyped(evt);
            }
        });

        textFieldIssueDate2.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate2.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate2.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate2.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldIssueDate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldIssueDate2ActionPerformed(evt);
            }
        });

        textFieldBookId3.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId3.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId3.setBorder(textFieldUsn.getBorder());
        textFieldBookId3.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId3KeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("3");
        jLabel6.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldIssueDate3.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate3.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate3.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate3.setPreferredSize(new java.awt.Dimension(125, 35));

        textFieldIssueDate4.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate4.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate4.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate4.setPreferredSize(new java.awt.Dimension(125, 35));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("4");
        jLabel8.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldBookId4.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId4.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId4.setBorder(textFieldUsn.getBorder());
        textFieldBookId4.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId4KeyTyped(evt);
            }
        });

        textFieldIssueDate5.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate5.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate5.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate5.setPreferredSize(new java.awt.Dimension(125, 35));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("5");
        jLabel11.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldBookId5.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId5.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId5.setBorder(textFieldUsn.getBorder());
        textFieldBookId5.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId5KeyTyped(evt);
            }
        });

        textFieldIssueDate6.setBackground(new java.awt.Color(25, 25, 25));
        textFieldIssueDate6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldIssueDate6.setForeground(new java.awt.Color(153, 255, 153));
        textFieldIssueDate6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldIssueDate6.setBorder(textFieldUsn.getBorder());
        textFieldIssueDate6.setPreferredSize(new java.awt.Dimension(125, 35));

        textFieldBookId6.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId6.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldBookId6.setBorder(textFieldUsn.getBorder());
        textFieldBookId6.setPreferredSize(new java.awt.Dimension(125, 35));
        textFieldBookId6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldBookId6KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("6");
        jLabel13.setPreferredSize(new java.awt.Dimension(75, 35));

        buttonIssueBooks.setBackground(new java.awt.Color(61, 80, 255));
        buttonIssueBooks.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonIssueBooks.setForeground(new java.awt.Color(255, 255, 255));
        buttonIssueBooks.setText("I verified. Now Issue Books");
        buttonIssueBooks.setPreferredSize(new java.awt.Dimension(535, 35));
        CLMS.CollegeLibraryManagementSystem.RoundedBorder c = a.new RoundedBorder(10);
        buttonIssueBooks.setBorder(c);

        buttonIssueBooks.setMnemonic(java.awt.event.KeyEvent.VK_I);
        buttonIssueBooks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonIssueBooksActionPerformed(evt);
            }
        });
        buttonIssueBooks.setVisible(false);

        buttonVerify.setBackground(new java.awt.Color(61, 80, 255));
        buttonVerify.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonVerify.setForeground(new java.awt.Color(255, 255, 255));
        buttonVerify.setText("Verify");
        buttonVerify.setToolTipText("You can't modify book Id and issue date once the book has been issued. So please verify the above fields.");
        buttonVerify.setBorder(buttonIssueBooks.getBorder());
        buttonVerify.setPreferredSize(new java.awt.Dimension(125, 35));
        buttonVerify.setBorder(c);

        buttonVerify.setMnemonic(java.awt.event.KeyEvent.VK_M);
        buttonVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVerifyActionPerformed(evt);
            }
        });
        buttonVerify.setVisible(true);

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Issue Date");
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 35));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("1");
        jLabel15.setPreferredSize(new java.awt.Dimension(75, 35));

        toastLabel.setBackground(new java.awt.Color(119, 65, 244));
        toastLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        toastLabel.setForeground(new java.awt.Color(255, 255, 255));
        toastLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        toastLabel.setBorder(buttonIssueBooks.getBorder());
        toastLabel.setOpaque(true);
        toastLabel.setPreferredSize(new java.awt.Dimension(555, 35));
        toastLabel.setVisible(false);

        buttonReVerify.setBackground(new java.awt.Color(61, 80, 255));
        buttonReVerify.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonReVerify.setForeground(new java.awt.Color(255, 255, 255));
        buttonReVerify.setText("ReVerify");
        buttonReVerify.setPreferredSize(new java.awt.Dimension(535, 35));
        buttonReVerify.setBorder(c);

        buttonIssueBooks.setMnemonic(java.awt.event.KeyEvent.VK_I);
        buttonReVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReVerifyActionPerformed(evt);
            }
        });
        buttonReVerify.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldBookId5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldIssueDate5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldBookId4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldIssueDate4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldBookId3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldIssueDate3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldBookId2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(textFieldIssueDate2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldBookId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(textFieldIssueDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(textFieldBookId6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(textFieldIssueDate6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(buttonIssueBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonReVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textFieldUsn, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(toastLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(224, 224, 224))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldUsn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldBookId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIssueDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIssueDate2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIssueDate3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIssueDate4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldIssueDate5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldIssueDate6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonVerify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonIssueBooks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonReVerify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toastLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 948, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textFieldIssueDate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldIssueDate2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldIssueDate2ActionPerformed

    private void buttonIssueBooksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonIssueBooksActionPerformed
        try {
            // Get TextFields Data and add it to text array list
            for (int i = 0; i < textFields.length; i++) {
                text.add(i, textFields[i].getText());
            }

            sql = """
                     UPDATE ISSUE_BOOKS
                     SET    BOOK_ID_1 = ?, ISSUE_DATE_1 = ?, BOOK_ID_2 = ?, ISSUE_DATE_2 = ?,
                            BOOK_ID_3 = ?, ISSUE_DATE_3 = ?, BOOK_ID_4 = ?, ISSUE_DATE_4 = ?,
                            BOOK_ID_5 = ?, ISSUE_DATE_5 = ?, BOOK_ID_6 = ?, ISSUE_DATE_6 = ?
                     WHERE  USN = ?
                  """;
            ps = cn.prepareStatement(sql);

            for (int i = 0; i < textFields.length; i++) {
                if (i % 2 == 0) {
                    if (text.get(i).toString().isEmpty()) {
                        ps.setNull((i + 1), java.sql.Types.INTEGER);
                    } else {
                        ps.setInt((i + 1), Integer.parseInt((String) text.get(i)));
                    }
                } else {
                    if (text.get(i).toString().isEmpty()) {
                        ps.setNull((i + 1), java.sql.Types.DATE);
                    } else {
                        ps.setString((i + 1), (String) text.get(i));
                    }
                }
            }
            ps.setString(13, textFieldUsn.getText());
            ps.executeUpdate();
            cn.commit();

            buttonIssueBooks.setFocusable(false);
            buttonIssueBooks.setText("Data updated successfully.");
            buttonIssueBooks.setBackground(Color.GREEN);
            buttonIssueBooks.setForeground(Color.BLACK);

            toastLabel.setVisible(true);
            toastLabel.setText("Now you can Issue books.");
        } catch (SQLIntegrityConstraintViolationException ex) {
            showToast("BOOK ID does not exist in the database.");
            editableTextFields();
        } catch (java.sql.SQLDataException ex) {
            showToast("Date should be in DD-MMM-YYYY format.");
            editableTextFields();
        } catch (SQLException ex) {
            Logger.getLogger(IssueBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonIssueBooksActionPerformed

    private void textFieldIssueDate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldIssueDate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldIssueDate1ActionPerformed

    private void textFieldBookId1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId1KeyTyped
        textFieldIssueDate1.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId1KeyTyped

    private void textFieldBookId2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId2KeyTyped
        textFieldIssueDate2.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId2KeyTyped

    private void textFieldBookId3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId3KeyTyped
        textFieldIssueDate3.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId3KeyTyped

    private void textFieldBookId4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId4KeyTyped
        textFieldIssueDate4.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId4KeyTyped

    private void textFieldBookId5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId5KeyTyped
        textFieldIssueDate5.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId5KeyTyped

    private void textFieldBookId6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldBookId6KeyTyped
        textFieldIssueDate6.setText(todayDate);
    }//GEN-LAST:event_textFieldBookId6KeyTyped

    private void textFieldUsnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldUsnFocusGained
        getIssuedBooks();
        textFieldUsn.setFocusable(false);
    }//GEN-LAST:event_textFieldUsnFocusGained

    private void buttonVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVerifyActionPerformed
        try {
            for (int i = 0; i < textFields.length; i++) {
                textFields[i].setEditable(false);
                textFields[i].setFocusable(false);

                if (i % 2 == 0) {
                    if (textFields[i].getText().isEmpty()
                            && textFields[i + 1].getText().length() > 0) {
                        textFields[i + 1].setText(null);
                    } else if (textFields[i].getText().length() > 0
                            /*Check and throw if the book ID is non number.*/
                            && !textFields[i].getText().matches("\\d+$") // \d = Regex [0-9]
                            && textFields[i + 1].getText().length() > 0) {
                        textFields[i].setEditable(true);
                        textFields[i].setFocusable(true);
                        error = "Please enter Book Id as digits only";
                        throw new NumberFormatException();
                    }
                }
            }
            buttonVerify.setVisible(false);
            buttonIssueBooks.setVisible(true);
        } catch (NumberFormatException ex) {
            showToast(error);
        }
    }//GEN-LAST:event_buttonVerifyActionPerformed

    private void buttonReVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReVerifyActionPerformed
        buttonReVerify.setVisible(false);
        buttonVerify.setVisible(true);
        buttonVerifyActionPerformed(evt);
    }//GEN-LAST:event_buttonReVerifyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonIssueBooks;
    private javax.swing.JButton buttonReVerify;
    private javax.swing.JButton buttonVerify;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField textFieldBookId1;
    private javax.swing.JTextField textFieldBookId2;
    private javax.swing.JTextField textFieldBookId3;
    private javax.swing.JTextField textFieldBookId4;
    private javax.swing.JTextField textFieldBookId5;
    private javax.swing.JTextField textFieldBookId6;
    private javax.swing.JTextField textFieldIssueDate1;
    private javax.swing.JTextField textFieldIssueDate2;
    private javax.swing.JTextField textFieldIssueDate3;
    private javax.swing.JTextField textFieldIssueDate4;
    private javax.swing.JTextField textFieldIssueDate5;
    private javax.swing.JTextField textFieldIssueDate6;
    public javax.swing.JTextField textFieldUsn;
    private javax.swing.JLabel toastLabel;
    // End of variables declaration//GEN-END:variables
}
