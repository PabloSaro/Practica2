package simulator.factories;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws ExceptionJSONConstructor, ExceptionConstructObject;
}
