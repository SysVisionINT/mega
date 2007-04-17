package net.java.mega.tags.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.java.mega.action.util.URLUtil;
import net.java.mega.tags.model.BaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class FormTag extends BaseTag {
	private static final long serialVersionUID = -5532740559135162767L;
	
	private static Log log = LogFactory.getLog(FormTag.class);
	
	private String method = null;
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int doStartTag () throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);
		
		setId(getMethod());
		
		try {
			pageContext.getOut().print("<form action=\"");
			pageContext.getOut().print(url.getMethodURL(null, getMethod()));
			pageContext.getOut().print(" \" method=\"POST\"");
			writeAttributes();
			pageContext.getOut().println(">");
			
		} catch (Exception e) {
			log.error("Error while writing FORM TAG", e);
			throw new JspException(e);
		}
		
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().println("</form>");
		} catch (Exception e) {
			log.error("Error while writing FORM TAG", e);
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}
}
