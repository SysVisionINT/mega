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
package net.java.mega.common.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServletConfig {
	private List mapping = new ArrayList();

	public List getMapping() {
		return mapping;
	}

	public void addMap(ServletMapping sm) {
		mapping.add(sm);
	}

	public void prune(String servletName) {
		List deleteList = new ArrayList();

		ServletMapping servletMapping = null;

		for (Iterator i = mapping.iterator(); i.hasNext();) {
			servletMapping = (ServletMapping) i.next();
			
			if (!servletMapping.getName().equals(servletName)) {
				deleteList.add(servletMapping);
			}
		}
		
		for (Iterator i = deleteList.iterator(); i.hasNext();) {
			mapping.remove(i.next());
		}
	}
}
