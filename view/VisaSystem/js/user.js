layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;


	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backRoleConsole/getAllBackuserByCondition',
		// contentType: 'application/json',
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
					field: 'account',
					align: 'center',
					title: '账户名'
				},
				{
					field: 'userName',
					align: 'center',
					title: '用户名'
				},
				{
					field: 'roles',
					align: 'center',
					title: '账户角色',
					templet: function(d) {
						if (d.roles == null) {
							var str = "暂未添加角色"
							return str
						} else {
							var str = '';
							$.each(d.roles, function(index, item) {
								str += item.rolename + ','
							});
							return str
						}
					}
				},
				{
					field: 'createdTime',
					align: 'center',
					title: '创建时间',
					templet: function(d) {
						if (d.createdTime != 0) {
							return new Date(+new Date(d.createdTime) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(
								/\.[\d]{3}Z/, '')
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
		toolbar: '#add_user',
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
	table.on('row(demo)', function(obj) {
		var param = obj.data;
		form.val('test', {
			"userId": param.id
		});
		form.val('roleUser', {
			"userId": param.id,
			"account": param.account
		});
		form.val('mod_pwd', {
			"id": param.id,
			"userName": param.userName	
		});
		if (param.roles != null) {
			$.each(param.roles, function(inex, item) {
				$('input[type=checkbox]').each(function() {
					if (item.id == $(this).val()) {
						$(this).prop("checked", true);
					}
				});

			});
			form.render("checkbox");
		} else {
			$('input[type=checkbox]').each(function() {
				$(this).prop("checked", false);
			});
			form.render("checkbox");
		}
	})
	$(document).on('click', '#modifyPwd', function() {
		resetPwd()
	});
	//重置密码
	form.on('submit(update_Pwd)', function(data) {
		var param = data.field;
		if(param.newPwd!=param.pwd){
			setMsg('两次密码不一致！',2);
			return 
		}
		var url = "/backRoleConsole/updateBackUserPwd2";
		ajaxPOST(url, param);
		table.reload('idTest');
		layer.close(index);
	});

	//分配角色
	form.on('submit(subRole)', function(data) {
		var param = data.field;
		var userRoleRelations = [];
		$('input[type=checkbox]:checked').each(function() {
			userRoleRelations.push({
				"rid": $(this).val(),
				"uid": param.userId
			})
		});
		var list = {
			userRoleRelations: userRoleRelations
		}
		console.log(userRoleRelations)
		var url = "/backRoleConsole/saveUserRole";
		ajaxPOST(url, list);
		table.reload('idTest');
		layer.close(index);
	});
	//添加账户
	form.on('submit(add_account)', function(data) {
		var param = data.field;
		var url = "/backRoleConsole/registBackUser";
		ajaxPOST(url, param);
		table.reload('idTest');
		layer.close(index);
	});

})
