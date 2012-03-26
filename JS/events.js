function totalEncode(str){
	var s = escape(trim(str));
	
	s = s.replace(/+/g, "+");
	s = s.replace(/@/g, "@");
	s = s.replace(///g, "/");
	s = s.replace(/*/g, "*");
	
	return(s);
}

function ajaxPOST(path) {	
	var params = "";
	var i = 1;
	
	while (i < arguments.length) {
		if (i > 1) {
			params += "&";
		}
		
		params += arguments[i] + "=";
		params += totalEncode(arguments[i + 1]);
		
		i += 2;
	}
	
	var xmlhttp = getXMLHTTPObject();
	
	http.open("POST", path, true);
	
	//Send the proper header information along with the request
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.setRequestHeader("Content-length", params.length);
	xmlhttp.setRequestHeader("Connection", "close");
	
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			processAjaxResponse(xmlhttp.responseXML);
		}
	}
	
	xmlhttp.send(params);
}

function getXMLHTTPObject() {
	var xmlhttp;

	if (window.XMLHttpRequest) {
	// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp = new XMLHttpRequest();
	} else {
	// code for IE6, IE5
	  xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	return xmlhttp;
}

function processAjaxResponse(response) {
	var output = response.getElementsByTagName("EVENT-OUTPUT");
	
	for (i = 0; i < output.length; i++) {
	  txt=txt + x[i].childNodes[0].nodeValue + "<br />";
	}
}