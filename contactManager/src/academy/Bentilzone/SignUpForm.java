package academy.Bentilzone;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SignUpForm extends JFrame{

    private JPanel rootPanel;
    private JPanel headPanel;
    private JLabel heading;
    private JPanel bodyPanel;
    private JButton cancelButton;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JCheckBox showCheckBox;
    private JLabel loginLink;
    private JTextField firstNAmeTextField;
    private JTextField lastNameTextField;
    private JPasswordField retypePasswordField;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JLabel retypePasswordLabel;
    private JLabel pictureHolder;
    private JButton browseButton;
    private JLabel pictureHolderLabel;
     private static JFrame frame = new JFrame("Contacts Management System");
    public static JFrame getFrame(){
        return frame;
    }
    myFunction function = new myFunction();
    String imgPath = null;

    public SignUpForm() {
        showCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showCheckBox.isSelected()){
                    passwordTextField.setEchoChar((char) 0);
                    retypePasswordField.setEchoChar((char) 0);
                }else{
                    passwordTextField.setEchoChar('*');
                    retypePasswordField.setEchoChar('*');
                }
            }
        });
        // navigating to login page
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                LoginForm.getFrame().setVisible(true);
            }
        });

        // selecting profile picture
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFunction function = new myFunction();
                imgPath = function.browseImage(pictureHolder);
            }
        });
        // creating new user
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validateData()){
                    Connection con = dbConfig.getConnection();
                    PreparedStatement stmt;
                    if(!isUsernameExist(usernameTextField.getText())){
                        try{
                            stmt = con.prepareStatement("INSERT INTO `user`(`fistName`, `lastName`, `username`, `password`, `picture`) VALUES (?,?,?,?,?)");
                            stmt.setString(1, firstNAmeTextField.getText());
                            stmt.setString(2, lastNameTextField.getText());
                            stmt.setString(3, usernameTextField.getText());
                            stmt.setString(4, String.valueOf(passwordTextField.getPassword()));

                            InputStream img = new FileInputStream(new File(imgPath));
                            stmt.setBlob(5, img);

                            // display feedback message
                            if(stmt.executeUpdate() != 0){
                                function.successMessage("Success Message","Account Created successfully" );
                                emptyFields();
                            }else{
                                function.errorMessage("Unknown Error","Sorry something went wrong. Please try again" );
                            }
                        }catch (Exception ex){
//                            Logger.getLogger(SignUpForm.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!firstNAmeTextField.getText().equals("")){
                    emptyFields();
                }else if(!lastNameTextField.getText().equals("")){
                    emptyFields();
                }else if(!usernameTextField.getText().equals("")){
                    emptyFields();
                }else if(!String.valueOf(passwordTextField.getPassword()).equals("")){
                    emptyFields();
                }else if(!String.valueOf(retypePasswordField.getPassword()).equals("")){
                    emptyFields();
                }else if(imgPath != null){
                    emptyFields();
                }else{
                    System.exit(0);
                }
            }
        });
    }
    public boolean isUsernameExist(String username){
        boolean isExist = false;
        Connection con = dbConfig.getConnection();
        PreparedStatement stmt;
        ResultSet rs;
        try{
            stmt = con.prepareStatement("SELECT * FROM `user` WHERE `username` = ?");
            stmt.setString(1, usernameTextField.getText());
            rs = stmt.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Username has been taken already");
                isExist = true;
            }else{
                isExist = false;
            }
        }catch (SQLException ex){
//            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return isExist;
    }
    public boolean validateData(){
        boolean validated = true;
        // firstName - lastName - username and Password field empty
        if(firstNAmeTextField.getText().equals("") ||lastNameTextField.getText().equals("")||
                usernameTextField.getText().equals("")||String.valueOf(passwordTextField.getPassword()).equals("")||
                String.valueOf(retypePasswordField.getPassword()).equals(""))
        {
            function.errorMessage("Empty field detected","Fill out all fields" );
            validated = false;
        }
        // passwords do not match
        else if(!function.isValidPassword(String.valueOf(passwordTextField.getPassword()))){
            validated = false;
        }else if(!String.valueOf(passwordTextField.getPassword()).equals(String.valueOf(retypePasswordField.getPassword())))
        {
            function.errorMessage("Invalid Password","Passwords do not match" );
            validated = false;
        }
        // profile picture not selected
        else if(imgPath == null){
            function.errorMessage("Select Profile","No Profile Picture selected!" );
            validated = false;
        }
        return validated;
    }
    public void emptyFields(){
        firstNAmeTextField.setText("");
        lastNameTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
        retypePasswordField.setText("");
        imgPath = null;
        pictureHolder.setIcon(null);
    }

    public static void register(){
        getFrame().setContentPane(new SignUpForm().rootPanel);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().pack();
        getFrame().setLocationRelativeTo(null);
        getFrame().setResizable(false);
        getFrame().setVisible(true);
    }

}
