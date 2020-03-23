layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		laydate = layui.laydate;

	form.on('submit(add_order)', function(data) {
		var param = data.field;
		if(param.courierName==-1){
			setMsg('请选择快递公司！',7);
			return 
		}
		if(param.orderPath=="其他"){
			if(param.orderPathIn==""){
				setMsg('请输入订单来源！',7);
				return 
			}else{
				param.orderPath=param.orderPathIn;
			}
		}
		if(param.problem=="是"){
			if(param.problemIn==""){
				setMsg('请输问题原因！',7);
				return 
			}else{
				param.problem=param.problemIn;
			}
		}
		console.log(param)
		var url = "/backExpressReceipt/expressReceipt";
		ajaxPOST(url, param);
	});

	//自动匹配订单
	$(document).on('click', '#matching', function() {
		var data = form.val("order"); //取出电话号码还有姓名
		var phone = data.telephoneNumber;
		//然后根据这两个去查询其订单
		var url = "/backExpressReceipt/getOrderByPhone?phone="+phone;
		var data = getAjaxData(url);
		var arr=data.data;
		if(data.code!='0010'||arr.orderId==null){
			layer.msg('暂无相关订单！');
			return 
		}
		//查询完后把查询到的数据返回，然后赋值上去
		form.val("order", {
			"courierNumber": arr.courierNumber,
			"telephoneNumber": arr.phone,
			"signatory": arr.signatory,
			"orderNumber": arr.orderId,
			"count": arr.count,
			"schedule":arr.schedule,
			"returnAddress":arr.returnAddress,
			"address":arr.address
		})
	})
	//没有问题就隐藏输入框
	form.on('radio(problem)', function(data) {
		if (data.value == "是") {
			$("#pro").removeClass('yc');
		} else if (data.value == "否") {
			$("#pro").addClass('yc');
		}
	});
	//其他的时候显示输入框
	form.on('radio(orderPath)', function(data) {
		console.log(data.value);
		if (data.value == "其他") {
			$("#ord").removeClass('yc');
		} else if (data.value == "飞猪" || data.value == "公众号") {
			$("#ord").addClass('yc');
		}
	});
	
	//添加备注的方法先获取订单号，没有订单号不给添加
	
	$(document).on('click','#reamark',function(){
		var data = form.val("order");
		var orderNumber=data.orderNumber;
		if(orderNumber==""){
			setMsg('请先完成订单数据填写',2)
		}else{
			reamark(data.orderNumber);
		}
	});
	//自动匹配订单
	// $(document).on('click','#matching',function(obj){
	// 	var phone=$(obj).parent().parent().parent().find('input[name=telephoneNumber]').val();
	// 	console.log(phone)
	// });
	//跳转备注页面
	function reamark(id) {
		index = layer.open({
			type: 2,
			area: ['70%', '70%'],
			anim: 2,
			title: '备注信息',
			maxmin: true,
			shadeClose: true,
			content: "Remarks.html?id="+id
		});
	}
})
