import javax.swing.JFrame;

public class SimpleCalendar {

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setSize(900,  900);
		window.add(new MonthDisplay());
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		
	}

}
