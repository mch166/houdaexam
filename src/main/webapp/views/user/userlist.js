layui
		.define(
				[ 'baseUtil', 'autotable', 'jquery', 'table' ],
				function(exports) {
					
					//引入其他模块
					var $ = layui.$;
					var autotable = layui.autotable;
					var table = layui.table;
					var baseUtil = layui.baseUtil;
					var admin = layui.admin;
					
					//设置table参数
					var options = {
						tableTitleUrl : layui.setter.base+"json/user/userlisthead.json",//(必需)
						dataUrl : "/houdaexam/rest/user/getUserList",//(必需)
						//新增编辑页面
						editHtml : 'useredit.html',//(必需)
						//查看页面
						viewHtml : 'userview.html',//(必需)
						//HLTODO 删除Url
						delUrl : ''
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var userlist = function(options){
						userlist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(userlist, autotable.tableClass);
					
					/**
					 * 密码重置
					 */
					userlist.prototype.passResetOperation = function(data){
						$.ajax({
							type : "post",
							url : '',
							data : data,// 参数
							dataType : "json",
							success : function(data, status) {
								if(data.success){
									layer.msg("密码重置成功");
								}else{
									layer.msg("密码重置失败");
								}
							},
							error : function() {
								layer.msg("密码重置失败");
							}
						});
					}
					
					var userlistObj = new userlist(options);
					exports('userlist', {});
				});