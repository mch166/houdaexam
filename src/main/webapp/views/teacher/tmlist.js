layui.define([ 'baseUtil', 'autotable', 'jquery', 'table', 'form' ], function(
		exports) {

	// 引入其他模块
	var $ = layui.$;
	var autotable = layui.autotable;
	var table = layui.table;
	var baseUtil = layui.baseUtil;
	var admin = layui.admin;
	var form = layui.form;

	// 设置table参数
	var options = {
		tableTitleUrl : layui.setter.base + "json/teacher/tmlisthead.json",// (必需)
		// HLTODO 请求题目list数据
		dataUrl : '/houdaexam/rest/subject/selectAll',// (必需)
		// 新增编辑页面
		editHtml : 'tmedit.html',// (必需)
		// 查看页面
		viewHtml : 'tmview.html',// (必需)
	};

	// 定义当前页面对象
	// 并且继承autotable.tableClass
	var tmlist = function(options) {
		this.initView();
		tmlist.superClass.constructor.apply(this, arguments);
	}
	baseUtil.extend(tmlist, autotable.tableClass);

	tmlist.prototype.initView = function() {
		$.ajax({
			async : false,
			url : "/houdaexam/rest/exam/getAllExam",
			type : 'post',
			dataType : "json",
			success : function(returnData) {
				if (returnData.success) {
					var data = returnData.obj;
					$.each(data, function(i, val) {
						if (!baseUtil.isNullOrEmpty(val)) {
							$("#sjid").append(
									'<option value="' + data[i].id + '">'
											+ data[i].name + '</option>');
						}
					});
					form.render();
				} else {
					parent.layer.msg(returnData.msg);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
			}
		});
	}

	/* 监听提交 */
	form.on('submit(searchSubmit)', function(data) {
		table.reload('table-page',{
			where : { // 设定异步数据接口的额外参数，任意设
				sjid : data.field.sjid,
				tmxh : data.field.tmxh
			},
			page : {
				curr : 1
			}
		});
	});

	var tmlistObj = new tmlist(options);
	exports('tmlist', {});
});