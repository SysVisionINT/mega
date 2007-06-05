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

import net.java.mega.action.api.ResponseProvider;
import net.java.mega.action.model.Action;

public class ResponseMetaData {
	private Action action = null;
	private MessageContainer messageContainer = null;
	private ResponseProvider responseProvider = null;
	private boolean sessionInvalidated = false;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public MessageContainer getMessageContainer() {
		return messageContainer;
	}

	public void setMessageContainer(MessageContainer messageContainer) {
		this.messageContainer = messageContainer;
	}

	public ResponseProvider getResponseProvider() {
		return responseProvider;
	}

	public void setResponseProvider(ResponseProvider responseProvider) {
		this.responseProvider = responseProvider;
	}

	public boolean isSessionInvalidated() {
		return sessionInvalidated;
	}

	public void setSessionInvalidated(boolean sessionInvalidated) {
		this.sessionInvalidated = sessionInvalidated;
	}
}
