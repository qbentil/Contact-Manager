package academy.Bentilzone;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class myFunction {
    // resize image
    public ImageIcon resizePic(String path, byte[] BLOBpic, int width, int height){
        ImageIcon originalImage;
        if(path != null){
            originalImage = new ImageIcon(path);
        }else{
            originalImage = new ImageIcon(BLOBpic);
        }
        Image resizedImage = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
        return resizedImageIcon;
    }
    public String browseImage(JLabel pictureHolder){
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        String userDir = System.getProperty("user.home");
//        JFileChooser fc = new JFileChooser(userDir +"/Desktop");
        fileChooser.setCurrentDirectory(new File(userDir+"/Desktop"));

        //file extension
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("*.Images", "jpg", "png", "gif");
        fileChooser.addChoosableFileFilter(fileFilter);

        int fileState = fileChooser.showSaveDialog(null);
        // if the user select a file
        if(fileState == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            //imgPath = path;

            // display in the picture label using resized pic
            pictureHolder.setIcon(resizePic(path, null, pictureHolder.getWidth(), pictureHolder.getHeight()));
        }
        // if user cancel action
        else if(fileState == JFileChooser.CANCEL_OPTION){
            System.out.println("No image selected");
        }
        return path;
    }
    public boolean isContactExist(String phone, String userId){
        boolean isExist = false;
        Connection con = dbConfig.getConnection();
        PreparedStatement stmt;
        ResultSet rs;
        try{
            stmt = con.prepareStatement("SELECT * FROM `contact` WHERE `phone` = ? and `user_id` = ?");
            stmt.setString(1, phone);
            stmt.setString(2, userId);
            rs = stmt.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Contact with this number already exist");
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
    public boolean validateEmail(String email){
        boolean validated = true;
        Pattern regExPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\.)]+@[(a-zA-Z)]+\\.[(a-zA-Z)]{2,3}$");
        Matcher regExMatcher = regExPattern.matcher(email);
        if(!regExMatcher.matches()){
           validated = false;
        }
        return validated;
    }
    public boolean validatePhone(String phone){
        Pattern regExPattern = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$");
        Matcher regExMatcher = regExPattern.matcher(phone);
        if(!regExMatcher.matches()){
            return false;
        }
        return true;
    }
    public void errorMessage(String title, String message){
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
    public void successMessage(String title, String message){
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
    public boolean isValidPassword(String password){
        boolean isValid = true;
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        if (password.length() > 15 || password.length() < 8)
        {
            errorMessage("Password Error","Password must be less than 20 and more than 8 characters in length.");
            isValid = false;
        }
        else if (!password.matches(upperCaseChars ))
        {
            errorMessage("Password Error","Password must have atleast one uppercase character");
            isValid = false;
        }
        else if (!password.matches(lowerCaseChars ))
        {
            errorMessage("Password Error","Password must have atleast one lowercase character");
            isValid = false;
        }
        else if (!password.matches(numbers ))
        {
            errorMessage("Password Error","Password must have atleast one number");
            isValid = false;
        }
//        String specialChars = "(.*[@,#,$,%].*$)";
//        if (!password.matches(specialChars ))
//        {
//            System.out.println("Password must have atleast one special character among @#$%");
//            isValid = false;
//        }
        return isValid;
    }

}

