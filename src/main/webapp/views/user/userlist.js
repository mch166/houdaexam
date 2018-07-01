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
						//数据id字段
						idCode : "yhid",//(必需)
						
						tableTitleUrl : layui.setter.base+"json/user/userlisthead.json",//(必需)
						dataUrl : "/web/system?_service=/code/getAllCodeList",//(必需)
						//新增编辑页面
						editHtml : '',//(必需)
						//查看页面
						viewHtml : '',//(必需)
						//删除Url
						delUrl : ''
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var userlist = function(options){
						userlist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(userlist, autotable.tableClass);
					
					
					/**
					 * 明细操作
					 */
					userlist.prototype.codemxOperation = function(data) {
						var that = this;
						layer.open({
							type : 2,
							title : "编码详情",
							content : "codeItemlist.html",
							maxmin : !0,
							area : admin.screen() <= 2 ? ['80%', '80%'] : ['600px', '400px'],
							success : function(layero, index) {
								baseUtil.setLayerParam(layero,data);
							}
						});
					}
	

					var userlistObj = new userlist(options);
					exports('userlist', {});
				});