package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {
	private int timeSlot;

	public RoundRobinStrategyBuilder(String type) {
		super(type);
		this.timeSlot = 1;
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		RoundRobinStrategy rr = null;
		if (data.has("timeslot")){
			this.timeSlot = data.getInt("timeslot");
			rr = new RoundRobinStrategy(this.timeSlot);
		}
		return rr;
	}



}
