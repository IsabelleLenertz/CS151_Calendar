import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class CalendarTester {

		
	

	public static void main(String[] args) {
		printCurrentMonth();
		char choice = 0;
		
		while (choice != 'Q') {
			choice = displayMainMenu();
			
			switch(choice) {
				case('L'):
					System.out.println("l");
					break;
				case('V'):
					System.out.println("v");
					break;
				case('C'):
					System.out.println("c");
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

	public static void printCurrentMonth() {
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
			while(dayOfWeek < MyCalendar.DAYS_IN_A_WEEK) {
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
	
	public static Calendar getTodayCalifornianCalendar() {
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

	public static char displayMainMenu() {
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
}
