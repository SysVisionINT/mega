package net.java.mega.tags.form;

import javax.servlet.jsp.JspException;

import net.java.mega.tags.model.SubmitBaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class SubmitTag extends SubmitBaseTag {
	private static final long serialVersionUID = 6684109710319221195L;
	
	private static Log log = LogFactory.getLog(SubmitTag.class);
	
	public void initTag() {
	}

	public int writeStartTag() throws JspException {
		return NOT_INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		String value = getInnerHtml();
		
		try {
			pageContext.getOut().print("<input type=\"button\" value=\"");
			pageContext.getOut().print(value);
			pageContext.getOut().print("\"");

			writeAttributes();

			pageContext.getOut().println("/>");
		} catch (Exception e) {
			log.error("Error while writing INPUT button TAG", e);
			throw new JspException(e);
		}
	}

	public boolean elementCanBeDisabled() {
		return true;
	}
}
