import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleCalendar {
	private static final String FILE_NAME = "myCalendar.txt";

	public static void main(String[] args) {
		// Set up elements for display
		JFrame window = new JFrame();
		window.setLayout(new BorderLayout());
		window.setSize(900,  900);
		JPanel panel1 = new JPanel();		
		JPanel panel2 = new JPanel();
		
		// Create/load the models
		final DayModel dayToLookAt = new DayModel(MyCalendar.getTodayCalifornianCalendar());// Model for the current day
		final MyCalendar cal = load();												// Model storing the events

		// Load the views
		final MonthDisplay monthToLookAtView = new MonthDisplay(dayToLookAt);
		final DayDisplay dayToLookAtView = new DayDisplay(cal, dayToLookAt);
		dayToLookAt.addView(monthToLookAtView);
		dayToLookAt.addView(dayToLookAtView);
		panel2.add(monthToLookAtView);		// View the month (also a controller for calendar of events, through the create button, and controller for the month, through the clickable days))
		panel2.add(dayToLookAtView);				// View for the calendar events and dayToLookAt (uses info sorted in dayToLookAt to display info from calendar events)
		
		// Create Controller for the view of the month ( <-- and --> buttons change dayToLookAt and notify the views)
		JButton previous = new JButton("<");
		previous.addActionListener(e -> {
			// Get calendar
			Calendar c = dayToLookAt.getDay();
			c.add(Calendar.DATE,  -1);			// Go to the next day
			dayToLookAt.setDay(c);				// Set the new day in the model, the model's constructor will notify the views
		});
		
		JButton next = new JButton (">");
		next.addActionListener(e -> {
			// Get calendar
			Calendar c = dayToLookAt.getDay();
			c.add(Calendar.DATE,  1);			// Go to the next day
			dayToLookAt.setDay(c);				// Set the new day in the model, the model's constructor will notify the views
		});
		panel1.add(previous);
		panel1.add(next);

		// Create the quit button (saves the calendar event to memory)
		JButton quit = new JButton("Quit");
		panel1.add(quit);
				
		window.add(panel1, BorderLayout.PAGE_START);
		window.add(panel2, BorderLayout.CENTER);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		
	}
	
	/**
	 * Load a calendar from memory
	 * @return a reference to a calendar (read from memory or newly created if non available)
	 */
	private static MyCalendar load() {	
		MyCalendar cal = null;
		
		// Try to load the calendar from memory
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))){
			cal = (MyCalendar) ois.readObject(); // cast is needed.
			ois.close();
		// Create a new calendar if could not load it
		} catch (Exception e) {
			cal = new MyCalendar();
		}
		
		return cal;
	}

}
