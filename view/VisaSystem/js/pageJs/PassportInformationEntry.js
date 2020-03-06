layui.use(['form', 'table', 'laydate', 'upload'], function() {
	var form = layui.form,
		upload = layui.upload,
		laydate = layui.laydate,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	table.render({
		elem: '#express',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backExpressReceipt/getExpressReceiptByCondition',
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
					field: 'TradeName',
					align: 'center',
					title: '商品名称'
				},
				{
					field: 'courierName',
					align: 'center',
					title: '快递公司'
				},
				{
					field: 'courierNumber',
					align: 'center',
					title: '快递单号'
				},
				{
					field: 'count',
					align: 'center',
					title: '护照本数'
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},
				{
					field: 'urgent',
					align: 'center',
					title: '工期'
				},
				{
					field: 'createTime',
					align: 'center',
					title: '签收时间',
					templet: function(d) {
						if (d.createTime != 0) {
							var date = new Date(d.createTime); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return  Y+M+D
						} else {
							return "-"
						}
					}
				},
				{
					field: 'account',
					align: 'center',
					title: '操作',
					width: 300,
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#addex',
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
				arr = res.data.content;
				total = res.total;
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
	var id;
	table.on('row(express)', function(obj) {
		var param = obj.data;
		id = param.id;
		form.val('passport', {
			"orderNumber": param.orderNumber,
			"courierNumber": param.courierNumber,
			"name": param.name,
			"address": param.address,
			"status": param.status,
			"visaId": param.id
		});
	})
	$(document).on('click', '.reamark', function() {
		reamark(id)
	});
	$(document).on('click', '.passprtId', function() {
		look(id)
	});
	//添加护照
	form.on('submit(passport_entry)', function(data) {
		var param = data.field;
		var essential = {
			"courierNumber": param.courierNumber,
			"expressAddress": param.expressAddress,
			"name": param.name,
			"address": param.address,
			"orderNumber": param.orderNumber,
			"status": param.status
		};
		var time = new Date(param.birthDay);
		var time2 = new Date(param.expiryDate);
		var information = {
			"birthDay": time.getTime(),
			"birthPlace": param.birthPlace,
			"expiryDate": time2.getTime(),
			"habitation": param.habitation,
			"id": "string",
			"luggage": param.luggage,
			"name": param.name,
			"passportEncoding": param.passportEncoding,
			"passprot": param.passprot,
			"returnAddress": param.returnAddress,
			"sex": param.sex,
			"status": param.status,
			"telephoneNumber": param.telephoneNumber
		};
		var url = "/visaHandle/passportEntry";
		var list = {
			"essential": essential,
			"information": information,
			"visaId": param.visaId
		}
		ajaxPOST(url, list);
		table.reload('idTest');
		layer.close(index);
	});


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		if (param.status == -1) {
			param.status = "";
		}
		// var date = new Date(param.signingTime);
		// param.signingTime = date.getTime();

		table.reload('idTest', {
			where: {
				"orderNumber": param.orderNumber,
				"courierName": param.courierName,
				"courierNumber": param.courierNumber,
				"signatory": param.signatory,
				"telephoneNumber": param.telephoneNumber,
				"signingTime": param.signingTime
			}
		})
	})
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

	function look(id) {
		index = layer.open({
			type: 2,
			area: ['80%', '100%'],
			anim: 2,
			title: '查看护照',
			maxmin: true,
			shadeClose: true,
			content: "PassportList.html?passprtId="+id
		});
	}
	var uploadInst = upload.render({
		elem: '#upload',
		url: httpUrl() + '/visaHandle/passportEntryPic',
		headers: {
			'accessToken': getToken(),
		},
		acceptMime: 'image/*',
		done: function(res) {
			if (res.code == '0010') {
				setMsg('上传成功！', 1)
			} else {
				setMsg('上传失败！', 2)
			}
		},
		error: function() {}
	});
})
