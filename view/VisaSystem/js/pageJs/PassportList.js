layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate,
		table = layui.table;

	var id = UrlSearch();
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getPassportById?passprtId=' + id,
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
					field: 'txm',
					align: 'center',
					width: 200,
					title: '条形码号'
				},
				{
					field: 'name',
					align: 'center',
					title: '姓名'
				},
				{
					field: 'passportEncoding',
					align: 'center',
					title: '护照编码	'
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},
				// {
				// 	field: 'ReturnAddress',
				// 	align: 'center',
				// 	title: '寄回地址'
				// },
				{
					field: 'birthDay',
					align: 'center',
					title: '出生日期',
					templet: function(d) {
						if (d.birthDay != 0) {
							var date = new Date(d.birthDay); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return  Y+M+D
						} else {
							return "-"
						}
					}
				}, {
					field: 'birthPlace',
					align: 'center',
					title: '居住地'
				},
				{
					field: 'expiryDate',
					align: 'center',
					title: '有效日期',
					templet: function(d) {
						if (d.expiryDate != 0) {
							var date = new Date(d.expiryDate); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
							var Y = date.getFullYear() + '-';
							var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
							var D = date.getDate() + ' ';
							return  Y+M+D
						} else {
							return "-"
						}
					}
				}, {
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

	})
	$(document).on('click', '.to', function() {
		layer.confirm('是否要打印条形码？', function(index) {})
	})
})
