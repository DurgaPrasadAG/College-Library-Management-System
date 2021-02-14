package CLMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Durga Prasad A G (dpstar7582) (https://durgaprasadag.github.io/dp/)
 */
public class ReturnBooks extends javax.swing.JInternalFrame {

    CollegeLibraryManagementSystem lms = new CollegeLibraryManagementSystem();
    CollegeLibraryManagementSystem.JLabelTimer timer = lms.new JLabelTimer();

    private Connection cn;
    private Statement st;
    private String sql, error;
    private ResultSet rs;
    private PreparedStatement ps;
    protected String select = "SELECT * FROM ISSUE_BOOKS ORDER BY USN";
    protected JTextField[] textFields;
    protected JButton[] buttons;
    protected ArrayList<Object> text = new ArrayList<>();
    String getDate, formattedDate;
    Date unFormattedDate;

    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String todayDate = "" + formatter.format(date);

    /**
     * Creates new form ReturnBooks
     */
    public ReturnBooks() {
        initComponents();
        connectToDatabase(select);
        defaultLookAndFeel();
        textFieldsAndButtons();
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

    private void defaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(IssueBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void textFieldsAndButtons() {
        textFields = new JTextField[]{
            textFieldBookId1, textFieldBookId2, textFieldBookId3, textFieldBookId4,
            textFieldBookId5, textFieldBookId6
        };

        buttons = new JButton[]{
            buttonReturnBook1, buttonReturnBook2, buttonReturnBook3, buttonReturnBook4,
            buttonReturnBook5, buttonReturnBook6
        };
    }

    private void showToast(String s) {
        toastLabel.setVisible(true);
        toastLabel.setText(s);
        timer.invisible(toastLabel);
    }

    /**
     * Fetches Book ID from the database and display it.
     *
     * @throws SQLException If USN is not found in the database.
     * @throws ParseException If the date format is incorrect.
     */
    private void display() throws SQLException, ParseException {
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText(rs.getString((i + 1)));
            if (textFields[i].getText().length() > 0) {
                buttons[i].setVisible(true);
            }
        }
    }

    /**
     * Searches for the given USN in the database.
     */
    private void searchUsn() {
        try {
            String usn = (textFieldUsn.getText());
            sql = """
                  SELECT BOOK_ID_1, BOOK_ID_2, BOOK_ID_3, BOOK_ID_4, BOOK_ID_5, BOOK_ID_6
                  FROM ISSUE_BOOKS
                  WHERE  USN = ?
                  """;
            ps = cn.prepareStatement(sql);
            ps.setString(1, (String) usn);
            rs = ps.executeQuery();
            rs.next();
            display();
        } catch (SQLException ex) {
            showToast("USN not found in database.");
            buttonSearch.setBackground(java.awt.Color.RED);
        } catch (ParseException ex) {
            showToast("Please use Date format : DD-MMM-YYYY only");
        }
    }

    private void exeUpdate(int i, JTextField field, JButton btn) {
        try {
            sql = "UPDATE ISSUE_BOOKS SET BOOK_ID_" + i + " = ?, ISSUE_DATE_" + i + " = ?"
                    + " WHERE USN = ?";

            ps = cn.prepareStatement(sql);
            ps.setNull(1, java.sql.Types.INTEGER);
            ps.setNull(2, java.sql.Types.DATE);
            ps.setString(3, textFieldUsn.getText());
            ps.executeUpdate();
            cn.commit();
            field.setText(null);
            btn.setVisible(false);
            showToast("Data updated successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(ReturnBooks.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel11 = new javax.swing.JLabel();
        textFieldBookId5 = new javax.swing.JTextField();
        textFieldBookId6 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textFieldBookId2 = new javax.swing.JTextField();
        textFieldBookId3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textFieldUsn = new javax.swing.JTextField();
        textFieldBookId1 = new javax.swing.JTextField();
        textFieldBookId4 = new javax.swing.JTextField();
        buttonSearch = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        toastLabel = new javax.swing.JLabel();
        buttonReturnBook1 = new javax.swing.JButton();
        buttonReturnBook2 = new javax.swing.JButton();
        buttonReturnBook3 = new javax.swing.JButton();
        buttonReturnBook4 = new javax.swing.JButton();
        buttonReturnBook5 = new javax.swing.JButton();
        buttonReturnBook6 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Return Books");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(960, 695));

        jPanel1.setBackground(new java.awt.Color(45, 45, 45));
        jPanel1.setPreferredSize(new java.awt.Dimension(958, 693));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("5");
        jLabel11.setPreferredSize(new java.awt.Dimension(75, 35));

        textFieldBookId5.setEditable(false);
        textFieldBookId5.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId5.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId5.setPreferredSize(new java.awt.Dimension(150, 35));
        CLMS.CollegeLibraryManagementSystem a = new CLMS.CollegeLibraryManagementSystem();
        CLMS.CollegeLibraryManagementSystem.RoundedBorder d = a.new RoundedBorder(5);
        textFieldBookId5.setBorder(d);

        textFieldBookId6.setEditable(false);
        textFieldBookId6.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId6.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId6.setBorder(textFieldBookId5.getBorder());
        textFieldBookId6.setPreferredSize(new java.awt.Dimension(150, 35));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("6");
        jLabel13.setPreferredSize(new java.awt.Dimension(75, 35));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("1");
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 35));

        textFieldBookId2.setEditable(false);
        textFieldBookId2.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId2.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId2.setBorder(textFieldBookId5.getBorder());
        textFieldBookId2.setPreferredSize(new java.awt.Dimension(150, 35));

        textFieldBookId3.setEditable(false);
        textFieldBookId3.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId3.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId3.setBorder(textFieldBookId5.getBorder());
        textFieldBookId3.setPreferredSize(new java.awt.Dimension(150, 35));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("3");
        jLabel6.setPreferredSize(new java.awt.Dimension(75, 35));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("4");
        jLabel8.setPreferredSize(new java.awt.Dimension(75, 35));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USN");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 35));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Book ID");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 35));

        textFieldUsn.setBackground(new java.awt.Color(25, 25, 25));
        textFieldUsn.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldUsn.setForeground(new java.awt.Color(153, 255, 153));
        textFieldUsn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldUsn.setText("3BR");
        textFieldUsn.setBorder(textFieldBookId5.getBorder());
        textFieldUsn.setPreferredSize(new java.awt.Dimension(150, 35));
        textFieldUsn.setBorder(d);
        textFieldUsn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldUsnKeyReleased(evt);
            }
        });

        textFieldBookId1.setEditable(false);
        textFieldBookId1.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId1.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId1.setBorder(textFieldBookId5.getBorder());
        textFieldBookId1.setPreferredSize(new java.awt.Dimension(150, 35));

        textFieldBookId4.setEditable(false);
        textFieldBookId4.setBackground(new java.awt.Color(25, 25, 25));
        textFieldBookId4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        textFieldBookId4.setForeground(new java.awt.Color(153, 255, 153));
        textFieldBookId4.setBorder(textFieldBookId5.getBorder());
        textFieldBookId4.setPreferredSize(new java.awt.Dimension(150, 35));

        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
        buttonSearch.setForeground(new java.awt.Color(255, 255, 255));
        buttonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/search.png"))); // NOI18N
        buttonSearch.setToolTipText("Search");
        buttonSearch.setPreferredSize(new java.awt.Dimension(50, 35));
        CollegeLibraryManagementSystem.RoundedBorder c = a.new RoundedBorder(10);
        buttonSearch.setBorder(c);
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed(evt);
            }
        });
        buttonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);

        buttonClear.setBackground(new java.awt.Color(61, 80, 255));
        buttonClear.setForeground(new java.awt.Color(255, 255, 255));
        buttonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CLMS/icon/clear.png"))); // NOI18N
        buttonClear.setToolTipText("Clear");
        buttonClear.setPreferredSize(new java.awt.Dimension(50, 35));
        buttonClear.setBorder(c);
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });
        buttonClear.setMnemonic(java.awt.event.KeyEvent.VK_C);

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("2");
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 35));

        toastLabel.setBackground(new java.awt.Color(119, 65, 244));
        toastLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        toastLabel.setForeground(new java.awt.Color(255, 255, 255));
        toastLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        toastLabel.setOpaque(true);
        toastLabel.setPreferredSize(new java.awt.Dimension(555, 35));
        toastLabel.setVisible(false);
        toastLabel.setBorder(c);

        buttonReturnBook1.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook1.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook1.setText("Return");
        buttonReturnBook1.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook1.setBorder(c);
        buttonReturnBook1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook1ActionPerformed(evt);
            }
        });
        buttonReturnBook1.setVisible(false);

        buttonReturnBook2.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook2.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook2.setText("Return");
        buttonReturnBook2.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook2.setBorder(c);
        buttonReturnBook2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook2ActionPerformed(evt);
            }
        });
        buttonReturnBook2.setVisible(false);

        buttonReturnBook3.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook3.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook3.setText("Return");
        buttonReturnBook3.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook3.setBorder(c);
        buttonReturnBook3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook3ActionPerformed(evt);
            }
        });
        buttonReturnBook3.setVisible(false);

        buttonReturnBook4.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook4.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook4.setText("Return");
        buttonReturnBook4.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook4.setBorder(c);
        buttonReturnBook4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook4ActionPerformed(evt);
            }
        });
        buttonReturnBook4.setVisible(false);

        buttonReturnBook5.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook5.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook5.setText("Return");
        buttonReturnBook5.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook5.setBorder(c);
        buttonReturnBook5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook5ActionPerformed(evt);
            }
        });
        buttonReturnBook5.setVisible(false);

        buttonReturnBook6.setBackground(new java.awt.Color(61, 80, 255));
        buttonReturnBook6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        buttonReturnBook6.setForeground(new java.awt.Color(255, 255, 255));
        buttonReturnBook6.setText("Return");
        buttonReturnBook6.setPreferredSize(new java.awt.Dimension(118, 35));
        buttonReturnBook6.setBorder(c);
        buttonReturnBook6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReturnBook6ActionPerformed(evt);
            }
        });
        buttonReturnBook6.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(243, 243, 243)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textFieldBookId1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonReturnBook1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(textFieldBookId6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buttonReturnBook6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textFieldUsn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(textFieldBookId5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(textFieldBookId2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(textFieldBookId3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(textFieldBookId4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(buttonReturnBook4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonReturnBook2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonReturnBook3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonReturnBook5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addComponent(toastLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldUsn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldBookId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonReturnBook1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldBookId2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonReturnBook2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonReturnBook3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReturnBook4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReturnBook5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldBookId5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldBookId6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonReturnBook6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(toastLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 948, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, 673, 673, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        buttonSearch.setBackground(new java.awt.Color(61, 80, 255));
        textFieldUsn.setText("3BR");

        for (int i = 0; i < 6; i++) {
            textFields[i].setText(null);
            buttons[i].setVisible(false);
        }
    }//GEN-LAST:event_buttonClearActionPerformed

    private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
        if (textFieldUsn.getText().length() == 10) {
            buttonSearch.setBackground(java.awt.Color.GREEN);
            searchUsn();
        } else {
            showToast("USN should have 10 characters.");
        }
    }//GEN-LAST:event_buttonSearchActionPerformed

    private void textFieldUsnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldUsnKeyReleased
        textFieldUsn.setText(textFieldUsn.getText().toUpperCase());
    }//GEN-LAST:event_textFieldUsnKeyReleased

    private void buttonReturnBook1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook1ActionPerformed
        exeUpdate(1, textFieldBookId1, buttonReturnBook1);
    }//GEN-LAST:event_buttonReturnBook1ActionPerformed

    private void buttonReturnBook2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook2ActionPerformed
        exeUpdate(2, textFieldBookId2, buttonReturnBook2);
    }//GEN-LAST:event_buttonReturnBook2ActionPerformed

    private void buttonReturnBook3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook3ActionPerformed
        exeUpdate(3, textFieldBookId3, buttonReturnBook3);
    }//GEN-LAST:event_buttonReturnBook3ActionPerformed

    private void buttonReturnBook4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook4ActionPerformed
        exeUpdate(4, textFieldBookId4, buttonReturnBook4);
    }//GEN-LAST:event_buttonReturnBook4ActionPerformed

    private void buttonReturnBook5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook5ActionPerformed
        exeUpdate(5, textFieldBookId5, buttonReturnBook5);
    }//GEN-LAST:event_buttonReturnBook5ActionPerformed

    private void buttonReturnBook6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReturnBook6ActionPerformed
        exeUpdate(6, textFieldBookId6, buttonReturnBook6);
    }//GEN-LAST:event_buttonReturnBook6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonReturnBook1;
    private javax.swing.JButton buttonReturnBook2;
    private javax.swing.JButton buttonReturnBook3;
    private javax.swing.JButton buttonReturnBook4;
    private javax.swing.JButton buttonReturnBook5;
    private javax.swing.JButton buttonReturnBook6;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTextField textFieldUsn;
    private javax.swing.JLabel toastLabel;
    // End of variables declaration//GEN-END:variables
}
