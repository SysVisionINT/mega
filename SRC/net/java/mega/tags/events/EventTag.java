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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.tags.model.Attribute;
import net.java.mega.tags.model.AttributeContainer;

public class EventTag extends net.java.mega.action.tag.events.EventTag {
	private static final long serialVersionUID = 8690811938831037093L;

	private String trigger = null;

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

			StringBuilder buffer = new StringBuilder();

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
}
