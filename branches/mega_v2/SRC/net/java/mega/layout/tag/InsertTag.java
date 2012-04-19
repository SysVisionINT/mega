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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.layout.model.Block;
import net.java.mega.layout.model.Layout;
import net.java.mega.layout.model.Page;
import net.java.mega.layout.model.PageExtend;
import net.java.mega.layout.util.Constant;
import net.java.mega.layout.util.LayoutUtil;
import net.java.sjtools.logging.plus.RLog;

public class InsertTag extends TagSupport {
	private static final long serialVersionUID = -7691347878853261726L;
	
	private String pageLayout = null;
	private Page page = null;

	public int doStartTag () {
		page = new Page();
		page.setPageName(Constant.INNER_PAGE);
		
		PageExtend pageBase = new PageExtend();
		pageBase.setPageName(getPageLayout());
		
		page.setPageBase(pageBase);
		
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		if (RLog.isTraceEnabled()) {
			RLog.trace("TAG insert = " + pageLayout);
		}

		Layout layout = (Layout) pageContext.getServletContext().getAttribute(Constant.LAYOUT);
		
		if (layout == null) {
			RLog.error(Constant.LAYOUT + " Not found!");
			throw new JspException(Constant.LAYOUT + " Not found!");
		}
		
		String layoutLocation = LayoutUtil.getLayoutLocation(layout, pageLayout);

		if (layoutLocation == null) {
			RLog.error("Page Layout " + pageLayout + " not found");
			throw new JspException("Page Layout " + pageLayout + " not found");			
		} else {
			try {
				ServletRequest request = pageContext.getRequest();
				
				request.setAttribute(Constant.PAGE_NAME, Constant.INNER_PAGE);
				request.setAttribute(Constant.INNER_PAGE, page);
				
				pageContext.include(layoutLocation);
			} catch (Exception e) {
				RLog.error("Page Layout " + pageLayout + " not found");
				throw new JspException(e);	
			}
		}

		return EVAL_PAGE;
	}

	public String getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(String pageLayout) {
		this.pageLayout = pageLayout;
	}
	
	public void addBlock(Block block) {
		page.addBlock(block);
	}
}
