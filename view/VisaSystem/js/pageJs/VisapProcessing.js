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
					align: 'center',
					// width:50
					type: 'numbers'
				},
				{
					field: 'number2',
					align: 'center',
					title: '订单号'
				},
				{
					field: 'TradeName',
					align: 'center',
					title: '商品名称	'
				},
				// {
				// 	field: 'OrderType',
				// 	align: 'center',
				// 	title: '订单类型'
				// },
				{
					// 同一列两行
					field: 'urgent',
					align: 'center',
					title: '工期/状态'
				},
				{
					field: 'pName',
					align: 'center',
					title: '申请人姓名'
				},
				{
					// 同一列两行
					field: 'over',
					align: 'center',
					title: '工期(天)'
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
					field: 'txm',
					align: 'center',
					title: '条形码号'
				},
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
		done:function(res,curr,count){
			var that=this.elem.next();
			res.data.forEach(function(item,index){
				if(item.over<3){
					var tr=that.find(".layui-table-box tbody tr[data-index='"+index+"']").css("background-color","red");
					 tr=that.find(".layui-table-box tbody tr[data-index='"+index+"']").css("color","white");
				}
				if(item.ben=="3"){
					var tr=that.find(".layui-table-box tbody tr[data-index='"+index+"']").css("background-color","rgba(60, 187, 255, 0.5)");
					 tr=that.find(".layui-table-box tbody tr[data-index='"+index+"']").css("color","white");
				}
			})
		},
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

	window.info= function() {
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
		// info()
	});
	window.toinfo= function() {
		index = layer.open({
			type: 1,
			area: ['30%', '70%'],
			anim: 2,
			title: '寄回',
			maxmin: true,
			shadeClose: true,
			content:$('#toinfo')
		});
	}
})
