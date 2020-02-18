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
					field: 'number',
					align: 'center',
					title: '订单号'
				},
				{
					field: 'TradeName',
					align: 'center',
					title: '商品名称	'
				},
				{
					field: 'OrderType',
					align: 'center',
					title: '订单类型'
				},
				{
					field: 'urgent',
					align: 'center',
					title: '是否加急'
				},
				{
					field: 'pName',
					align: 'center',
					title: '申请人姓名'
				},
				{
					field: 'phone2',
					align: 'center',
					title: '联系电话'
				},	{
					field: 'Operator',
					align: 'center',
					title: '最近操作人'
				},	{
					field: 'optionTime',
					align: 'center',
					title: '最近操作时间'
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