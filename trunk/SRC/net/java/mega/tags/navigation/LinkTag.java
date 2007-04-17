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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.java.mega.action.util.URLUtil;
import net.java.mega.tags.model.BaseBodyTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class LinkTag extends BaseBodyTag {
	private static final long serialVersionUID = -3738161366330292080L;

	private static Log log = LogFactory.getLog(LinkTag.class);

	private String action = null;
	private String method = null;
	private Map parameters = null;

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
	
	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	public int doStartTag () {
		parameters = new HashMap();
		
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);

		try {
			pageContext.getOut().print("<a href=\"");
			pageContext.getOut().print(url.getMethodURL(getAction(), getMethod()));
			
			if (!parameters.isEmpty()) {
				pageContext.getOut().print("?");
				pageContext.getOut().print(getParameters());
			}
			
			pageContext.getOut().print("\"");
			writeAttributes();
			pageContext.getOut().print(">");
			pageContext.getOut().print(getBodyContent().getString().trim());
			pageContext.getOut().println("</a>");
		} catch (Exception e) {
			log.error("Error while writing A TAG", e);
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	private String getParameters() {
		StringBuffer buffer = new StringBuffer();
		
		String name = null;
		
		for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
			name = (String) i.next();
			
			if (buffer.length() != 0) {
				buffer.append("&");
			}
			
			buffer.append(name);
			buffer.append("=");
			buffer.append(parameters.get(name));
		}
		
		return buffer.toString();
	}
}
