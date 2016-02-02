package application;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

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

public class MainController {

	//Date Formatter for Input Validation
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	//MainMenu Declaration for "Main.fxml"
	@FXML
	Button closeMenu;

	//********contactMenu Declarations*******//
	TextField cName,cNotes,cSearchInput;
	TableView<StringContact> ctable;
	Stage cWindow;
	//Last Contact SELECTED Declaration
	StringContact clastselected;

	//********Meeting Declarations*******//
	TextField mDate,mContSetString,mNotes,mSearchInput;
	TableView<StringMeeting> mtable;
	Stage mWindow;
	//Last Contact SELECTED Declaration
	StringMeeting mlastselected;
	//meeting_Viewer-Editor Declaration
	StringContact mView_Contact_a,mView_Contact_s;
	TextField mViewDate,mViewNotes;
	TableView<StringContact> mView_ctable_s,mView_ctable_a;
	Stage mViewWindow;
	
	//.txt Readers*********************************************/
	/**@param  Put Contacts.txt's into "ObservableArrayList" of the type Contact*/
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
	/**@param  Put Meetings.txt's into "ObservableArrayList" of the type Meeting*/
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

	/**@param Gets Contacts Attending Certain meeting*/
	public ObservableList<StringContact> ContactsAttendingAMeeting(StringMeeting meeting){
		ObservableList<StringContact>AllContacts = getContactsObsList();
		ObservableList<StringContact>AttendingContacts = getContactsObsList();
		for(StringContact contact:AllContacts){
			String[]splited = meeting.getContSetString().split("/");
			for(int i =0;i<splited.length;i++){
				if(splited[i].equals(contact.getID())){
					AttendingContacts.add(contact);
				}
			}
		}
		return AttendingContacts;
	}

	/**@param Main Menu closer*/
	public void closeMenu(ActionEvent event){
		Stage primarystage = (Stage) closeMenu.getScene().getWindow();
		primarystage.close();
	}
	//Most Important Methods
	/**@param Contact Window creation*/
	public void contactMenu(ActionEvent event){
		//*******TABLE COLUMNS*******//
		//ID column
		TableColumn<StringContact, Integer> IDColumn = new TableColumn<>("ID");
		IDColumn.setMinWidth(100);
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Name column
		TableColumn<StringContact, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setMinWidth(200);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		//Notes column
		TableColumn<StringContact, String> NotesColumn = new TableColumn<>("Notes");
		NotesColumn.setMinWidth(200);
		NotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));


		//*******USER INPUT TEXTBOXES*******//

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

		//*******OBSERVABLEARRAYLIST CONTROLLING BUTTONS//
		//cAdd,cRemove,cEdit,cView
		Button cAdd = new Button("Add");

		cAdd.setOnAction(e -> cAddClicked());
		Button cRemove = new Button("Remove");
		cRemove.setOnAction(e -> cRemoveClicked());
		//CVIEW/EDIT NEW WINDOW
		Button cView = new Button("View/Edit Contact");
		cView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				cEditViewClicked(clastselected);
			}
		});
		//*******PROGRAM BUTTONS*******//
		//cSearch,cSave,cClose;
		Button cSearch = new Button("Search");
		cSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				String Searched = cSearchInput.getText();

				TableColumn<StringContact, Integer> IDColumn = new TableColumn<>("ID");
				IDColumn.setMinWidth(100);
				IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

				//Name column
				TableColumn<StringContact, String> nameColumn = new TableColumn<>("Name");
				nameColumn.setMinWidth(200);
				nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

				//Notes column
				TableColumn<StringContact, String> NotesColumn = new TableColumn<>("Notes");
				NotesColumn.setMinWidth(200);
				NotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

				ObservableList<StringContact> searchCont = FXCollections.observableArrayList();

				//SELECTS Contacts in ctable that contain the String "Searched"
				for(StringContact cont:ctable.getItems()){
					if((cont.getID()+"").contains(Searched)){
						searchCont.add(cont);
					}else if(cont.getName().contains(Searched)){
						searchCont.add(cont);
					}else if(cont.getNotes().contains(Searched)){
						searchCont.add(cont);
					}
				}

				TableView<StringContact> ctable_search = new TableView<StringContact>();
				ctable_search.setItems(searchCont);
				ctable_search.getColumns().addAll(IDColumn, nameColumn, NotesColumn);

				//LAYOUT
				Scene scene = new Scene(ctable_search);
				Stage cSearchWindow = new Stage();
				cSearchWindow.setTitle("Contact Menu");
				cSearchWindow.setScene(scene);
				cSearchWindow.show();
			}
		});
		//sClose and the "X" Button DON'T DO the same thing the "X" Doesn't save only closes, the "Close" button saves to .txt then close the window.
		Button cClose = new Button("Save and Close");
		cClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				//Closes
				cWindow.close();
			}});

		//*******CWINDOW LAYOUT*******//
		//Horizontal Box for Adding and Removing Contacts that goes at bottom of CWINDOW
		HBox addRemoveHBox = new HBox();
		addRemoveHBox.setPadding(new Insets(10,10,10,10));
		addRemoveHBox.setSpacing(10);
		addRemoveHBox.getChildren().addAll(cName,cNotes,cAdd,cRemove);

		//Contact Table Instantiated
		ctable = new TableView<>();
		ctable.setItems(getContactsObsList());
		ctable.getColumns().addAll(IDColumn, nameColumn, NotesColumn);

		//Last Selected Contact Listener
		ctable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

			if (newValue != null) {
				clastselected = newValue;
			}else if(oldValue != null) {
				clastselected = oldValue;
			}});
		//Top Bar for CWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(cSearchInput,cSearch,cClose);

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

		//*******CWINDOW SHOW*******//
		Scene scene = new Scene(root);
		cWindow = new Stage();
		cWindow.setTitle("Contact Menu");
		cWindow.setScene(scene);
		cWindow.show();
	}
	/**@param Meetings Window creation**/
	public void meetingMenu(ActionEvent event){
		//*******TABLE COLUMNS*******//
		//ID column
		TableColumn<StringMeeting, Integer> IDColumn = new TableColumn<>("ID");
		IDColumn.setMinWidth(100);
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Date column
		TableColumn<StringMeeting, String> DateColumn = new TableColumn<>("Date Held");
		DateColumn.setMinWidth(100);
		DateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

		//Contact Attending column
		TableColumn<StringMeeting, String> ContColumn = new TableColumn<>("Contacts Attending");
		ContColumn.setMinWidth(200);
		ContColumn.setCellValueFactory(new PropertyValueFactory<>("ContSetString"));

		//Notes column
		TableColumn<StringMeeting, String> NotesColumn = new TableColumn<>("Notes");
		NotesColumn.setMinWidth(200);
		NotesColumn.setCellValueFactory(new PropertyValueFactory<>("Notes"));


		//*******USER INPUT TEXTBOXES*******//
		//Contact Name TextField
		mDate = new TextField();
		mDate.setPromptText("Meeting Date");

		//Contact Notes TextField
		mContSetString = new TextField();
		mContSetString.setPromptText("ContactID's split with a '/' with no spaces");
		mContSetString.setMinWidth(240);

		//Contact Notes TextField
		mNotes = new TextField();
		mNotes.setPromptText("Notes (Optional)");

		//Search Input TextField
		mSearchInput = new TextField();
		mSearchInput.setPromptText("Search Meeting");
		mSearchInput.setMinWidth(450);

		//**********Buttons**********//
		//*******ARRAYLIST CONTROLLING BUTTONS*******//
		//Button mAdd,mRemove,mEdit,mSearch,mView,mSave,mClose;
		Button mAdd = new Button("Add");
		mAdd.setOnAction(e -> mAddClicked());
		Button mRemove = new Button("Remove");
		mRemove.setOnAction(e -> mRemoveClicked());
		//MVIEW/EDIT NEW WINDOW
		Button mView = new Button("View/Edit Meeting");
		mView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mEditViewClicked(mlastselected);
			}
		});

		//PROGRAM BUTTONS*******//
		//cSearch,cSave,cClose;
		//Search Button
		Button mSearch = new Button("Search");
		mSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

				mSearchClicked();
			}
		});

		//sClose and the "X" Button do the same thing except the "X" Save to .txt then close the window.
		Button mClose = new Button("Save and Close");
		mClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mSaveClicked();
				mWindow.close();
			}});

		//*******CWINDOW LAYOUT*******//
		//Horizontal Box for Adding and Removing Contacts that goes at bottom of CWINDOW
		HBox addRemoveHBox = new HBox();
		addRemoveHBox.setPadding(new Insets(10,10,10,10));
		addRemoveHBox.setSpacing(10);
		addRemoveHBox.getChildren().addAll(mDate,mContSetString,mNotes,mAdd,mRemove);

		//Contact Table Instantiated
		mtable = new TableView<>();
		mtable.setItems(getMeetingsObsList());
		mtable.getColumns().addAll(IDColumn, DateColumn,ContColumn, NotesColumn);

		//Last Selected Contact Listener
		mtable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				mlastselected = newValue;
			}else if(oldValue != null) {
				mlastselected = oldValue;
			}
		});

		//Top Bar for CWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(mSearchInput,mSearch,mClose);

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

		//*******CWINDOW SHOW*******//
		Scene scene = new Scene(root);
		mWindow = new Stage();
		mWindow.setTitle("Meeting Menu");
		mWindow.setScene(scene);
		mWindow.show();
	}

	//** Contact-Menu - (meetingMenu) Button Methods*************/
	/**@param Adds a Contact from TextFields*/
	public void cAddClicked(){

		//Null Checker
		if(cName.getText().isEmpty()==false && cNotes.getText().isEmpty()==false){
			StringContact cont = new StringContact();
			cont.setID(ctable.getItems().size());
			cont.setName(cName.getText()+"");
			cont.setNotes(cNotes.getText() + "");
			//Add Contact to table
			ctable.getItems().add(cont);
		}else{
			System.err.println("Please Fill in the [Name] and [Notes] boxes");
		}
		cName.clear();
		cNotes.clear();
	}
	/**@param Removes a Contact from Meeting Table*/
	public void cRemoveClicked(){
		StringContact StringContactselected;
		ObservableList<StringContact> allContacts;
		allContacts = ctable.getItems();
		StringContactselected = ctable.getSelectionModel().getSelectedItem();
		allContacts.remove(StringContactselected);
		allContacts.remove(null);
		ctable.setItems(allContacts);
	}
	/**@param Gets Contact ObsArrayList Matching search criteria*/
	public ObservableList<StringContact> cSearchClicked(){
		ObservableList<StringContact> AllContacts = getContactsObsList();
		ObservableList<StringContact> SelectContacts = FXCollections.observableArrayList();
		for (StringContact g : AllContacts){

			String search = cSearchInput.getText().toUpperCase();

			if(g.getName().toUpperCase().contains(search.toUpperCase())){
				SelectContacts.add(g);
			}
			if(g.getName().toUpperCase().contains(search)){
				SelectContacts.add(g);
			}
			SelectContacts.add(g);
		}
		return SelectContacts;
	}	
	public void cSaveClicked(){
		try {

			ObservableList<StringContact> ALLContacts = ctable.getItems();
			BufferedWriter writer = new BufferedWriter(new FileWriter("Contacts.txt", true));
			//Duplication Checker
			for(int i =0;i<ALLContacts.size();i++){
				for(int j =0;j<ALLContacts.size();j++){
					if(ALLContacts.get(i).equals(ALLContacts.get(j)) && i!=j){
						ALLContacts.remove(i);
					}
				}
			}
			for(int i = 0;i<ctable.getItems().size();i++){
				if(ALLContacts.get(i)!=null && ALLContacts.get(i).getName()!= null){
					writer.write(ALLContacts.get(i).getID()+",");
					writer.write(ALLContacts.get(i).getName()+",");
					writer.write(ALLContacts.get(i).getNotes()+",\n");
				}
			}
			new PrintWriter("Contacts.txt").close();
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

		Label ViewIDInput = new Label();
		ViewIDInput.setPadding(new Insets(5,10,5,10));
		ViewIDInput.setText(lastselect.getID()+"");

		//************Text Fields**************//
		//All Text Fields (Defining & Instantiation)
		TextField ViewNameInput = new TextField(lastselect.getName());
		ViewNameInput.setMaxWidth(150);

		TextField ViewNotesInput = new TextField(lastselect.getNotes());
		ViewNotesInput.setMaxWidth(150);

		//Defines TextField VBox
		VBox VBoxTextFields = new VBox();
		VBoxTextFields.setPadding(new Insets(10,10,10,10));
		VBoxTextFields.getChildren().addAll(ViewIDInput,ViewNameInput,ViewNotesInput);
		//************LABELS**************//
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

		//********All Labels and TextFields as one********//
		HBox ContactViewer = new HBox();
		ContactViewer.setPadding(new Insets(10,10,10,10));
		ContactViewer.getChildren().addAll(VBoxLabels,VBoxTextFields);

		//********BUTTONS*********//
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
		Button cViewer_SaveandExit = new Button("Save and Exit Viewer");
		cViewer_SaveandExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
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
					ctable.getItems().remove(lastselect);
					ctable.getItems().add(newContact);
					ctable.refresh();
					//Duplication Checker a
				}else{
					System.err.println("Please Fill in the [Name] and [Notes] boxes");
				}
				for(int i =0;i<ctable.getItems().size();i++){
					if(ctable.getItems().get(i).getID()+""==ViewIDInput.getText()){
						ctable.getItems().get(i).setID(Integer.parseInt(ViewIDInput.getText()));
						ctable.getItems().get(i).setName(ViewNameInput.getText());
						ctable.getItems().get(i).setNotes(ViewNameInput.getText());
					}


				}
				stage.close();
			}});

		//********All Buttons as one********//
		HBox HBoxButtons = new HBox();
		HBoxButtons.setPadding(new Insets(10,10,10,10));
		HBoxButtons.getChildren().addAll(cViewer_Exit,cViewer_SaveandExit);

		//********ALL LABELS,TEXTFIELDS,BUTTONS IN VerticalBox*******//
		// Adds Title then
		VBox root = new VBox(Title,ContactViewer,HBoxButtons);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return lastselect;

	}

	//** Meeting-Menu - (meetingMenu) Button Methods**************/
	/**@param Adds a Meeting from TextFields*/
	public void mAddClicked(){
		//Null Checker for mID,mDate,mContSetString,mNotes
		if(!(mDate.getText().isEmpty() && mContSetString.getText().isEmpty())){

			Calendar date = Calendar.getInstance();
			try {
				date.setTime(formatter.parse(mDate.getText()));
			} catch (ParseException e) {
				System.err.println("Invalid Date, please use format 'DD/MM/YYYY'");
			}

			StringMeeting meet = new StringMeeting();
			//SET ID
			meet.setID(mtable.getItems().size());
			//SET DATE
			meet.setDate(mDate.getText());

			//SET CONTSET
			meet.setContSetString(mContSetString.getText());

			//SET NOTES
			meet.setNotes(mNotes.getText()+"");

			//Add Contact to table
			mtable.getItems().add(meet);
			//mID,mDate,mContSetString,mNotes
			mDate.clear();
			mContSetString.clear();
			mNotes.clear();
		}else{
			System.err.println("Please Fill in at least [Whos Attending]&[DateHeld]");
		}
	}
	/**@param Removes a Meeting from Meeting Table*/
	public void mRemoveClicked(){
		StringMeeting StringContactselected;
		ObservableList<StringMeeting> allContacts;
		allContacts = mtable.getItems();
		StringContactselected = mtable.getSelectionModel().getSelectedItem();
		allContacts.remove(StringContactselected);
		allContacts.remove(null);
		mtable.setItems(allContacts);
	}
	/**@param Creates a new Window with searched data from Meeting Table*/
	public void mSearchClicked(){
		String Searched = mSearchInput.getText();

		//Needs to be duplicated or it will change the other table
		//ID column
		TableColumn<StringMeeting, Integer> IDColumn = new TableColumn<>("ID");
		IDColumn.setMinWidth(100);
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Date column
		TableColumn<StringMeeting, String> DateColumn = new TableColumn<>("Date Held");
		DateColumn.setMinWidth(100);
		DateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

		//Contact Attending column
		TableColumn<StringMeeting, String> ContColumn = new TableColumn<>("Contacts Attending");
		ContColumn.setMinWidth(200);
		ContColumn.setCellValueFactory(new PropertyValueFactory<>("ContSetString"));

		//Notes column
		TableColumn<StringMeeting, String> NotesColumn = new TableColumn<>("Notes");
		NotesColumn.setMinWidth(200);
		NotesColumn.setCellValueFactory(new PropertyValueFactory<>("Notes"));

		ObservableList<StringMeeting> searchMeet = FXCollections.observableArrayList();

		//SELECTS Contacts in ctable that contain the String "Searched"
		for(StringMeeting meet:mtable.getItems()){
			if((meet.getID()+"").toUpperCase().contains(Searched)){
				searchMeet.add(meet);
			}else if(meet.getDate().toUpperCase().contains(Searched)){
				searchMeet.add(meet);
			}else if(meet.getContSetString().toUpperCase().contains(Searched)){
				searchMeet.add(meet);
			}else if(meet.getNotes().toUpperCase().contains(Searched)){
				searchMeet.add(meet);
			}
		}
		TableView<StringMeeting> mtable_search = new TableView<StringMeeting>();
		mtable_search.setItems(searchMeet);
		mtable_search.getColumns().addAll(IDColumn, DateColumn,ContColumn, NotesColumn);

		//LAYOUT
		Scene scene = new Scene(mtable_search);
		Stage cSearchWindow = new Stage();
		cSearchWindow.setTitle("Meeting Search Criterea: " + Searched);
		cSearchWindow.setScene(scene);
		cSearchWindow.show();
	}	
	/**@param Saves Meeting Changes to .txt*/
	public void mSaveClicked(){
		try {
			ObservableList<StringMeeting> ALLMeetings = mtable.getItems();
			BufferedWriter writer = new BufferedWriter(new FileWriter("Meetings.txt", true));
			//Duplication Checker
			for(int i =0;i<ALLMeetings.size();i++){
				for(int j =0;j<ALLMeetings.size();j++){
					if(ALLMeetings.get(i).equals(ALLMeetings.get(j)) && i!=j){
						ALLMeetings.remove(i);
					}
				}
			}
			ALLMeetings.remove(null);
			for(int i = 0;i<mtable.getItems().size();i++){
				if(ALLMeetings.get(i)!=null && ALLMeetings.get(i).getDate()!=null && ALLMeetings.get(i).getContSetString()!=null){
					writer.write(ALLMeetings.get(i).getID()+",");
					writer.write(ALLMeetings.get(i).getDate()+",");
					writer.write(ALLMeetings.get(i).getContSetString()+",");
					if(ALLMeetings.get(i).getNotes()!=null){
						writer.write(ALLMeetings.get(i).getNotes()+",");
					}
					writer.write("\n");
				}
			}
			new PrintWriter("Meetings.txt").close();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	/**@param Opens Edit Window to SELECTED Meeting*/
	public void mEditViewClicked(StringMeeting lastselect){
		//TextFields and ID Label

		//

		//Meeting Date TextField
		mViewDate = new TextField();
		mViewDate.setPromptText("Meeting Date");

		//Contact Notes TextField
		mViewNotes = new TextField();
		mViewNotes.setPromptText("Notes (Optional)");




		Label ViewIDInput = new Label();
		ViewIDInput.setPadding(new Insets(5,10,5,10));
		ViewIDInput.setText(lastselect.getID()+"");

		//************Text Fields**************//
		//All Text Fields (Defining & Instantiation)
		TextField ViewDateInput = new TextField(lastselect.getDate());
		ViewDateInput.setMaxWidth(150);

		TextField ViewNotesInput = new TextField(lastselect.getNotes());
		ViewNotesInput.setMaxWidth(150);

		//Defines TextField VBox
		VBox VBoxTextFields = new VBox();
		VBoxTextFields.setPadding(new Insets(10,10,10,10));
		VBoxTextFields.getChildren().addAll(ViewIDInput,ViewDateInput,ViewNotesInput);
		//************LABELS**************//
		//ALL Labels (Defining & Instantiation)
		Label ViewIDLabel = new Label("ID:");
		ViewIDLabel.setMaxWidth(100);

		Label ViewDateLabel = new Label("Date:");
		ViewDateLabel.setMaxWidth(100);
		Label ViewNotesLabel = new Label("Notes:");
		ViewNotesLabel.setMaxWidth(100);

		//*******TABLE COLUMNS*******//
		//ID column
		TableColumn<StringContact, Integer> IDColumn_a = new TableColumn<>("ID");
		IDColumn_a.setMinWidth(100);
		IDColumn_a.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Name column
		TableColumn<StringContact, String> nameColumn_a = new TableColumn<>("Name");
		nameColumn_a.setMinWidth(200);
		nameColumn_a.setCellValueFactory(new PropertyValueFactory<>("name"));

		//Notes column
		TableColumn<StringContact, String> NotesColumn_a = new TableColumn<>("Notes");
		NotesColumn_a.setMinWidth(200);
		NotesColumn_a.setCellValueFactory(new PropertyValueFactory<>("notes"));

		//ID column SELECTED
		TableColumn<StringContact, Integer> IDColumn_s = new TableColumn<>("ID");
		IDColumn_s.setMinWidth(100);
		IDColumn_s.setCellValueFactory(new PropertyValueFactory<>("iD"));

		//Name column SELECTED
		TableColumn<StringContact, String> nameColumn_s = new TableColumn<>("Name");
		nameColumn_s.setMinWidth(200);
		nameColumn_s.setCellValueFactory(new PropertyValueFactory<>("name"));

		//Notes column SELECTED
		TableColumn<StringContact, String> NotesColumn_s = new TableColumn<>("Notes");
		NotesColumn_s.setMinWidth(200);
		NotesColumn_s.setCellValueFactory(new PropertyValueFactory<>("notes"));

		//*******ARRAYLIST CONTROLLING BUTTONS*******//
		//cAdd,cRemove,cEdita,cView.
		Button cAdd = new Button("Add");
		cAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mView_ctable_s.getItems().add(mView_Contact_a);
			}
		});
		Button cRemove = new Button("Remove");
		cRemove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mView_ctable_s.getItems().remove(mView_Contact_s);
			}
		});

		//*******PROGRAM BUTTONS*******//
		//mSave,mClose;
		//sClose and the "X" Button DON'T do the same thing except the "X" Save to .txt then close the window.
		Button mClose = new Button("Save and Exit");
		mClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				mViewSaveClicked();
				mViewWindow.close();
			}});
		//*******CWINDOW LAYOUT*******//
		//Horizontal Box for Adding and Removing Contacts that goes at bottom of CWINDOW
		HBox addRemoveHBox = new HBox();
		addRemoveHBox.setPadding(new Insets(10,10,10,10));
		addRemoveHBox.setSpacing(10);
		addRemoveHBox.getChildren().addAll(cAdd,cRemove);

		//Selected Contact Table Instantiated
		mView_ctable_s = new TableView<>();
		mView_ctable_s.setItems(getStringContacts(StringtoArray(lastselect.getContSetString())));
		mView_ctable_s.getColumns().addAll(IDColumn_s, nameColumn_s, NotesColumn_s);
		//ALL Contact Table Instantiated
		mView_ctable_a = new TableView<>();
		mView_ctable_a.setItems(getContactsObsList());
		mView_ctable_a.getColumns().addAll(IDColumn_a, nameColumn_a, NotesColumn_a);
		//Selected Contact Listener (mView_Contact_s;)
		mView_ctable_s.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				mView_Contact_s = newValue;
			}else if(oldValue != null) {
				mView_Contact_s = oldValue;
			}
		});
		//Selected Contact Listener (mView_Contact_a)
		mView_ctable_a.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				mView_Contact_a = newValue;
			}else if(oldValue != null) {
				mView_Contact_a = oldValue;
			}
		});

		//Top Bar for MWINDOW
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10,10,10,10));
		topBar.setSpacing(10);
		topBar.getChildren().addAll(mClose);

		//ADD Label,Add,Button
		Label cAllLbl = new Label("All Contacts: ");
		VBox AllContBody = new VBox();
		AllContBody.setPadding(new Insets(10,10,10,10));
		AllContBody.setSpacing(10);
		AllContBody.getChildren().addAll(cAllLbl,cAdd,mView_ctable_a);

		Label cSelectLbl = new Label("Contacts Attending Meeting: ");
		VBox SelectContBody = new VBox();
		SelectContBody.setPadding(new Insets(10,10,10,10));
		SelectContBody.setSpacing(10);
		SelectContBody.getChildren().addAll(cSelectLbl,cRemove,mView_ctable_s);

		//Centre part of MWINDOW
		HBox centreHBox = new HBox();
		centreHBox.setPadding(new Insets(10,10,10,10));
		centreHBox.setSpacing(10);
		centreHBox.getChildren().addAll(AllContBody,SelectContBody);

		//Defines Label VBox
		VBox VBoxLabels = new VBox();
		VBoxLabels.setPadding(new Insets(15,10,10,10));
		VBoxLabels.setSpacing(10);
		VBoxLabels.getChildren().addAll(ViewIDLabel,ViewDateLabel,ViewNotesLabel);

		//********All Labels and TextFields as one********//
		HBox MeetingViewer = new HBox();
		MeetingViewer.setPadding(new Insets(10,10,10,10));
		MeetingViewer.getChildren().addAll(VBoxLabels,VBoxTextFields);

		//Main Container for MeetingMenu
		VBox root = new VBox();
		root.getChildren().addAll(topBar,MeetingViewer,centreHBox);

		//*******MVIEWWINDOW SHOW*******//
		Scene scene = new Scene(root);
		mViewWindow = new Stage();
		mViewWindow.setTitle("Meeting - Viewer");
		mViewWindow.setScene(scene);
		mViewWindow.show();
	}
	/**@param Saves changes from Edit Window to 'Meeting Table'*/
	public void mViewSaveClicked(){
		for(int i = 0;i<mtable.getItems().size();i++){

			if((mtable.getItems().get(i).getID()+"").equals(mlastselected.getID()+"")){
				StringMeeting meet = mlastselected;
				meet.setDate(mViewDate.getText());
				meet.setContSetString(ContactListToString(mView_ctable_s.getItems()));
				meet.setNotes(mViewNotes.getText());
				mtable.getItems().set(i, meet);
				mtable.setItems(mtable.getItems());
			}
		}
	}

	//** Conversion Methods***************************************/
	/**@param converts String to int Array*/
	public int[] StringtoArray(String StringIDs){
		String[] StringIds = StringIDs.split("/");
		int[]a = new int[StringIds.length];
		for(int i=0; i<StringIds.length;i++){
			if(isInteger(StringIds[i])){
				a[i] = Integer.parseInt(StringIds[i]);
			}
		}
		return a;
	}
	/**@param Converts ContactIDs(from mtable AKA Meeting Table)-> ObservableList*/
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
	/**@param Converts ObservableList -> ContactIDs(to then change mtable AKA the Meeting Table)*/
	public String ContactListToString(ObservableList<StringContact> ContactsToAdd){

		//Removes Duplication
		for(int i =0;i<ContactsToAdd.size();i++){
			for(int j =0;j<ContactsToAdd.size();j++){
				if(ContactsToAdd.get(i).getID()==ContactsToAdd.get(j).getID() && i!=j){
					ContactsToAdd.remove(j);
				}
			}
		}

		//Converts ObservableList to String
		String ContSetString ="";
		for(StringContact c:ContactsToAdd){
			ContSetString = ContSetString + c.getID() + "/";
		}
		return ContSetString;
	}
	/**@param Input Validation if string is an Integer*/
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException |NullPointerException e) { 
			return false;
		}
		return true;
	}
}