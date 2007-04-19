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
import net.java.mega.tags.model.BaseBodyTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class FormTag extends BaseBodyTag {
	private static final long serialVersionUID = -5532740559135162767L;
	
	private static Log log = LogFactory.getLog(FormTag.class);
	
	private String method = null;
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void writeStartTag() throws JspException {
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
	}
}
