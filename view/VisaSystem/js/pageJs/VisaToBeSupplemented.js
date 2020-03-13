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
		elem: '#supplement',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getProblemPassport',
		contentType: 'application/json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
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
					title: '姓名'
				},

				// {
				// 	field: 'TradeName',
				// 	align: 'center',
				// 	title: '商品名称	'
				// },
				{
					field: 'passportEncoding',
					align: 'center',
					title: '护照编码	'
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},

				// {
				// 	field: 'bDate',
				// 	align: 'center',
				// 	title: '出生日期'
				// },
				// 	{
				// 	field: 'Operator',
				// 	align: 'center',
				// 	title: '最近操作人'
				// },	{
				// 	field: 'optionTime',
				// 	align: 'center',
				// 	title: '最近操作时间'
				// },
				// {
				// 	field: 'addres',
				// 	align: 'center',
				// 	title: '护照签发地'
				// },
				{
					field: 'expiryDate',
					align: 'center',
					title: '有效日期',
					templet: function(d) {
						if (d.expiryDate != 0) {
							var date = new Date(d.expiryDate); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
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
					title: '工期(天)'
				},
				{
					field: 'txm',
					align: 'center',
					title: '条形码号'
				},
				{
					field: 'problem',
					align: 'center',
					title: '问题'
				},
				{
					field: 'account',
					align: 'center',
					title: '操作',
					width: 250,
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


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {
				"orderNumber": param.orderNumber,
				"name": param.name,
				"passportEncoding": param.passportEncoding,
				"telephoneNumber": param.telephoneNumber
			}
		})
	})
	var pid;
	var id;
	table.on('row(supplement)', function(obj) {
		var param = obj.data;
		id = param.expressReceiptId;
		pid = param.id;
	})
	$(document).on('click', '.list', function() {
		// getVisa(pid)
		getToExamine(pid);
	});
	$(document).on('click', '.reamark', function() {
		reamark(id)
	});
	$(document).on('click', '.tocheck', function() {
		toCheck(pid)
	});
	//跳转备注页面
	window.reamark = function(id) {
		index = layer.open({
			type: 2,
			area: ['70%', '70%'],
			anim: 2,
			title: '备注信息',
			maxmin: true,
			shadeClose: true,
			content: "Remarks.html?id=" + id
		});
	}
	//获取护照信息
	function getVisa(id) {
		var url = '/visaHandle/getPassportByPassId?id=' + id
		var data = getAjaxPostData(url);
		var param = data.data.information;
		var arr = data.data.essential;
		var str;
		if (param.status == 'P') {
			str = "审核未通过"
		}
		form.val('visa', {
			"orderNumber": arr.orderNumber,
			"courierNumber": arr.courierNumber,
			"name": param.name,
			"expressAddress": arr.expressAddress,
			"status": str,
			"visaId": param.id,
			"telephoneNumber": param.telephoneNumber,
			"birthPlace": param.birthPlace,
			"passportEncoding": param.passportEncoding,
			"problem": param.problem
		});
	}

	function getToExamine(id) {
		var url = "/visaHandle/getPassportByPassId?id=" + id;
		var data = getAjaxPostData(url);
		var essential = data.data.essential;
		var information = data.data.information;
		// if(essential.status=='P'){
		// 	str="审核未通过"
		// }
		$("input[name='sex']").each(function() {
			if ($(this).val() == information.sex) {
				$(this).prop("checked", true);
			}
		});
		form.val('visa', {
			"orderNumber": essential.orderNumber,
			"courierNumber": essential.courierNumber,
			"name": essential.name,
			"status": essential.status,

			"birthDay": toDate(information.birthDay),
			"birthPlace": information.birthPlace,
			"habitation": information.habitation,
			"expiryDate": toDate(information.expiryDate),
			"telephoneNumber": information.telephoneNumber,
			"problem": information.problem,
			"passportEncoding": information.passportEncoding,
			"returnAddress": information.returnAddress,

			"id": information.id
		});
		form.render();
	}

	function toDate(data) {
		var date = new Date(data); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		var D = date.getDate() + ' ';
		return Y + M + D
	}
	form.on('submit(modify_info)', function(obj) {
		var param = obj.field;
		var url = "/visaHandle/updatePassport";
		var essential = {
			"orderNumber": param.orderNumber,
			"courierNumber": param.courierNumber,
			"expressAddress": param.expressAddress
		};
		var time1 = new Date(param.birthDay);
		var time2 = new Date(param.expiryDate);
		var information = {
			"id": param.id,
			"name": param.name,
			"sex": param.sex,
			"birthDay": time1.getTime(),
			"birthPlace": param.birthPlace,
			"habitation": param.habitation,
			"expiryDate": time2.getTime(),
			"passportEncoding": param.passportEncoding,
			"telephoneNumber": param.telephoneNumber,
			"returnAddress": param.returnAddress,
			"luggage": param.luggage,
		};
		list = {
			"essential": essential,
			"information": information
		}
		var res = ajaxPOST(url, list);
		if (res.code == "0010") {
			layer.msg('修改成功!');
		}
		table.reload('idTest');
		layer.close(index);
	});
	//发回重审
	function list() {
		index = layer.open({
			type: 1,
			area: ['70%', '100%'],
			anim: 2,
			title: '录入护照',
			maxmin: true,
			shadeClose: true,
			content: $('#addInfo')
		});
	}

	function toCheck(id) {
		var url = '/visaHandle/aRetrialPassPort';
		var param = {
			"pid": id
		}
		ajaxPOST(url, param);
		table.reload('idTest');
	}
})
