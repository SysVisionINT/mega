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
package net.java.mega.tags.navigation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.java.mega.action.util.Constants;
import net.java.mega.action.util.URLUtil;
import net.java.mega.action.util.WorkflowControlUtil;
import net.java.mega.tags.model.BaseBodyTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.TextUtil;

public class LinkTag extends BaseBodyTag {
	private static final long serialVersionUID = -3738161366330292080L;

	private static Log log = LogFactory.getLog(LinkTag.class);

	private String action = null;

	private String method = null;

	private List parameters = null;
	
	private boolean useWorkflowControl = true;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void addParameter(String value) {
		parameters.add(value);
	}

	private String getParameters() throws JspException {
		StringBuffer buffer = new StringBuffer();

		String value = null;
		int count = 0;

		for (Iterator i = parameters.iterator(); i.hasNext();) {
			value = (String) i.next();

			if (count > 0 || isUseWorkflowControl()) {
				buffer.append("&amp;");
			}

			buffer.append(getArgName(count));
			buffer.append("=");

			try {
				buffer.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("Error while writing A TAG", e);
				throw new JspException(e);
			}

			count++;
		}

		return buffer.toString();
	}

	private String getArgName(int count) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(Constants.GET_ARG);
		buffer.append(TextUtil.format(String.valueOf(count), 3, '0', TextUtil.ALLIGN_RIGHT));

		return buffer.toString();
	}

	public void initTag() {
		parameters = new ArrayList();
	}

	public int writeStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);

		try {
			pageContext.getOut().print("<a href=\"");
			pageContext.getOut().print(url.getMethodURL(getAction(), getMethod()));

			if (!parameters.isEmpty()) {
				pageContext.getOut().print("?");
				
				if (isUseWorkflowControl()) {  
					pageContext.getOut().print(WorkflowControlUtil.WORKFLOW_CONTROL_FIELD);
					pageContext.getOut().print("=");
					pageContext.getOut().print(WorkflowControlUtil.getCurrentToken(request));
				}
				
				pageContext.getOut().print(getParameters());
			}

			pageContext.getOut().print("\"");
			writeAttributes();
			pageContext.getOut().print(">");
		} catch (Exception e) {
			log.error("Error while writing A TAG", e);
			throw new JspException(e);
		}

		return INCLUDE_INNER_HTML;
	}

	public void writeEndTag() throws JspException {
		try {
			pageContext.getOut().println("</a>");
		} catch (Exception e) {
			log.error("Error while writing A TAG", e);
			throw new JspException(e);
		}
	}

	
	public boolean isUseWorkflowControl() {
		return useWorkflowControl;
	}

	
	public void setUseWorkflowControl(boolean useWorkflowControl) {
		this.useWorkflowControl = useWorkflowControl;
	}
}
