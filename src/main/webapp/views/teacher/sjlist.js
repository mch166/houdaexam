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
						tableTitleUrl : layui.setter.base+"json/teacher/sjlisthead.json",//(必需)
						//HLTODO 请求题目list数据
						dataUrl : '/houdaexam/rest/exam/getExamList',//(必需)
						//新增编辑页面
						editHtml : 'sjedit.html',//(必需)
						//HLTODO 删除Url
						delUrl : ''
					};

					//定义当前页面对象
					//并且继承autotable.tableClass
					var sjlist = function(options){
						sjlist.superClass.constructor.apply(this, arguments);
					}
					baseUtil.extend(sjlist, autotable.tableClass);
					
					/**
					 * 加载表格数据
					 * 满屏调整：
					 * 		没有搜索条件：full-63
					 * 		当只有一行搜索条件：full-135
					 * 		两行：full-175
					 * 		三行：220
					 */
					autotable.loadTable = function(tableId,titleData,dataUrl) {
						var titleArr = new Array();
						titleArr.push(titleData);
						table.render({
							elem : "#"+tableId,//表格id
							url  : dataUrl,//列表数据连接
							cols : titleArr,//表头数据
							height: 'full-63',//页面自动调整满屏
							cellMinWidth: 80,
							page : true
						});
					}
					
					var sjlistObj = new sjlist(options);
					exports('sjlist', {});
				});