package simulator.model;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionIncorrectValue;
import simulator.exception.ExceptionJSONConstructor;

public class NewJunctionEvent extends Event {

	
	
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int x;
	private int y;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, 
			DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
	}

	@Override
	void execute(RoadMap map) throws ExceptionIncorrectValue, ExceptionConstructObject, ExceptionJSONConstructor {
		Junction j = new Junction(this.id, this.lsStrategy, this.dqStrategy, this.x, this.y);
		map.addJunction(j);
	}
	
	@Override
	public String toString() {
		return "New Junction '"+this.id+"'";
	}

}
