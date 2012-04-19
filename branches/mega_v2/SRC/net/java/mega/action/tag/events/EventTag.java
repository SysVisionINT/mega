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
package net.java.mega.action.tag.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.java.mega.action.model.EventArg;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.URLUtil;
import net.java.sjtools.logging.plus.RLog;

public class EventTag extends BodyTagSupport {
	private static final long serialVersionUID = -3132410083638333240L;

	private List eventArgList = new ArrayList();

	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doEndTag() throws JspException {
		StringBuilder buffer = new StringBuilder();
		buffer.append(getJavascript());
		buffer.append("return false;");
		
		try {
			pageContext.getOut().print(buffer.toString());
		} catch (IOException e) {
			RLog.error("Error while writing Javascript for EVENT " + getName(), e);
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	protected String getJavascript() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);

		StringBuilder buffer = new StringBuilder();

		try {
			buffer.append("executeEvent('");
			buffer.append(url.getMethodURL(null, null));
			buffer.append("', {");
			buffer.append(Constants.MEGA_EVENT_NAME);
			buffer.append(":");
			buffer.append("'");
			buffer.append(getName());
			buffer.append("'");

			for (Iterator i = eventArgList.iterator(); i.hasNext();) {
				EventArg arg = (EventArg) i.next();

				buffer.append(",");
				buffer.append(arg.getName());
				buffer.append(":");

				if (arg.isConstant()) {
					buffer.append("'");
				}

				buffer.append(arg.getValue());

				if (arg.isConstant()) {
					buffer.append("'");
				}
			}

			buffer.append("});");
		} catch (Exception e) {
			RLog.error("Error while writing EVENT TAG", e);
			throw new JspException(e);
		}

		return buffer.toString();
	}

	public void addEventArg(String name, String value, boolean constant) {
		eventArgList.add(new EventArg(name, value, constant));
	}
}
