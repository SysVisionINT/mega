package net.java.mega.action.output;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.RequestMetaData;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.api.events.EventChange;
import net.java.mega.action.api.events.EventChangesContainer;
import net.java.mega.action.error.ActionException;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EventOutput implements ResponseProvider {
	private static Log log = LogFactory.getLog(EventOutput.class);
	
	private EventChangesContainer container = null;
	
	public EventOutput(EventChangesContainer container) {
		this.container = container;
	}

	public void process(HttpServletRequest request, HttpServletResponse response, RequestMetaData requestMetaData, ResponseMetaData responseMetaData) throws ActionException {
		JSONArray outputArray = new JSONArray();
		
		for (Iterator i = container.getEventChangeList().iterator(); i.hasNext();) {
			EventChange eventChange = (EventChange) i.next();
			
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("functionName", eventChange.getFunctionName());
			jsonObject.put("data", eventChange.getJSONObject());
			
			outputArray.add(jsonObject);
		}

		try {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter writer = response.getWriter();
			
			writer.println(outputArray.toJSONString());
			
			writer.flush();
		} catch (Exception e) {
			log.error("Error while generating JSON Array for event " + requestMetaData.getEventName() + " on path " + requestMetaData.getPath(), e);
			throw new ActionException("Error while generating JSON Array for event " + requestMetaData.getEventName() + " on path " + requestMetaData.getPath(), e);
		}
	}

}
