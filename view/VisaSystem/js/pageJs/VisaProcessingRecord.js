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
		elem: '#Record',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backRoleConsole/getVisaOperationRecordByCondition',
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
					field: 'goodsName',
					align: 'center',
					title: '商品名称	'
				},
				// {
				// 	field: 'OrderType',
				// 	align: 'center',
				// 	title: '订单类型'
				// },
				{
					field: 'applicantName',
					align: 'center',
					title: '订单申请人姓名'
				},
				{
					field: 'operaterName',
					align: 'center',
					title: '操作人姓名'
				},
				// {
				// 	field: 'phone',
				// 	align: 'center',
				// 	title: '联系电话'
				// },
				{
					field: 'operationName',
					align: 'center',
					title: '操作名称'
				}, {
					field: 'createTime',
					align: 'center',
					title: '操作时间',
					templet: function(d) {
						if (d.createTime != 0) {
							return new Date(+new Date(d.createTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
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


	//根据条件查询
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {
				"orderNumber": param.orderNumber,
				"applicantName": param.applicantName,
				"telephoneNumber": param.telephoneNumber,
				"operaterName": param.operaterName,
				"operationTime": param.operationTime,
			}
		})
	})

	function mao() {
		var arr = [];
		for (var i = 0; i < arr.length; i++) {
			for (var j = i; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					var tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}
})
