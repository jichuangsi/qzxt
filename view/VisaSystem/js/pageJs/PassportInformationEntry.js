layui.use(['form', 'table', 'laydate', 'upload','element'], function() {
	var form = layui.form,
		upload = layui.upload,
		laydate = layui.laydate,
		element =layui.element,
		table = layui.table;

	$('.time').each(function() {
		laydate.render({
			elem: this,
			type: 'date'
		});
	})
	table.render({
		elem: '#express',
		method: "post",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backExpressReceipt/getExpressReceiptByCondition',
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
					field: 'orderNumber',
					align: 'center',
					title: '订单号'
				},

				{
					field: 'TradeName',
					align: 'center',
					title: '商品名称'
				},
				{
					field: 'courierName',
					align: 'center',
					title: '快递公司'
				},
				{
					field: 'signatory',
					align: 'center',
					title: '签收人'
				},

				{
					field: 'courierNumber',
					align: 'center',
					title: '快递单号'
				},
				{
					field: 'count',
					align: 'center',
					title: '护照本数'
				},
				{
					field: 'telephoneNumber',
					align: 'center',
					title: '联系电话'
				},

				{
					field: 'orderPath',
					align: 'center',
					title: '平台'
				},
				{
					field: 'schedule',
					align: 'center',
					title: '工期(天)'
				},
				{
					field: 'createTime',
					align: 'center',
					title: '签收时间',
					templet: function(d) {
						if (d.createTime != 0) {
							var date = new Date(d.createTime); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
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
					field: 'account',
					align: 'center',
					title: '操作',
					width: 300,
					toolbar: '#operation'
				}
			]
		],
		// toolbar: '#addex',
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
				arr = res.data.content;
				total = res.data.totalElements;
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
	var id;
	table.on('row(express)', function(obj) {
		var param = obj.data;
		id = param.id;
		form.val('passport', {
			"orderNumber": param.orderNumber,
			"courierNumber": param.courierNumber,
			"name": param.name,
			"address": param.address,
			"status": param.status,
			"visaId": param.id
		});
		console.log(111)
		upload.render({ //允许上传的文件后缀
			elem: '#toUpload',
			url: httpUrl() + '/visaHandle/savePassPortByExcel/' + id,
			accept: 'file',
			exts: 'xls',
			headers: {
				'accessToken': getToken()
			},
			done: function(res) {
				if (res.code == "0010") {
					var arr = res.data.informationList;
					getEntry(arr);
					layer.msg('上传成功');
					console.log(res)
				} else {
					layer.msg('上传失败');
				}

			}
		});
	})
	$(document).on('click', '.reamark', function() {
		reamark(id)
	});
	$(document).on('click', '.passprtId', function() {
		look(id)
	});
	var informationList;
	$(document).on('click', '.toAdd', function() {
		var url = "/visaHandle/addPassPortByBatch";
		param = {
			"informations": informationList,
			"orderId": id
		}
		var arr= getAjaxPostData(url, param);
		if(arr.code=="0010"){
			layer.msg('提交成功!');
		}else{
			layer.msg(arr.msg);
		}
		table.reload('idTest');
		layer.close(index);
		$("#info").empty();
		$("#VisaInfo").hide()
	});
	//添加护照
	form.on('submit(passport_entry)', function(data) {
		var param = data.field;
		var essential = {
			"courierNumber": param.courierNumber,
			"expressAddress": param.expressAddress,
			"name": param.name,
			"address": param.address,
			"orderNumber": param.orderNumber,
			"status": param.status
		};
		var time = new Date(param.birthDay);
		var time2 = new Date(param.expiryDate);
		var information = {
			"birthDay": time.getTime(),
			"birthPlace": param.birthPlace,
			"expiryDate": time2.getTime(),
			"habitation": param.habitation,
			"id": "string",
			"luggage": param.luggage,
			"name": param.name,
			"passportEncoding": param.passportEncoding,
			"passprot": param.passprot,
			"returnAddress": param.returnAddress,
			"sex": param.sex,
			"status": param.status,
			"telephoneNumber": param.telephoneNumber
		};
		var url = "/visaHandle/passportEntry";
		var list = {
			"essential": essential,
			"information": information,
			"visaId": param.visaId
		}
		ajaxPOST(url, list);
		table.reload('idTest');
		layer.close(index);
		
	});


	//根据条件查询訂單
	form.on('submit(search)', function(data) {
		var param = data.field;
		if (param.status == -1) {
			param.status = "";
		}
		// var date = new Date(param.signingTime);
		// param.signingTime = date.getTime();

		table.reload('idTest', {
			where: {
				"orderNumber": param.orderNumber,
				"courierName": param.courierName,
				"courierNumber": param.courierNumber,
				"signatory": param.signatory,
				"telephoneNumber": param.telephoneNumber,
				"signingTime": param.signingTime
			}
		})
	})
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

	function look(id) {
		index = layer.open({
			type: 2,
			area: ['80%', '100%'],
			anim: 2,
			title: '查看护照',
			maxmin: true,
			shadeClose: true,
			content: "PassportList.html?passprtId=" + id
		});
	}
	// var uploadInst = upload.render({
	// 	elem: '#upload',
	// 	url: httpUrl() + '/visaHandle/passportEntryPic',
	// 	headers: {
	// 		'accessToken': getToken(),
	// 	},
	// 	acceptMime: 'image/*',
	// 	done: function(res) {
	// 		if (res.code == '0010') {
	// 			form.on("passport", {
	// 				'passportId': res.data
	// 			});
	// 			setMsg('上传成功！', 1)
	// 		} else {
	// 			setMsg('上传失败！', 2)
	// 		}
	// 	},
	// 	error: function() {}
	// });

	/* 护照信息的列表 */
	function getEntry(data) {
		var str = '';
		informationList = data;
		// $('#VisaInfo').empty();
		$("#info").empty();
		$.each(data, function(index, item) {
			str += '<tr>';
			str += '<td>' + item.travel + '</td>';
			str += '<td>' + item.name + '</td>';
			str += '<td>' + item.sex + '</td>';
			str += '<td>' + item.passportEncoding + '</td>';
			str += '<td>' + toDate(item.birthDay) + '</td>';
			// str+='<td>'+item.name+'</td>';
			// str+='<td>'+item.name+'</td>';
			str += '<td>' + item.signAddress + '</td>';
			str += '<td>' + toDate(item.signDate) + '</td>';
			str += '<td>' + toDate(item.expiryDate) + '</td>';
			str += '<td>' + item.remarks + '</td>';
			str += '</tr>'
		});
		$("#info").append(str);
		$("#VisaInfo").show();
	}

	function toDate(data) {
		var date = new Date(data); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
		var D = date.getDate() + ' ';
		return Y + M + D
	}
	//多图片上传
	var files;
	var uploadInst = layui.upload.render({
		elem: "#upload",
		auto: false,
		multiple: true,
		choose: function(obj) {
			files = obj.pushFile();
			var count=0;
			for (let i in files) {
				count++
			}
			layer.msg('一共选择'+count+'张图片!');
			console.log(count);
			
		}
	});
	$("#btn").click(function() {
		var form = new FormData();
		for (let i in files) {
			form.append("file", files[i]);
		}
		console.log(form);
		// form.append("test", $("#test").val())
		$.ajax({
			type:"post",
			url: httpUrl() + "/visaHandle/localUploadPics/"+id,
			contentType: false,
			processData: false,
			async: true,
			data: form,
			headers: {
				'accessToken': getToken()
			},
			success: function(data) {
				if(data.code=='0010'){
					layer.msg('上传成功!');
				}
			},
			error: function() {

			}
		});
	});
	//https://httpbin.org/post 上传测试地址

})
