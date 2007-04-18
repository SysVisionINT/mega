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
package net.java.mega.tags.form;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.java.mega.action.util.Constants;
import net.java.mega.common.util.PageContextUtil;
import net.java.mega.tags.model.BaseTag;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.BeanUtil;

public class OptionsTag extends BaseTag{
	private static final long serialVersionUID = 6019952761717128436L;

	private static Log log = LogFactory.getLog(OptionsTag.class);
	
	private String name = Constants.CURRENT_ACTION;
	private String property = null;
	private String value = null;
	private String label = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int doEndTag() throws JspException {
		log.info("OPTIONS");
		Collection list = (Collection) PageContextUtil.getObject(pageContext, name, property);
		
		if (list != null) {
			Object propertyValue = getPropertyValue();
			Object element = null;
			
			for (Iterator i = list.iterator(); i.hasNext();) {
				element = i.next();
				
				write(element, propertyValue);
			}
		}

		return EVAL_PAGE;
	}

	private void write(Object element, Object propertyValue) throws JspException {
		BeanUtil beanUtil = new BeanUtil(element);
		
		try {
			String value = String.valueOf(beanUtil.get(getValue()));
			
			pageContext.getOut().print("<option value=\"");
			pageContext.getOut().print(value);
			pageContext.getOut().print("\"");

			if (propertyValue != null) {
				if (value.equals(String.valueOf(propertyValue))) {
					pageContext.getOut().print("selected=\"selected\"");
				}
			}
			
			pageContext.getOut().print(">");
			
			if (getLabel() != null) {
				pageContext.getOut().print(beanUtil.get(getLabel()));
			} else {
				pageContext.getOut().print(value);
			}
			
			pageContext.getOut().println("</option>");
		} catch (Exception e) {
			log.error("Error while writing OPTION TAG", e);
			throw new JspException(e);
		}
	}

	private Object getPropertyValue() {
		Tag tag = findAncestorWithClass(this, ComboBoxTag.class);
		
		if (tag != null) {
			return ((ComboBoxTag)tag).getValue();
		}
		
		return null;
	}
}
