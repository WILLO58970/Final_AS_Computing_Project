package application;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainController {
	//Date Formatter
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	//Home Instatiations
	@FXML
	Button closeMenu;

	/********contactMenu Instantiations*******/
	TextField cID,cName,cNotes,cSearchInput;
	TableView<StringContact> ctable;
	Stage cWindow;
	//contact_Viewer/Editor Instantiations
	StringContact clastselected;
	TextField ViewIDInput,ViewNameInput,ViewNotesInput;


	/********Meeting Instantiations*******/
	TextField mID,mDate,mContSetString,mNotes,mSearchInput;
	TableView<StringMeeting> mtable;
	Stage mWindow;
	//meeting_Viewer/Editor Instantiations
	StringMeeting mlastselected;
	TextField mViewIDInput,mViewNameInput,mViewNotesInput;
	TableView<StringContact> mView_ctable;
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
		//ALLString Meetings = All the objects in .txt
		ObservableList<StringMeeting>ALLStringMeetings = FXCollections.observableArrayList();

		String lineText = null;
		try{
			LineNumberReader lineReader = new LineNumberReader(new FileReader("Meetings.txt"));

			while ((lineText = lineReader.readLine()) != null) {

				String[] splited = lineText.split(",");

				int ID = Integer.parseInt(splited[0]);
				String Date = splited[1];
				String ContSetString = splited[2];
				if(splited.length ==3){
					ALLStringMeetings.add(new StringMeeting(ID,Date,ContSetString,""));
				}else{
					ALLStringMeetings.add(new StringMeeting(ID,Date,ContSetString,splited[3]));
				}
				ALLStringMeetings.remove(null);
				for(StringMeeting temp: ALLStringMeetings){
					if(temp.getContSetString()==null){
						ALLStringMeetings.remove(temp);
					}
				}
			}
			lineReader.close();
		}
		catch(IOException e){
			System.err.println(e);
		}
		
		return ALLStringMeetings;
	}
	/********************************Main Menu Methods*******************************/
	public void closeMenu(ActionEvent event){
		Stage primarystage = (Stage) closeMenu.getScene().getWindow();
		primarystage.close();
	}

	/********************************Contact Menu Method*******************************/
	public void contactMenu(ActionEvent event){
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
		//CVIEW/EDIT NEW WINDOW
		Button cView = new Button("View");
		cView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				cEditViewClicked(clastselected);
			}
		});
		Button cEdit = new Button("Edit");
		cEdit.setOnAction(e -> cEditClicked(clastselected));



		/*******PROGRAM BUTTONS[See Plan() for info]*******/
		//cSearch,cSave,cClose;
		Button cSearch = new Button("Search");
		cSearch.setOnAction(e -> cSearchClicked());

		Button cSave = new Button("Save");
		cSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				System.out.println("Saving...");
				cSaveClicked();
				System.out.println("Done!");
			}});
		//sClose and the "X" Button do the same thing except the "X" Save to .txt then close the window.
		Button cClose = new Button("Close without saving");
		cClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//Closes
				cWindow.close();
			}});

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

		//Last Selected Contact Listener
		ctable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			//Check whether item is selected and set value of selected item to Label
			/*System.out.println(oldValue + " Old");
			System.out.println(observableValue + " Obs");
			System.out.println(newValue + " New");*/
			if (newValue != null) {
				clastselected = newValue;
				System.out.println(clastselected);
			}else if(oldValue != null) {
				clastselected = oldValue;
				System.out.println(clastselected);
			}
		});

		//Top Bar for CWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(cSearchInput,cSearch,cSave,cClose);

		//Buttons on the right side
		VBox sideButtons = new VBox();
		sideButtons.setPadding(new Insets(10,10,10,10));
		sideButtons.setSpacing(10);
		sideButtons.getChildren().addAll(cView);

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
		cWindow.setTitle("Contact Menu");
		cWindow.setScene(scene);
		cWindow.show();
		//When the "X" Button is pressed 
		cWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent actionEvent) {
				cSaveClicked();
				cWindow.close();
			}
		});
	}
	/********************************Meeting Menu Methods*******************************/
	public void meetingMenu(ActionEvent event){
		/*******TABLE COLUMNS*******/
		//ID column
		TableColumn<StringMeeting, Integer> IDColumn = new TableColumn<>("ID");
		IDColumn.setMinWidth(100);
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Date column
		TableColumn<StringMeeting, String> DateColumn = new TableColumn<>("Date Held");
		DateColumn.setMinWidth(200);
		DateColumn.setCellValueFactory(new PropertyValueFactory<>("Date Held"));

		//Date column
		TableColumn<StringMeeting, String> ContColumn = new TableColumn<>("Contacts Attending");
		ContColumn.setMinWidth(200);
		ContColumn.setCellValueFactory(new PropertyValueFactory<>("Contacts Attending"));

		//Notes column
		TableColumn<StringMeeting, String> notesColumn = new TableColumn<>("Notes");
		notesColumn.setMinWidth(200);
		notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));


		/*******USER INPUT TEXTBOXES*******/
		//Contact ID TextField
		mID = new TextField();
		mID.setPromptText("Meeting ID (Optional)");

		//Contact Name TextField
		mDate = new TextField();
		mDate.setPromptText("Meeting Date");

		//Contact Notes TextField
		mContSetString = new TextField();
		mContSetString.setPromptText("ContactID's split with a '/' with no spaces");

		//Contact Notes TextField
		mNotes = new TextField();
		mNotes.setPromptText("Notes");

		//Search Input TextField
		mSearchInput = new TextField();
		mSearchInput.setPromptText("Search For Meeting");
		mSearchInput.setMinWidth(332);

		/**********Buttons**********/
		/*******ARRAYLIST CONTROLLING BUTTONS[See Plan() for info]*******/
		//Button mAdd,mRemove,mEdit,mSearch,mView,mSave,mClose;
		Button mAdd = new Button("Add");
		mAdd.setOnAction(e -> mAddClicked());
		Button mRemove = new Button("Remove");
		mRemove.setOnAction(e -> mRemoveClicked());
		//MVIEW/EDIT NEW WINDOW
		Button mView = new Button("View Contacts Attending Meeting");
		mView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mEditViewClicked(mlastselected);
			}
		});
		/**************************Move to better place****************/
		Button mEdit = new Button("Edit");
		mEdit.setOnAction(e -> mEditClicked(mlastselected));



		/*******PROGRAM BUTTONS[See Plan() for info]*******/
		//cSearch,cSave,cClose;
		Button mSearch = new Button("Search");
		mSearch.setOnAction(e -> mSearchClicked());

		Button mSave = new Button("Save");
		mSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				System.out.println("Saving...");
				mSaveClicked();
				System.out.println("Done!");
			}});
		//sClose and the "X" Button do the same thing except the "X" Save to .txt then close the window.
		Button mClose = new Button("Close without saving");
		mClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//Closes
				mWindow.close();
			}});

		/*******CWINDOW LAYOUT*******/
		//Horizontal Box for Adding and Removing Contacts that goes at bottom of CWINDOW
		HBox addRemoveHBox = new HBox();
		addRemoveHBox.setPadding(new Insets(10,10,10,10));
		addRemoveHBox.setSpacing(10);
		addRemoveHBox.getChildren().addAll(mID,mDate,mContSetString,mNotes,mAdd,mRemove);

		//Contact Table Instantiated
		mtable = new TableView<>();
		mtable.setItems(getMeetingsObsList());
		mtable.getColumns().addAll(IDColumn, DateColumn,ContColumn, notesColumn);

		//Last Selected Contact Listener
		mtable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				mlastselected = newValue;
				System.out.println(mlastselected);
			}else if(oldValue != null) {
				mlastselected = oldValue;
				System.out.println(mlastselected);
			}
		});

		//Top Bar for CWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(mSearchInput,mSearch,mSave,mClose);

		//Buttons on the right side
		VBox sideButtons = new VBox();
		sideButtons.setPadding(new Insets(10,10,10,10));
		sideButtons.setSpacing(10);
		sideButtons.getChildren().addAll(mView);

		//Centre part of CWINDOW
		HBox centreHBox = new HBox();
		centreHBox.setPadding(new Insets(10,10,10,10));
		centreHBox.setSpacing(10);
		centreHBox.getChildren().addAll(mtable,sideButtons);


		//Main Container for ContactMenu
		VBox root = new VBox();
		root.getChildren().addAll(topBar,centreHBox,addRemoveHBox);

		/*******CWINDOW SHOW*******/
		Scene scene = new Scene(root);
		mWindow = new Stage();
		mWindow.setTitle("Meeting Menu");
		mWindow.setScene(scene);
		mWindow.show();
		//When the "X" Button is pressed 
		mWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent actionEvent) {
				mSaveClicked();
				mWindow.close();
			}
		});
	}
	/****************************Contact Menu Button Methods****************************/
	public void cAddClicked(){

		//Null Checker
		if(cName.getText().isEmpty()==false && cNotes.getText().isEmpty()==false){
			StringContact cont = new StringContact();
			if(cID.getText().isEmpty()){
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
		cID.clear();
		cName.clear();
		cNotes.clear();
	}
	public void cRemoveClicked(){
		StringContact StringContactselected;
		ObservableList<StringContact> allContacts;
		allContacts = ctable.getItems();
		StringContactselected = ctable.getSelectionModel().getSelectedItem();
		allContacts.remove(StringContactselected);
		allContacts.remove(null);
		ctable.setItems(allContacts);
	}
	public void cSearchClicked(){}
	public void cSaveClicked(){
		try {
			new PrintWriter("Contacts.txt").close();
			ObservableList<StringContact> ALLContacts = ctable.getItems();
			BufferedWriter writer = new BufferedWriter(new FileWriter("Contacts.txt", true));
			for(int i = 0;i<ctable.getItems().size();i++){
				if(ALLContacts.get(i)!=null && ALLContacts.get(i).getName()!= null){
					writer.write(ALLContacts.get(i).getID()+",");
					writer.write(ALLContacts.get(i).getName()+",");
					writer.write(ALLContacts.get(i).getNotes()+",\n");
				}
			}
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public StringContact cEditViewClicked(StringContact lastselect){
		Stage stage = new Stage();

		stage.setTitle("View/Edit Contact");

		//Title
		Label Title = new Label();
		Title.setLineSpacing(10);
		Title.setPadding(new Insets(10,0,0,30));
		Title.setText("View/Edit Contact");
		Title.setFont(Font.font(25));

		/************Text Fields**************/
		//All Text Fields (Defining & Instantiation)
		TextField ViewIDInput = new TextField(Integer.toString(lastselect.getID()));
		ViewIDInput.setMaxWidth(150);

		TextField ViewNameInput = new TextField(lastselect.getName());
		ViewNameInput.setMaxWidth(150);

		TextField ViewNotesInput = new TextField(lastselect.getNotes());
		ViewNotesInput.setMaxWidth(150);

		//Defines TextField VBox
		VBox VBoxTextFields = new VBox();
		VBoxTextFields.setPadding(new Insets(10,10,10,10));
		VBoxTextFields.getChildren().addAll(ViewIDInput,ViewNameInput,ViewNotesInput);
		/************LABELS**************/
		//ALL Labels (Defining & Instantiation)
		Label ViewIDLabel = new Label("ID:");
		ViewIDLabel.setMaxWidth(100);

		Label ViewNameLabel = new Label("Name:");
		ViewNameLabel.setMaxWidth(100);

		Label ViewNotesLabel = new Label("Notes:");
		ViewNotesLabel.setMaxWidth(100);

		//Defines Label VBox
		VBox VBoxLabels = new VBox();
		VBoxLabels.setPadding(new Insets(15,10,10,10));
		VBoxLabels.setSpacing(10);
		VBoxLabels.getChildren().addAll(ViewIDLabel,ViewNameLabel,ViewNotesLabel);

		/********All Labels and TextFields as one********/
		HBox ContactViewer = new HBox();
		ContactViewer.setPadding(new Insets(10,10,10,10));
		ContactViewer.getChildren().addAll(VBoxLabels,VBoxTextFields);

		/********BUTTONS*********/
		//Viewer Saver and Exiter

		//Button: Exit Viewer
		Button cViewer_Exit = new Button("Close Viewer");
		cViewer_Exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//Closes
				stage.close();
			}});

		//Button: Save and Exit Viewer
		Button cViewer_SaveandExit = new Button(" Save and Exit Viewer");
		cViewer_SaveandExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				cEditView_SaveClicked(lastselect);

				stage.close();
			}});

		/********All Buttons as one********/
		HBox HBoxButtons = new HBox();
		HBoxButtons.setPadding(new Insets(10,10,10,10));
		HBoxButtons.getChildren().addAll(cViewer_Exit,cViewer_SaveandExit);

		/**************ALL LABELS,TEXTFIELDS,BUTTONS IN VerticalBox***************/
		// Adds Title then
		VBox root = new VBox(Title,ContactViewer,HBoxButtons);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return lastselect;

	}
	public void cEditClicked(StringContact lastselect){
		ObservableList<StringContact>allcont = ctable.getItems();
		cID.setText(lastselect.getID()+"");
		cID.setText(lastselect.getID()+"");
		cID.setText(lastselect.getID()+"");
		allcont.remove(lastselect);
	}

	/****************************Meeting Menu Button Methods****************************/
	public void mAddClicked(){}
	public void mRemoveClicked(){}
	public void mEditClicked(StringMeeting mlastselect){}
	public void mSearchClicked(){}
	public void mEditViewClicked(StringMeeting lastselect){}
	public void mSaveClicked(){}
	public void mCloseClicked(){}

	public void cEditView_SaveClicked(StringContact lastselecte){
		StringContact newContact = new StringContact();
		if(ViewNameInput.getText().isEmpty()==false && ViewNotesInput.getText().isEmpty()==false){
			if(ViewIDInput.getText().isEmpty()){
				newContact.setID(clastselected.getID());
			}else{
				newContact.setID(Integer.parseInt(ViewIDInput.getText()));
			}
			newContact.setName(ViewNameInput.getText()+"");
			newContact.setNotes(ViewNotesInput.getText() + "");
			//Add Contact to table
			cRemoveClicked();
			ctable.getItems().remove(null);
			ctable.getItems().add(newContact);
			ctable.refresh();
			//Duplication Checker a
		}else{
			System.err.println("Please Fill in the [Name] and [Notes] boxes");
		}
		/***********************************************************************/
		ctable.getItems().remove(lastselecte);
		ctable.getItems().add(newContact);
	};
	public void cDupplicationChecker(){
		for(StringContact contempcheck1:ctable.getItems()){
			for(StringContact contempcheck2:ctable.getItems()){
				if(contempcheck1==contempcheck2){

				}

			}
		}
	}

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