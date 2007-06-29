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
package net.java.mega.action.util.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListMap implements Serializable {
	private static final long serialVersionUID = 2432252270553520753L;
	
	private List keyPares = new ArrayList();

	public void clear() {
		keyPares.clear();
	}

	public void put(String key, Object value) {
		ListMapEntry entry = (ListMapEntry) get(key);
		
		if (entry != null) {
			entry.setValue(value);
		} else {
			keyPares.add(new ListMapEntry(key, value));
		}
	}

	public boolean isEmpty() {
		return keyPares.isEmpty();
	}

	public Object get(String key) {
		ListMapEntry entry = new ListMapEntry(key, null);
		
		int pos = keyPares.indexOf(entry);
		
		if (pos >= 0) {
			return keyPares.get(pos);
		}
			
		return null;
	}

	public Collection values() {
		List list = new ArrayList();
		
		ListMapEntry entry = null;
		
		for (Iterator i = keyPares.iterator(); i.hasNext();) {
			entry = (ListMapEntry) i.next();
			
			list.add(entry.getValue());
		}
		
		return list;
	}

	public Collection keys() {
		List list = new ArrayList();
		
		ListMapEntry entry = null;
		
		for (Iterator i = keyPares.iterator(); i.hasNext();) {
			entry = (ListMapEntry) i.next();
			
			list.add(entry.getKey());
		}
		
		return list;
	}

	
}
