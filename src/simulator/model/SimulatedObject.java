package simulator.model;

import org.json.JSONObject;

import simulator.exception.ExceptionIncorrectValue;

public abstract class SimulatedObject {

	protected String _id;

	SimulatedObject(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time)  throws ExceptionIncorrectValue;

	abstract public JSONObject report();
}
