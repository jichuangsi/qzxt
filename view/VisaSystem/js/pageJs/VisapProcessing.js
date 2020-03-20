layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})

	table.render({
		elem: '#Visa',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getPassPortByPass',
		contentType: 'application/json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					align: 'center',
					// width:50
					type: 'numbers'
				},
				{
					field: 'orderNumber',
					align: 'center',
					title: '订单号'
				},
				{
					field: 'TradeName',
					align: 'center',
					title: '商品名称	'
				},
				// {
				// 	field: 'OrderType',
				// 	align: 'center',
				// 	title: '订单类型'
				// },
				{
					// 同一列两行
					field: 'schedule',
					align: 'center',
					title: '工期'
				},
				{
					// 同一列两行
					field: 'diff_DATE',
					align: 'center',
					title: '剩余天数(天)',
					templet: function(d) {
						if (d.diff_DATE == 99) {

							return '已寄回'
						} else if (d.isSendBack == 'N') {
							if (d.diff_DATE == 0) {
								return '已过期未处理'
							} else {
								return d.diff_DATE
							}
						} else {
							return d.diff_DATE
						}
					}
				},
				{
					field: 'name',
					align: 'center',
					title: '申请人姓名'
				},

				{
					// 同一列两行
					field: 'status',
					align: 'center',
					title: '状态',
					templet: function(d) {
						if (d.sendStatus == "N") {
							return '<div>待送签</div>'
						} else if (d.sendStatus == "S") {
							return '<div>已送签</div>'
						} else if (d.sendStatus == "O") {
							return '<div>已出签</div>'
						} else if (d.sendStatus == "R") {
							return '<div>已拒签</div>'
						} else if (d.isSendBack == 'SB') {
							return '<div>已寄回</div>'
						}
					}
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},
				// {
				// 	field: 'vStatus',
				// 	align: 'center',
				// 	title: '签证状态'
				// },
				// // {
				// 	field: 'result',
				// 	align: 'center',
				// 	title: '出签结果'
				// }, {
				// 	field: 'LogisticsStatus',
				// 	align: 'center',
				// 	title: '物流状态'
				// },
				{
					field: 'txm',
					align: 'center',
					title: '条形码号'
				},
				{
					field: 'account',
					// align: 'center',
					width: 250,
					title: '操作',
					toolbar: '#operation'
				}
			]
		],
		page: true,
		limit: 10,
		// loading: true,
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {},
		done: function(res, curr, count) {
			var that = this.elem.next();
			var orderId = [];
			res.data.forEach(function(item, index) {
				orderId.push(item.orderNumber)
			})
			//去掉重复的订单
			function unique(arr) {
				return arr.filter(function(item, index, arr) {
					return arr.indexOf(item, 0) === index;
				});
			}
			var count = 1;
			var arr = unique(orderId);
			var list = [];
			$.each(arr, function(index, item) {
				list.push({
					order: item,
					num: count
				})
				count++;
			});
			var modity = [];
			for (var i = 0; i < list.length; i++) {
				if (list[i].num % 2 == 0) {
					modity.push(list[i].order)
				}
			}
			$.each(modity, function(index, arr) {
				res.data.forEach(function(item, index) {
					if (item.diff_DATE < 3 && item.isSendBack != "SB") {
						var tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("background-color",
							"red");
						tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("color", "white");
					}
					if (arr == item.orderNumber) {
						var tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("background-color",
							"#eee");
						tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("color", "white");
					}
				})

			});

		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.data.list;
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


	//根据条件查询
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {
				"orderNumber": param.orderNumber,
				"name": param.name,
				"passportEncoding": param.passportEncoding,
				"telephoneNumber": param.telephoneNumber,
				"sendStatus": param.sendStatus
			}
		})
	})

	window.info = function() {
		index = layer.open({
			type: 1,
			area: ['500px', '550px'],
			anim: 2,
			title: '信息',
			maxmin: true,
			shadeClose: true,
			content: $('#info')
		});
	}
	var id;
	//
	table.on('row(Visa)', function(data) {
		var param = data.data;
		id = param.id;
		// info()
		$(document).on('click', '.visa', function() {
			Signature(param.id)
		});
		$(document).on('click', '.noVisa', function() {
			toVisa(param.id, 'R', '是否已拒签？');
		});
		$(document).on('click', '.toVisa', function() {
			toVisa(param.id, 'O', "是否已出签?");
		});
		// $(document).on('click', '.toVisa', function() {
		// 	location.href=""
		// });
		// $(document).on('click', '.visa', function() {
		// 	sendBack(id);
		// });
		$("input[name='sex']").each(function() {
			if ($(this).val() == param.sex) {
				$(this).prop("checked", true);
			}
		});
		var time = toDate(param.expiryDate);

		form.val("test", {
			"passportId": param.id,
			"get": param.name,
			"birthDay": param.birthDay,
			"birthPlace": param.birthPlace,
			"habitation": param.habitation,
			"passportEncoding": param.passportEncoding,
			"expiryDate": time,
			"getPhone": param.telephoneNumber,
			"getAddress": param.returnAddress
		});
		form.val("infoTo", {
			"passportId": param.id,
			"get": param.name,
			"birthDay": param.birthDay,
			"birthPlace": param.birthPlace,
			"habitation": param.habitation,
			"passportEncoding": param.passportEncoding,
			"expiryDate": time,
			"getPhone": param.telephoneNumber,
			"getAddress": param.returnAddress
		});

	});
	window.toinfo = function() {
		index = layer.open({
			type: 1,
			area: ['35%', '75%'],
			anim: 2,
			title: '寄回',
			maxmin: true,
			shadeClose: true,
			content: $('#toinfo')
		});
	}

	window.oneInfo = function() {
		index = layer.open({
			type: 1,
			area: ['550px', '75%'],
			anim: 2,
			title: '确认信息',
			maxmin: true,
			shadeClose: true,
			content: $('#toOneInfo')
		});
	}
	//送签
	function Signature(id) {
		var url = '/visaHandle/sendVisa/' + id;
		layer.confirm('是否要送签？', function(index) {
			ajaxPOST(url);
			table.reload('idTest');
			layer.close(index);
		})
	}
	//拒签 出签 
	function toVisa(id, status, msg) {
		var url = '/visaHandle/outAndRefuseVisa/' + id + '/' + status;
		layer.confirm(msg, function(index) {
			ajaxPOST(url);
			table.reload('idTest');
			layer.close(index);
		})
	}
	//寄回
	function sendBack(id) {
		var url = '/visaHandle/sendBackVisa/' + id;
		layer.confirm('是否要送签？', function(index) {
			ajaxPOST(url);
			table.reload('idTest');
			layer.close(index);
		})
	}
	form.on('submit(sendOneBack)', function(data) {
		var param = data.field;
		var url = "/visaHandle/sendBackVisaTogether";
		var time = new Date(param.birthDay);
		param.birthDay = time.getTime();
		var time2 = new Date(param.expiryDate);
		param.expiryDate = time2.getTime();
		var res = getAjaxPostData(url, param)
		if (res.code == "0010") {
			var data = res.data;
			layer.msg('寄回成功，一共寄回' + data.sendBackNum + '本');
			form.val('test', {
				"orderId": data.orderId,
				"sfExpress": data.sfExpress
			});
			code();
			$('#btn2').show();
			$('#sendBack2').addClass('yc');
			$('#barcode2').show();
			$('#orderId2').show();
		} else {
			layer.msg(res.msg);
		}
		table.reload('idTest');
		// layer.close(index);
	});
	form.on('submit(sendBack)', function(data) {
		var param = data.field;
		var url = "/visaHandle/sendBackVisa";
		var time = new Date(param.birthDay);
		param.birthDay = time.getTime();
		var time2 = new Date(param.expiryDate);
		param.expiryDate = time2.getTime();
		var res = getAjaxPostData(url, param)
		if (res.code == "0010") {
			var data = res.data;
			layer.msg('寄回成功，一共寄回' + data.sendBackNum + '本');
			form.val('test', {
				"orderId": data.orderId,
				"sfExpress": data.sfExpress
			});
			code128();
			$('#btn').show();
			$('#sendBack').addClass('yc');
			$('#barcode').show();
			$('#orderId').show();
		} else {
			layer.msg(res.msg);
		}
		table.reload('idTest');
		// layer.close(index);
	});
	$(document).on('click', '#btn', function() {
		Print('#wrap', {
			onStart: function() {
				console.log('onStart', new Date())
			},
			onEnd: function() {
				console.log('onEnd', new Date())
			}
		})
	});
	$(document).on('click', '#btn2', function() {
		Print('#wrap2', {
			onStart: function() {
				console.log('onStart', new Date())
			},
			onEnd: function() {
				console.log('onEnd', new Date())
			}
		})
	});
	$(document).on('click', '#pri', function() {
		Print('#toinfos', {
			onStart: function() {
				console.log('onStart', new Date())
			},
			onEnd: function() {
				console.log('onEnd', new Date())
			}
		})
	});
	$(document).on('click', '#priAll', function() {
		Print('#toinfosAll', {
			onStart: function() {
				console.log('onStart', new Date())
			},
			onEnd: function() {
				console.log('onEnd', new Date())
			}
		})
	});

	function code128() {
		$("#barcode").barcode($('#bjid').val(), "code128", {
			barWidth: 2,
			barHeight: 60,
			showHRI: true
		});
	}

	function code() {
		$("#barcode2").barcode($('#bjid').val(), "code128", {
			barWidth: 2,
			barHeight: 60,
			showHRI: true
		});
	}


	function codePrint() {
		$("#print").barcode($('#sf').val(), "code128", {
			barWidth: 2,
			barHeight: 60,
			showHRI: true
		});
	}
	$(document).on('click', '.printAll', function() {
		printAll()
		printInfo(id)
	});
	$(document).on('click', '.print', function() {
		print()
		printInfo(id)
	});
	//获取打印信息
	function printInfo(id) {
		var url = '/visaHandle/getsendBackMessageByPassPortId?passportId=' + id;
		var data = getAjaxPostData(url);
		var expressReceipt = data.data.expressReceipt;
		var logistics = data.data.logistics;
		var p = data.data.passportInformation;
		form.val('sendInfo', {
			"orderNumber": expressReceipt.orderNumber,
			"schedule": expressReceipt.schedule,
			"checkTime": p.checkTime,
			"createTime": expressReceipt.createTime
		});
		form.val('sendInfoAll', {
			"sfExpress": logistics.sfExpressId,
			"orderNumber": expressReceipt.orderNumber,
			"getP": logistics.getP,
			"getPhone": geTel(logistics.getPhone),
			"getAddress": logistics.getAddress,
			"sender": logistics.sender,
			"senderPhone":geTel(logistics.senderPhone),
			"sendAddress": logistics.sendAddress
		});
		codePrint();
	}
	function geTel(tel){
	    var reg = /^(\d{3})\d{4}(\d{4})$/;  
	    return tel.replace(reg, "$1****$2");
	}
})
