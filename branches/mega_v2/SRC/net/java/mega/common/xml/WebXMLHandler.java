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
package net.java.mega.common.xml;

import net.java.mega.common.model.ServletConfig;
import net.java.mega.common.model.ServletMapping;
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.xml.SimpleHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WebXMLHandler extends SimpleHandler {
	public Object proccessElement(String elementType, Object currentObject, Attributes attributes) {
		if (elementType.equals("web-app")) {
			return new ServletConfig();
		} else if (elementType.equals("servlet-mapping")) {
			ServletMapping ret = new ServletMapping();
			
			((ServletConfig)currentObject).addMap(ret);
			
			return ret;
		}

		return null;
	}

	public void processPCDATA(String elementType, Object currentObject, String value) {		
		if (elementType.equals("servlet-name") && currentObject instanceof ServletMapping) {
			if (RLog.isErrorEnabled()) {
				RLog.trace("servlet-name = " + value);
			}
			
			((ServletMapping)currentObject).setName(value);
		} else if (elementType.equals("url-pattern") && currentObject instanceof ServletMapping) {
			if (RLog.isErrorEnabled()) {
				RLog.trace("url-pattern = " + value);
			}
			
			((ServletMapping)currentObject).addUrl(value);
		}
	}
	
	public void error(SAXParseException error) throws SAXException {
		RLog.warn("SAXParseException while reading web.xml", error);
	}	
}