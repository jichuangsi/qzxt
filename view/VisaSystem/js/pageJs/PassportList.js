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

	var id = UrlSearch();
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getPassportByExpressReceiptId',
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
					field: 'txm',
					align: 'center',
					width: 200,
					title: '条形码号'
				},
				{
					field: 'name',
					align: 'center',
					title: '姓名',
					templet:function(d){
						return d.information.name
					}
				},
				{
					field: 'passportEncoding',
					align: 'center',
					title: '护照编码	',
					templet:function(d){
						return d.information.passportEncoding
					}
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话',
					templet:function(d){
						return d.information.telephoneNumber
					}
				},
				// {
				// 	field: 'ReturnAddress',
				// 	align: 'center',
				// 	title: '寄回地址'
				// },
				{
					field: 'birthDay',
					align: 'center',
					title: '出生日期',
					templet: function(d) {
						if (d.information.birthDay != 0) {
							var date = new Date(d.information.birthDay); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return Y + M + D
						} else {
							return "-"
						}
					}
				}, {
					field: 'birthPlace',
					align: 'center',
					title: '居住地',
					templet:function(d){
						return d.information.birthPlace
					}
				},
				{
					field: 'expiryDate',
					align: 'center',
					title: '有效日期',
					templet: function(d) {
						if (d.information.expiryDate != 0) {
							var date = new Date(d.information.expiryDate); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return Y + M + D
						} else {
							return "-"
						}
					}
				}, {
					field: 'account',
					align: 'center',
					width: 300,
					title: '操作',
					toolbar: '#operation'
				}
			]
		],
		// toolbar: '#importImg',
		page: true,
		limit: 10,
		// loading: true,
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {
			"expressReceiptId":id
		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.data.list;
				total =res.data.total;
				code = 0;
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}

	})
	table.on('row(demo)', function(obj) {
		var param = obj.data;
		$("input[name='sex']").each(function() {
			if ($(this).val() == param.sex) {
				$(this).prop("checked", true);
			}
		});
		var status = ''
		if (param.information.status == "W") {
			status = '未匹配'
		}
		form.val('test', {
			"id": param.information.id,
			"orderNumber": param.expressReceipt.orderNumber,
			"courierNumber": param.expressReceipt.courierNumber,
			"status": status,
			"expressAddress": param.expressReceipt.address,
			"name": param.information.name,
			"birthDay": toDate(param.information.birthDay),
			"birthPlace": param.information.birthPlace,
			"habitation": param.information.habitation,
			"passportEncoding": param.information.passportEncoding,
			"expiryDate": toDate(param.information.expiryDate),
			"telephoneNumber": param.information.telephoneNumber,
			"returnAddress": param.information.returnAddress,
		});
	})

	function toDate(data) {
		var date = new Date(data); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		var D = date.getDate() + ' ';
		return Y + M + D
	}
	//修改护照信息
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
		var res = getAjaxPostData(url, list);
		if (res.code == "0010") {
			layer.msg('修改成功!');
		}
		table.reload('idTest');
		layer.close(index);
	});
	$(document).on('click', '.to', function() {
		layer.confirm('是否要打印条形码？', function(index) {})
	})
	$(document).on('click', '.list', function() {
		// list()
		index = layer.open({
			type: 1,
			area: ['70%', '100%'],
			anim: 2,
			title: '录入护照',
			maxmin: true,
			shadeClose: true,
			content: $('#addInfo')
		});
	});
})
