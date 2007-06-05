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
package net.java.mega.tags.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.action.error.ActionException;
import net.java.mega.action.util.URLUtil;
import net.java.mega.tags.form.FormTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public abstract class SubmitBaseTag extends BaseBodyTag {
	private static final String ONCLICK = "onclick";

	private static Log log = LogFactory.getLog(SubmitBaseTag.class);
	
	private String method = null;
	private String tabIndex = null;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}
	
	public abstract boolean elementCanBeDisabled(); 

	public void writeAttributes() throws IOException {
		if (getTabIndex() != null) {
			addAttribute(new Attribute("tabindex", getTabIndex()));
		}
		
		Tag tag = findAncestorWithClass(this, FormTag.class);

		if (tag != null) {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

			URLUtil url = new URLUtil(request, response);
			
			String formName = ((FormTag) tag).getId();
			String fieldName = ((FormTag) tag).getActionField();
			
			StringBuffer buffer  = new StringBuffer();
			
			String userOnClick = getUserOnClick();
			
			if (userOnClick != null) {
				buffer.append(userOnClick);
			}
			
			if (elementCanBeDisabled()) {
				buffer.append("this.disabled=true;");
			}
			
			buffer.append("document.forms['");
			buffer.append(formName);
			buffer.append("']['");
			buffer.append(fieldName);
			buffer.append("'].value='");
			
			try {
				buffer.append(url.getRAWMethodName(null, getMethod()));
			} catch (ActionException e) {
				log.error("Error while trying to generate URL for method " + getMethod(), e);
				throw new IOException(e.getMessage());
			}
			
			buffer.append("';document.forms['");
			buffer.append(formName);
			buffer.append("'].submit();return false;");
			
			addAttribute(new Attribute(ONCLICK, buffer.toString()));
		}
		
		super.writeAttributes();
	}

	private String getUserOnClick() {
		String value = deleteAttribute(ONCLICK);
		
		if (value != null) {
			value = value.trim();
			
			if (!value.endsWith(";")) {
				value = value.concat(";");
			}
		}
		
		return value;
	}
}
