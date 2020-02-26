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
		elem: '#demo',
		method: "get",
		async: false,
		id: 'idTest',
		url: '../json/r.json',
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
					field: 'rearmark',
					align: 'center',
					width: 400,
					title: '备注信息'
				},
				{
					field: 'date',
					align: 'center',
					title: '备注时间'
				},
				{
					field: 'name',
					align: 'center',
					title: '操作员工'
				},
				{
					field: 'opt',
					align: 'center',
					title: '操作'
				}
			]
		],
		// toolbar: '#add',
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
