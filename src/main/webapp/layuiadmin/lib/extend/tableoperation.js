/**
 * 
 * @Name：自动加载表格操作
 * @Author：HL
 * 
 */
layui.define([ 'jquery','form','baseUtil'], function(exports) {
	"use strict";

	var $ = layui.$;
	var baseUtil = layui.baseUtil;
	var admin = layui.admin;
	var form = layui.form;

	// 核心入口
	var tableoperation = {};
	
	// 参数处理
	tableoperation.showClass = function(options){
		this.operType = baseUtil.isNullOrEmpty(options.operType) ? 'view' : options.operType;
		this.showDataUrl = baseUtil.isNullOrEmpty(options.showDataUrl) ? '' : options.showDataUrl;
		
		//form表单参数
		this.formLayFilter = baseUtil.isNullOrEmpty(options.formLayFilter) ? 'component-form-group' : options.formLayFilter;
		
		this.submitUrl = baseUtil.isNullOrEmpty(options.submitUrl) ? '' : options.submitUrl;
		this.refreshTableId = baseUtil.isNullOrEmpty(options.refreshTableId) ? 'table-page' : options.refreshTableId;
		
		this.init()
		this.render();
	}
	
	tableoperation.showClass.prototype.init = function(){
		
	}
	
	tableoperation.showClass.prototype.render = function(){
		var that = this;
		var operType = that.operType + "Show";
		that[operType]();
	}
	
	
	/**
	 * 查看页面回显
	 */
	tableoperation.showClass.prototype.viewShow = function(){
		
		var that = this;
		var param = baseUtil.getLayerParentParam();
		var dataUrl = that.showDataUrl;
		var data = that.getDataObj(dataUrl,param);
		$.each(data, function(i, val) {
			if(!baseUtil.isNullOrEmpty(val)){
				$(".layerCard div[name="+i+"]").text(val);
			}
		});
	}
    
    /**
	 * 编辑页面数据回显 dataUrl：查看对象详情url params：参数。例如查看对象id
	 * formLayFilter：form表单lay-filter值
	 */
	tableoperation.showClass.prototype.editShow = function(){
    	var that = this;
    	//加载form表单
		if(baseUtil.isNullOrEmpty(that.formLayFilter)){
			console.log('表单lay-filter不能为空');
		}
		form.render(null, that.formLayFilter);
		//数据回显
		var param = baseUtil.getLayerParentParam();
		var dataUrl = that.showDataUrl;
		var dataObj = that.getDataObj(dataUrl,param);
		form.val(that.formLayFilter, dataObj);
		//加载表单提交事件
		that.formSubmit();
	}
	
	 /**
	 * 编辑页面数据回显 dataUrl：查看对象详情url params：参数。例如查看对象id
	 * formLayFilter：form表单lay-filter值
	 */
	tableoperation.showClass.prototype.addShow = function(){
    	var that = this;
    	//加载form表单
		if(baseUtil.isNullOrEmpty(that.formLayFilter)){
			console.log('表单lay-filter不能为空');
		}
		form.render(null, that.formLayFilter);
		//加载表单提交事件
		that.formSubmit();
	}
	
	/**
	 * 提交表单，成功关闭弹窗
	 */
	tableoperation.showClass.prototype.formSubmit = function(){
    	var that = this;
    	var submitBtnLayFilter = $(".layerCard button[lay-submit]").attr("lay-filter");
		/* 监听提交 */
		form.on('submit('+submitBtnLayFilter+')', function(data) {
			$.ajax({
				async:false,
				url:that.submitUrl,
				data:data.field,
				type:'post',
				dataType : "json",
				success:function(returnData){
					if(returnData.success){
						var index = parent.layer.getFrameIndex(window.name);
						parent.layer.msg("修改成功");
						parent.layer.close(index);
						// 表格刷新
						parent.layui.table.reload(that.refreshTableId);
					}else{
						parent.layer.msg(returnData.msg);
					}
				},
				error:function(jqXHR,textStatus,errorThrown){
					layer.msg("操作失败");
				}
			});
			return false;
		});
	}
   
	
	/**
	 * 获取对象实体数据 dataUrl：查看对象详情url params：参数。例如查看对象id
	 */
	tableoperation.showClass.prototype.getDataObj = function(dataUrl,param){
		var dataObj;
		if(baseUtil.isNullOrEmpty(param)){
			return dataObj;
		}
		$.ajax({
			type : "GET",
			async : false,
			url : dataUrl,
			data : param,// 参数
			dataType : "json",
			success : function(data) {
				if(data.success){
					dataObj = data.obj;
				}else{
					layer.msg(data.msg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("数据加载出错");
			}
		});
		return dataObj;
	}
    
// =======================================弹窗内容编辑，结束==========================================
	
	
	exports("tableoperation", tableoperation);
});
