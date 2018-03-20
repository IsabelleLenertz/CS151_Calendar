import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.TreeSet;

/**
 * Test the personal calendar
 * @author isabelle Delmas
 *
 */
public class CalendarTester {	
	private static final String FILE_NAME = "myCalendar.txt";

	public static void main(String[] args) {
		printCurrentMonth();
		MyCalendar calendar = null;
		char choice = 0;
		
		
		System.out.println("Any modification to the calendar (add, delete); will automaticaly be saved to disk.\n"
				+ "Do not forget to load the existing file first if your do not want to overwrite it.");

		
		while (choice != 'Q') {
			choice = displayMainMenu();
			
			switch(choice) {
				case('L'):
					calendar = load();
					break;
				case('V'):
					System.out.println("v");
					viewBy(calendar);
					break;
				case('C'):
					calendar = create(calendar);
					break;
				case('G'):
					goTo(calendar);
					break;
				case('E'):
					if (calendar != null) {
						System.out.println(calendar);
					}
					break;
				case('D'):
					delete(calendar);
					break;
			}
		}
		// Update the file before exiting
		save(calendar);
		
	}

	/**
	 * Print a view of the current month
	 * Helper function for main()
	 */
	private static void printCurrentMonth() {
		Calendar calendar = getTodayCalifornianCalendar();
		String currentMonth = Event.Month.values()[calendar.get(Calendar.MONTH)].toString();

		//TODO: center header text
		System.out.println( currentMonth + " " + calendar.get(Calendar.YEAR));
		for (Event.DayAbbreviation d : Event.DayAbbreviation.values()) {
			System.out.print(d + " " );
		}
		System.out.print("\n");
		

		int today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int currentDay = 1;
		String dayNumber;
		for(int i = 0; i < dayOfWeek; i++) {
			System.out.print("   ");
		}
		
		while(currentDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			while(dayOfWeek < YearlyCalendar.DAYS_IN_A_WEEK) {
				dayNumber = String.format("%-3s", currentDay);
				if (currentDay != today) {
					System.out.print(dayNumber);
				} else {
					System.out.print("{" + currentDay + "}");

				}
				dayOfWeek++;
				currentDay++;
			}
			System.out.print("\n");
			dayOfWeek = 0;
		}

		
		
	}
	
	/**
	 * Get a Gregorian calendar setup with the pacific time zone and the daylight saving time
	 * the day is today and the time is the time of the run
	 * Helper function for main()
	 * @return today's calendar
	 */
	private static Calendar getTodayCalifornianCalendar() {
		 // Get local time zone for California (Pacific Standard Time)
		 String[] timeZoneId = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
		 // if no timeZone was returned, exit program.
		 if (timeZoneId.length == 0) {
		     System.out.println("Could not initiate the time zone, exiting program");
			 System.exit(0);
		 }
		 SimpleTimeZone pacificTimeZone = new SimpleTimeZone(-8 * 60 * 60 * 1000, timeZoneId[0]);
		
		 // Set up rules for Daylight Saving Time
		 pacificTimeZone.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		 pacificTimeZone.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		
		 // create a GregorianCalendar with the Pacific Daylight time zone and the current date and time
		 Calendar calendar = new GregorianCalendar(pacificTimeZone);
		 Date runTime = new Date();
		 calendar.setTime(runTime);
		 
		 return calendar;
	}

	/**
	 * Display the main menu and ask the user for a choice
	 * Helper function for main()
	 * @return a char with the user's choice
	 */
	private static char displayMainMenu() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		char userInput = 0;
		
		while( userInput != 'L' && userInput != 'V' && userInput != 'C' && userInput != 'G' && userInput != 'E' && userInput != 'D' && userInput != 'Q' ) {
			System.out.println("Select one of the following options: \n" + 
					"[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
			userInput = in.nextLine().toUpperCase().charAt(0);
			if(userInput != 'L' && userInput != 'V' && userInput != 'C' && userInput != 'G' && userInput != 'E' && userInput != 'D' && userInput != 'Q') {
				System.out.println("Invalide input.");
			}
		}
		return userInput;
	}
	
	/**
	 * Load a calendar from memory
	 * @return null if the calendar was not read, else a reference to the calendar into memory
	 */
	private static MyCalendar load() {	
		MyCalendar cal = null;
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CalendarTester.FILE_NAME))){
			cal = (MyCalendar) ois.readObject(); // cast is needed.
			ois.close();
			System.out.println("Calendar loaded from memory");

		} catch (Exception e) {
			System.out.println("You need to create and save a calendar first.");
		}

		return cal;
	}
	
	/**
	 * Get input from the user to create a new event
	 * Add the event to the calendar if valid
	 * Save to disk if the calendar was modified
	 * Helper function for main()
	 * @param cal calendar to add the event to
	 */
	private static MyCalendar create(MyCalendar cal) {
		if (cal == null) {
			cal = new MyCalendar();
		}
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		// Get the title
		System.out.print("Title: ");
		String title = in.nextLine();
		
		// Get the date and validate input
		in.useDelimiter("[^0-9]+");
		int month = -1;
		int date = -1;
		int year = -1;
		do {
			System.out.print("Date (Format MM/DD/YYYY): ");
			
			if (in.hasNextInt()) {
				month = in.nextInt();
			}
			if (in.hasNextInt()) {
				date = in.nextInt();
			}
			if (in.hasNextInt()) {
				year = in.nextInt();
			}
			in.nextLine();
			
			// Create a new event
			Calendar day = getTodayCalifornianCalendar();
			try{
				day.set(Calendar.YEAR,  year);
				day.set(Calendar.MONTH, month-1);
				day.set(Calendar.DATE, date);
			} catch (ArrayIndexOutOfBoundsException e) {
				year = -1;
			}
		} while( (month < 1 || month > 12) && (date < 0) && year < 0);
		
		// Get starting time
		int startH = -1;
		int startM = -1;
		LocalTime start = null;
		do {
			System.out.print("Starting time (24 hours clock, HH:MM): ");
			if (in.hasNextInt()) {
				startH = in.nextInt();
			}
			if (in.hasNextInt()) {
				startM = in.nextInt();
			}
			in.nextLine();
			try {
				start = LocalTime.of(startH, startM);
			} catch (DateTimeException  e) {
				System.out.println("Invalid time.");
				// Make sure to ask user for another time
				startH = -1;
				startM = -1;
			}
		} while (startH < 0 || startM < 0);

		
		// Get ending time
		System.out.print("Ending time (24 hours clock, HH:MM, leave blank if not appropriate): ");
		Scanner endtime = new Scanner(in.nextLine());
		Integer endH = null;
		Integer endM= null;
		endtime.useDelimiter("[^0-9]+");
		if(endtime.hasNextInt()) {
			endH = endtime.nextInt();
		} 
		if (endtime.hasNextInt()) {
			endM = endtime.nextInt();
		}
		endtime.close();
		LocalTime end = null;
		try {
			if(endH != null) {
				end = LocalTime.of(endH,  endM);
			} else {
				System.out.println("No ending time recorded.");
			}
		} catch (DateTimeException e) {
			System.out.println("Invalid time. No ending time recorded.");
		}

		
		
		// Create a new event
		Calendar day = getTodayCalifornianCalendar();
		day.set(Calendar.YEAR,  year);
		day.set(Calendar.MONTH, month-1);
		day.set(Calendar.DATE, date);
		Event newEvent = new Event(day, title, start, end);
		

		// Check if the event is conflicting
		if (cal.addEvent(newEvent)) {
			// Add to calendar		
			System.out.println("The following event has been added to the calendar:");
			System.out.println(newEvent);
			
			// Save to disk
			save(cal);
		} else {
			System.out.println("Event not saved, there was a conflict with the calendar.");
		}
		 return cal;
	}
	
	/**
	 * Save the calendar to disk
	 * Helper function for main()
	 * @param cal calendar to save to disk
	 */
	private static void save(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CalendarTester.FILE_NAME)) ){
			oos.writeObject(cal);
			oos.close();
		} catch (IOException e) {
			System.out.println("Could not save the calendar to disk. Error: " + e.getMessage());
		}
	}

	/**
	 * Let the user view events in the calendar
	 * Helper function for main()
	 * @param cal calendar to look at
	 */
	private static void viewBy(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		char userInput = 0;
		
		while( userInput != 'D' && userInput != 'M') {
			System.out.println("[D]ay view or [M]onth view ? ");
			userInput = in.nextLine().toUpperCase().charAt(0);
			if(userInput != 'D' && userInput != 'M') {
				System.out.println("Invalide input.");
			}
		}
		// Get today's day and month
		Calendar date = getTodayCalifornianCalendar();
				
		if (userInput == 'D'){
			do {
				printDay(cal, date);
				do {
					System.out.println("[P]revious or [N]ext or [M]ain menu ? ");
					userInput = in.nextLine().toUpperCase().charAt(0);
				} while (userInput != 'M' && userInput != 'P' && userInput != 'N');
				if (userInput == 'N') {
					date.add(Calendar.DATE,  1);
				} else if (userInput == 'P') {
					date.add(Calendar.DATE,  -1);
				}
			} while (userInput !='M');
		}
		else if (userInput == 'M'){
			do {
				printMonth(cal, date);
				do {
					System.out.println("[P]revious or [N]ext or [M]ain menu ? ");
					userInput = in.nextLine().toUpperCase().charAt(0);
				} while (userInput != 'M' && userInput != 'P' && userInput != 'N');
				if (userInput == 'N') {
					date.add(Calendar.MONTH,  1);
				} else if (userInput == 'P') {
					date.add(Calendar.MONTH,  -1);
				}
			} while (userInput != 'M');
		}

	}
	
	/**
	 * Display all the event occurring on a specific day
	 * Helper function for main()
	 * @param cal calendar to look at
	 * @param date date to look at
	 */
	private static void printDay(MyCalendar cal, Calendar date) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		ArrayList<Event> list = cal.getDailyEvents(date);
		
		if(list == null || list.size() == 0) {
			System.out.println("Nothing for today");
			return;
		}
		
		for(Event event : list) {
			System.out.println(event);
		}
	}
	
	/**
	 * Display all the event occurring on a specific month
	 * Helper function for main()
	 * @param cal calendar to look at
	 * @param date month to look at (only the month is used)
	 */
	private static void printMonth(MyCalendar cal, Calendar date) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		Set<Event> list = cal.getMonthlyEvents(date.get(Calendar.YEAR), Event.Month.values()[date.get(Calendar.MONTH)]);
		// Convert the list of Events into a list of day (we only care when the events occur)
		Set<Integer> days = new TreeSet<Integer>();
		if(list == null || list.isEmpty()) {
			System.out.println("Nothing for this month");
			return;
		}
		
		for (Event event:list) {
			days.add(event.getEventDate().get(Calendar.DATE));
		}
		
		String currentMonth = Event.Month.values()[date.get(Calendar.MONTH)].toString();
		
		//TODO: center header text
		System.out.println( currentMonth + " " + date.get(Calendar.YEAR));
		for (Event.DayAbbreviation d : Event.DayAbbreviation.values()) {
			System.out.print(d + " " );
		}
		System.out.print("\n");
		
		// Make sure the original date is not modified
		Calendar calendar = (Calendar) date.clone();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int currentDay = 1;
		for(int i = 0; i < dayOfWeek; i++) {
			System.out.print("   ");
		}
		
		while(currentDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			while(dayOfWeek < YearlyCalendar.DAYS_IN_A_WEEK) {
				if (days.contains(currentDay)) {
					System.out.print("{");
					System.out.print(currentDay);
					System.out.print("}");
				} else {
					System.out.printf("%-3s", currentDay);

				}
				dayOfWeek++;
				currentDay++;
			}
			System.out.print("\n");
			dayOfWeek = 0;
		}
	}

	/**
	 * Go to a specific day of the calendar to look at events
	 * Ask the user for the day
	 * Helper function for main()
	 * @param cal calendar to look at
	 */
	private static void goTo(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		// Ask the user for a day
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		int month = -1;
		int date = -1;
		int year = -1;
		Calendar day = getTodayCalifornianCalendar();
		in.useDelimiter("[^0-9]+");
		do {
			System.out.println("Enter a date (MM/DD/YYYY): ");
			if (in.hasNextInt()){
				month = in.nextInt();
			}
			if (in.hasNextInt()) {
				date = in.nextInt();
			}
			if (in.hasNextInt()) {
				year = in.nextInt();
			}
			in.nextLine();
			try {
				// Create the appropriate calendar object
				day.set(Calendar.YEAR,  year);
				day.set(Calendar.MONTH, month-1);
				day.set(Calendar.DATE, date);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Invalid date");
				month = -1;
			}
		} while (month <= 0 || month <= 0 || year <= 0 );
		
		// Print the events of the day
		printDay(cal, day);
	}
	
	/**
	 * Remove event(s) from the calendar
	 * Save the calendar to disk if modified
	 * @param cal calendar to update
	 */
	private static void delete(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		// Ask the user for a day
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		int month = -1;
		int date = -1;
		int year = -1;
		Calendar day = getTodayCalifornianCalendar();
		in.useDelimiter("[^0-9]+");
		do {
			System.out.println("Enter a date (MM/DD/YYYY): ");
			if (in.hasNextInt()){
				month = in.nextInt();
			}
			if (in.hasNextInt()) {
				date = in.nextInt();
			}
			if (in.hasNextInt()) {
				year = in.nextInt();
			}
			in.nextLine();
			try {
				// Create the appropriate calendar object
				day.set(Calendar.YEAR,  year);
				day.set(Calendar.MONTH, month-1);
				day.set(Calendar.DATE, date);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Invalid date");
				month = -1;
			}
		} while (month <= 0 || month <= 0 || year <= 0 );
		
		// Get all the events that day;
		ArrayList<Event> list = cal.getDailyEvents(day);
		
		if(list == null || list.size() == 0) {
			System.out.println("Nothing for that day");
			return;
		}
		
		for(int i = 0; i < list.size(); i++) {
			System.out.println( (i+1) + ") " + list.get(i));
		}
		
		// Get user choice
		char userChoice = 0;
		in.reset();
		while(userChoice != 'S' && userChoice != 'A') {
			System.out.println("Delete [S]election or [A]ll? ");
			userChoice = in.nextLine().toUpperCase().charAt(0);
		}
		switch(userChoice) {
			// Select an event to remove
			case('S'):
				int userInt = 0;
				do {
					System.out.println("Number of the event you want to remove (between 1 and " + list.size() + ":" );
					in.useDelimiter("[^0-9]+");
					userInt = in.nextInt();
				}while (userInt < 1 || userInt > list.size());
				cal.remove(list.get(userInt-1));
				break;
			
			// Remove all the events
			case('A'):
				for (Event event : list) {
					cal.remove(event);
				}
				break;
		}
		save(cal);

		
	}
}
