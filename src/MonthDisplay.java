import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * JPanel display the left menu of the calendar application as follow
 * [CREATE BUTTON]
 * [MONTH] --> is today's month by default
 * [TABLE WITH INDIVIDUAL DAYS] --> today is selected by default
 * This JPanel does not display any information specific to the calendar, but allows for modifications
 * ie, is the Controller in the MVC pattern
 * @author Isabelle Delmas
 * @date 05/10/2018
 * @update 
 *
 */
public class MonthDisplay extends JPanel implements View{
	
	private static final long serialVersionUID = -344928104922528344L;
	private final static int ROWS = 6;
	private final static int COLS = 7;
	private JButton createBtn;
	private JTextArea currentMonth;
	private DayModel currentDay;
	private JList<String> listDays;
	private MyCalendar eventCalendar;
	
	

	/**
	 * Create a JPanel display the current Month
	 * All buttons are created active
	 */
	public MonthDisplay(MyCalendar cal, DayModel today) {
		// Saves reference to the model to modify it when needed
		this.currentDay = today;
		this.eventCalendar = cal;
		
		// Set the layout manager (vertical display)
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Add the Create Button
		this.createBtn = new JButton("Create");
		this.add(this.createBtn);
		// TODO define MouseListener for JButton
		// Controller for the event calendar
		this.createBtn.addActionListener(e->{
			// Open a controller window
			EventsControllerPopUp popup = new EventsControllerPopUp();
			
			
		});
		
		// Add display of the current Month
		this.currentMonth = new JTextArea (Event.Month.values()[this.currentDay.getDay().get(Calendar.MONTH)].toString()) ;
		this.currentMonth.setEditable(false);
		this.add(currentMonth);
		
		// Create Jlist and populate it with all the day existing in that month
		// Get the list of days as an array, blank filed are set to ""
		String[] arrayDays = getArrayOfDays();

		listDays = new JList<String>(arrayDays);
		this.listDays.setCellRenderer(new MonthDisplay.CellRenderer());
		this.listDays.setPrototypeCellValue("123");
		this.listDays.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.listDays.setVisibleRowCount(COLS);
		// Controller for the dayToLookAt
		this.listDays.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String selected = listDays.getSelectedValue();
				try {
					int day = Integer.parseInt(selected);
					// Get the calendar
					Calendar cal = currentDay.getDay();
					cal.set(Calendar.DAY_OF_MONTH,  day);
					// Set the new calendar
					currentDay.setDay(cal);			// Change model, the model's mutator will notify the views
					
				} catch (Exception err) {
					// do nothing
				}
			}
		});
		this.add(listDays);
	
	}
	
	private String[] getArrayOfDays() {
		String[] arrayDays = new String[49];
		int index;
		Calendar today = (Calendar) this.currentDay.getDay().clone();

		for(index = 0; index < COLS; index++) {
			arrayDays[index] = Event.DayAbbreviation.values()[index].toString();
		}
		// get first day of the month
		int day = 1;
		today.set(Calendar.DAY_OF_MONTH,  day);
		
		// Start filling it at the right index
		index += today.get(Calendar.DAY_OF_WEEK) - 1;
		
		// Fill the empty calendar days
		for(; index < this.currentDay.getDay().get(Calendar.DAY_OF_WEEK); index++) {
			arrayDays[index] = ".";
		}
		// Fill in the fields with the days
		for(; day <= this.currentDay.getDay().getActualMaximum(Calendar.DAY_OF_MONTH) ; index++) {
			arrayDays[index] = "" + day;
			day++;
		}
		// Fill in the last empty days
		for(;index < ROWS*COLS; index++) {
			arrayDays[index] = ".";
		}
		return arrayDays;
	}
	
	/**
	 * Notify a change in display
	 * @param day reference to the new day to display
	 */
	public void updateDisplay() {
		// Look at the new day and updates the table of days
		this.listDays.setListData(this.getArrayOfDays());
		// Look at the new day and update the display of the month
		this.currentMonth.setText(Event.Month.values()[currentDay.getDay().get(Calendar.MONTH)].toString());
		repaint();
	}
	
	private class EventsControllerPopUp extends JFrame{
		
		public EventsControllerPopUp() {
			this.setLayout(new GridLayout(0, 1));
			this.setSize(300,  100);

			// Create the frame elements
			JTextField eventTitle = new JTextField();
			eventTitle.setText("Untitled event");
			eventTitle.setSelectionStart(0);
			eventTitle.setSelectionEnd(eventTitle.getText().length());
			
			JPanel bottomPanel = new JPanel();
			JTextField eventStart = new JTextField();
			eventStart.setText("10:00");
			JTextField eventEnd = new JTextField();
			eventEnd.setText("10:59");
			JButton saveBtn = new JButton("SAVE");
			bottomPanel.add(eventStart);
			bottomPanel.add(eventEnd);
			bottomPanel.add(saveBtn);
			
			// Add listener to the save button
			saveBtn.addActionListener(e->{
				Event event = null;
				// Read user input
				String eventName = eventTitle.getText();
				String startTimeStr = eventStart.getText();
				String endTimeStr = eventEnd.getText();
				int startHours;
				int startMinutes;
				int endHours;
				int endMinutes;
				LocalTime startLocalTime = null;
				LocalTime endLocalTime = null;

				try {
					startHours = Integer.parseInt(startTimeStr.substring(0, 2));
					startMinutes = Integer.parseInt(startTimeStr.substring(3, 5));
					endHours = Integer.parseInt(endTimeStr.substring(0, 2));
					endMinutes = Integer.parseInt(endTimeStr.substring(3, 5));
					
					startLocalTime = LocalTime.of(startHours, startMinutes);
					endLocalTime = LocalTime.of(endHours, endMinutes);
					event = new Event(currentDay.getDay(), eventName, startLocalTime, endLocalTime);

				} catch(Exception err){ 
					// Do nothing
				}
				
				
				if (event != null) {
					if (eventCalendar.addEvent(event)) {
						System.out.println(event);
						this.setVisible(false); //you can't see me!
						this.dispose(); //Destroy the JFrame object
					} else {
						// Display error message
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame,
							    "You already have an event at that time. Please choose another time frame",
							    "Conflit Error",
							    JOptionPane.ERROR_MESSAGE);					
						}
				}
			});
				// Update events
			
				// Close pop-up window
			
			this.add(eventTitle);
			this.add(bottomPanel);
			this.setVisible(true);
			this.pack();			
		}
	}
	
	// Define the display of the JList
	private class CellRenderer extends JLabel implements ListCellRenderer<String> {


		private static final long serialVersionUID = -8766576576989171271L;

		/**
		 * Configures the JLabel created for each String in the JList
		 */
		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list,
				String value,
				int index,
				boolean isSelected,
				boolean cellHasFocus){
			// TODO Auto-generated method stub
	        setText(value);
	        
	        // Get the day
	        int day = 0;
	        try {
	        	day = Integer.parseInt(value);
	        } catch (Exception e) {
	        	// do nothing
	        }
	        if (day == currentDay.getDay().get(Calendar.DAY_OF_MONTH)) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        setEnabled(list.isEnabled());
	        setFont(list.getFont());
	        setOpaque(true);
	        return this;
		}
	}

}
