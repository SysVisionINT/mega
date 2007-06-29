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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.java.mega.action.MessageContainer;
import net.java.mega.action.api.Message;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.ActionMessageUtil;
import net.java.mega.common.http.HTMLUtil;
import net.java.mega.common.resource.LocaleUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.TextUtil;

public class MessageTag extends TagSupport {
	private static final long serialVersionUID = 1824165782784735482L;

	private static final String HEADER = "errors.header";
	private static final String PREFIX = "errors.prefix";
	private static final String SUFFIX = "errors.suffix";
	private static final String FOOTER = "errors.footer";

	private static Log log = LogFactory.getLog(MessageTag.class);

	private String key = null;
	private boolean filter = true;
	private boolean all = false;

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		Locale locale = LocaleUtil.getUserLocate(request);

		String header = ActionMessageUtil.getMessage(HEADER, locale);
		String prefix = ActionMessageUtil.getMessage(PREFIX, locale);
		String suffix = ActionMessageUtil.getMessage(SUFFIX, locale);
		String footer = ActionMessageUtil.getMessage(FOOTER, locale);

		if (header == null) {
			header = "";
		}

		if (prefix == null) {
			prefix = "";
		}

		if (suffix == null) {
			suffix = "";
		}

		if (footer == null) {
			footer = "";
		}

		JspWriter writer = pageContext.getOut();
		Message message = null;
		String value = null;

		MessageContainer container = (MessageContainer) request.getAttribute(Constants.MESSAGE_CONTAINER);

		if (container != null) {
			List messages = null;

			if (key != null) {
				messages = container.getMessages(key);
			} else if (all) {
				messages = container.getAllMessages();
			} else {
				messages = container.getMessages();
			}

			if (messages != null && !messages.isEmpty()) {
				try {
					writer.print(header);

					for (Iterator i = messages.iterator(); i.hasNext();) {
						message = (Message) i.next();

						value = ActionMessageUtil.getMessage(message.getMessageKey(), locale);

						if (value != null) {
							if (message.getParameters() != null && !message.getParameters().isEmpty()) {
								value = TextUtil.replace(value, message.getParameters());
							}

							if (filter) {
								value = HTMLUtil.filter(value);
							}
						} else {
							value = "{".concat(message.getMessageKey()).concat("}");
						}

						writer.print(prefix);
						writer.print(value);
						writer.print(suffix);
					}

					writer.print(footer);
				} catch (IOException e) {
					log.error("Error while writing messages for key = " + key, e);

					throw new JspException(e);
				}
			}
		}

		return EVAL_PAGE;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}
}
