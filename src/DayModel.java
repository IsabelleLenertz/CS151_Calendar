import java.util.ArrayList;
import java.util.Calendar;

public class DayModel {
	private ArrayList<View> views;
	private Calendar day;
	
	public DayModel(Calendar today) {
		views = new ArrayList<View>();
		this.day = today;
	}
	
	public void setDay(Calendar newDay) {
		day = newDay;
		this.notifyViews();
	}
	
	public Calendar getDay() {
		return (Calendar) this.day.clone();
	}
		
	
	public void addView(View v) {
		views.add(v);
	}
	
	public void notifyViews() {
		for(View element : views) {
			element.updateDisplay();
		}
	}

}
