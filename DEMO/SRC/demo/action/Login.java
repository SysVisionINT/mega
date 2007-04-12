package demo.action;

import net.java.mega.action.api.AbstractAction;
import net.java.mega.action.api.Message;
import net.java.mega.action.api.Validator;
import net.java.sjtools.util.TextUtil;

public class Login extends AbstractAction implements Validator{
	
	private String user = null;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void onLoad() {
		
	}

	public boolean isInputValid() {
		if (TextUtil.isEmptyString(getUser())) {
			addMessage("user", new Message("login.user.null"));
		}
			
		return true;
	}
	
	public void login() {
		getHttpSession().setAttribute("USER", getUser());
		
		gotoAction(RecordList.class);
	}
}
