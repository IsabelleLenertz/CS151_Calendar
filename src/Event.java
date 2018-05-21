import java.io.Serializable;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * Store information about a specific event
 * @author isabelle Delmas
 *
 */
public final class Event implements Comparable<Event>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 304520817428918788L;

	public enum Day {
	    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday 
	}
	public enum DayAbbreviation{
		Su, Mo, Tu, We, Th, Fr, Sa
	}
	public enum Month{
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEMPTEMBER, OCTOBER, NOVEMBER, DECEMBER
	}
	
	private Calendar date;
	private String name;
	private LocalTime start;
	private LocalTime end;
	
	/**
	 * Constructor
	 * @param nDate the day the event occurs
	 * @param nName title of the event
	 * @param nStart time the event starts on
	 * @param nEnd time the event ends on, null if no end time
	 */
	public Event(Calendar nDate, String nName, LocalTime nStart, LocalTime nEnd) {
		this.date = nDate;
		this.name = nName;
		this.start = nStart;
		this.end = nEnd;
	}
	
	/**
	 * Get the time event starts
	 * @return time the event start
	 */
	public LocalTime getEventStart() { return this.start; }
	
	/**
	 * get the end time of the event
	 * @return end time of the event, null if no end time
	 */
	public LocalTime getEventEnd() { return this.end; }
	
	/**
	 * Get the day the event occurs
	 * @return the day the event occurs
	 */
	public Calendar getEventDate() { return this.date; }
	
	/**
	 * Get the information about the event nicely formated
	 * @return a String with the info about the event
	 */
	public String toString() { 
		String returnString = name + " " + start.toString().substring(0, 5);
			
		if (end != null) {
			returnString += " - " + end.toString().substring(0, 5);
		}
		
		return returnString;
		
	}

	/**
	 * Compare two events
	 * @param other event to compare to
	 * @return a negative number if the other event comes first, a positive number if this comes first, 0 if they start at the same time.
	 */
	@Override
	public int compareTo(Event other) {		
		// Compare the year, month, and day
		int year = this.date.get(Calendar.YEAR) - other.date.get(Calendar.YEAR);
		if (year != 0) { return year; }
		int month = this.date.get(Calendar.MONTH) - other.date.get(Calendar.MONTH);
		if (month != 0) { return month; }
		int day = this.date.get(Calendar.DATE) - other.date.get(Calendar.DATE);
		if (day != 0) { return day; }
		
		// Then compare the starting time
		return this.start.compareTo(other.start);
	}

}
