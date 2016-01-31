package application;


public class StringMeeting{

	private int ID;
	private String Date;
	private String ContSetString;
	private String Notes;

	public StringMeeting(int ID,String Date, String ContSetString,String Notes) {
		this.setID(ID);
		this.setDate(Date);
		this.setContSetString(ContSetString);
		this.setNotes(Notes);

	}
	/*public StringMeeting(Meeting meet) {
		this.setID(meet.getID());
		this.setDate(sm.CalendartoString(meet.getDate()));
		this.setContSet(meet.getContSet());
		this.setNotes(meet.getNotes());
	}*/

	public StringMeeting(int i, String Date, String ContSetString) {
		this.setID(ID);
		this.setDate(Date);
		this.setContSetString(ContSetString);
	}

	/** @return the ID */
	public int getID() {
		return ID;
	}

	/** @param iD the iD to set */
	public void setID(int iD) {
		ID = iD;
	}

	/** @return the date */
	public String getDate() {
		return Date;
	}

	/** @param date the date to set */
	public void setDate(String date) {
		Date = date;
	}

	/** @return the contSetString */
	public String getContSetString() {
		return ContSetString;
	}

	/** @param contSetString the contSetString to set */
	public void setContSetString(String contSetString) {
		ContSetString = contSetString;
	}

	/** @return the notes */
	public String getNotes() {
		return Notes;
	}

	/** @param notes the notes to set */
	public void setNotes(String notes) {
		Notes = notes;
	}
}