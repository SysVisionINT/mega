package demo.action.db;

import net.java.mega.action.api.AbstractAction;
import net.java.mega.action.api.CustomResponseProvider;
import net.java.mega.action.api.ResponseProvider;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import demo.out.ListOut;

public class ListAction extends AbstractAction implements CustomResponseProvider {
	private static Log log = LogFactory.getLog(ListAction.class);
	
	public void onLoad() {
	}

	public ResponseProvider getResponseProvider() {
		log.info("getResponseProvider()");
		return new ListOut();
	}

}
