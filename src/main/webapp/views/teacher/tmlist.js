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
						idCode : "bmbh",	//不传值时，默认：id//(必需)
						//是否多选-默认：true
						isCheckbox : true,
						//表格ID 不传值时，默认：table-page
						tableId : "table-page",
						// 是否存在操作列。（默认：true）
						//操作列id 不传值时，默认：operationBtn
						hasOpera : true,
						operationBtn : "operationBtn",
						// 是否存在工具条。（默认：true）
						//工具条ID 不传值时，默认：toolbar
						hasToolbar : true,
						toolbarId : "toolbar",
						//搜索id 不传值时，默认：search
						searchId : "search",
						
						tableTitleUrl : layui.setter.base+"json/student/tmlisthead.json",//(必需)
						dataUrl : layui.setter.base+"json/student/tmlistdata.json",//(必需)
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