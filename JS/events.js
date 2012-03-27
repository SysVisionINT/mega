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
	var json = transport.responseText.evalJSON(true);
	
	alert(json);
}

function doError (transport) {
	alert(transport);
}

function innerHTML (obj) {
	$(obj.id).update(obj.html);
} 