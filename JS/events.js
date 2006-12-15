function ajaxGET(path) {	
	http_request = getXMLHTTPObject();

	if (!http_request) {
		alert('ERROR: Cannot create an XMLHTTP instance');
		return false;
	}

	http_request.onreadystatechange = function() {
		if (http_request.readyState == 4) {
			if (http_request.status == 200) {
				processAjaxResponse(http_request.responseText);
			} else {
				alert('Server not respond!');
			}
		} else {}
	}

	http_request.open('GET', path, true);

	http_request.send(null);
}

function getXMLHTTPObject() {
	var http_request;

	if (window.XMLHttpRequest) { // Mozilla, Safari,...
		http_request = new XMLHttpRequest();
		
		if (http_request.overrideMimeType) {
			http_request.overrideMimeType('text/xml');
		}
	} else if (window.ActiveXObject) { // IE
		try {
			http_request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				http_request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {}
		}	
	}

	return http_request;
}

function processAjaxResponse( response ) {
	var text = new String(response);
	
	var pos = text.indexOf("<mega-events");
	var posIDIni = 0;
	var posIDEnd = 0;
	var posHTMLIni = 0;
	var posHTMLEnd = 0;	
	
	var responseId = "";
	var responseHTML = "";
	
	while (pos >= 0) {
		posIDIni = text.indexOf("id=\"", pos);
		posIDEnd = text.indexOf("\"", posIDIni + 4);
		
		responseId = text.substring(posIDIni + 4, posIDEnd);
		
		posHTMLIni = text.indexOf(">", pos);
		posHTMLEnd = text.indexOf("</mega-events>", posHTMLIni);
		
		responseHTML = text.substring(posHTMLIni + 1, posHTMLEnd);
		
		document.getElementById(responseId).innerHTML = responseHTML;
		
		pos = text.indexOf("<mega-events", posHTMLEnd);
	}
}