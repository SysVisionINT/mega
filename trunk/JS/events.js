function getValue(input) {
	return $F(input);
}

function getInputValue(formID, inputName) {
	var form = $(formID);
	var input = form[inputName];
	
	return getValue(input);
}

function executeEvent(url, obj) {
	new Ajax.Request(url, {
		   parameters: obj,
		   onSuccess: doResponse,
		   onFailure: doError
	});
}

function doResponse (transport) {
	var jArray = transport.responseText.evalJSON(true);
	
	for (var index = 0, len = jArray.length; index < len; ++index) {
		var change = jArray[index];

		var finalFunction = new Function("obj", change.functionName + "(obj)");
		
		finalFunction(change.data);
	}
}

function doError (transport) {
	alert(transport);
}

function innerHTML (obj) {
	$(obj.id).update(obj.html);
} 