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
		elem: '#express',
		method: "get",
		async: false,
		id: 'idTest',
		url: '../json/data.json',
		// contentType: 'application/json',
		// headers: {
		// 	'accessToken': getToken()
		// },
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'SerialNumber',
					align: 'center',
					title: '流水号'
				},
				{
					field: 'number',
					align: 'center',
					title: '订单号'
				},
				{
					field: 'Company',
					align: 'center',
					title: '快递公司'
				},
				{
					field: 'CourierNumber',
					align: 'center',
					title: '快递单号'
				},
				{
					field: 'Signatory',
					align: 'center',
					title: '签收人'
				},
				{
					field: 'phone',
					align: 'center',
					title: '联系电话'
				},
				{
					field: 'SigningTime',
					align: 'center',
					title: '签收时间'
				},
				{
					field: 'account',
					align: 'center',
					title: '操作',
					toolbar: '#operation'
				}
			]
		],
		toolbar: '#add',
		page: true,
		limit: 10,
		// loading: true,
		request: {
			pageName: 'pageIndex',
			limitName: "pageSize"
		},
		where: {},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.list;
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


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {}
		})
	})

})