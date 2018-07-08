layui
		.define(
				[ 'baseUtil', 'autotable', 'jquery', 'table','upload','form' ],
				function(exports) {
					
					//引入其他模块
					var $ = layui.$;
					var autotable = layui.autotable;
					var table = layui.table;
					var upload = layui.upload;
					var form = layui.form;
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
						delUrl : '/houdaexam/rest/user/deleteUser'
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var userlist = function(options){
						userlist.superClass.constructor.apply(this, arguments);
						this.initUpload();
						this.initForm();
					}
					baseUtil.extend(userlist, autotable.tableClass);
					
					userlist.prototype.initForm = function(){
						form.render();
						/* 监听提交 */
						form.on('submit(searchSubmit)', function(data) {
							table.reload('table-page',{
								where : { // 设定异步数据接口的额外参数，任意设
									name : data.field.name
								},
								page : {
									curr : 1
								}
							});
						});
					}
					
					userlist.prototype.initUpload = function(){
						var that = this;
						 upload.render({ //允许上传的文件后缀
							    elem: '#upToolbar'
							    ,url: '/houdaexam/rest/user/importUser'
							    ,accept: 'file' //普通文件
							    ,exts: 'xls|xlsx'
							    ,done: function(res){
							      console.log(res);
							      that.reload();
							    }
						 });
					}
					/**
					 * 密码重置
					 */
					userlist.prototype.passResetOperation = function(data){
						$.ajax({
							type : "post",
							url : '/houdaexam/rest/user/ResetPassword',
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