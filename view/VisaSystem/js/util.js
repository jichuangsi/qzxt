layui.config({
	base: '../js/modules/dist/'
}).extend({
	xmSelect: 'xm-select'
}).use(['layer', 'laypage', 'notice', 'multiSelect', 'xmSelect'], function(exports) {
	var notice = layui.notice,
		xmSelect = layui.xmSelect,
		multiSelect = layui.multiSelect;
	notice.options = {
		closeButton: true, //显示关闭按钮
		debug: false, //启用debug
		positionClass: "toast-top-right", //弹出的位置,
		showDuration: "2000", //显示的时间
		hideDuration: "1000", //消失的时间
		timeOut: "2000", //停留的时间
		extendedTimeOut: "1000", //控制时间
		showEasing: "swing", //显示时的动画缓冲方式
		hideEasing: "linear", //消失时的动画缓冲方式
		iconClass: 'toast-info', // 自定义图标，有内置，如不需要则传空 支持layui内置图标/自定义iconfont类名
		onclick: null // 点击关闭回调

		//notice.warning("提示信息:警告警告!");
		//notice.info("提示信息:显示!");
		//notice.error("提示信息,报错啦!");
		//notice.success("提示信息,成功!");
	};
})
//layui.config({
//	base: '../js/modules/dist/'
//}).extend({
//	xmSelect: 'xm-select'
//}).use(['xmSelect'], function() {});

function setMsg(msg, icon) {
	layer.msg(msg, {
		icon: icon,
		time: 1000
	});
}