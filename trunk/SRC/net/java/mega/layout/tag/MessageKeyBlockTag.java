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
package net.java.mega.layout.tag;

import net.java.mega.layout.model.BlockContent;
import net.java.mega.layout.model.MessageKeyContent;

public class MessageKeyBlockTag extends BaseBlockTag {
	private static final long serialVersionUID = -427852391259046306L;
	
	private String value = null;
	private String filter = "true";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public BlockContent getBlockContent() {
		MessageKeyContent content = new MessageKeyContent();
		
		content.setValue(getValue());
		content.setFilter(getFilter().equals("true"));

		return content;
	}
}
