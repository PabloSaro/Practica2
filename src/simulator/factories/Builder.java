package simulator.factories;

import org.json.JSONObject;

import simulator.exception.ExceptionConstructObject;
import simulator.exception.ExceptionJSONConstructor;

public abstract class Builder<T> {
	protected String _type;

	public Builder(String type) {
		if (type == null)
			throw new IllegalArgumentException("Invalid type: " + type);
		else
			_type = type;
	}

	public T createInstance(JSONObject info) throws ExceptionJSONConstructor, ExceptionConstructObject{

		T b = null;

		if (_type != null && _type.equals(info.getString("type"))) {
			b = createTheInstance(info.has("data") ? info.getJSONObject("data") : null);
		}

		return b;
	}

	protected abstract T createTheInstance(JSONObject data) throws ExceptionJSONConstructor, ExceptionConstructObject;
}
