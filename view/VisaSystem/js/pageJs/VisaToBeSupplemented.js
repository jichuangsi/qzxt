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
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getProblemPassport',
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
					field: 'name',
					align: 'center',
					title: '姓名'
				},
				{
					field: 'orderNumber',
					align: 'center',
					title: '订单号'
				},
				// {
				// 	field: 'TradeName',
				// 	align: 'center',
				// 	title: '商品名称	'
				// },
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
				// 	field: 'bDate',
				// 	align: 'center',
				// 	title: '出生日期'
				// },
				// 	{
				// 	field: 'Operator',
				// 	align: 'center',
				// 	title: '最近操作人'
				// },	{
				// 	field: 'optionTime',
				// 	align: 'center',
				// 	title: '最近操作时间'
				// },
				// {
				// 	field: 'addres',
				// 	align: 'center',
				// 	title: '护照签发地'
				// },
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
				},
				{
					field: 'urgent',
					align: 'center',
					title: '工期'
				},
				{
					field: 'txm',
					align: 'center',
					title: '条形码号'
				},
				{
					field: 'problem',
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


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {}
		})
	})
	var pid;
	var id;
	table.on('row(supplement)', function(obj) {
		var param = obj.data;
		id = param.expressReceiptId;
		pid=param.id;
		
	})
	$(document).on('click', '.list', function() {
		getVisa(pid)
	});
	$(document).on('click', '.reamark', function() {
		reamark(id)
	});
	//跳转备注页面
	window.reamark = function(id) {
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
	//获取护照信息
	function getVisa(id){
		var url='/visaHandle/getPassportByPassId?id='+id
		var data=getAjaxPostData(url);
		console.log(data)
		var param=data.data.information;
		var arr=data.data.essential;
		var str;
		if(param.status=='P'){
			str="审核未通过"
		}
		form.val('visa', {
			"orderNumber": arr.orderNumber,
			"courierNumber": arr.courierNumber,
			"name": param.name,
			"expressAddress": arr.expressAddress,
			"status": str,
			"visaId": param.id,
			"telephoneNumber":param.telephoneNumber,
			"birthPlace":param.birthPlace,
			"passportEncoding":param.passportEncoding,
			"problem":param.problem
		});
	}
})
