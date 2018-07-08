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
						tableTitleUrl : layui.setter.base+"json/teacher/tmlisthead.json",//(必需)
						//HLTODO 请求题目list数据
						dataUrl : '/houdaexam/rest/subject/selectAll',//(必需)
						//新增编辑页面
						editHtml : 'tmedit.html',//(必需)
						//查看页面
						viewHtml : 'tmview.html',//(必需)
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var tmlist = function(options){
						tmlist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(tmlist, autotable.tableClass);
					
					
					
					var tmlistObj = new tmlist(options);
					exports('tmlist', {});
				});