package demo.action;

import net.java.mega.action.api.AbstractAction;

public class Hello extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private String helloMessage = null;

	public void onLoad() {
		this.helloMessage = "Hello, world!";
	}

	public String getHelloMessage() {
		return helloMessage;
	}

}