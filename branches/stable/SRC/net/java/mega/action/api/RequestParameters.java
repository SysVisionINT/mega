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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class RequestParameters implements Serializable {
	private static final long serialVersionUID = -6010548639382373229L;
	
	private Hashtable parameters = new Hashtable();
	
	public RequestParameters(Hashtable parameters) {
		this.parameters = parameters;
	}
	
	public Enumeration getParameterNames() {
		return parameters.keys();
	}

	public Map getParameterMap() {
		return parameters;
	}
	
	public String getParameter(String name) {
		String[] values =  (String[]) parameters.get(name);
		
		if (values != null) {
			return values[0];
		}
		
		return null;
	}

	public String[] getParameterValues(String name) {
		return (String[]) parameters.get(name);
	}
	
	public void removeParameter(String name) {
		parameters.remove(name);
	}
}
