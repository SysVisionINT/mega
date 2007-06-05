package net.java.mega.tags.form;

import javax.servlet.jsp.JspException;

import net.java.mega.tags.model.SubmitBaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class HTMLSubmitTag extends SubmitBaseTag {
	private static final long serialVersionUID = 6684109710319221195L;
	
	private static Log log = LogFactory.getLog(HTMLSubmitTag.class);
	
	private String tag = null;
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void initTag() {
	}

	public int writeStartTag() throws JspException {
		try {
			pageContext.getOut().print("<");
			pageContext.getOut().print(getTag());

			writeAttributes();

			pageContext.getOut().print(">");
		} catch (Exception e) {
			log.error("Error while writing INPUT html (submit) TAG", e);
			throw new JspException(e);
		}
		
		return INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {		
		try {
			pageContext.getOut().print("</");
			pageContext.getOut().print(getTag());
			pageContext.getOut().println(">");
		} catch (Exception e) {
			log.error("Error while writing INPUT html (submit) TAG", e);
			throw new JspException(e);
		}
	}
	
	public boolean elementCanBeDisabled() {
		return false;
	}	
}
