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

package net.java.mega.action.api;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.RequestMetaData;
import net.java.mega.action.RequestProcessor;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.model.ActionWrapper;


public abstract class AbstractWrapper implements ActionWrapper {
	private ActionWrapper next = null;
	private Properties config = new Properties();

	public void addProperty(String name, String value) {
		config.setProperty(name, value);
	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public void setNext(ActionWrapper actionWrapper) {
		next = actionWrapper;
	}

	public ResponseMetaData executeNext(HttpServletRequest request, HttpServletResponse response,
			RequestMetaData requestMetaData) throws Exception {

		if (next == null) {
			RequestProcessor processor = new RequestProcessor(request, response);

			return processor.process(requestMetaData);
		}

		return next.execute(request, response, requestMetaData);
	}

	public abstract ResponseMetaData execute(HttpServletRequest request, HttpServletResponse response,
			RequestMetaData requestMetaData) throws Exception;
}
