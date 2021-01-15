package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Vehicle> vehicleList;
	
	private String[] column = {"Id", "Location", "Itinary", "CO2Class", "Max Speed",
			"Speed", "Total CO2", "Distance"};
	
	private Controller _ctrl;
	
	public VehiclesTableModel(Controller _ctrl) {
		this._ctrl = _ctrl;
		this._ctrl.addObserver(this);
		this.vehicleList = new ArrayList<Vehicle>();
	}

	@Override
	public int getRowCount() {
		return this.vehicleList.size();
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
			res = "" + this.vehicleList.get(rowIndex).getId();
		}else if(columnIndex == 1) {
			if(this.vehicleList.get(rowIndex).getStatus().equals(VehicleStatus.PENDING)) {
				res = "Pending";	
			}else if(this.vehicleList.get(rowIndex).getStatus().equals(VehicleStatus.WAITING)) {
				res = this.vehicleList.get(rowIndex).getRoad().getDestination().getId() + ":Waiting";
			}else if(this.vehicleList.get(rowIndex).getStatus().equals(VehicleStatus.ARRIVED)) {
				res = "Arrived";
			}else {
				res =  this.vehicleList.get(rowIndex).getRoad().getId() + ":" + this.vehicleList.get(rowIndex).getLocation();
			}
		}else if(columnIndex == 2) {
			res =  "" + this.vehicleList.get(rowIndex).getItinerary();
		}else if(columnIndex == 3) {
			res =  "" + this.vehicleList.get(rowIndex).getContamination();
		}else if(columnIndex == 4) {
			res =  "" + this.vehicleList.get(rowIndex).getMaxSpeed();
		}else if(columnIndex == 5) {
			res =  "" + this.vehicleList.get(rowIndex).getCurrentSpeed();
		}else if(columnIndex == 6) {
			res =  "" + this.vehicleList.get(rowIndex).getTotalContamination();
		}else if(columnIndex == 7){
			res =  "" + this.vehicleList.get(rowIndex).getDistance();
		}
		return res;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.vehicleList = new ArrayList<Vehicle>();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.vehicleList = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
