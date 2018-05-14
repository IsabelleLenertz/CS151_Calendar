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
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		hour.setEditable(false);
		this.add(hour, c);
		
		// Display the event happening within that hour
		JTextArea eventDisplay= new JTextArea();
		date = day;
		ArrayList<Event> listOfEvents = cal.getDailyEvents(date);
		this.start = LocalTime.of(hour24,  00);
		this.end = LocalTime.of( (hour24 + 1) % 24,  0);
		for(Event element : listOfEvents) {
			if(element.getEventStart().isAfter(this.start) && element.getEventStart().isBefore(end)) {
				eventDisplay.setText(element.toString());
			} else {
				eventDisplay.setText("Nothing at that time of day.");
			}
		}
		eventDisplay.setEditable(false);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.LINE_START;
		this.add(eventDisplay, c);
		
		
	}
	

}
