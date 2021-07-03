package academy.Bentilzone;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactQuery {
    public boolean insertContact(Contact contact){
        boolean inserted = false;
        myFunction function = new myFunction();
        Connection con = dbConfig.getConnection();
        PreparedStatement stmt;

        if (!function.isContactExist(contact.getPhone(), String.valueOf(contact.getUid()))){
            try {
                stmt = con.prepareStatement("INSERT INTO `contact`(`firstName`, `lastName`, `groupc`, `phone`, `email`, `address`, `picture`, `user_id`) VALUES (?,?,?,?,?,?,?,?)");
                stmt.setString(1, contact.getfName());
                stmt.setString(2, contact.getlName());
                stmt.setString(3, contact.getGroup());
                stmt.setString(4, contact.getPhone());
                stmt.setString(5, contact.getEmail());
                stmt.setString(6, contact.getAddress());
                stmt.setBytes(7, contact.getPic());
                stmt.setInt(8, contact.getUid());

                if(stmt.executeUpdate() != 0){
                    inserted = true;
                    JOptionPane.showMessageDialog(null, "Contact added successfully");
                }else{
                    inserted = false;
                    JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                }
            } catch (SQLException ex) {
//                Logger.getLogger(ContactQuery.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        return inserted;
    }
    public boolean updateContact(Contact contact, boolean hasImage){
        boolean updated = true;
        myFunction function = new myFunction();
        Connection con = dbConfig.getConnection();
        PreparedStatement stmt;
        String sqlQuery = "";
        if(hasImage == true){
            sqlQuery ="UPDATE `contact` SET `firstName`=?,`lastName`=?,`groupc`=?,`phone`=?,`email`=?,`address`=?,`picture`=? WHERE ID = ?";
            try {
                stmt = con.prepareStatement(sqlQuery);
                stmt.setString(1, contact.getfName());
                stmt.setString(2, contact.getlName());
                stmt.setString(3, contact.getGroup());
                stmt.setString(4, contact.getPhone());
                stmt.setString(5, contact.getEmail());
                stmt.setString(6, contact.getAddress());
                stmt.setBytes(7, contact.getPic());
                stmt.setInt(8, contact.getCid());

                if(stmt.executeUpdate() != 0){
                    updated = true;
                    function.successMessage("Contact Updated", "Contact update successful.");
                }else{
                    updated = false;
                    function.errorMessage("Update Error", "Sorry something went wrong");
                }
            } catch (SQLException ex) {
//                Logger.getLogger(ContactQuery.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        }else{
            sqlQuery = "UPDATE `contact` SET `firstName`=?,`lastName`=?,`groupc`=?,`phone`=?,`email`=?,`address`=? WHERE ID = ?";
            try {
                stmt = con.prepareStatement(sqlQuery);
                stmt.setString(1, contact.getfName());
                stmt.setString(2, contact.getlName());
                stmt.setString(3, contact.getGroup());
                stmt.setString(4, contact.getPhone());
                stmt.setString(5, contact.getEmail());
                stmt.setString(6, contact.getAddress());
                stmt.setInt(7, contact.getCid());

                if(stmt.executeUpdate() != 0){
                    updated = true;
                    function.successMessage("Contact Updated", "Contact update successful.");
                }else{
                    updated = false;
                    function.errorMessage("Update Error", "Sorry something went wrong");
                }
            } catch (SQLException ex) {
//                Logger.getLogger(ContactQuery.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    return updated;
    }
    public void deleteContact(int contactId){
        myFunction function = new myFunction();
        Connection con = dbConfig.getConnection();
        PreparedStatement stmt;
        try {
            stmt = con.prepareStatement("DELETE FROM `contact` WHERE `ID` =?");
            stmt.setInt(1, contactId);
            if(stmt.executeUpdate() !=0){
                function.successMessage("Deleted Record", "Contact deleted successfully.");
            }

        } catch (SQLException ex) {
//            Logger.getLogger(ContactQuery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    // array of contacts
    public ArrayList<Contact> contactList(int userId){
        ArrayList<Contact> list = new ArrayList<>();
        Connection con = dbConfig.getConnection();
        Statement stmt;
        ResultSet rs;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT `ID`, `firstName`, `lastName`, `groupc`, `phone`, `email`, `address`, `picture` FROM `contact` WHERE user_id = "+userId);
            Contact contact;
            while (rs.next()){
                contact = new Contact(rs.getInt("ID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("groupc"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getBytes("picture"),
                        userId
                );
                list.add(contact);
            }


        } catch (SQLException ex) {
//            Logger.getLogger(ContactQuery.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    return list;
    }
}
