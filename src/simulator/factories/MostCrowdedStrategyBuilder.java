package simulator.factories;

import org.json.JSONObject;
import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;


public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	private int timeSlot;
	
	public MostCrowdedStrategyBuilder(String type) {
		super(type);
		this.timeSlot = 1;
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {

		MostCrowdedStrategy mc = null;
		if (data.has("timeslot")){
			this.timeSlot = data.getInt("timeslot");
			mc = new MostCrowdedStrategy(this.timeSlot);
		}
		return mc;
	}

}
