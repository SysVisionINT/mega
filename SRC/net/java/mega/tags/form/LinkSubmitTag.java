package net.java.mega.tags.form;

import javax.servlet.jsp.JspException;

import net.java.mega.tags.model.SubmitBaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class LinkSubmitTag extends SubmitBaseTag {
	private static final long serialVersionUID = 6684109710319221195L;
	
	private static Log log = LogFactory.getLog(LinkSubmitTag.class);
	
	public void initTag() {
	}

	public int writeStartTag() throws JspException {
		try {
			pageContext.getOut().print("<a href=\"#\"");

			writeAttributes();

			pageContext.getOut().print(">");
		} catch (Exception e) {
			log.error("Error while writing INPUT a (submit) TAG", e);
			throw new JspException(e);
		}
		
		return INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {		
		try {
			pageContext.getOut().println("</a>");
		} catch (Exception e) {
			log.error("Error while writing INPUT a (submit) TAG", e);
			throw new JspException(e);
		}
	}

	public boolean elementCanBeDisabled() {
		return false;
	}
}
