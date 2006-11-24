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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.java.mega.action.api.Message;


public class MessageContainer implements Serializable {
	private static final long serialVersionUID = 6209135557365223874L;

	private static final String GLOBAL_MESSAGE = MessageContainer.class.getName();

	private Map messages = new HashMap();

	public void clearMessages() {
		messages.clear();
	}

	public void addMessage(String key, Message message) {
		List keyList = getMessages(key);

		if (keyList == null) {
			keyList = new ArrayList();

			messages.put(key, keyList);
		}

		keyList.add(message);
	}

	public void addMessage(Message message) {
		addMessage(GLOBAL_MESSAGE, message);
	}

	public boolean hasMessages() {
		return !messages.isEmpty();
	}

	public boolean hasGlobalMessages() {
		return hasMessages(GLOBAL_MESSAGE);
	}

	public boolean hasMessages(String key) {
		List keyList = getMessages(key);

		if (keyList != null && !keyList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public List getMessages(String key) {
		return (List) messages.get(key);
	}

	public List getMessages() {
		return getMessages(GLOBAL_MESSAGE);
	}

	public List getAllMessages() {
		List ret = new ArrayList();

		for (Iterator i = messages.values().iterator(); i.hasNext();) {
			ret.addAll((List) i.next());
		}

		return ret;
	}

	public Collection getKeys() {
		Set keys = messages.keySet();

		keys.remove(GLOBAL_MESSAGE);

		return keys;
	}
}
