package demo.action;

import java.util.Locale;

import net.java.mega.action.api.AbstractAction;

public class CurrentLocate extends AbstractAction {
	
	public String getCurrentLocate() {
		return getLocale().getDisplayLanguage();
	}

	public void onLoad() {
		
	}

	public void change() {
		if (getLocale().equals(Locale.US)) {
			setLocale(new Locale("pt", "PT"));
		} else {
			setLocale(Locale.US);
		}
	}
}
