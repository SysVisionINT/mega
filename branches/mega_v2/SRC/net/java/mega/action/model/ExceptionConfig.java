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

import net.java.mega.action.api.ResponseProvider;


public class ExceptionConfig implements Serializable {
	private static final long serialVersionUID = -7570488832968223483L;
	
	private String className = null;
	private ResponseProvider forward = null;
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public ResponseProvider getForward() {
		return forward;
	}
	
	public void setForward(ResponseProvider forward) {
		this.forward = forward;
	}
}
