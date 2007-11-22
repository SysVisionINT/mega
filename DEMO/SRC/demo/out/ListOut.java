package demo.out;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.RequestMetaData;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.error.ActionException;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ListOut implements ResponseProvider {
	private static Log log = LogFactory.getLog(ListOut.class);
	
	public void process(HttpServletRequest request, HttpServletResponse response, RequestMetaData requestMetaData,
			ResponseMetaData responseMetaData) throws ActionException {
		
		log.debug("vou escrever...");
		
		log.debug("HttpServletResponse = " + response.getClass().getName());
		
		try {
			response.setContentType("text/html");
			response.setStatus(200);
			response.getWriter().write("Sou o maior!");
		} catch (IOException e) {
			throw new ActionException("Não sou o maior", e);
		}
		
		log.debug("já escrevi...");
	}

}
