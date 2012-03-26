package net.java.mega.action.api.events;

import org.json.simple.JSONObject;

public interface EventChange {
	public String getChangeType();
	public JSONObject getJSONObject();
}
