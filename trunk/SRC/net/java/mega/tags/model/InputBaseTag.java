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

import net.java.mega.action.util.Constants;
import net.java.mega.common.http.WARContextUtil;

public abstract class InputBaseTag extends BaseBodyTag {
	private static final long serialVersionUID = -6219700728769842285L;
	
	private String property = null;
	private String tabIndex = null;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public Object getPropertyValue() {
		return WARContextUtil.getValue(pageContext, Constants.CURRENT_ACTION, property);
	}

	public void writeAttributes() throws IOException {
		if (getTabIndex() != null) {
			addAttribute(new Attribute("tabindex", getTabIndex()));
		}
		
		super.writeAttributes();
	}
}
