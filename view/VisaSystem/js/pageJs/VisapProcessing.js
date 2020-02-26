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
		method: "get",
		async: false,
		id: 'idTest',
		url: '../json/data.json',
		contentType: 'application/json',
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
					// 同一列两行
					field: 'urgent',
					align: 'center',
					title: '工期/订单状态'
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
				},
				{
					field: 'vStatus',
					align: 'center',
					title: '签证状态'
				},
				// {
				// 	field: 'result',
				// 	align: 'center',
				// 	title: '出签结果'
				// }, {
				// 	field: 'LogisticsStatus',
				// 	align: 'center',
				// 	title: '物流状态'
				// },
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


	//根据条件查询
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {}
		})
	})

	function info() {
		index = layer.open({
			type: 1,
			area: ['500px', '500px'],
			anim: 2,
			title: '信息',
			maxmin: true,
			shadeClose: true,
			content: $('#info')
		});
	}
	//
	table.on('row(Visa)', function(data) {
		info()
	});
})
