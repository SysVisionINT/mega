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

import net.java.mega.action.api.events.EventChange;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UpdateListBox implements EventChange {
	private static final long serialVersionUID = 2846007885294488906L;
	
	private String id = null;
	private JSONArray options = new JSONArray();
	
	public UpdateListBox(String id) {
		this.id = id;
	}
	
	public void addOption(String text, String value, boolean selected) {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("text", text);
		jsonObject.put("value", value);
		jsonObject.put("selected", new Boolean(selected));
		
		options.add(jsonObject);
	}

	public String getFunctionName() {
		return "updateOptions";
	}

	public JSONObject getJSONObject() {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("id", id);
		jsonObject.put("options", options);
		
		return jsonObject;
	}
}
