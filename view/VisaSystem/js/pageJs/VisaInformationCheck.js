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
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/visaHandle/getPassPortByStatus',
		contentType: 'application/json',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{

					field: 'id',
					title: '序号',
					align: 'center',
					// width:50
					type: 'numbers'
				},
				{
					field: 'orderNumber',
					align: 'center',
					title: '订单号'
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
					field: 'tradeName',
					align: 'center',
					title: '商品名称	'
				},
				// {
				// 	field: 'bDate',
				// 	align: 'center',
				// 	title: '出生日期'
				// },
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},

				// {
				// 	field: 'OrderType',
				// 	align: 'center',
				// 	title: '订单类型'
				// },
				{
					field: 'addres',
					align: 'center',
					title: '护照签发地'
				},
				{
					field: 'schedule',
					align: 'center',
					title: '工期（天）'
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
							return Y + M + D
						} else {
							return "-"
						}
					}
				},
				{
					field: 'txm',
					align: 'center',
					title: '条形码号'
				},
				// {
				// 	field: 'Operator',
				// 	align: 'center',
				// 	title: '最近操作人'
				// }, {
				// 	field: 'optionTime',
				// 	align: 'center',
				// 	title: '最近操作时间'
				// },

				// {
				// 	field: 'Ostatus',
				// 	align: 'center',
				// 	title: '订单状态'
				// },
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
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {},
		done: function(res, curr, count) {
			var that = this.elem.next();
			res.data.forEach(function(item, index) {
				if (item.ben == "3") {
					var tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("background-color",
						"rgba(60, 187, 255, 0.5)");
					tr = that.find(".layui-table-box tbody tr[data-index='" + index + "']").css("color", "white");
				}
			})
		},
		parseData: function(res) {
			var arr;
			var code;
			var total = 0;
			if (res.code == "0010") {
				arr = res.data.list;
				total = res.data.total;
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
			where: {
				"orderNumber": param.orderNumber,
				"name": param.name,
				"passportEncoding": param.passportEncoding,
				"telephoneNumber": param.telephoneNumber
			}
		})
	})
	var id;
	var eid;
	table.on('row(Visa)', function(obj) {
		var param = obj.data;
		id = param.id;
		eid = param.expressReceiptId;
		// form.val('passport', {
		// 	"orderNumber": param.orderNumber,
		// 	"courierNumber": param.courierNumber,
		// 	"name": param.name,
		// 	"address": param.address,
		// 	"status": param.status,
		// 	"visaId": param.id
		// });

	})
	$(document).on('click', '.reamark', function() {
		reamark(eid)
	});

	$(document).on('click', '.list', function() {
		// getVisa(pid)
		getToExamine(id)
	});

	function getToExamine(id) {
		var url = "/visaHandle/getPassportByPassId?id=" + id;
		var data = getAjaxPostData(url);
		var essential = data.data.essential;
		var information = data.data.information;
		$("input[name='sex']").each(function() {
			if ($(this).val() == information.sex) {
				$(this).prop("checked", true);
			}
		});
		form.val('testTeacher', {
			"orderNumber": essential.orderNumber,
			"courierNumber": essential.courierNumber,
			"name": essential.name,
			"status": essential.status,

			"birthDay": toDate(information.birthDay),
			"birthPlace": information.birthPlace,
			"habitation": information.habitation,
			"expiryDate": toDate(information.expiryDate),
			"telephoneNumber": information.telephoneNumber,
			"problem": information.problem,
			"passportEncoding": information.passportEncoding,
			"returnAddress":information.returnAddress,

			"id": information.id
		});
		form.render();
	}

	function toDate(data) {
		var date = new Date(data); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		var D = date.getDate() + ' ';
		return Y + M + D
	}
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
	//签证审核（同意审核）
	form.on('submit(adopt)', function(data) {
		var param=data.field;
		var url = "/visaHandle/checkPassportById";
		var list = {
			"pid": param.id,
			"status":'P'
		}
		ajaxPOST(url,list);
		table.reload('idTest');
		layer.close(index);
	});
	form.on('submit(noAdopt)', function(data) {
		var param = data.field;
		var url = "/visaHandle/checkPassportById";
		var str = ''
		$('input[name=question]:checked').each(function() {
			str += $(this).val() + ','
		});
		if (param.problem != "") {
			str += param.problem;
		}
		if(str==''){
			setMsg("请选择不通过原因或补充说明！",7)
			return
		}
		var list = {
			"pid": param.id,
			"problem":str,
			"status":'F'
		}
		ajaxPOST(url,list);
		table.reload('idTest');
		layer.close(index);
	});
	/* var demo = xmSelect.render({
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
	}) */
})
