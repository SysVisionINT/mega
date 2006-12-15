package demo.action;

import java.util.Locale;

import net.java.mega.action.api.AbstractAction;

public class CurrentLocate extends AbstractAction {
	
	public String getCurrentLocate() {
		return getLocate().getDisplayLanguage();
	}

	public void onLoad() {
		
	}

	public void change() {
		if (getLocate().equals(Locale.US)) {
			setLocate(new Locale("pt", "PT"));
		} else {
			setLocate(Locale.US);
		}
	}
}
