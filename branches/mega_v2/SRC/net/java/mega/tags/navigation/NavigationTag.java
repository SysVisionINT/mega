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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.common.http.NavigationUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class NavigationTag extends TagSupport {
	private static final long serialVersionUID = 41436596675023117L;

	private static Log log = LogFactory.getLog(NavigationTag.class);

	private String forward = null;
	private String redirect = null;

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		try {
			if (forward != null) {
				NavigationUtil.forward(request, response, forward);

				return SKIP_PAGE;
			}

			if (redirect != null) {
				NavigationUtil.redirect(request, response, redirect);

				return SKIP_PAGE;
			}
		} catch (Exception e) {
			log.error("Error while doing a " + (forward == null ? "redirect" : "forward") + " to "
					+ (forward == null ? redirect : forward), e);
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
