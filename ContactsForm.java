package academy.Bentilzone;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactsForm extends JFrame{
    private static ContactsForm frame = new ContactsForm();
    private JPanel headPanel;
    private JLabel heading;
    private JPanel rootPanel;
    private JLabel userPic;
    private JLabel username;
    private JPanel bodyPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField phoneTextField;
    private JTextField emailTextField;
    private JComboBox groupComboBox;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel groupLabel;
    private JLabel profilePictureHolder;
    private JTextArea addressTextArea;
    private JLabel firstNameLabel;
    private JLabel profilePicLabel;
    private JLabel addressLabel;
    private JButton browseButton;
    private JScrollPane scroll;
    private JPanel actionsPanel;
    private JButton deleteButton;
    private JButton addButton;
    private JButton updateButton;
    private JTable contactsTable;
    private JButton nextButton;
    private JButton firstButton;
    private JButton lastButton;
    private JButton previousButton;
    private JPanel tableControlsPanel;
    private JPanel showContacts;
    private JLabel logout;
    private JLabel userid;
    private static int currentUserId = LoginForm.currentUserId;
    private int pos = 0;
    String imgPath = null;
    Integer selectedContactId = null;
    //custom classes
    myFunction function = new myFunction();
    ContactQuery query = new ContactQuery();

    //App constructor
    public ContactsForm() {
        Icon logoutIcon = function.resizePic("C:\\Users\\User\\masterClass\\icons\\logout.png", null, 45, 45);
        logout.setIcon(logoutIcon);
        addressTextArea.setAutoscrolls(true);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contactsTable.setShowGrid(true);
        contactsTable.setGridColor(Color.lightGray);
        JTableHeader th = contactsTable.getTableHeader();
        th.setForeground(contactsTable.getSelectionBackground());
        th.setFont(new Font("Tahoma",Font.PLAIN, 14 ));
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imgPath = function.browseImage(profilePictureHolder);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validateData()){
                    String firstName = firstNameTextField.getText();
                    String lastName = lastNameTextField.getText();
                    String group = groupComboBox.getSelectedItem().toString();
                    String phone = phoneTextField.getText();
                    String email = emailTextField.getText();
                    String address = addressTextArea.getText();
                    byte[] img = null;
                    try {
                        Path path = Paths.get(imgPath);
                        img = Files.readAllBytes(path);
                    } catch (FileNotFoundException ex) {
//                        Logger.getLogger(ContactsForm.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    } catch (IOException ex) {
//                        Logger.getLogger(ContactsForm.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    // Creating contact
                    Contact contact = new Contact(null, firstName, lastName, group, phone, email, address, img, currentUserId);
                    if(query.insertContact(contact)){
                        emptyFields();
                        populateTable();

                    }
                }
            }
        });
        populateTable();
        contactsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = contactsTable.getSelectedRow();
                selectedContactId = (int) contactsTable.getValueAt(rowIndex, 0);
                firstNameTextField.setText(contactsTable.getValueAt(rowIndex, 1).toString());
                lastNameTextField.setText(contactsTable.getValueAt(rowIndex, 2).toString());
                groupComboBox.setSelectedItem(contactsTable.getValueAt(rowIndex, 3));
                phoneTextField.setText(contactsTable.getValueAt(rowIndex, 4).toString());
                emailTextField.setText(contactsTable.getValueAt(rowIndex, 5).toString());
                addressTextArea.setText(contactsTable.getValueAt(rowIndex, 6).toString());
               // retrieving JLabel from cell
                JLabel imageLabel = (JLabel) contactsTable.getValueAt(rowIndex, 7);
                // retrieving image from JLabel
                ImageIcon imageIcon = (ImageIcon)imageLabel.getIcon();
                Image contactImage = imageIcon.getImage().getScaledInstance(profilePictureHolder.getWidth(), profilePictureHolder.getHeight(), Image.SCALE_SMOOTH);
                profilePictureHolder.setIcon(new ImageIcon(contactImage));
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedContactId != null){
                    if(validateOnUpdate()){
                        String firstName = firstNameTextField.getText();
                        String lastName = lastNameTextField.getText();
                        String group = groupComboBox.getSelectedItem().toString();
                        String phone = phoneTextField.getText();
                        String email = emailTextField.getText();
                        String address = addressTextArea.getText();
                        if(imgPath != null){
                            byte[] img = null;
                            try {
                                Path path = Paths.get(imgPath);
                                img = Files.readAllBytes(path);
                            } catch (FileNotFoundException ex) {
//                                Logger.getLogger(ContactsForm.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                            } catch (IOException ex) {
//                                Logger.getLogger(ContactsForm.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                            }
                            // Creating contact
                            Contact contact = new Contact(selectedContactId, firstName, lastName, group, phone, email, address, img, currentUserId);
                            if(query.updateContact(contact, true)){
                                emptyFields();
                                populateTable();
                            }
                        }else{
                            // Creating contact
                            Contact contact = new Contact(selectedContactId, firstName, lastName, group, phone, email, address, null, currentUserId);
                            ContactQuery query = new ContactQuery();
                            if(query.updateContact(contact, false)){
                                emptyFields();
                                populateTable();
                            }
                        }
                    }
                }else {
                    function.errorMessage("Update Contact Error", "Select contact from table to update.");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedContactId != null){
                    query.deleteContact(selectedContactId);
                }else {
                    function.errorMessage("Delete Contact Error", "Select contact from table to delete.");
                }
                populateTable();
            }
        });
        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showData(0);

            }
        });
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pos = contactsTable.getRowCount()-1;
                showData(pos);
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pos > 0){
                    pos--;
                }else {
                    pos = contactsTable.getRowCount()-1;
                }
                showData(pos);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pos < contactsTable.getRowCount()-1){
                    pos++;
                }else {
                    pos = 0;
                }
                showData(pos);
            }
        });
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                LoginForm.currentUserId = 0;
                frame.setVisible(false);
                LoginForm.login();
            }
        });
    }
    // handling control buttons
    private void showData(int rowIndex){
        selectedContactId = (int) contactsTable.getValueAt(rowIndex, 0);
        firstNameTextField.setText(contactsTable.getValueAt(rowIndex, 1).toString());
        lastNameTextField.setText(contactsTable.getValueAt(rowIndex, 2).toString());
        groupComboBox.setSelectedItem(contactsTable.getValueAt(rowIndex, 3));
        phoneTextField.setText(contactsTable.getValueAt(rowIndex, 4).toString());
        emailTextField.setText(contactsTable.getValueAt(rowIndex, 5).toString());
        addressTextArea.setText(contactsTable.getValueAt(rowIndex, 6).toString());
        // retrieving JLabel from cell
        JLabel imageLabel = (JLabel) contactsTable.getValueAt(rowIndex, 7);
        // retrieving image from JLabel
        ImageIcon imageIcon = (ImageIcon)imageLabel.getIcon();
        Image pic = (Image)imageIcon.getImage().getScaledInstance(profilePictureHolder.getWidth(), profilePictureHolder.getHeight(), Image.SCALE_SMOOTH);
        profilePictureHolder.setIcon(new ImageIcon(pic));
    }
    public void populateTable(){
        ContactQuery cq = new ContactQuery();
        ArrayList<Contact> cList = cq.contactList(currentUserId);
        String[] colNames = {"ID","First Name","Last Name","Group","Phone","Email","Address","Image"};
        Object[][] rows = new Object[cList.size()][8];
        for(int i = 0; i < cList.size(); i++){
            rows[i][0] = cList.get(i).getCid();
            rows[i][1] = cList.get(i).getfName();
            rows[i][2] = cList.get(i).getlName();
            rows[i][3] = cList.get(i).getGroup();
            rows[i][4] = cList.get(i).getPhone();
            rows[i][5] = cList.get(i).getEmail();
            rows[i][6] = cList.get(i).getAddress();
            byte[] img = cList.get(i).getPic();
            JLabel tableImg = new JLabel();
            tableImg.setSize(150, 150);
            ImageIcon image = new ImageIcon(img);
            Image im = image.getImage();
            Image myImage = im.getScaledInstance(tableImg.getWidth(), tableImg.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon newImage = new ImageIcon(myImage);
            tableImg.setIcon(newImage);
            rows[i][7] = tableImg;
//            ImageIcon pic = new ImageIcon(new ImageIcon
//                    (cList.get(i).getPic())
//                    .getImage()
//                    .getScaledInstance(tableImg.getWidth(), tableImg.getHeight(), Image.SCALE_SMOOTH));
//            tableImg.setIcon(pic);
//            rows[i][7] = tableImg;

            DefaultTableModel model = new DefaultTableModel(rows,colNames){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            contactsTable.setModel(model);
            TableColumnModel columns = contactsTable.getColumnModel();
            columns.getColumn(0).setMaxWidth(50);
            contactsTable.setRowHeight(150);
            columns.getColumn(7).setPreferredWidth(150);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            columns.getColumn(0).setCellRenderer(centerRenderer);
            columns.getColumn(1).setCellRenderer(centerRenderer);
            columns.getColumn(2).setCellRenderer(centerRenderer);
            columns.getColumn(3).setCellRenderer(centerRenderer);
            columns.getColumn(4).setCellRenderer(centerRenderer);
            columns.getColumn(5).setCellRenderer(centerRenderer);
            columns.getColumn(6).setCellRenderer(centerRenderer);
            // render image cell
            columns.getColumn(7).setCellRenderer(new myTableCellRenderer());
        }

    }
    public boolean validateData(){
        myFunction function = new myFunction();
        boolean validated = true;
        // firstName - lastName - username and Password field empty
        if(firstNameTextField.getText().equals("") ||lastNameTextField.getText().equals("")||
                groupComboBox.getSelectedItem().equals("")||phoneTextField.getText().equals("") ||
                emailTextField.getText().equals("") || addressTextArea.getText().equals(""))
        {
            function.errorMessage("Empty Fields Detected","Fill out all fields correctly");
            validated =  false;
        }
        else if(!function.validateEmail(emailTextField.getText())){
            function.errorMessage("Invalid Contact Email","Enter a valid contact email address");
            validated = false;
        }
        else if(!function.validatePhone(phoneTextField.getText())){
            function.errorMessage("Invalid Phone Number","Enter a correct mobile number");
            validated = false;
        }
        // profile picture not selected
        else if(imgPath == null){
            JOptionPane.showMessageDialog(null, "No Contact Picture selected!");
            validated =  false;
        }
        return validated;
    }
    public boolean validateOnUpdate(){
        myFunction function = new myFunction();
        boolean validated = true;
        // firstName - lastName - username and Password field empty
        if(firstNameTextField.getText().equals("") ||lastNameTextField.getText().equals("")||
                groupComboBox.getSelectedItem().equals("")||phoneTextField.getText().equals("") ||
                emailTextField.getText().equals("") || addressTextArea.getText().equals(""))
        {
            function.errorMessage("Empty Fields Detected","Fill out all fields correctly");
            validated =  false;
        }
        else if(!function.validateEmail(emailTextField.getText())){
            function.errorMessage("Invalid Contact Email","Enter a valid contact email address");
            validated = false;
        }
        else if(!function.validatePhone(phoneTextField.getText())){
            function.errorMessage("Invalid Phone Number","Enter a correct mobile number");
            validated = false;
        }

        return validated;
    }
    public void emptyFields(){
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
        groupComboBox.setSelectedIndex(0);
        groupComboBox.setSelectedItem(null);
        imgPath = null;
        addressTextArea.setText("");
        profilePictureHolder.setIcon(null);
    }
    public static void run(byte[] blob, String name, int currentUserId){
        frame.setContentPane(frame.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.userPic.setIcon(new myFunction().resizePic(null, blob, frame.userPic.getWidth(), frame.userPic.getHeight()));
        frame.username.setText(name);
        frame.userid.setText(String.valueOf(currentUserId));
    }

    class myTableCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (Component) value;
        }
    }
}
