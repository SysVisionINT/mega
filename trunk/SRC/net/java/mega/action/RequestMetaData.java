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
package net.java.mega.action;

import net.java.mega.action.api.RequestParameters;
import net.java.mega.action.model.ActionConfig;
import net.java.mega.action.util.Constants;
import net.java.mega.action.util.MethodConstants;

public class RequestMetaData {
	private ActionConfig actionConfig = null;
	private String path = null;
	private String token = null;
	private String methodName = MethodConstants.ON_LOAD;
	private String doMethod = Constants.HTTP_GET;
	private RequestParameters parameters = null; 
	
	public String getDoMethod() {
		return doMethod;
	}

	public void setDoMethod(String doMethod) {
		this.doMethod = doMethod;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public ActionConfig getActionConfig() {
		return actionConfig;
	}

	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public RequestParameters getParameters() {
		return parameters;
	}

	public void setParameters(RequestParameters parameters) {
		this.parameters = parameters;
	}
}
