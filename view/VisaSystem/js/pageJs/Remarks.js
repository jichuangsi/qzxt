layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;
	var id = UrlSearch();
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backExpressReceipt/getExpressReceiptRemark?expressId='+id,
		// contentType: 'application/json',
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
					field: 'remarks',
					align: 'center',
					width: 400,
					title: '备注信息'
				},
				{
					field: 'operationTime',
					align: 'center',
					title: '备注时间',
					templet: function(d) {
						if (d.operationTime != 0) {
							return new Date(+new Date(d.operationTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
								/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'operator',
					align: 'center',
					title: '操作员工'
				}
				// ,
				// {
				// 	field: 'operation',
				// 	align: 'center',
				// 	title: '操作'
				// }
			]
		],
		// toolbar: '#add',
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
				arr = res.data;
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
	table.on('row(demo)', function(obj) {
		var param=obj.data;
		form.val('test', {
			"id": param.id
		});
		
	})
	form.val('test',{
		"id":id
	});
	//添加备注
	form.on('submit(addRemarks)', function(data) {
		var param = data.field;
		var url = "/backExpressReceipt/expressReceiptRemark?expressId="+id;
		ajaxPOST(url, param);
		table.reload('idTest');
	});

})
