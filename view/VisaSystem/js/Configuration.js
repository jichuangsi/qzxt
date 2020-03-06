var url;
var token;
//修改路径
function httpUrl() {
	// url = "http://192.168.1.3:8080"
	url = "http://192.168.31.84:8080"
	return url;
}
//获取token
function getToken() {
	return token = sessionStorage.getItem('token');
}

function ajaxGET(url) {
	$.ajax({
		type: "GET",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken()
		},
		success: function(res) {
			if (res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				return res.data;
			} else if (res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if (res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});
}

function ajaxPOST(url, param) {
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken(),
		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			if (res.code == '0010') {
				layui.notice.success("提示信息:成功!");
				data = res.data;
			} else if (res.code == '0031') {
				layui.notice.info("提示信息：权限不足");
			} else if (res.code == '0050') {
				layui.notice.error("提示信息:错误!");
			}
		}
	});
	return false;
}

function setMsg(msg, icon) {
	layer.msg(msg, {
		icon: icon,
		time: 1000
	});
}

function getAjaxData(url) {
	var data;
	$.ajax({
		type: "GET",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken()
		},
		success: function(res) {
			data = res;
		}
	});
	return data;
}

function getAjaxPostData(url,param) {
	var data;
	$.ajax({
		type: "post",
		url: httpUrl() + url,
		async: false,
		headers: {
			'accessToken': getToken(),
		},
		contentType: 'application/json',
		data: JSON.stringify(param),
		success: function(res) {
			data=res;
		}
	});
	return data;
}
function UrlSearch() { //获取url里面的参数
	var name, value;
	var str = location.href; //取得整个地址栏
	var num = str.indexOf("?")
	str = str.substr(num + 1); //取得所有参数   stringvar.substr(start [, length ]
	var arr = str.split("="); //各个参数放到数组里
	return arr[1];
}