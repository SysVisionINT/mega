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
package net.java.mega.layout.xml;

import net.java.mega.layout.model.*;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.util.TextUtil;
import net.java.sjtools.xml.SimpleHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LayoutConfigHandler extends SimpleHandler {

	private static Log log = LogFactory.getLog(LayoutConfigHandler.class);

	public Object proccessElement(String elementType, Object currentObject, Attributes attributes) {
		if (elementType.equals("layout-config")) {
			return new Layout();
		} else if (elementType.equals("page")) {
			Layout layout = (Layout) currentObject;

			Page ret = new Page();

			ret.setPageName(attributes.getValue("name"));

			layout.addPage(ret);

			if (log.isDebugEnabled()) {
				log.debug("new Page = " + ret.getPageName());
			}

			return ret;
		} else if (elementType.equals("message-resources")) {
			Layout layout = (Layout) currentObject;

			String bundleName = attributes.getValue("name");

			layout.addBundle(bundleName);

			if (log.isDebugEnabled()) {
				log.debug("bundle = " + bundleName);
			}
		} else if (elementType.equals("layout-path")) {
			Page page = (Page) currentObject;

			LayoutPath ret = new LayoutPath();

			ret.setPath(attributes.getValue("value"));

			page.setPageBase(ret);

			if (log.isDebugEnabled()) {
				log.debug(" - Page(" + page.getPageName() + ") -> layout-path = " + ret.getPath());
			}

			return ret;
		} else if (elementType.equals("extends")) {
			Page page = (Page) currentObject;

			PageExtend ret = new PageExtend();

			ret.setPageName(attributes.getValue("page-name"));

			page.setPageBase(ret);

			if (log.isDebugEnabled()) {
				log.debug(" - Page(" + page.getPageName() + ") -> extends = " + ret.getPageName());
			}

			return ret;
		} else if (elementType.equals("block")) {
			Page page = (Page) currentObject;

			Block ret = new Block();

			ret.setBlockName(attributes.getValue("name"));

			page.addBlock(ret);

			if (log.isDebugEnabled()) {
				log.debug(" - Page(" + page.getPageName() + ") -> add(new Block(" + ret.getBlockName() + "))");
			}

			return ret;
		} else if (elementType.equals("string")) {
			Block block = (Block) currentObject;

			StringContent ret = new StringContent();

			ret.setValue(attributes.getValue("value"));

			block.setContent(ret);

			if (log.isDebugEnabled()) {
				log.debug(" -- Block(" + block.getBlockName() + ") -> string = '" + ret.getValue() + "'");
			}

			return ret;
		} else if (elementType.equals("message-key")) {
			Block block = (Block) currentObject;

			MessageKeyContent ret = new MessageKeyContent();

			ret.setValue(attributes.getValue("value"));

			block.setContent(ret);

			if (log.isDebugEnabled()) {
				log.debug(" -- Block(" + block.getBlockName() + ") -> key = '" + ret.getValue() + "'");
			}

			return ret;			
		} else if (elementType.equals("path")) {
			Block block = (Block) currentObject;

			PathContent ret = new PathContent();

			ret.setValue(attributes.getValue("value"));

			block.setContent(ret);

			if (log.isDebugEnabled()) {
				log.debug(" -- Block(" + block.getBlockName() + ") -> path = " + ret.getValue());
			}

			return ret;
		} else if (elementType.equals("bean")) {
			Block block = (Block) currentObject;

			BeanContent ret = new BeanContent();

			ret.setName(attributes.getValue("name"));

			if (!TextUtil.isEmptyString(attributes.getValue("property"))) {
				ret.setProperty(attributes.getValue("property"));
			}

			block.setContent(ret);

			if (log.isDebugEnabled()) {
				log.debug(" -- Block(" + block.getBlockName() + ") -> bean = " + ret.getName()
						+ (ret.getProperty() == null ? "" : "." + ret.getProperty()));
			}

			return ret;
		} else if (elementType.equals("controler")) {
			Block block = (Block) currentObject;

			ControlerContent ret = new ControlerContent();

			ret.setClassName(attributes.getValue("class-name"));

			block.setContent(ret);

			if (log.isDebugEnabled()) {
				log.debug(" -- Block(" + block.getBlockName() + ") -> controler = " + ret.getClassName());
			}

			return ret;
		} else if (elementType.equals("parameter")) {
			ControlerContent controlerContent = (ControlerContent) currentObject;

			String name = attributes.getValue("name");
			String value = attributes.getValue("value");

			controlerContent.addParameter(name, value);

			if (log.isDebugEnabled()) {
				log.debug(" --- Controler(" + controlerContent.getClassName() + ") -> addParameter(" + name + ", "
						+ value + ")");
			}

			return null;
		}

		return null;
	}

	public void processPCDATA(String elementType, Object currentObject, String value) {
	}

	public void error(SAXParseException error) throws SAXException {
		log.error("SAX Error", error);

		throw new SAXException(error);
	}
}