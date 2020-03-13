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
					templet:function(d){
						if(d.diff_DATE==99){
							return 0
						}else {
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
						} else if (d.sendStatus == "SB") {
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
			res.data.forEach(function(item, index) {
				if (item.diff_DATE < 3 && item.sendStatus!="SB") {
					var tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("background-color", "red");
					tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("color", "white");
				}
				if (item.ben == "3") {
					var tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("background-color",
						"rgba(60, 187, 255, 0.5)");
					tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("color", "white");
				}
			})
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
	//
	table.on('row(Visa)', function(data) {
		var param = data.data;

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
		form.val("test", {
			"id":param.id,
			"name": param.name,
			"birthDay": param.birthDay,
			"birthPlace": param.birthPlace,
			"habitation": param.habitation,
			"passportEncoding": param.passportEncoding,
			"expiryDate": param.expiryDate,
			"telephoneNumber": param.telephoneNumber,
			"returnAddress": param.returnAddress
		});

	});
	window.toinfo = function() {
		index = layer.open({
			type: 1,
			area: ['30%', '75%'],
			anim: 2,
			title: '寄回',
			maxmin: true,
			shadeClose: true,
			content: $('#toinfo')
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
	form.on('submit(sendBack)', function(data) {
		var param = data.field;
		var url="/visaHandle/sendBackVisa";
		var  time=new Date(param.birthDay);
		param.birthDay=time.getTime();
		var time2=new Date(param.expiryDate);
		param.expiryDate=time2.getTime();
		var res=getAjaxPostData(url,param)
		if(res.code=="0010"){
			layer.msg('寄回成功!');
		}else{
			layer.msg(res.msg);
		}
		table.reload('idTest');
		layer.close(index);
	});

})
