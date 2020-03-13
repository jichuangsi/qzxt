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
		elem: '#abnormal',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backExpressReceipt/getUnusualExpressReceiptByCondition',
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
					field: 'signatory',
					align: 'center',
					title: '签收人'
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},
				{
					field: 'createTime',
					align: 'center',
					title: '签收时间',
					templet: function(d) {
						if (d.createTime != 0) {
							return new Date(+new Date(d.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
								/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'account',
					align: 'center',
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
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.data.content;
				total = res.data.totalElements;
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
				"courierName": param.courierName,
				"courierNumber": param.courierNumber,
				"signatory": param.signatory,
				"telephoneNumber": param.telephoneNumber,
				"signingTime": param.signingTime
			}
		})
	})
	//跳转备注页面
	function reamark(id) {
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
	var id;
	table.on('row(abnormal)', function(obj) {
		var param = obj.data;
		id = param.id;
		form.val('express', {
			"id":param.id,
			"courierNumber": param.courierNumber,
			"telephoneNumber": param.telephoneNumber,
			"signatory": param.signatory,
			"orderNumber": param.orderNumber,
			"count": param.count,
			"orderPathIn": param.orderPath,
			"problemIn": param.problem,
			"returnAddress": param.returnAddress,
			"address": param.address
		});
		if (param.orderPath != "飞猪" && param.orderPath != "公众号") {
			$("input[name='orderPath']").each(function() {
				if ($(this).val() == "其他") {
					$(this).prop("checked", true);
				}
			});
		} else if (param.orderPath == "公众号") {
			console.log($("input[name='orderPath']"))
			$("input[name='orderPath']").each(function() {
				if ($(this).val() == "公众号") {
					$(this).prop("checked", true);
				}
			});
		}
		if (param.problem != "否") {
			$("input[name='problem']").each(function() {
				if ($(this).val() == "是") {
					$(this).prop("checked", true);
				}
			});
		}
		form.render();
	})
	$(document).on('click', '.reamark', function() {
		reamark(id)
	});
	form.on('submit(modify)', function(data) {
		var param = data.field;
		if(param.courierName==-1){
			setMsg('请选择快递公司！',7);
			return 
		}
		if(param.orderPath=="其他"){
			if(param.orderPathIn==""){
				setMsg('请输入订单来源！',7);
				return 
			}else{
				param.orderPath=param.orderPathIn;
			}
		}
		if(param.problem=="是"){
			if(param.problemIn==""){
				setMsg('请输问题原因！',7);
				return 
			}else{
				param.problem=param.problemIn;
			}
		}
		var url = "/backExpressReceipt/updateExpressReceipt";
		ajaxPOST(url, param);
		table.reload('idTest');
		layer.close(index);
	});
})
