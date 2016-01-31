package application;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
	//Date Formatter
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	//Main Menu Buttons
	@FXML
	Button contactMenu,meetingMenu,closeMenu;

	//REMOVE Buttons once in meetingmenu method
	Button mAdd,mRemove,mEdit,mSearch,mView,mSave,mClose;

	//contactMenu Instantiations
	TextField cID,cName,cNotes,cSearchInput;
	TableView<StringContact> ctable;
	Stage cWindow;

	//meetingMenu Instantiations
	TextField mID,mDate,mContSetString;
	TableView<StringMeeting> mtable;
	/***************************************Plan***************************************/
	public void Plan(){
		/* Create Contact Menu
	Methods
		List Controlling Buttons
			- cAdd Method(Contacts.add(TextFields)) TextFields(ID,Name,Notes)
			- cRemove Method(Contacts.remove(selected))
			- cEdit Method(TextFields.setText(selected),Contacts.remove(selected))
			- cSearch Method() = if(AnyCell.contains(Search)) TextField(Search)
			- (Selection) Not a button but needs a similar method to get meeting
		New Window Buttons
			- cView: public ObservableList<Meetings> ViewClicked (Contact cont){
				for each meeting if(meeting.getContSet.contains(selected))

		Program Buttons
			- cSave Method(Saves ObservableList<StringContact>ALLStringContacts to .txt)
			- cClose Method(cSave,ContactMenu.close());


	Create Meeting Menu
		List Controlling Buttons
			- mAdd Method(Meetings.add(TextFields)) TextFields[convert from string->objects](ID,Date,mContSetString,Notes) 
			- mRemove Method(Meetings.remove(selected))
			- mEdit Method(TextFields.setText(selected),Contacts.remove(selected))
			- mSearch Method() = if(AnyCell.contains(Search)) TextField(Search)
		New Window Buttons
			- mView: public ObservableList<Contact> ViewClicked (Meeting){
				for each Contact add to a table

		Program Buttons
			- mSave Method(Saves ObservableList<StringMeeting>ALLStringMeetings to .txt)
			- mClose Method(cSave,ContactMenu.close());
			- (Selection) Not a button but needs Method similar to buttons.
		 */
	}
	/*********************Puts .txt's into "ObservableArrayList"'s*********************/
	public ObservableList<StringContact> getContactsObsList(){
		ObservableList<StringContact>ALLStringContacts = FXCollections.observableArrayList();
		String lineText;
		try {
			LineNumberReader lineReader = new LineNumberReader(new FileReader("Contacts.txt"));

			while ((lineText = lineReader.readLine()) != null) {

				String[] splited = lineText.split(",");
				ALLStringContacts.add(new StringContact(Integer.parseInt(splited[0]),splited[1],splited[2]));  
			}
			lineReader.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		return ALLStringContacts;
	}
	public ObservableList<StringMeeting> getMeetingsObsList(){
		ObservableList<StringMeeting>ALLStringMeetings = FXCollections.observableArrayList();
		String lineText = null;
		try{
			LineNumberReader lineReader = new LineNumberReader(new FileReader("Meetings.txt"));

			while ((lineText = lineReader.readLine()) != null) {

				String[] splited = lineText.split(",");

				ALLStringMeetings.add(new StringMeeting(Integer.parseInt(splited[0]),splited[1],splited[2]));
			}
			lineReader.close();
		}
		catch(IOException e){
			System.err.println(e);
		}
		return ALLStringMeetings;
	}
	/********************************Contact Menu Method*******************************/
	public void contactMenu(ActionEvent event){

		/*******CSTAGE INSTANSIATION*******/
		Stage cStage = new Stage();
		cStage.setTitle("Contact Menu");


		/*******TABLE COLUMNS*******/
		//ID column
		TableColumn<StringContact, Integer> IDColumn = new TableColumn<>("ID");
		IDColumn.setMinWidth(100);
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Name column
		TableColumn<StringContact, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		//Notes column
		TableColumn<StringContact, String> notesColumn = new TableColumn<>("Notes");
		notesColumn.setMinWidth(200);
		notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));


		/*******USER INPUT TEXTBOXES*******/
		//Contact ID TextField
		cID = new TextField();
		cID.setPromptText("Contact ID (Optional)");

		//Contact Name TextField
		cName = new TextField();
		cName.setPromptText("Contact Name");

		//Contact Notes TextField
		cNotes = new TextField();
		cNotes.setPromptText("Contact Notes");

		//Search Input TextField
		cSearchInput = new TextField();
		cSearchInput.setPromptText("Search Contacts");
		cSearchInput.setMinWidth(332);

		/*******ARRAYLIST CONTROLLING BUTTONS[See Plan() for info]*******/
		//cAdd,cRemove,cEdita,cView.
		Button cAdd = new Button("Add");
		cAdd.setOnAction(e -> cAddClicked());
		Button cRemove = new Button("Remove");
		cRemove.setOnAction(e -> cRemoveClicked());
		Button cEdit = new Button("Edit");
		cEdit.setOnAction(e -> cEditClicked());

		Button cView = new Button("View");
		cView.setOnAction(e -> cViewClicked());
		/*******PROGRAM BUTTONS[See Plan() for info]*******/
		//cSearch,cSave,cClose;
		Button cSearch = new Button("Search");
		cSearch.setOnAction(e -> cSearchClicked());
		Button cSave = new Button("Save");
		cSave.setOnAction(e -> cSaveClicked());
		Button cClose = new Button("Close");
		cClose.setOnAction(e -> cCloseClicked());



		/*******CWINDOW LAYOUT*******/
		//Horizontal Box for Adding and Removing Contacts that goes at bottom of CWINDOW
		HBox addRemoveHBox = new HBox();
		addRemoveHBox.setPadding(new Insets(10,10,10,10));
		addRemoveHBox.setSpacing(10);
		addRemoveHBox.getChildren().addAll(cID,cName,cNotes,cAdd,cRemove);

		//Contact Table Instantiated
		ctable = new TableView<>();
		ctable.setItems(getContactsObsList());
		ctable.getColumns().addAll(IDColumn, nameColumn, notesColumn);

		//Top Bar for CWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(cSearchInput,cSearch,cSave,cClose);

		//Buttons on the right side
		VBox sideButtons = new VBox();
		sideButtons.setPadding(new Insets(10,10,10,10));
		sideButtons.setSpacing(10);
		sideButtons.getChildren().addAll(cEdit,cView);

		//Centre part of CWINDOW
		HBox centreHBox = new HBox();
		centreHBox.setPadding(new Insets(10,10,10,10));
		centreHBox.setSpacing(10);
		centreHBox.getChildren().addAll(ctable,sideButtons);

		//Main Container for ContactMenu
		VBox root = new VBox();
		root.getChildren().addAll(topBar,centreHBox,addRemoveHBox);

		/*******CWINDOW SHOW*******/
		Scene scene = new Scene(root);
		cWindow = new Stage();
		cWindow.setScene(scene);
		cWindow.show();
	}
	/********************************Meeting Menu Methods*******************************/
	public void meetingMenu(ActionEvent event){

	}
	public void closeMenu(ActionEvent event){
		Stage primarystage = (Stage) closeMenu.getScene().getWindow();
		primarystage.close();
	}
	/****************************Contact Menu Button Methods****************************/
	public void cAddClicked(){

		//Null Checker
		if(cName.getText()!=null && cNotes.getText()!=null){
			StringContact cont = new StringContact();
			if(cID.getText()==null&& cID.getText()==""){
				cont.setID(ctable.getItems().size());
			}else{
				cont.setID(Integer.parseInt(cID.getText()));
			}
			cont.setName(cName.getText()+"");
			cont.setNotes(cNotes.getText() + "");
			//Add Contact to table
			ctable.getItems().add(cont);
		}else{
			System.err.println("Please Fill in the [Name] and [Notes] boxes");
		}
	}
	public void cRemoveClicked(){}
	public void cEditClicked(){}
	public void cSearchClicked(){}
	public void cViewClicked(){}
	public void cSaveClicked(){}
	public void cCloseClicked(){}
	/****************************Meeting Menu Button Methods****************************/
	public void mAddClicked(){}
	public void mRemoveClicked(){}
	public void mEditClicked(){}
	public void mSearchClicked(){}
	public void mViewClicked(){}
	public void mSaveClicked(){}
	public void mCloseClicked(){}
	/*********************************Conversion Methods*********************************/
	public int[] StringtoArray(String StringIDs){
		String[] StringIds = StringIDs.split("/");
		int[]a = new int[StringIds.length];
		for(int i=0; i<StringIds.length;i++){
			a[i] = Integer.parseInt(StringIds[i]);
		}
		return a;
	}
	public ObservableList<StringContact>getStringContacts(int[]Ids){

		ObservableList<StringContact>AllContacts = getContactsObsList();
		ObservableList<StringContact>tempContacts = FXCollections.observableArrayList();
		for(int i =0;i<AllContacts.size();i++){
			for(int j = 0;j<Ids.length;j++){
				if(Ids[j]==AllContacts.get(i).getID()){
					tempContacts.add(AllContacts.get(i));
				}
			}
		}
		return tempContacts;
	}
}