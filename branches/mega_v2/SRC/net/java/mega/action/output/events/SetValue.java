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
package net.java.mega.action.output.events;

import org.json.simple.JSONObject;

import net.java.mega.action.api.events.EventChange;

public class SetValue implements EventChange {
	private static final long serialVersionUID = -7698549838412054791L;
	
	private String id = null;
	private String value = null;
	
	public SetValue(String id, String value) {
		this.id = id;
		this.value = value;
	}

	public String getFunctionName() {
		return "setValue";
	}

	public JSONObject getJSONObject() {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("id", id);
		jsonObject.put("value", value);
		
		return jsonObject;
	}
}
