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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.action.ActionManager;
import net.java.mega.action.model.Action;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.URLUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class ActionTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private static Log log = LogFactory.getLog(ActionTag.class);

	private String action = null;

	private String method = null;

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		URLUtil url = new URLUtil(request, response);

		try {
			if (action == null) {
				Action actionObject = (Action) request.getAttribute(Constants.CURRENT_ACTION);
				ActionConfig actionConfig = ActionManager.getInstance().getActionConfig(actionObject.getClass());
				action = actionConfig.getName();
			}

			if (method == null) {
				pageContext.getOut().print(url.getActionURL(action));
			} else {
				pageContext.getOut().print(url.getMethodURL(action, method));
			}
		} catch (Exception e) {
			log
					.error("Error while writing URL for (action = " + action + (method == null ? "" : " method = " + method)
							+ ")", e);

			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

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
}
