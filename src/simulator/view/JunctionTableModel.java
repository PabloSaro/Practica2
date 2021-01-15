package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Junction> junctionList;
	
	private String[] column = {"Id", "Green", "Queues"};
	
	private Controller _ctrl;
	
	public JunctionTableModel(Controller _ctrl) {
		this._ctrl = _ctrl;
		this._ctrl.addObserver(this);
		this.junctionList = new ArrayList<Junction>();
	}

	@Override
	public int getRowCount() {
		return this.junctionList.size();
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
			res = "" + this.junctionList.get(rowIndex).getId();
		}else if(columnIndex == 1) {
			int green = this.junctionList.get(rowIndex).getCurrentGreen();
			if( green == -1) {
				res = "NONE";
			}else {
				res =  "" + this.junctionList.get(rowIndex).getRoadIn().get(green).getId();
			}
		}else if(columnIndex == 2) {
			res = "" + this.junctionList.get(rowIndex).getQueueRoad();	
		}
		return res;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.junctionList = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.junctionList = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.junctionList = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.junctionList = new ArrayList<Junction>();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.junctionList = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
