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
package net.java.mega.layout.util;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import net.java.mega.common.http.HTMLUtil;
import net.java.mega.common.http.WARContextUtil;
import net.java.mega.common.resource.LocaleUtil;
import net.java.mega.common.resource.MessageUtil;
import net.java.mega.layout.extention.Controller;
import net.java.mega.layout.model.BeanContent;
import net.java.mega.layout.model.Block;
import net.java.mega.layout.model.BlockContent;
import net.java.mega.layout.model.ControlerContent;
import net.java.mega.layout.model.Layout;
import net.java.mega.layout.model.MessageKeyContent;
import net.java.mega.layout.model.Page;
import net.java.mega.layout.model.PathContent;
import net.java.mega.layout.model.StringContent;
import net.java.sjtools.logging.plus.RLog;

public class LayoutServletUtil {

	public static void processBlock(PageContext pageContext, String blockName) throws IOException, ServletException {
		if (RLog.isTraceEnabled()) {
			RLog.trace("processBlock(..., " + blockName + ")");
		}

		String pageName = (String) pageContext.getRequest().getAttribute(Constant.PAGE_NAME);
		Layout layout = (Layout) pageContext.getServletContext().getAttribute(Constant.LAYOUT);

		if (pageName == null || layout == null) {
			RLog.error("System not initialize");

			return;
		}

		Page page = null;

		if (pageName.equals(Constant.INNER_PAGE)) {
			page = (Page) pageContext.getRequest().getAttribute(Constant.INNER_PAGE);
		} else {
			page = layout.getPage(pageName);
		}

		if (page != null) {
			Block block = LayoutUtil.findBlock(layout, page, blockName);

			if (block != null) {
				BlockContent content = block.getContent();

				processContent(pageContext, pageName, blockName, content);
			} else {
				RLog.error("No definition for block " + blockName + " on page " + pageName);
			}
		} else {
			RLog.error("Page " + pageName + " not found");
		}
	}

	private static void processContent(PageContext pageContext, String pageName, String blockName, BlockContent content) throws IOException, ServletException {

		if (content instanceof PathContent) {
			PathContent pathContent = (PathContent) content;

			if (RLog.isTraceEnabled()) {
				RLog.trace(pageName + "." + blockName + " = " + pathContent.getValue());
			}

			includeContent(pageContext, pathContent);
		} else if (content instanceof StringContent) {
			StringContent stringContent = (StringContent) content;

			if (RLog.isTraceEnabled()) {
				RLog.trace(pageName + "." + blockName + " = '" + stringContent.getValue() + "'");
			}

			includeContent(pageContext, stringContent);
		} else if (content instanceof MessageKeyContent) {
			MessageKeyContent keyContent = (MessageKeyContent) content;

			if (RLog.isTraceEnabled()) {
				RLog.trace(pageName + "." + blockName + " = '" + keyContent.getValue() + "'");
			}

			includeContent(pageContext, keyContent);
		} else if (content instanceof BeanContent) {
			BeanContent beanContent = (BeanContent) content;

			if (RLog.isTraceEnabled()) {
				RLog.trace(pageName + "." + blockName + " = " + beanContent.getName() + (beanContent.getProperty() == null ? "" : "." + beanContent.getProperty()));
			}

			includeContent(pageContext, beanContent);
		} else if (content instanceof ControlerContent) {
			ControlerContent controlerContent = (ControlerContent) content;

			if (RLog.isTraceEnabled()) {
				RLog.trace(pageName + "." + blockName + " -> " + controlerContent.getClassName());
			}

			executeControler(pageContext, pageName, blockName, controlerContent);
		}
	}

	private static void executeControler(PageContext pageContext, String pageName, String blockName, ControlerContent controlerContent) throws IOException, ServletException {

		Controller controler = null;

		try {
			controler = getControler(pageContext, controlerContent);
		} catch (Exception e) {
			RLog.error("Unable to create instance of " + controlerContent.getClassName(), e);

			throw new ServletException("Unable to create instance of " + controlerContent.getClassName(), e);
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		processContent(pageContext, pageName, blockName, controler.getBlockContent(pageName, blockName, controlerContent.getParameters(), request, response));
	}

	private static Controller getControler(PageContext pageContext, ControlerContent controlerContent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Controller controler = null;

		StringBuilder buffer = new StringBuilder();
		buffer.append(Constant.CONTROLER);
		buffer.append(controlerContent.getClassName());

		String key = buffer.toString();

		controler = (Controller) pageContext.getServletContext().getAttribute(key);

		if (controler == null) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			controler = (Controller) cl.loadClass(controlerContent.getClassName()).newInstance();

			pageContext.getServletContext().setAttribute(key, controler);
		}

		return controler;
	}

	private static void includeContent(PageContext pageContext, StringContent content) throws IOException {
		pageContext.getOut().print(content.getValue());
	}

	private static void includeContent(PageContext pageContext, MessageKeyContent content) throws IOException {
		Layout layout = (Layout) pageContext.getServletContext().getAttribute(Constant.LAYOUT);
		Locale locale = LocaleUtil.getUserLocate((HttpServletRequest) pageContext.getRequest());

		String msg = MessageUtil.getMessage(layout.getBundleList(), content.getValue(), locale);

		if (msg != null) {
			if (content.isFilter()) {
				msg = HTMLUtil.filter(msg);
			}
		} else {
			msg = "{".concat(content.getValue()).concat("}");
		}

		pageContext.getOut().print(msg);
	}

	private static void includeContent(PageContext pageContext, BeanContent content) throws IOException {
		pageContext.getOut().print(WARContextUtil.getValue(pageContext, content.getName(), content.getProperty()));
	}

	private static void includeContent(PageContext pageContext, PathContent content) throws IOException, ServletException {
		pageContext.include(content.getValue());
	}
}
