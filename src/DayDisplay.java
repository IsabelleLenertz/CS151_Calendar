import java.awt.Color;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;


public class DayDisplay extends JPanel{

	private static final long serialVersionUID = -929261367290895680L;
	private static final int HOURS_IN_DAY = 24;
	Calendar today;		
	MyCalendar calendar;
	JTextArea dateDisplay;
	JLabel separator;
	JScrollPane scroll;
	HourDisplay[] events; 
	
	
	public DayDisplay(MyCalendar cal) {
		this.calendar = cal;
		today = MyCalendar.getTodayCalifornianCalendar();
		
		// Get a layout manager (vertical display)
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Create the display of the date
		String date =  Event.Day.values()[today.get(Calendar.DAY_OF_WEEK)-1].toString() + ", " 
				+ Event.Month.values()[today.get(Calendar.MONTH)].toString().substring(0, 1)
				+ Event.Month.values()[today.get(Calendar.MONTH)].toString().substring(1, 3).toLowerCase()+ " "
				+ today.get(Calendar.DATE) + ", " + today.get(Calendar.YEAR) + "   ";
		this.dateDisplay = new JTextArea(date);
		this.dateDisplay.setEditable(false);
		this.add(this.dateDisplay);
		
		// Create the separator
		this.separator = new JLabel();
		separator.setBackground(Color.GRAY);
		separator.setOpaque(true);
		//separator.setText("???");
		this.add(this.separator);
		
		// Create the scroll pane with the events display
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));
		events = new HourDisplay[HOURS_IN_DAY];
		for(int i = 0; i < HOURS_IN_DAY; i++) {
			events[i] = new HourDisplay(i, this.today, this.calendar);
			scrollPanel.add(events[i]);
		}
		this.scroll = new JScrollPane(scrollPanel);
		this.add(scroll);
	}
	
	/**
	 * Notify the view to change the day to display
	 * @param newDay
	 */
	public void changeDay(Calendar newDay) {
		this.today = newDay;
		repaint();
	}
	
	/**
	 * Notify the view the the current model has changed
	 * Update the display
	 */
	public void CalendarHasChanged() {
		// Update display
		this.repaint();
	}
	

}
