import java.time.LocalTime;
import java.util.Calendar;

public final class Event {
	
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
	public String toString() { return 

}
