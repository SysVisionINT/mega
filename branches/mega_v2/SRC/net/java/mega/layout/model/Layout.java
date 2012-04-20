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
package net.java.mega.layout.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Layout implements Serializable {
	private static final long serialVersionUID = -7728406004762204001L;
	
	private Map<String, Page> pageMap = new HashMap<String, Page>();
	private List<String> bundleList = new ArrayList<String>();
	
	public void addPage(Page page) {
		pageMap.put(page.getPageName(), page);
	}
	
	public Page getPage(String pageName) {
		return pageMap.get(pageName);
	}
	
	public String[] getPageNames() {
		Set<String> set = pageMap.keySet();
		
		return set.toArray(new String[set.size()]);
	}
	
	public void addBundle(String bundleName) {
		bundleList.add(bundleName);
	}
	
	public List<String> getBundleList() {
		return bundleList;
	}
}
