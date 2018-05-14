import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFrame;

public class SimpleCalendar {
	private static final String FILE_NAME = "myCalendar.txt";

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setSize(900,  900);
		window.add(new DayDisplay(load()));
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		
	}
	
	/**
	 * Load a calendar from memory
	 * @return null if the calendar was not read, else a reference to the calendar into memory
	 */
	private static MyCalendar load() {	
		MyCalendar cal = null;
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))){
			cal = (MyCalendar) ois.readObject(); // cast is needed.
			ois.close();
			System.out.println("Calendar loaded from memory");

		} catch (Exception e) {
			System.out.println("You need to create and save a calendar first.");
		}

		return cal;
	}

}
