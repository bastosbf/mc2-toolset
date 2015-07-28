String.prototype.startsWith = function(str) {
	return (this.match("^" + str) == str)
}

function wait(milliseconds) {
	setTimeout(function() {
		var start = new Date().getTime();
		while ((new Date().getTime() - start) < milliseconds)
			;
	}, 0);
}
function popup(mylink, width, height) {
	if (!window.focus) {
		return true;
	}
	
	var href;
	if (typeof (mylink) == 'string') {
		href = mylink;
	} else {
		href = mylink.href;
	}
	
	var left = (screen.width/2)-(width/2);
	var top = (screen.height/2)-(height/2);	
	popupWindow = window
			.open(href, "", "width=" + width + ",height=" + height + ",scrollbars=yes,top=" + top + ",left=" + left);
	if (popupWindow == null || typeof (popupWindow) == 'undefined') {
		alert('Please disable your pop-up blocker and try to download again.');
	} else {
		popupWindow.focus();
	}
	return popupWindow;
}
function showHideDiv(div, value) {
	var el = document.getElementById(div);
	if (el.style.display == 'none') {
		el.style.display = 'block';
	} else if (value == null || el.innerHTML == value) {
		el.style.display = 'none';
	}
	if (value != null) {
		el.innerHTML = value;
	}
}
function post(url, target) {
	var query = url.substring(url.indexOf("?") + 1);
	var action = url.substring(0, url.indexOf("?"));
	var elements = query.split("&");
	var map = new Array();
	for ( var i in elements) {
		var el = elements[i].split("=");
		map[el[0]] = el[1];
	}
	if (target == null) {
		target = window;
	}
	var form = target.document.createElement("FORM");
	for ( var key in map) {
		var param = target.document.createElement("INPUT");
		param.type = "hidden";
		param.name = key;
		param.id = key;
		param.value = map[key];
		form.appendChild(param);
	}
	form.method = "POST";
	form.action = action;
	// add form to body due firefox
	target.document.body.appendChild(form);
	form.submit();
}
function showLoading(id) {	
	if(id == null) {
		id = "loading-indicator";
	}
	$('#'+id).modal({
	"backdrop" : "static",
	"keyboard" : true,
	"show" : true
	});
}

function hideLoading(id) {
	if(id == null) {
		id = "loading-indicator";
	}
	$('#'+id).modal('hide');
}

function startDownload(action) {
	var id = Date.now();
	var div = document.getElementById("downloads");
	if(div == null) {
		div = parent.document.getElementById("downloads");
	}
	var iframe = document.createElement('iframe');
	iframe.id = id;
	iframe.width = "100%";
	iframe.height = "85px";
	iframe.frameBorder = "0";
	iframe.src = action;
	div.appendChild(iframe);
}

function closeIFrame(id) {
	$('#' + id).remove();
}