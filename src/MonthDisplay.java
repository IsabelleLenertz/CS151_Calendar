import java.awt.Component;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
public class MonthDisplay extends JPanel {
	
	private static final long serialVersionUID = -344928104922528344L;
	private final static int ROWS = 6;
	private final static int COLS = 7;
	//private MyCalendar calendar;
	JButton createBtn;
	JTextArea currentMonth;
	private Calendar currentDay;
	JList<String> listDays;
	
	

	/**
	 * Create a JPanel display the current Month
	 * All buttons are created active
	 */
	public MonthDisplay(Calendar today) {
		// Saves reference to the model to modify it when needed
		this.currentDay = today;
		
		// Set the layout manager (vertical display)
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Add the Create Button
		this.createBtn = new JButton("Create");
		this.add(this.createBtn);
		// TODO define MouseListener for JButton
		
		// Add display of the current Month
		this.currentMonth = new JTextArea (Event.Month.values()[this.currentDay.get(Calendar.MONTH)].toString()) ;
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
		// TODO define MouseListener for JList
		this.add(listDays);
	
	}
	
	private String[] getArrayOfDays() {
		String[] arrayDays = new String[49];
		int index;
		Calendar today = (Calendar) this.currentDay.clone();

		for(index = 0; index < COLS; index++) {
			arrayDays[index] = Event.DayAbbreviation.values()[index].toString();
		}
		// get first day of the month
		int day = 1;
		today.set(Calendar.DAY_OF_MONTH,  day);
		
		// Start filling it at the right index
		index += today.get(Calendar.DAY_OF_WEEK) - 1;
		
		// Fill the empty calendar days
		for(; index < this.currentDay.get(Calendar.DAY_OF_WEEK); index++) {
			arrayDays[index] = ".";
		}
		// Fill in the fields with the days
		for(; day <= this.currentDay.getActualMaximum(Calendar.DAY_OF_MONTH) ; index++) {
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
		this.currentMonth.setText(Event.Month.values()[currentDay.get(Calendar.MONTH)].toString());
		repaint();
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
	        if (day == currentDay.get(Calendar.DAY_OF_MONTH)) {
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
