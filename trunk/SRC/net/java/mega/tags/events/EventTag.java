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
package net.java.mega.tags.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.action.util.Constants;
import net.java.mega.action.util.URLUtil;
import net.java.mega.tags.model.Attribute;
import net.java.mega.tags.model.AttributeContainer;
import net.java.mega.tags.model.EventArg;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class EventTag extends BodyTagSupport {

	private static final long serialVersionUID = 8690811938831037093L;

	private static Log log = LogFactory.getLog(EventTag.class);

	private List eventArgList = new ArrayList();

	private String trigger = null;
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public int doEndTag() throws JspException {
		Tag tag = findAncestorWithClass(this, AttributeContainer.class);

		if (tag != null) {
			AttributeContainer attributeContainer = (AttributeContainer) tag;

			StringBuffer buffer = new StringBuffer();

			String userTrigger = getUserTrigger(attributeContainer, getTrigger());

			if (userTrigger != null) {
				buffer.append(userTrigger);
			}

			buffer.append(getJavascript());

			attributeContainer.addAttribute(new Attribute(getTrigger(), buffer.toString()));
		}

		return EVAL_PAGE;
	}

	private String getUserTrigger(AttributeContainer attributeContainer, String triggerName) {
		String value = attributeContainer.deleteAttribute(triggerName);

		if (value != null) {
			value = value.trim();

			if (!value.endsWith(";")) {
				value = value.concat(";");
			}
		}

		return value;
	}

	private String getJavascript() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);

		StringBuffer buffer = new StringBuffer();

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

			buffer.append("})");
		} catch (Exception e) {
			log.error("Error while writing EVENT TAG", e);
			throw new JspException(e);
		}

		return buffer.toString();
	}

	public void addEventArg(String name, String value, boolean constant) {
		eventArgList.add(new EventArg(name, value, constant));
	}
}
