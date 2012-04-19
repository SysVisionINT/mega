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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.layout.util.LayoutServletUtil;
import net.java.sjtools.logging.plus.RLog;

public class BlockTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private String blockName = null;

	public int doEndTag() throws JspException {
		if (RLog.isTraceEnabled()) {
			RLog.trace("TAG block = " + blockName);
		}

		try {
			LayoutServletUtil.processBlock(pageContext, blockName);
		} catch (Exception e) {
			RLog.error("Error writing block " + getName() + " content", e);
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	public String getName() {
		return blockName;
	}

	public void setName(String blockName) {
		this.blockName = blockName;
	}
}
