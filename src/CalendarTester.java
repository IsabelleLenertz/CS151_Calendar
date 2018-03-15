import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class CalendarTester {	

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
					break;
				case('C'):
					create(calendar);
					break;
				case('G'):
					System.out.println("g");
					break;
				case('E'):
					System.out.println("e");
					break;
				case('D'):
					System.out.println("d");
					break;
			}
		}
		
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
	 */
	private static MyCalendar load() {	
		MyCalendar cal = null;
		
		// TODO: load the calendar

		if (cal == null) {
			System.out.println("You need to create and save a calendar first.");
		}
		return cal;
	}
	
	private static void create(MyCalendar cal) {
		Scanner in = new Scanner(System.in);
		// Get the title
		System.out.print("Title: ");
		String title = in.nextLine();
		
		// Get the date
		in.useDelimiter("[^0-9]+");
		System.out.print("Date (Format MM/DD/YYYY): ");
		int month = in.nextInt();
		int date = in.nextInt();
		int year = in.nextInt();
		in.nextLine();
		
		// Get starting time
		System.out.print("Starting time (24 hours clock, HH:MM): ");
		int startH = in.nextInt();
		int startM = in.nextInt();
		in.nextLine();
		
		// Get ending time
		System.out.print("Starting time (24 hours clock, HH:MM, leave blank if not appropriate): ");
		Integer endH = null;
		Integer endM= null;
		if(in.hasNextInt()) {
			endH = in.nextInt();
			in.next();
			endM = in.nextInt();
		}
		in.nextLine();
		
		// Create a new event
		Calendar day = getTodayCalifornianCalendar();
		day.set(Calendar.YEAR,  year);
		day.set(Calendar.MONTH, month);
		day.set(Calendar.DATE, date);
		LocalTime start = LocalTime.of(startH, startM);
		LocalTime end = null;
		if(endH != null) {
			end = LocalTime.of(endH,  endM);
		}
		

		// Check if the event is conflicting
		
		// Add to calendar
		
		// Save to disk
	}
}
