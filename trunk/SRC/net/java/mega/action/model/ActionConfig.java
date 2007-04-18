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
import java.util.Properties;

import net.java.mega.action.api.ResponseProvider;

public class ActionConfig implements Serializable {
	private static final long serialVersionUID = 7956945334098859770L;

	private Properties config = new Properties();
	private String name = null;
	private ResponseProvider forward = null;
	private Class clazz = null;
	private String wrapperChain = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public String getWrapperChain() {
		return wrapperChain;
	}

	public void setWrapperChain(String wrapperChain) {
		this.wrapperChain = wrapperChain;
	}

	public void addProperty(String name, String value) {
		config.setProperty(name, value);
	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public Properties getProperties() {
		return config;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof ActionConfig)) {
			return false;
		}

		ActionConfig other = (ActionConfig) obj;

		return getClazz().getName().equals(other.getClazz().getName());
	}

	public ResponseProvider getForward() {
		return forward;
	}

	public void setForward(ResponseProvider forward) {
		this.forward = forward;
	}
}
