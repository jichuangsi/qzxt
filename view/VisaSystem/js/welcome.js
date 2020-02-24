layui.config({
	base: './js/modules/'
}).extend({
	FlowChart: 'FlowChart/FlowChart'
}).use(['element', 'FlowChart'], function() {

	var FlowChart = layui.FlowChart;
	var arr = [{
			"left": "596px",
			"top": "18px",
			"url": "OD",
			"indexno": "",
			"text": "收件员",
			"img": "./js/modules/FlowChart/image/nav_icon_2.png",
			"sta": "new"
		}, {
			"left": "777px",
			"top": "18px",
			"url": "so",
			"indexno": "",
			"text": "客服",
			"img": "./js/modules/FlowChart/image/nav_icon_5.png",
			"sta": "new"
		},
		{
			"left": "980px",
			"top": "18px",
			"url": "so",
			"indexno": "",
			"text": "检查员资料",
			"img": "./js/modules/FlowChart/image/nav_icon_6.png",
			"sta": "new"
		},
		{
			"left": "1175px",
			"top": "18px",
			"url": "OD",
			"indexno": "",
			"text": "签证处理员",
			"img": "./js/modules/FlowChart/image/nav_icon_2.png",
			"sta": "new"
		},
		{
			"left": "686px",
			"top": "43px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "880px",
			"top": "43px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "1080px",
			"top": "43px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "596px",
			"top": "158px",
			"url": "OD",
			"indexno": "",
			"text": "收到快件",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "596px",
			"top": "335px",
			"url": "pages/ReceiptManagement.html",
			"indexno": "",
			"text": "输入收快件信息",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "596px",
			"top": "510px",
			"url": "pages/ElectricitySupplierOrder.html",
			"indexno": "",
			"text": "查询订单",
			"img": "./js/modules/FlowChart/image/nav_icon_9.png",
			"sta": "new"
		},
		{
			"left": "596px",
			"top": "685px",
			"url": "pages/ElectricitySupplierOrder.html",
			"indexno": "",
			"text": "点击“导入订单”按钮，</br>系统订单数据导入</br>，并打印标签；</br>人工将标签贴上快件的护照上",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "596px",
			"top": "865px",
			"url": "pages/ReceiptManagement.html",
			"indexno": "",
			"text": "更新快件状态为“已处理”",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "627px",
			"top": "272px",
			"url": "OD",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
		{
			"left": "627px",
			"top": "450px",
			"url": "OD",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		}, {
			"left": "626px",
			"top": "592px",
			"url": "OD",
			"indexno": "",
			"text": "查到对应订单信息",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		}, {
			"left": "627px",
			"top": "805px",
			"url": "OD",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
		{
			"left": "759px",
			"top": "640px",
			"url": "pages/AbnormalExpress.html",
			"indexno": "",
			"text": "查看异常快件信息，联系客户更正订单信息",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "754px",
			"top": "550px",
			"url": "OD",
			"indexno": "no",
			"text": "查询不到订单信息",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
		{
			"left": "686px",
			"top": "540px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "687px",
			"top": "923px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_12.png",
			"sta": "new"
		},
		{
			"left": "866px",
			"top": "927px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_13.png",
			"sta": "new"
		},
		{
			"left": "788px",
			"top": "1025px",
			"url": "",
			"indexno": "",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "759px",
			"top": "865px",
			"url": "pages/VisaToBeSupplemented.html",
			"indexno": "",
			"text": "查看待补充签证信息，并联系客户",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "1005px",
			"top": "889px",
			"url": "pages/VisaInformationCheck.html",
			"indexno": "",
			"text": "检查资料是否正确完整",
			"img": "./js/modules/FlowChart/image/nav_icon_9.png",
			"sta": "new"
		},
		{
			"left": "1002px",
			"top": "679px",
			"url": "pages/VisaInformationCheck.html",
			"indexno": "",
			"text": "将不完善的签证信息设置为 待补充",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "1036px",
			"top": "798px",
			"url": "OD",
			"indexno": "no",
			"text": "否",
			"img": "./js/modules/FlowChart/image/line_8.png",
			"sta": "new"
		},
		{
			"left": "901px",
			"top": "771px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "825px",
			"top": "779px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
		{
			"left": "831px",
			"top": "771px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "914px",
			"top": "895px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_9.png",
			"sta": "new"
		},
		{
			"left": "853px",
			"top": "895px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_1.png",
			"sta": "new"
		},
		{
			"left": "1010px",
			"top": "1057px",
			"url": "pages/VisaToBeSupplemented.html",
			"indexno": "",
			"text": "扫描护照，并将护照图片导入对应签证资料</br>将信息设置为“已检查”",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "1042px",
			"top": "1002px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
		{
			"left": "1221px",
			"top": "894px",
			"url": "pages/VisapProcessing.html",
			"indexno": "",
			"text": "查看签证资料，处理签证",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "1227px",
			"top": "1074px",
			"url": "pages/VisapProcessing.html",
			"indexno": "",
			"text": "点击“完成”按钮，</br>系统签证信息状态改为“完成”，</br>反馈电商平台；可对对应的信息设置快递状态“已寄出”（",
			"img": "./js/modules/FlowChart/image/nav_icon_10.png",
			"sta": "new"
		},
		{
			"left": "1101px",
			"top": "952px",
			"url": "OD",
			"indexno": "no",
			"text": "->",
			"img": "./js/modules/FlowChart/image/line_13.png",
			"sta": "new"
		},
		{
			"left": "1274px",
			"top": "1011px",
			"url": "OD",
			"indexno": "no",
			"text": "",
			"img": "./js/modules/FlowChart/image/line_6.png",
			"sta": "new"
		},
	];
	var urlstr = "./js/modules/FlowChart/image/";
	FlowChart.render({
		elem: '#box'
			/*AJAX请求地址*/
			,
		url: '/main/getajax',
		ImageURL: [
				urlstr + "nav_icon_1.png",
				urlstr + "nav_icon_2.png",
				urlstr + "nav_icon_3.png",
				urlstr + "nav_icon_4.png",
				urlstr + "nav_icon_5.png",
				urlstr + "nav_icon_6.png",
				urlstr + "nav_icon_7.png",
				urlstr + "nav_icon_8.png",
				urlstr + "line_1.png",
				urlstr + "line_6.png",
				urlstr + "line_12.png",
				urlstr + "line_13.png",
			]
			// 赋值渲染
			,
		data: arr
			// 保存到缓存测试
			,
		chartTest: false
			// 保存提示信息
			,
		saveMsg: "保存中.."
			// 保存后回调
			,
		callBack: function(data) {
				console.log(data.url)
			}
			// 点击当前图标的回调 ，编辑状态下无法跳转
			,
		link: function(elem) {
				console.log(11)
				// 
				var str = elem.getAttribute('url');
				if (str == 'so' || str == 'OD') {

				} else {
					location.href = str;
				}
			}
			// 页面唯一识别 默认数据中的，我原来的业务使用到，可以删除，并Savedata自定义
			,
		pageIndex: "so"
			/*
			 *自定义提交数据结构 已有data,尽量使用其他
			 * 默认数据
			 * obj.action = MOD_NAME;
			 * obj.menucode = _config.PageIndex;
			 * obj.data = [];
			 */
			,
		saveData: {
			"a": 123,
			"b": 456,
		}
	});
})
