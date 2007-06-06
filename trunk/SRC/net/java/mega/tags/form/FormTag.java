/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.java.mega.tags.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.java.mega.action.util.URLUtil;
import net.java.mega.action.util.WorkflowControlUtil;
import net.java.mega.common.util.CommonConstants;
import net.java.mega.tags.model.BaseBodyTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class FormTag extends BaseBodyTag {
	private static final long serialVersionUID = -5532740559135162767L;
	
	private static final String FORM_COUNT = "___FORM___COUNT___";
	
	private static Log log = LogFactory.getLog(FormTag.class);

	public int writeStartTag() throws JspException {		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);
		
		try {
			pageContext.getOut().print("<form action=\"");
			pageContext.getOut().print(url.getActionURL(null));
			pageContext.getOut().print("\" method=\"POST\"");
			writeAttributes();
			pageContext.getOut().println(">");
			
			pageContext.getOut().print("<input type=\"hidden\" name=\"");
			pageContext.getOut().print(getActionField());
			pageContext.getOut().println("\"/>");			
			
			pageContext.getOut().print("<input type=\"hidden\" name=\"");
			pageContext.getOut().print(WorkflowControlUtil.WORKFLOW_CONTROL_FIELD);
			pageContext.getOut().print("\" value=\"");
			pageContext.getOut().print(WorkflowControlUtil.getCurrentToken(request));
			pageContext.getOut().println("\"/>");				
		} catch (Exception e) {
			log.error("Error while writing FORM TAG", e);
			throw new JspException(e);
		}
		
		return INCLUDE_INNER_HTML;
	}

	private String generateFormID() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("FORM_");
		
		Integer formCount = (Integer) pageContext.getRequest().getAttribute(FORM_COUNT);
		int id = 1;
		
		if (formCount != null) {
			id = formCount.intValue() + 1;
		}
		
		buffer.append(id);
		
		pageContext.getRequest().setAttribute(FORM_COUNT, new Integer(id));
		
		return buffer.toString();
	}
	
	public String getActionField() {
		return CommonConstants.MEGA_FORM_ACTION;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().println("</form>");
		} catch (Exception e) {
			log.error("Error while writing FORM TAG", e);
			throw new JspException(e);
		}
	}

	public void initTag() {
		if (getId() == null) {
			setId(generateFormID());
		}
	}
}
