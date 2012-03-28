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

// Helper functions

function getValue(input) {
	return $F(input);
}

function getInputValue(formID, inputName) {
	var form = $(formID);
	var input = form[inputName];
	
	return getValue(input);
}

// Ajax processing

function executeEvent(url, obj) {
	new Ajax.Request(url, 
		{
		   parameters: obj,
		   onSuccess: doResponse
		}
	);
}

function doResponse (transport) {
	var jArray = transport.responseText.evalJSON(true);
	
	for (var i = 0; i < jArray.length; i++) {
		var change = jArray[i];

		var finalFunction = new Function("obj", change.functionName + "(obj)");
		
		finalFunction(change.data);
	}
}

// Event change functions

function innerHTML (obj) {
	$(obj.id).update(obj.html);
} 

function setValue (obj) {
	$(obj.id).setValue(obj.value);
}

function updateOptions (obj) {
	var element = document.getElementById(obj.id);
	
	element.options.length = 0;
	
	for (var i = 0; i < obj.options.length; i++) {
		element.options[i] = new Option();
		
		element.options[i].text = obj.options[i].text;
		element.options[i].value = obj.options[i].value;
		
		if (obj.options[i].selected) {
			element.options[i].selected = true;
		}
	}
}

function updateChecks (obj) {
    var elements = document.getElementsByName(obj.name);
    
    for (var i = 0; i < elements.length; i++) {
          elements[i].checked = false;
          
          for (var j = 0; j < obj.values.length; j++) {
                if (elements[i].value == obj.values[j]) {
                      elements[i].checked = true;
                }
          }
    }
}