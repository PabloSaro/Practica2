package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Event> eventList;
	
	private String[] column = {"Time", "Desc."};
	
	private Controller _ctrl;
	
	
	public EventsTableModel(Controller _ctrl) {
		this._ctrl = _ctrl;
		this._ctrl.addObserver(this);
		this.eventList = new ArrayList<Event>();
	}
	
	@Override
	public int getRowCount() {
		return this.eventList.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}
	
	public String getColumnName(int columnIndex) {
		return this.column[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String res = "";
		if(columnIndex == 0) {
			res = "" + this.eventList.get(rowIndex).getTime();
		}else if(columnIndex == 1) {
			res = this.eventList.get(rowIndex).toString();
		}
		return res;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.eventList = events;
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.eventList = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.eventList = events;
		//this.eventList.add(e);
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.eventList = new ArrayList<Event>();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.eventList = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
