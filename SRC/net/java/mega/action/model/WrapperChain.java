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
package net.java.mega.action.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.mega.action.wrapper.BasicWrapper;
import net.java.sjtools.util.TextUtil;


public class WrapperChain implements Serializable {
	private static final long serialVersionUID = -7561581642985780823L;
	
	private String name = null;
	private ActionWrapper actionWrapper = new BasicWrapper();
	private List urlPatternList = new ArrayList();
	
	public ActionWrapper getActionWrapper() {
		return actionWrapper;
	}
	
	public void setActionWrapper(ActionWrapper actionWrapper) {
		this.actionWrapper = actionWrapper;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addPattern(String urlPattern) {
		Map map = new HashMap();
		map.put("*", ".*");
		map.put("?", ".");
		
		String expression = TextUtil.replace(urlPattern, map);
		
		urlPatternList.add(expression);
	}
	
	public boolean matches(String path) {
		if (urlPatternList.isEmpty()) {
			return false;
		}
		
		for (Iterator i = urlPatternList.iterator(); i.hasNext();) {
			if (path.matches((String) i.next())) {
				return true;
			}
		}
		
		return false;
	}
}
