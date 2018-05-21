import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HourDisplay extends JPanel {
	
	private static final long serialVersionUID = -4120518469674405986L;
	private JTextArea hour;
	private JTextArea eventDisplay;
	private Calendar date;
	private MyCalendar cal;
	private LocalTime start;
	private LocalTime end;
	
	public HourDisplay(int hour24, Calendar day, MyCalendar calendar) {
		// Saves the reference to the model
		this.cal = calendar;
		
		// Get a horizontal layout manager
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Display the appropriate time of day
		String time = "";
		if (hour24 > 12) {
			time = (hour24-12) + "pm";
		} else {
			time = hour24+"am";
		}
		hour = new JTextArea(time);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 40;
		c.anchor = GridBagConstraints.WEST;
		hour.setEditable(false);
		this.add(hour, c);
		
		// Display the event happening within that hour
		JTextArea eventDisplay= new JTextArea();
		String eventString = ".........................................................................";
		date = day;
		ArrayList<Event> listOfEvents = cal.getDailyEvents(date);
		this.start = LocalTime.of(hour24,  00);
		this.end = LocalTime.of( (hour24 + 1) % 24,  0);
		for(Event element : listOfEvents) {
			// Get all the events happening within that hour in one string
			if(element.getEventStart().isAfter(this.start) && element.getEventStart().isBefore(end)) {
				eventString += element.toString() + "/n";
			}
		}
		// Display the string with the info about all the events
		eventDisplay.setText(eventString);

		eventDisplay.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 200;

		this.add(eventDisplay, c);
		
		
	}
	

}
