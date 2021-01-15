package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar  extends JPanel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	
	private int time;
	
	private JLabel event;
	
	private JLabel ticks;
	
	
	public StatusBar(Controller _ctrl) {
		iniGui();
		this._ctrl = _ctrl;
		this.time = 0;
		this._ctrl.addObserver(this);
	}
	
	public void iniGui() {
		this.setLayout(new BorderLayout());
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		this.ticks = new JLabel("Time: ");
		this.event = new JLabel();
		bar.add(ticks);
		bar.addSeparator();
		bar.add(event);
		this.add(bar, BorderLayout.WEST);
		this.setVisible(true);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.ticks.setText("Time: " +  this.time);
		this.event.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.ticks.setText("Time: " +  this.time);
		this.event.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.time = time;
		this.ticks.setText("Time: " +  this.time);
		this.event.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = 0;
		this.ticks.setText("Time: " +  this.time);
		this.event.setText("");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.ticks.setText("Time: " +  this.time);
		this.event.setText("");
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
