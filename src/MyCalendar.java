import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Calendar for an undetermined period of time (years are added on the go)
 * @author isabelle Delmas
 *
 */
public class MyCalendar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8135952509631155591L;
	private Map<Integer, YearlyCalendar> calendar;;

	/**
	 * Default constructor
	 */
	public MyCalendar() {
		this.calendar = new TreeMap<Integer, YearlyCalendar>();
	}
	
	/**
	 * Constructor adding a event at creation
	 * @param event event to add at the calendar
	 */
	public MyCalendar(Event event) {
		this();
		this.addEvent(event);
	}
	
	/**
	 * Add an event to the calendar
	 * precondition: event is non null
	 * @param event event to add to the calendar
	 * @return true if the event was successfully added
	 */
	public boolean addEvent(Event event) {
		boolean success;
		
		// Check if the year already exists, if not create the year
		Integer year = event.getEventDate().get(Calendar.YEAR);
		YearlyCalendar cal = this.calendar.get(year);
		if (cal == null) {
			cal = new YearlyCalendar(year);
		}
		success = cal.addEvent(event);
		this.calendar.put(year, cal);
		
		return success;
	}
	
	/**
	 * Get a set with all the event occurring that month
	 * @param year year of occurrence
	 * @param month month of occurrence
	 * @return a set with all the events
	 */
	public Set<Event> getMonthlyEvents(int year, Event.Month month){
		YearlyCalendar cal = this.calendar.get(year);
		if(cal != null) {
			return this.calendar.get(year).getMonthlyEvents(month);
		}
		return null;
	}
	
	/**
	 * Get all the events occurring on a specific day
	 * @param day day the events are occurring on (only Year, Month, and Date matter)
	 * @return an arrayList with all the events
	 */
	public ArrayList<Event> getDailyEvents(Calendar day){
		int year = day.get(Calendar.YEAR);
		YearlyCalendar cal = this.calendar.get(year);
		if (cal != null) {
			return cal.getOneDayEvents(day);
		}
		return null;
	}
	
	
	/**
	 * Display all the events in chronological order
	 */
	public String toString() {
		String returnValue = "";
		for (Map.Entry<Integer, YearlyCalendar> entry : calendar.entrySet()) {
			returnValue += entry.getKey() + "\n" 
					+ entry.getValue().toString();
		}
		return returnValue;
	}
	
	/**
	 * Remove an event from the calendar.
	 * Does nothing if the event is not in the calendar
	 * @param event event to remove
	 */
	public void remove(Event event) {
		int year = event.getEventDate().get(Calendar.YEAR);
		YearlyCalendar cal = this.calendar.get(year);
		if (cal != null) {
			cal.remove(event);
			if(cal.isEmpty()) {
				this.calendar.remove(year);
			}
		}
	}

	


}
