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
					field: 'pName',
					align: 'center',
					title: '姓名'
				},
				{
					field: 'number',
					align: 'center',
					title: '订单号'
				},
				// {
				// 	field: 'TradeName',
				// 	align: 'center',
				// 	title: '商品名称	'
				// },
				{
					field: 'SerialNumber',
					align: 'center',
					title: '护照编码	'
				},
				{
					field: 'phone2',
					align: 'center',
					title: '联系电话'
				},
				
				{
					field: 'bDate',
					align: 'center',
					title: '出生日期'
				},
				// 	{
				// 	field: 'Operator',
				// 	align: 'center',
				// 	title: '最近操作人'
				// },	{
				// 	field: 'optionTime',
				// 	align: 'center',
				// 	title: '最近操作时间'
				// },
				{
					field: 'addres',
					align: 'center',
					title: '护照签发地'
				},
				{
					field: 'time',
					align: 'center',
					title: '有效日期'
				},
				{
					field: 'urgent',
					align: 'center',
					title: '工期'
				},
				{
					field: 'wt',
					align: 'center',
					title: '问题'
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
