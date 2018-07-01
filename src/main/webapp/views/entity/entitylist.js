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
						//不传值时，默认：id
						idCode : "stbh",//(必需)
						tableTitleUrl : layui.setter.base+"json/entity/entitylisthead.json",//(必需)
						dataUrl : "/web/system?_service=/entity/getAllEntityList",//(必需)
						//新增编辑页面
						editHtml : 'entityedit.html',
						//查看页面
						viewHtml : 'entityview.html',
						//删除Url
						delUrl : '/web/system?_service=/entity/deleteEntity'
					};
					
					//定义当前页面对象
					//并且继承autotable.tableClass
					var entitylist = function(options){
						entitylist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(entitylist, autotable.tableClass);
					
					
					//重写init方法，页面加载后就直接执行的方法入口。
					entitylist.prototype.init = function(){
						layer.msg("list.js==>init");
					}
					
					entitylist.prototype.detailOperation = function(data){
				    	var that = this;
				    	
				    	parent.layer.open({
				    		type: 2,
					    	title: '实体详情',
					    	area : admin.screen() <= 2 ? ['40%', '80%'] : ['400px', '500px'],
					    	shade: 0,//不要遮罩层。 默认：0.3
					    	maxmin: true,
					    	content: 'entity/entityview.html',
					    	btn: ['确认'],
					    	yes: function(index, layero){
					    		layer.close(index);
					    	},
					    	success : function(layero, index) {
					    		baseUtil.setLayerParam(layero,data);
							}
				    	});
					}
					
					//new自己创建的对象，要写到它定义的 ***.prototype.***方法后面。
					//即先定义方法，最后创建对象。
					var entitylistObj = new entitylist(options);

					exports('entitylist', {});
				});