/**
 * 
 * @Name：自动加载表格操作
 * @Author：HL
 * 
 */
layui.define([ 'jquery','form','baseUtil', 'table' ], function(exports) {
	"use strict";

	var $ = layui.$;
	var table = layui.table;
	var baseUtil = layui.baseUtil;
	var admin = layui.admin;
	var form = layui.form;

	// 核心入口
	var autotable = {};
	
	// 参数处理
    autotable.tableClass = function(options){
		// 一般为当前页面名称，（必填）。
		// 数据ID字段
		this.idCode = baseUtil.isNullOrEmpty(options.idCode) ? "id" : options.idCode;
		// 是否多选
		this.isCheckbox = baseUtil.isNullOrEmpty(options.isCheckbox) ? true:options.isCheckbox;
		// 表格ID
		this.tableId = baseUtil.isNullOrEmpty(options.tableId) ? "table-page":options.tableId;
		// 是否存在操作列。（默认：true）
		// 操作列id
		this.hasOpera = baseUtil.isNullOrEmpty(options.hasOpera) ? "true":options.hasOpera;
		this.operationBtn = baseUtil.isNullOrEmpty(options.operationBtn) ? "operationBtn":options.operationBtn;
		// 是否存在工具条。（默认：true）
		// 工具条ID
		this.hasToolbar = baseUtil.isNullOrEmpty(options.hasToolbar) ? "true":options.hasToolbar;
		this.toolbarId = baseUtil.isNullOrEmpty(options.toolbarId) ? "toolbar":options.toolbarId;
		// 搜索id
		this.searchId = baseUtil.isNullOrEmpty(options.searchId) ? "search":options.searchId;
		// 表头获取Url，（必填）
		this.tableTitleUrl = options.tableTitleUrl;
		// 数据获取Url，（必填）
		this.dataUrl = options.dataUrl;
		// 新增编辑页面
		this.editHtml = options.editHtml;
		// 查看页面
		this.viewHtml = options.viewHtml;
		// 删除Url
		this.delUrl = baseUtil.isNullOrEmpty(options.delUrl) ? '' : options.delUrl;
		
		this.init();
		this.render();
	}
// ========================================操作方法，开始===========================================
	autotable.tableClass.prototype.init = function(){
    	//打开页面后直接执行方法入口。
    }
	
	 /**
	 * 添加弹窗
	 */
    autotable.tableClass.prototype.addToolbar = function(data){
    	var that = this;
    	layer.open({
			type : 2,
			title : "添加",
			content : that.editHtml,
			maxmin : !0,
			area : admin.screen() < 2 ? ['60%', '95%'] : ['50%', '90%'],
			btn : [ "确定","取消" ],
			yes : function(index, layero) {
				var submit = layero.find('iframe').contents().find(".layerCard button[lay-submit]");
	            submit.trigger('click');
			},
			btn2 : function(index, layero){
				layer.close(index);
			},
			success : function(layero, index) {
			}
		});
	}
    
	/**
	 * 查看弹窗
	 */
    autotable.tableClass.prototype.detailOperation = function(data){
    	var that = this;
    	
    	layer.open({
			type : 2,
			title : "查看",
			content : that.viewHtml,
			maxmin : !0,
			area : admin.screen() < 2 ? ['80%', '80%'] : ['70%', '70%'],
			btn : ['确认'],
			yes : function(index, layero) {
				layer.close(index);
			},
			success : function(layero, index) {
				baseUtil.setLayerParam(layero,data);
			}
		});
	}
    
    /**
	 * 编辑弹窗
	 */
    autotable.tableClass.prototype.editOperation = function(data){
    	var that = this;
		layer.open({
			type : 2,
			title : "编辑",
			content : that.editHtml,
			maxmin : !0,
			area : admin.screen() < 2 ? ['60%', '95%'] : ['50%', '90%'],
			btn : [ "确定","取消" ],
			yes : function(index, layero) {
				var submit = layero.find('iframe').contents().find(".layerCard button[lay-submit]");
	            submit.trigger('click');
			},
			btn2 : function(index, layero){
				layer.close(index);
			},
			success : function(layero, index) {
				baseUtil.setLayerParam(layero,data);
			}
		});
	}
    
    /**
	 * 单个删除操作
	 */
    autotable.tableClass.prototype.delOperation = function(checkData){
    	var that = this;
    	
    	var param = checkData[that.idCode];
    	
    	layer.confirm("确定删除？",function(index){
    		that.delAjax(param);
    	});
	}
    
    /**
     * 批量删除
     */
    autotable.tableClass.prototype.delToolbar = function(checkData) { // 验证是否全选
		
		var that = this;
		var delCount = checkData.length;
		if(delCount == 0){
			layer.msg("请选择要删除的数据");
			return;
		}
		var param = "";
		for(var i=0; i<checkData.length; i++){
			param = param + checkData[i][that.idCode] + ",";
		}
    	layer.confirm("确定删除这"+delCount+"条数据？",function(index){
    		that.delAjax(param);
    	});
	};
	/**
	 * 删除Ajax
	 * param : 数组格式[]，参数
	 */
	autotable.tableClass.prototype.delAjax = function(param) {
		var that = this;
		
		$.ajax({
			type : "post",
			url : that.delUrl,
			data : {json:param},// 参数
			dataType : "json",
			success : function(data, status) {
				if(data.success){
					layer.msg(data.msg);
					that.reload();
				}
			},
			error : function() {
				layer.msg("删除失败");
			}
		});
	}
    
	/**
	 * 搜索
	 */
	autotable.tableClass.prototype.searchToolbar = function(checkData) { // 验证是否全选
			var that = this;
			layer.msg("搜索操作");
		};
   
    /**
	 * 表格刷新
	 */
    autotable.tableClass.prototype.reload = function(){
    	var that = this;
    	table.reload(that.tableId,{
    		url : that.dataUrl
    	})
    }
    
    /**
     * 表格加载
     */
    autotable.tableClass.prototype.render = function(){
    	var that = this;
    	// 首先获取表头
		var titleData = autotable.getTableTitle(that);
		// 加载数据
		autotable.loadTable(that.tableId,titleData,that.dataUrl);
		
		// 监听表格复选框选择
		table.on('checkbox(' + that.tableId + ')',
				function(obj) {
					console.log(obj)
				});
		
		// 监听操作列
		table.on('tool(' + that.tableId + ')',
				function(obj) {
					var data = obj.data;
					var objEvent = obj.event + "Operation";
					if (baseUtil.isFunction(that[objEvent])) {
						that[objEvent] ? that[objEvent](data):'';
					}
				});
		
		// 监听列表上方按钮
		$('#'+that.toolbarId+' .layui-btn').on('click',
				function() {
					var checkStatus = table.checkStatus(that.tableId);
					var data = checkStatus.data;
					var type = $(this).data('type') + "Toolbar";
					that[type] ? that[type](data): '';
				});
    }
    
    
// ========================================打开弹窗操作，结束===========================================

    
   
    

// ===========================================加载表格，开始===============================
	
	// 获取并加载表格行列
	autotable.getTableTitle = function(tableClassObj) {
		var tableUrl = tableClassObj.tableTitleUrl
		var returnTitle = new Array();
		$.ajax({
			type : "GET",
			async : false,
			url : tableUrl,
			data : {},// 参数
			dataType : "json",
			success : function(data, status) {
				var dataObj = data.data;//表头数据
				var operationBtnData = data.operationBtn;//操作列按钮
				var toolbarData = data.toolbar;//工具栏数据
				
				// 是否多选
				if(tableClassObj.isCheckbox || tableClassObj.isCheckbox == "true"){
					returnTitle.push({type:'checkbox',width:38, fixed: 'left'});
				}
				// 获取表头
				for(var i=0; i<dataObj.length; i++){
					returnTitle.push(dataObj[i]);
				}
				// 添加删、改、查按钮
				if(!baseUtil.isNullOrEmpty(operationBtnData) && tableClassObj.hasOpera && operationBtnData.length > 0){
					autotable.createOperationBtn(tableClassObj,operationBtnData);// 组建操作btn
					
					returnTitle.push({width:50+45*(operationBtnData.length), title:'操作', align:'center',  toolbar:'#'+tableClassObj.operationBtn});
				}
				//页面创建工具栏
				if(!baseUtil.isNullOrEmpty(toolbarData) && tableClassObj.hasToolbar && toolbarData.length > 0){
					autotable.createToolBar(tableClassObj,toolbarData)
				}
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("表格加载出错");
			}
		});
		return returnTitle;
	};
	
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
			height: 'full-135',//页面自动调整满屏
			cellMinWidth: 80,
			page : true
		});
	}
	
	/**
	 * 组建操作btn
	 * 如果页面有自己的特殊处理数据，该方法可以重写在自己的页面js中
	 */
	autotable.createOperationBtn = function(tableClassObj,operationBtnData){
		for(var i=0; i<operationBtnData.length; i++){
			var operationBtnHtml = "<a id='{codeid}' class='layui-btn layui-btn-xs {clazz}' lay-event='{code}'>{name}</a>";
			operationBtnHtml = operationBtnHtml.replace(/\{clazz\}/g,operationBtnData[i].clazz)
								.replace(/\{code\}/g,operationBtnData[i].code)
								.replace(/\{name\}/g,operationBtnData[i].name);
			$("#"+tableClassObj.operationBtn).append(operationBtnHtml);
		}
	};
	
	/**
	 * 生成toolbar
	 * 如果页面有自己的特殊方法，该方法可以重写在自己的页面js中
	 */
	autotable.createToolBar = function(tableClassObj,toolbarData){
		for(var i=0; i<toolbarData.length; i++){
			var toolbarHtml = '<button id="{codeid}" class="layui-btn layui-btn-sm" data-type="{code}"><i class="layui-icon">{icon}</i>{name}</button>';
			toolbarHtml = toolbarHtml.replace(/\{code\}/g,toolbarData[i].code)
								.replace(/\{icon\}/g,baseUtil.isNullOrEmpty(toolbarData[i].icon)? '':(toolbarData[i].icon+";"))
								.replace(/\{codeid\}/g,toolbarData[i].code+"Toolbar")
								.replace(/\{name\}/g,toolbarData[i].name);
			$("#"+tableClassObj.toolbarId).append(toolbarHtml);
		}
	};
// ======================================加载表格，结束===========================================


	exports("autotable", autotable);
});
