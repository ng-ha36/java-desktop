package sample.asm2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.asm2.dao.ContactDAO;
import sample.asm2.dao.GroupDAO;
import sample.asm2.entity.Contact;
import sample.asm2.entity.Group;
import java.util.List;
import java.util.Optional;

public class ContactController {
    @FXML
    private TextField search;
    @FXML
    private ComboBox<Group> cbGroup;
    @FXML
    private TableView tblContact;
    @FXML
    private Button btnSearch, btnGroup, btnAdd, btnUpdate, btnDelete;

    // List of contacts
    List<Contact> contacts;
    ContactDAO contactDAO = new ContactDAO();
    //data source for contact and group
    private final String GROUP = "data/group.txt";
    private final String CONTACT = "data/contact.txt";

    @FXML
    void initialize() {
        try {
            //load all contracts
            contacts = new ContactDAO().loadContact(CONTACT);
            // create table columns
            TableColumn<String, Contact> fname = new TableColumn<>("First Name");
            fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tblContact.getColumns().add(fname);

            TableColumn<String, Contact> lname = new TableColumn<>("Last Name");
            lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tblContact.getColumns().add(lname);

            TableColumn<String, Contact> phone = new TableColumn<>("Phone");
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            tblContact.getColumns().add(phone);

            TableColumn<String, Contact> email = new TableColumn<>("Email");
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tblContact.getColumns().add(email);

            TableColumn<String, Contact> dob = new TableColumn<>("Birth Date");
            dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            tblContact.getColumns().add(dob);

            TableColumn<String, Contact> group = new TableColumn<>("Group Name");
            group.setCellValueFactory(new PropertyValueFactory<>("group"));
            tblContact.getColumns().add(group);

            //get all groups
            showGroup(new GroupDAO().loadGroup(GROUP));
            //output contacts to tblContact
            showContact(contacts);
            tblContact.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(""+e);
        }
    }
    // output all contact to tblContact
    public void showContact(List<Contact> c) {
        // clear old data
        tblContact.getItems().clear();
        String group = cbGroup.getSelectionModel().getSelectedItem().getName();
        // output contacts in "c" to tableview
        if(group.equals("All")) {
            for (Contact x : c) {
                tblContact.getItems().add(x);
            }
        } else {
            for (Contact x : c) {
                if(x.getGroup().equalsIgnoreCase(group))
                    tblContact.getItems().add(x);
            }
        }
    }

    //output all groups to dropdownlist
    public void showGroup(List<Group> g) {
        cbGroup.getItems().add(new Group("All"));
        for (Group x : g) {
            cbGroup.getItems().add(x);
        }
        cbGroup.getSelectionModel().select(0);
    }

    //do corresponding actions for search, delete, update and add contact
    public void searchContact(ActionEvent event) throws Exception {
        if(event.getSource() == btnSearch) {
            String group = cbGroup.getSelectionModel().getSelectedItem().getName();
            List<Contact> c = contactDAO.search(contacts, group, search.getText());
            showContact(c);
        } else if(event.getSource() == btnAdd) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/addContact.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add a new Contact");
            stage.show();

            //pass list of current contact to aadContactController
            AddContactController c = loader.getController();
            c.setContacts(contacts);
            c.setAddContactController(this);
        } else if(event.getSource() == btnDelete) {
            deleteContact();
        } else if(event.getSource() == btnUpdate) {
            updateContact();
        } else if(event.getSource() == btnGroup) {
            groupPanel();
        }
    }

    // manage the groups
    public void groupPanel() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/group.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Group a Management");
        stage.show();
        // pass list of current contact to addContractController
        GroupController c = loader.getController();
        c.setContactController(this);
    }

    public void updateContact() throws Exception {
        int i = tblContact.getSelectionModel().getSelectedIndex();
        if(i >= tblContact.getItems().size() || i < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Select a Contact to update");
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/updateContact.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Update a Contact");
            stage.show();
            //pass list of current contact to addContactController
            UpdateController c = loader.getController();
            c.setContacts(contacts);
            c.setContactController(this);
            c.setUpdatedContact((Contact) tblContact.getItems().get(i));
        }
    }

    //delete a selected contact
    public void deleteContact() throws Exception {
        int i = tblContact.getSelectionModel().getSelectedIndex();
        if (i >= tblContact.getItems().size() || i < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Select a contact to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Do you want delete selected contact?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                contacts.remove(i);
                showContact(contacts);// update view
                contactDAO.saveToFile(contacts, CONTACT); // update source
            }
        }
    }
}
