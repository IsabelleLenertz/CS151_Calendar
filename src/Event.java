import java.io.Serializable;
import java.time.LocalTime;
import java.util.Calendar;

public final class Event implements Comparable<Event>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 304520817428918788L;

	public enum Day {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY 
	}
	public enum DayAbbreviation{
		Su, Mo, Tu, We, Th, Fr, Sa
	}
	public enum Month{
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEMPTEMBER, NOVEMBER, DECEMBER
	}
	
	private Calendar date;
	private String name;
	private LocalTime start;
	private LocalTime end;
	
	public Event(Calendar nDate, String nName, LocalTime nStart, LocalTime nEnd) {
		this.date = nDate;
		this.name = nName;
		this.start = nStart;
		this.end = nEnd;
	}
	
	public LocalTime getEventStart() { return this.start; }
	public LocalTime getEventEnd() { return this.end; }
	public Calendar getEventDate() { return this.date; }
	
	/**
	 * Format of the String returned by the method()
	 * Wednesday, Feb 21, 2018 
	 * Dr. Kim's office hour 9:15 - 10:15 

	 */
	public String toString() { 
		return Day.values()[date.get(Calendar.DAY_OF_WEEK)-1].toString() + ", " 
				+ Month.values()[date.get(Calendar.MONTH)-1].toString().substring(0, 3);
		
	}

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
