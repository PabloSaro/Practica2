package simulator.factories;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;


public class NewJunctionEventBuilder extends Builder<Event> {
	private int time;
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(String type, Factory<LightSwitchingStrategy> lssFactory,
			Factory<DequeuingStrategy> dqsFactory) {
		super(type);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data)throws ExceptionConstructObject, ExceptionJSONConstructor {
		NewJunctionEvent nj = null;
		if (data.has("time")){
			this.time = data.getInt("time");
		}
		if (data.has("id")){
			this.id = data.getString("id");
		}
		if (data.has("coor")){
			this.xCoor = data.getJSONArray("coor").getInt(0);
			this.yCoor = data.getJSONArray("coor").getInt(1);
		}
		this.lsStrategy = this.lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		this.dqStrategy = this.dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		nj = new NewJunctionEvent(this.time,this.id,this.lsStrategy,this.dqStrategy,this.xCoor,this.yCoor);

		return nj;
	}

}
