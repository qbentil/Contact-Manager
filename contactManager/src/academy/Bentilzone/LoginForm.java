package academy.Bentilzone;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginForm extends JFrame{
    private JPanel rootPanel;
    private JPanel headPanel;
    private JLabel heading;
    private JPanel bodyPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JCheckBox showCheckBox;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel signUpLink;

    public static int currentUserId;
    private static JFrame frame = new JFrame("Contacts Management System");
    public static JFrame getFrame() {
        return frame;
    }
    public LoginForm(){

        showCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showCheckBox.isSelected()){
                    passwordTextField.setEchoChar((char) 0);
                }else{
                    passwordTextField.setEchoChar('*');
                }
            }
        });

        signUpLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getFrame().setVisible(false);
                SignUpForm.register();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!usernameTextField.getText().equals("")){
                    emptyFields();
                }else if(!String.valueOf(passwordTextField.getPassword()).equals("")){
                    emptyFields();
                }else{
                    System.exit(0);
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validateData()){
                    Connection con = dbConfig.getConnection();
                    PreparedStatement stmt;
                    ResultSet rs;
                    try{
                        stmt = con.prepareStatement("SELECT `username`, `password`, `picture`, `ID` FROM `user` WHERE `username` = ? AND `password` = ?");
                        stmt.setString(1, usernameTextField.getText());
                        stmt.setString(2, String.valueOf(passwordTextField.getPassword()));
                        rs = stmt.executeQuery();
                        if(rs.next()){
                            // get current user id
                            currentUserId = rs.getInt("ID");
//                            ContactsForm page = new ContactsForm();
                            //JOptionPane.showMessageDialog(null, "Login Successful");
                            getFrame().setVisible(false);
                            ContactsForm.run(rs.getBytes(3), rs.getString(1), currentUserId);
                        }else{
                            JOptionPane.showMessageDialog(null, "Invalid username or password");
                        }
                    }catch (SQLException ex){
//                        Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
    }
    public boolean validateData(){
        // firstName - lastName - username and Password field empty
        if(usernameTextField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter your username");
            return false;
        }
        // passwords do not match
        else if(String.valueOf(passwordTextField.getPassword()).equals(""))
        {
            JOptionPane.showMessageDialog(null, "Enter your password");
            return false;
        }else{
            // form passed validation
            return true;
        }
    }
    public void emptyFields(){
        usernameTextField.setText("");
        passwordTextField.setText("");
    }
    public static void main(String[] args) {
        login();
    }
    public static void login(){
        getFrame().setContentPane(new LoginForm().rootPanel);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().pack();
        getFrame().setLocationRelativeTo(null);
        getFrame().setResizable(false);
        getFrame().setVisible(true);
    }

}
