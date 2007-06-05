package demo.action;

import net.java.mega.action.api.AbstractAction;
import net.java.mega.action.api.CustomResponseProvider;
import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.output.Redirector;

public class Sysvision extends AbstractAction implements CustomResponseProvider {
	public void onLoad() {
	}

	public ResponseProvider getResponseProvider() { 
		return new Redirector("http://www.sysvision.pt");
	}
}
