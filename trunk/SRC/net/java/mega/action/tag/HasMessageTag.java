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
package net.java.mega.action.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.action.MessageContainer;
import net.java.mega.action.util.Constants;

public class HasMessageTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private String key = null;
	private boolean all = false;
	
	public int doStartTag () {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		MessageContainer container = (MessageContainer) request.getAttribute(Constants.MESSAGE_CONTAINER);
		
		if (container == null) {
			return SKIP_BODY;
		}
		
		List messages = null;

		if (key != null) {
			messages = container.getMessages(key);
		} else if (all) {
			messages = container.getAllMessages();
		} else {
			messages = container.getMessages();
		}

		if (messages != null && !messages.isEmpty()) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}	

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}
}
