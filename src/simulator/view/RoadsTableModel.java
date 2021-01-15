package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Road> roadList;
	
	private String[] column = {"Id", "Length", "Weather", "Max Speed",
			"Speed Limit", "Total CO2", "CO2 Limit"};
	
	private Controller _ctrl;
	
	public RoadsTableModel(Controller _ctrl) {
		this._ctrl = _ctrl;
		this._ctrl.addObserver(this);
		this.roadList = new ArrayList<Road>();
	}

	@Override
	public int getRowCount() {
		return this.roadList.size();
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
			res = "" + this.roadList.get(rowIndex).getId();
		}else if(columnIndex == 1) {
			res =  "" + this.roadList.get(rowIndex).getLenght();
		}else if(columnIndex == 2) {
			res =  "" + this.roadList.get(rowIndex).getWeather().toString();
		}else if(columnIndex == 3) {
			res =  "" + this.roadList.get(rowIndex).getMaxSpeed();
		}else if(columnIndex == 4) {
			res =  "" + this.roadList.get(rowIndex).getSpeedLimit();
		}else if(columnIndex == 5) {
			res =  "" + this.roadList.get(rowIndex).getTotalContamination();
		}else if(columnIndex == 6) {
			res =  "" + this.roadList.get(rowIndex).getContaminationLimit();
		}
		return res;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.roadList = new ArrayList<Road>();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.roadList = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
