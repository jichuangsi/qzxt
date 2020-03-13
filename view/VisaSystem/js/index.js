layui.use("form", function() {
	var form = layui.form;
	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if (param.newPwd != param.yesPwd) {
			setMsg("两次密码不相同!", 7)
			return false;
		} else {
			$.ajax({
				type: "post",
				url: httpUrl() + "/backRoleConsole/updateBackUserPwd",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(res) {
					if (res.code == '0010') {
						setMsg('修改成功', 2)
						layer.close(index);
					} else {
						setMsg(res.msg, 2)
						layer.close(index);
					}
				}
			});
			return false;
		}
	});
	getUserRole();
	//获取角色显示的页面
	function getUserRole() {
		var url = '/backRoleConsole/getUrlByUserRole';
		var data = getAjaxData(url);
		var arr;
		if(data.data!=undefined){
			arr = data.data;
		}
		$('#nav').empty();
		// setMenu(data);
		var mainMenu = [];
		$.each(data.data, function(index, item) {
			mainMenu.push({
				id: item.modelId,
				name: item.modelName
			})
		});
		//去掉重复的菜单选项
		var obj = {};
		var mainMenu = mainMenu.reduce((cur, next) => {
			obj[next.id] ? "" : obj[next.id] = true && cur.push(next);
			return cur;
		}, [])
		var obj2={};
		var arr= arr.reduce((cur, next) => {
			obj2[next.staticPageId] ? "" : obj2[next.staticPageId] = true && cur.push(next);
			return cur;
		}, [])
		var content='';
		for (var i = 0; i < mainMenu.length; i++) {
			content += "<li>";
			content += '<a href="javascript:;">';
			content += '<i class="iconfont"></i>';
			content += '<cite>' + mainMenu[i].name + '</cite>';
			content += '<i class="iconfont nav_right">&#xe697;</i>';
			content += '</a>';
			content += '<ul class="sub-menu">';
			for (var j = 0; j < arr.length; j++) {
				if (mainMenu[i].id == arr[j].modelId) {
					content += '<li>';
					content += '<a _href="pages' + arr[j].staticPageUrl + '">';
					content += '<i class="iconfont">&#xe6a7;</i>';
					content += '<cite>' + arr[j].staticPageName + '</cite>';
					content += '</a>';
					content += '</li>';
				}
			}
			content += '</ul>';
			content += "</li>";
		}
		if (content == '') {
			$('.left-nav').addClass('jq');
			$('.page-content').addClass('cq')
		}
		$('#nav').append(content);
		setMenu();
	}

	function setMenu() {
		var tab = {
			tabAdd: function(title, url, id) {
				element.tabAdd('xbs_tab', {
					title: title,
					content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url +
						'" scrolling="yes" class="x-iframe"></iframe>',
					id: id
				})
			},
			tabDelete: function(othis) {
				element.tabDelete('xbs_tab', '44');
				othis.addClass('layui-btn-disabled');
			},
			tabChange: function(id) {
				element.tabChange('xbs_tab', id);
			}
		};

		$('.left-nav #nav li').click(function(event) {
			if ($(this).children('.sub-menu').length) {
				if ($(this).hasClass('open')) {
					$(this).removeClass('open');
					$(this).find('.nav_right').html('&#xe697;');
					$(this).children('.sub-menu').stop().slideUp();
					$(this).siblings().children('.sub-menu').slideUp();
				} else {
					$(this).addClass('open');
					$(this).children('a').find('.nav_right').html('&#xe6a6;');
					$(this).children('.sub-menu').stop().slideDown();
					$(this).siblings().children('.sub-menu').stop().slideUp();
					$(this).siblings().find('.nav_right').html('&#xe697;');
					$(this).siblings().removeClass('open');
				}
			} else {

				var url = $(this).children('a').attr('_href');
				var title = $(this).find('cite').html();
				var index = $('.left-nav #nav li').index($(this));

				for (var i = 0; i < $('.x-iframe').length; i++) {
					if ($('.x-iframe').eq(i).attr('tab-id') == index + 1) {
						tab.tabChange(index + 1);	
						event.stopPropagation();
						return;
					}
				};

				tab.tabAdd(title, url, index + 1);
				tab.tabChange(index + 1);
			}
			event.stopPropagation();
		})
	}
})
