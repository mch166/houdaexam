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
					
					
					var userlistObj = new userlist(options);
					exports('userlist', {});
				});