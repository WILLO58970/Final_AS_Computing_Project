package application;

public class StringContact{
	private String Name;

	private int ID;

	private String Notes;

	public StringContact(){
		this.ID = 0;
		this.Name = "";
		this.Notes = "";
	}

	public StringContact(int ID, String Name, String Notes) {
		this.ID = ID;
		this.Name = Name;
		this.Notes = Notes;
	}

	public StringContact(StringContact stringContact) {
		this.ID = ID;
		this.Name = Name;
		this.Notes = Notes;
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return Name;
	}

	public String getNotes() {
		return Notes;
	}
	
	public void setID(int Id) {
		this.ID = Id;
	}
	
	public void setName(String name) {
		this.Name = name;
	}

	public void setNotes(String notes) {
		this.Notes = notes;
	}
}
