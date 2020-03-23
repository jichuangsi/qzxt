layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			range: '~',
			// range: true,
			type: 'date'
		});
	})

	function getFormatDate() {
		var nowDate = new Date();
		var year = nowDate.getFullYear();
		var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth()) : nowDate.getMonth() + 1;
		var date = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
		return year + "-" + month + "-" + date;
	}

	function getDate() {
		var nowDate = new Date();
		var year = nowDate.getFullYear();
		var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
		var date = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
		return year + "-" + month + "-" + date;
	}
	var arrList;
	table.render({
		elem: '#order',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getStatisticsModel',
		contentType: 'application/json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					type: 'checkbox'
				}, {
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'orderNumber',
					align: 'center',
					title: '订单号'
				},
				{
					field: 'name',
					align: 'center',
					title: '申请人姓名'
				},
				{
					field: 'signTime',
					align: 'center',
					sort: true,
					title: '签收时间',
					templet: function(d) {
						if (d.signTime != 0) {
							var date = new Date(d.signTime); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return Y + M + D
						} else {
							return "-"
						}
					}
				},
				{
					field: 'schedule',
					align: 'center',
					title: '工期'
				},
				{
					field: 'passportNum',
					align: 'center',
					sort: true,
					title: '录入护照本数	'
				},
				{
					field: 'egisNum',
					align: 'center',
					sort: true,
					title: '审核通过数',
					style: 'background-color: #0096888a; color: #fff;'
				},
				{
					field: 'failNum',
					align: 'center',
					sort: true,
					title: '审核未通过数'
					
				},
				{
					field: 'sendNum',
					align: 'center',
					sort: true,
					title: '已送签数'
				}, {
					field: 'refuseNum',
					align: 'center',
					sort: true,
					title: '已拒签数'
				}, {
					field: 'outNum',
					align: 'center',
					sort: true,
					title: '已出签数'
				}, {
					field: 'sendBackNum',
					align: 'center',
					sort: true,
					title: '已寄回数',
					style: 'background-color: #ffb800; color: #fff;'
				},
				{
					field: 'exprotNum',
					align: 'center',
					sort: true,
					title: '已导出数'
				}
			]
		],
		toolbar: '#operation',
		defaultToolbar: ['filter'],
		page: true,
		limit: 10,
		// loading: true,
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {
			"timeStart": getFormatDate(),
			"timeEnd": getDate()
		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.data.list;
				arrList = arr;
				total = res.data.total;
				code = 0;
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		var time = [getFormatDate(), getDate()]
		if (param.time == "") {
			time.push()
		} else {
			time = (param.time).split('~')
		}
		console.log(time[0]);
		table.reload('idTest', {
			where: {
				"timeStart": time[0],
				"timeEnd": time[1],
				"orderNumber": param.orderNumber,
				"telephoneNumber": param.telephoneNumber,
				"signatory": param.signatory,
				"schedule": param.schedule,
				"status": param.status,
				"status": param.status
			}
		})
	})
	var orderIds = []
	//获取选中的数据
	table.on('checkbox(order)', function(obj) {
		let param = obj.data;
		if (obj.checked) {
			orderIds.push(param.orderNumber)
			if (obj.type == "all") {
				$.each(arrList, function(index, item) {
					orderIds.push(item.orderNumber);
				});
			}
		} else {
			if (obj.type == "all") {
				orderIds = [];
			} else {
				for (let i = 0; i < orderIds.length; i++) {
					if (orderIds[i] == param.orderNumber) {
						delete orderIds[i]
					}
				}
			}

		}
	});
	//提交获取的数据
	form.on('submit(toOrder)', function(obj) {
		let url = "/visaHandle/getPassportByOrderIds";
		if (orderIds.length == 0) {
			layer.msg('请选择订单！');
			return
		}
		let param = {
			"orderIds": orderIds
		}
		var data = getAjaxPostData(url, param);
		// var count=(data.data).length;
		// layer.msg('已打印'+count+'本护照信息！');
		setExcel(data);
		table.reload('idTest');
	});
	//打印全部
	form.on('submit(toAllOrder)', function(obj) {
		let url = "/visaHandle/getAllPassportByOrderIds";
		if (orderIds.length == 0) {
			layer.msg('请选择订单！');
			return
		}
		let param = {
			"orderIds": orderIds
		}
		var data = getAjaxPostData(url, param);
		// var count=(data.data).length;
		// layer.msg('已打印'+count+'本护照信息！');
		setExcel(data);
		table.reload('idTest');
	});
	
	function setExcel(data) {
		var tableStr = '<table border="0" cellspacing="" cellpadding="">'
		tableStr += '<tr>';
		tableStr += '<th width="15%">' + '旅行社' + '</td>';
		tableStr += '<th width="15%">' + '姓名' + '</td>';
		tableStr += '<th width="15%">' + '英文姓名' + '</td>';
		tableStr += '<th width="15%">' + '性别' + '</td>';
		tableStr += '<th width="15%">' + '护照号' + '</td>';
		tableStr += '<th width="15%">' + '出生日期' + '</td>';
		tableStr += '<th width="15%">' + '签证种类' + '</td>';
		tableStr += '<th width="15%">' + '备 注' + '</td>';
		tableStr += '<th width="15%">' + '出生地点拼音' + '</td>';
		tableStr += '<th width="15%">' + '签发地点拼音' + '</td>';
		tableStr += '<th width="15%">' + '签发日期' + '</td>';
		tableStr += '<th width="15%">' + '有效期至' + '</td>';
		tableStr += '</tr>';
		var len = data.data.length;
		var data = data.data;
		for (var i = 0; i < len; i++) {
			tableStr += '<tr>';
			if (data[i].travel == null) {
				data[i].travel = '';
			}
			if (data[i].englishName == null) {
				data[i].englishName = '';
			}
			if (data[i].visaType == null) {
				data[i].visaType = '';
			}
			tableStr += '<td>' + data[i].travel + '</td>';
			tableStr += '<td>' + data[i].name + '</td>';
			tableStr += '<td>' + data[i].englishName + '</td>';
			tableStr += '<td>' + data[i].sex + '</td>';
			tableStr += '<td>' + data[i].passportEncoding + '</td>';
			tableStr += '<td>' + toDate(data[i].birthDay) + '</td>';
			tableStr += '<td>' + data[i].visaType + '</td>';
			if (data[i].remarks == null) {
				data[i].remarks = '';
			}
			if (data[i].signAddress == null) {
				data[i].signAddress = '';
			}
			tableStr += '<td>' + data[i].remarks + data[i].orderId + '</td>';
			tableStr += '<td>' + data[i].birthPlace + '</td>';
			tableStr += '<td>' + data[i].signAddress + '</td>';
			if (data[i].signDate == 0) {
				tableStr += '<td></td>';
			} else {
				tableStr += '<td>' + toDate(data[i].signDate) + '</td>';
			}
			tableStr += '<td>' + toDate(data[i].expiryDate) + '</td>';
			tableStr += '</tr>';
		}
		if (len == 0) {
			tableStr += '<tr style="text-align: center">';
			tableStr += '<td colspan="11">' + '暂无记录' + '</font></td>';
			tableStr += '</tr>'
		}
		tableStr += '</table>';
		//添加到div中
		layer.confirm('已经选择' + len + '本护照是否打印', function(index) {
			exporExcel("护照信息", tableStr);
			layer.close(index);
		})


	}

	function exporExcel(FileName, excel) {
		var excelFile =
			"<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
		excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
		excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel';
		excelFile += '; charset=UTF-8">';
		excelFile += "<head>";
		excelFile += "<!--[if gte mso 9]>";
		excelFile += "<xml>";
		excelFile += "<x:ExcelWorkbook>";
		excelFile += "<x:ExcelWorksheets>";
		excelFile += "<x:ExcelWorksheet>";
		excelFile += "<x:Name>";
		excelFile += "{worksheet}";
		excelFile += "</x:Name>";
		excelFile += "<x:WorksheetOptions>";
		excelFile += "<x:DisplayGridlines/>";
		excelFile += "</x:WorksheetOptions>";
		excelFile += "</x:ExcelWorksheet>";
		excelFile += "</x:ExcelWorksheets>";
		excelFile += "</x:ExcelWorkbook>";
		excelFile += "</xml>";
		excelFile += "<![endif]-->";
		excelFile += "</head>";
		excelFile += "<body>";
		excelFile += excel;
		excelFile += "</body>";
		excelFile += "</html>";

		var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);

		var link = document.createElement("a");
		link.href = uri;

		link.style = "visibility:hidden";
		link.download = FileName; //格式默认为.xls

		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	}

	function toDate(data) {
		var date = new Date(data); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		var D = date.getDate() + ' ';
		return Y + M + D
	}

})
