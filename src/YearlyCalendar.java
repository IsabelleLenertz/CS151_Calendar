import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represent a calendar for a year
 * @author isabelle Delmas
 *
 */
public class YearlyCalendar implements Comparable<YearlyCalendar>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -588723263051515363L;

	public static final int DAYS_IN_A_WEEK = 7;
	public static final int MONTHS_IN_YEAR = 12;

	private Set<Event>[] eventsByMonths;
	private int year;

	
	/**
	 * Create a calendar for a specific year
	 * @param y year the calendar is representing
	 */
	@SuppressWarnings("unchecked")
	public YearlyCalendar(int y) {
		this.year = y;
		eventsByMonths = (Set<Event>[]) new Set<?>[MONTHS_IN_YEAR];
		// Initializes all the sets using a tree set.
		for(int i = 0; i < MONTHS_IN_YEAR; i++) {
			eventsByMonths[i] = new TreeSet<Event>();
		}
	}
	
	/**
	 * Add an event in the appropriate set
	 * @param event event to add to the calendar
	 * @return true if the event was successfully added
	 */
	public boolean addEvent(Event event) {
		boolean success = true;
		
		// Check if there is a conflicting event
		ArrayList<Event> list = this.getOneDayEvents(event.getEventDate());
		int i = 0;
		
		// Look at all the events from that day, in order
		while (i < list.size() && success) {
			// if the event start after another
			if (event.getEventStart().isAfter(list.get(i).getEventStart())) {
				// but before its end
				if (list.get(i).getEventEnd() != null) {
					if (event.getEventStart().isBefore(list.get(i).getEventEnd())) {
						// signify failure
						success = false;
					}
				}
			}
			i++;
		}
		// If no conflict was found
		if (success) {
			eventsByMonths[event.getEventDate().get(Calendar.MONTH)].add(event);
		}
		
		return success;
	}
	
	/**
	 * Get a set with all the events of the month
	 * @param month month to look at the events
	 * @return a set of events happening that month
	 */
	public Set<Event> getMonthlyEvents(Event.Month month ) {
		return eventsByMonths[month.ordinal()];
	}
	
	public int size() {
		int size = 0;
		for (Set<Event> list : eventsByMonths) {
			size += list.size();
		}
		return size;
	}
	
	/**
	 * Return true if there are no event in that calendar
	 * @return true if there are no event in that calendar
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	
	/**
	 * Get all the events occurring on a single day
	 * @param day day to look for
	 * @return a list of all the events occurring that day
	 */
	public ArrayList<Event> getOneDayEvents(Calendar day){
		ArrayList<Event> list = new ArrayList<>();
		int date = day.get(Calendar.DATE);
		int month = day.get(Calendar.MONTH);
		Iterator<Event> it = eventsByMonths[month].iterator();
		Event temp;
		
		// Get the first event
		if(it.hasNext()) {
			temp = it.next();	
		} else {
			return list;
		}
		// Check all the event the the last one visited
		while (it.hasNext() && temp.getEventDate().get(Calendar.DATE) < date) {
			if (temp.getEventDate().get(Calendar.DATE) == date){
				list.add(temp);
			}
			temp = it.next();
		}
		// Check the last one
		if (temp.getEventDate().get(Calendar.DATE) == date){
			list.add(temp);
		}
		
		
		return list;
	}
	
	/**
	 * Get the year the calendar represents
	 * @return year of the calendar
	 */
	public int getYear() {
		return this.year;
	}


	/**
	 * Compare to calendar
	 * @param o other yearly calendar to compare with
	 * @return negative number if this comes before other, 0 is same year, positive integer is this comes first.
	 */
	@Override
	public int compareTo(YearlyCalendar o) {
		// TODO Auto-generated method stub
		return this.year - o.year;
	}
	
	public String toString() {
		String returnValue = "";
		for (int i = 0; i < MONTHS_IN_YEAR; i++) {
			// Append to the string
			for (Event event : eventsByMonths[i]) {
				returnValue += event.toString() + "\n";
			}
		}
		return returnValue;
	}
	
	/**
	 * Remove a specific event from the calendar
	 * Does nothing if the event is not in the calendar.
	 * @param event reference to the event to remove
	 */
	public void remove(Event event) {
		eventsByMonths[event.getEventDate().get(Calendar.MONTH)].remove(event);		
	}
}
