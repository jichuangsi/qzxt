layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;


	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id: 'idTest',
		url: '../json/p.json',
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
					field: 'txm',
					align: 'center',
					width:200,
					title: '条形码号'
				},
				{
					field: 'name',
					align: 'center',
					title: '姓名'
				},
				{
					field: 'encod',
					align: 'center',
					title: '护照编码	'
				},
				{
					field: 'phone',
					align: 'center',
					title: '联系电话'
				},
				// {
				// 	field: 'ReturnAddress',
				// 	align: 'center',
				// 	title: '寄回地址'
				// },
				{
					field: 'birth',
					align: 'center',
					title: '出生日期'
				}, {
					field: 'residence',
					align: 'center',
					title: '居住地'
				},
				{
					field: 'EffectiveDate',
					align: 'center',
					title: '有效日期'
				} ,{
					field: 'account',
					align: 'center',
					width: 300,
					title: '操作',
					toolbar: '#operation'
				}
			]
		],
		// toolbar: '#importImg',
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

	})
	$(document).on('click', '.to', function() {
		layer.confirm('是否要打印条形码？', function(index) {
		})
	})
})
