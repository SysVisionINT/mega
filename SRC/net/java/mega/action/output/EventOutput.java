package net.java.mega.action.output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.RequestMetaData;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.api.events.EventChangesContainer;
import net.java.mega.action.error.ActionException;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class EventOutput implements ResponseProvider {
	private static Log log = LogFactory.getLog(EventOutput.class);
	
	private EventChangesContainer container = null;
	
	public EventOutput(EventChangesContainer container) {
		this.container = container;
	}

	public void process(HttpServletRequest request, HttpServletResponse response, RequestMetaData requestMetaData, ResponseMetaData responseMetaData) throws ActionException {
		// TODO Auto-generated method stub

	}

}
