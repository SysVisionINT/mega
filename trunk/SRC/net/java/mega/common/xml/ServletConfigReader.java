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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.java.mega.common.model.ServletConfig;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.xml.SimpleParser;

public class ServletConfigReader {
	private static Log log = LogFactory.getLog(ServletConfigReader.class);

	public static ServletConfig getServletConfig(String servletName, InputStream inputStream) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("getServletConfig(" + servletName + ", ...)");
		}

		WebXMLHandler handler = new WebXMLHandler();

		SimpleParser parser = new SimpleParser(handler, false);

		ServletConfig servletConfig = (ServletConfig) parser.parse(getSafeInputStream(inputStream));
		servletConfig.prune(servletName);

		return servletConfig;
	}

	private static InputStream getSafeInputStream(InputStream inputStream) throws Exception {
		String webXML = readText(inputStream);
		
		StringBuffer buffer = new StringBuffer();
		int pos = 0;
		int posEnd = 0;

		while ((pos = webXML.indexOf("<!DOCTYPE")) != -1) {
			buffer.setLength(0);

			posEnd = webXML.indexOf(">", pos);

			if (pos == 0) {
				buffer.append(webXML.substring(posEnd + 1));
			} else {
				buffer.append(webXML.substring(0, pos));
				buffer.append(webXML.substring(posEnd + 1));
			}

			webXML = buffer.toString();
		}
		
		return new ByteArrayInputStream(webXML.getBytes());
	}

	private static String readText(InputStream inputStream) throws Exception {
		byte[] buffer = null;

		try {
			if (inputStream.available() > 0) {
				buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}

		if (buffer == null) {
			return "";
		}
		
		return new String(buffer);
	}
}
