/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.
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
package net.java.mega.action.output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.mega.action.RequestMetaData;
import net.java.mega.action.ResponseMetaData;
import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.error.ActionException;
import net.java.mega.action.util.Constants;
import net.java.mega.common.util.NavigationUtil;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public class Forward implements ResponseProvider {
	private static Log log = LogFactory.getLog(Forward.class);

	private String location = null;

	public Forward(String location) {
		this.location = location;
	}

	public void process(HttpServletRequest request, HttpServletResponse response, RequestMetaData requestMetaData,
			ResponseMetaData responseMetaData) throws ActionException {
		request.setAttribute(Constants.CURRENT_ACTION, responseMetaData.getAction());
		request.setAttribute(Constants.MESSAGE_CONTAINER, responseMetaData.getMessageContainer());

		try {
			NavigationUtil.forward(request, response, location);
		} catch (Exception e) {
			log.error("Error while forward to " + location, e);
			throw new ActionException("Error while forward to " + location, e);
		}
	}
}