import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class CalendarTester {	
	private static final String FILE_NAME = "myCalendar.txt";

	public static void main(String[] args) {
		printCurrentMonth();
		MyCalendar calendar = null;
		char choice = 0;
		
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
					create(calendar);
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
					System.out.print(ConsoleColors.RED_BOLD);
					System.out.printf("%-3s", currentDay);
					System.out.print(ConsoleColors.RESET);

				}
				dayOfWeek++;
				currentDay++;
			}
			System.out.print("\n");
			dayOfWeek = 0;
		}

		
		
	}
	
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

	private static char displayMainMenu() {
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
	
	private static void create(MyCalendar cal) {
		if (cal == null) {
			cal = new MyCalendar();
		}
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
		// TODO fix it no integer entered
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
		if (!cal.isConflicting(newEvent)) {
			// Add to calendar
			cal.addEvent(newEvent);
			
			System.out.println("The following event has been added to the calendar:");
			System.out.println(newEvent);
			
			// Save to disk
			save(cal);
		} else {
			System.out.print("Event not saved, there was a conflict with the calendar.");
		}
		
	}
	
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

	private static void viewBy(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		System.out.println("[D]ay view or [M]view ? ");
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
					date.roll(Calendar.DATE,  true);
				} else if (userInput == 'P') {
					date.roll(Calendar.DATE,  false);
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
					date.roll(Calendar.MONTH,  true);
				} else if (userInput == 'P') {
					date.roll(Calendar.MONTH,  false);
				}
			} while (userInput != 'M');
		}

	}
	
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
	
	private static void printMonth(MyCalendar cal, Calendar date) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		Set<Event> list = cal.getMonthlyEvents(date.get(Calendar.YEAR), Event.Month.values()[date.get(Calendar.MONTH)]);
		// Convert the list of Events into a list of day (we only care when the events occur)
		Set<Integer> days = new TreeSet<Integer>();
		if(list == null) {
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
		String dayNumber;
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

	private static void goTo(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		
		// Ask the user for a day
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a date (MM/DD/YYYY): ");
		in.useDelimiter("[^0-9]+");
		int month = in.nextInt();
		int date = in.nextInt();
		int year = in.nextInt();
		in.nextLine();
		// Create the appropriate calendar object
		Calendar day = getTodayCalifornianCalendar();
		day.set(Calendar.YEAR,  year);
		day.set(Calendar.MONTH, month-1);
		day.set(Calendar.DATE, date);
		
		// Print the events of the day
		printDay(cal, day);
	}
	
	private static void delete(MyCalendar cal) {
		if (cal == null) {
			System.out.println("You need to create or load a calendar first.");
			return;
		}
		// Ask the user for a day
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a date (MM/DD/YYYY): ");
		in.useDelimiter("[^0-9]+");
		int month = in.nextInt();
		int date = in.nextInt();
		int year = in.nextInt();
		in.nextLine();
		// Create the appropriate calendar object
		Calendar day = getTodayCalifornianCalendar();
		day.set(Calendar.YEAR,  year);
		day.set(Calendar.MONTH, month-1);
		day.set(Calendar.DATE, date);
		
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
