package sample.asm2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.asm2.dao.GroupDAO;
import sample.asm2.entity.Contact;
import sample.asm2.entity.Group;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContactController {

    @FXML
    private TextField firstName, lastName, phone, email;
    @FXML
    private DatePicker dob;
    @FXML
    private ComboBox<Group> cbGroup;
    @FXML
    private Label lblFirstName, lblLastName, lblPhone, lblEmail, lbldob;
    @FXML
    private Button btnAdd, btnClose;

    private ContactController contactController;

    public void setAddContactController(ContactController contactController) {
        this.contactController = contactController;
    }

    private List<Contact> contacts;

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @FXML
    void initialize()throws  Exception {
        lblFirstName.setText("");
        lblLastName.setText("");
        lblPhone.setText("");
        lblEmail.setText("");
        lbldob.setText("");
        cbGroup.getItems().clear();
        for (Group x: new GroupDAO().loadGroup("data/group.txt")) {
           cbGroup.getItems().add(x);
        }
        cbGroup.getSelectionModel().select(0);
        dob.setValue(LocalDate.now());
    }

    public  void saveContact() throws Exception {
        String fname = firstName.getText().trim();
        if(fname.isEmpty()) {
            lblLastName.setText("First Name can be empty");
            return;
        }
        lblFirstName.setText("");
        String lname = lastName.getText().trim();
        if(lname.isEmpty()) {
            lblLastName.setText("Last Name can not be empty");
            return;
        }
        lblLastName.setText("");
        String mobile = phone.getText().trim();
        if(mobile.isEmpty() || !mobile.matches("\\d+")) {
            lblPhone.setText("Phone contains digit only");
            return;
        }
        lblPhone.setText("");
        String mail = email.getText().trim();
        Pattern emailNamePtrn = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher mtch = emailNamePtrn.matcher(mail);
        if(!mtch.matches()) {
            lblEmail.setText("Email is invalid");
            return;
        }
        lblEmail.setText("");
        String birthdate = (dob.getValue().toString());
        String group = cbGroup.getSelectionModel().getSelectedItem().getName();
        Contact c = new Contact(fname, lname, mobile, mail, birthdate, group);
        if(contactController.contactDAO.indexOf(contacts, c) == -1) {
            contactController.contactDAO.saveToList(contacts, c);
            contactController.showContact(contacts);
            contactController.contactDAO.saveToFile(contacts, "data/contact.txt"); // update data source
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("New Contacts has been added");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Information of contact is existed");
            alert.showAndWait();
        }
    }
    public  void saveContact(ActionEvent e)throws  Exception {
        if(e.getSource() == btnAdd) {
            saveContact();
        } else if(e.getSource() == btnClose) {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }

    }
}
