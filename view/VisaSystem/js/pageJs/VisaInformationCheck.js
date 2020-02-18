layui.use(['form', 'table', 'laydate', 'xmSelect'], function() {
	var form = layui.form,
		xmSelect = layui.xmSelect,
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
				}, {
					field: 'Operator',
					align: 'center',
					title: '最近操作人'
				}, {
					field: 'optionTime',
					align: 'center',
					title: '最近操作时间'
				},
				{
					field: 'account',
					align: 'center',
					width: 250,
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
	});


	//根据条件查询
	form.on('submit(search)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			where: {}
		})
	})
	
	var demo = xmSelect.render({
		el: '#demo', 
		on: function(data){
				//arr:  当前多选已选中的数据
				var arr = data.arr;
				//change, 此次选择变化的数据,数组
				var change = data.change;
				//isAdd, 此次操作是新增还是删除
				var isAdd = data.isAdd;
				console.log(arr);
				$.each(arr,function(index,item){
					if(item.value==-1){
						$('#bc').show()
					}
				})
				
			},
		data: [
			{name: '照片尺寸不合格', value: 1},
			{name: '照片超过半年', value: 2},
			{name: '护照有效期不够', value: 3},
			{name: '离上次回国时间太短', value: 4},
			{name: '缺少上一次的签证纸', value: 5},
			{name: '逾期逗留', value: 6},
			{name: '曾经逾期', value: 7},
			{name: '缺少身份证', value: 8},
			{name: '缺少户口本', value: 9},
			{name: '缺少公证文件', value: 10},
			{name: '缺少出生证', value: 11},
			{name: '缺少机票', value: 12},
			{name: '其他', value: -1},
		]
	})
})
