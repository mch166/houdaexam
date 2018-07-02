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
						tableTitleUrl : layui.setter.base+"json/student/tmlisthead.json",//(必需)
						//HLTODO 请求题目list数据
						dataUrl : layui.setter.base+"json/teacher/tmlistdata.json",//(必需)
						//新增编辑页面
						editHtml : 'tmedit.html',//(必需)
						//查看页面
						viewHtml : 'tmview.html',//(必需)
						//删除Url
						delUrl : ''
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var tmlist = function(options){
						this.Hltest = "123";
						tmlist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(tmlist, autotable.tableClass);
					
					
					/**
					 * 明细操作
					 */
					tmlist.prototype.codemxOperation = function(data) {
						var that = this;
						layer.open({
							type : 2,
							title : "编码详情",
							content : "codeItemlist.html",
							maxmin : !0,
							area : admin.screen() > 2 ? ['80%', '80%'] : ['70%', '70%'],
							success : function(layero, index) {
								baseUtil.setLayerParam(layero,data);
							}
						});
					}
					
					
					var tmlistObj = new tmlist(options);
					exports('tmlist', {});
				});