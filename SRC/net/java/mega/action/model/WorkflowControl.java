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

public class WorkflowControl implements Serializable {
	private static final long serialVersionUID = 5548851519003266640L;
	
	public WorkflowControl() {
		super();
	}
	
	private long currentToken = 0;

	public long getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(long lastToken) {
		this.currentToken = lastToken;
	}
}
