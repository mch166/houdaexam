/**
 * 
 * @Name：自动加载表格操作
 * @Author：HL
 * 
 */
layui.define(['jquery'], function(exports) {
	"use strict";

	var $ = layui.$
	,baseUtil = {};
	/**
	 * 继承一个类
	 * 
	 * @param subClass
	 *            子类
	 * @param superClass
	 *            要继承的父类
	 */
	baseUtil.extend = function(subClass, superClass) {
		var func = function() {
		};
		func.prototype = superClass.prototype;
		subClass.prototype = new func();
		subClass.prototype.constructor = subClass;
		subClass.superClass = superClass.prototype;
		if (superClass.prototype.constructor == Object.prototype.constructor) {
			superClass.prototype.constructor = superClass;
		}
	};
	
	/**
	 * 判断指定对象是否为函数
	 * 
	 * @param obj
	 *            要判断的对象
	 * @return 返回一个值，该值标识对象是否为函数
	 */
	baseUtil.isFunction = function(obj) {
		return typeof(obj) == "function";
	};

	/**
	 * 判断指定对象是否为空或者空字符串
	 * 
	 * @param obj
	 *            要判断的对象
	 * @return 返回一个值，该值标识对象是否为空或者空字符串
	 */
	baseUtil.isNullOrEmpty = function(obj) {
		return typeof(obj) == "undefined" || obj == null || obj == "";
	};
	
	/**
	 * layero:弹窗页面
	 * data：需要传递的参数（json格式）
	 */
	baseUtil.setLayerParam = function(layero,data){
		var layerPage = layero.find("iframe").contents().find(".layerCard").click();
		$.each(data, function(i, val) {
			if(!baseUtil.isNullOrEmpty(val)){
				layerPage.append("<input id='layerparam_"+i+"' type='hidden' name='layerparam' value='"+val+"'>");
			}
		});
	}
	
	/**
	 * 获取父页面传过来的参数实体
	 */
	baseUtil.getLayerParentParam = function(){
		var returnObj = {},
			flag = false;
		$("input[name='layerparam']").each(function(j,item){
			returnObj[item.id.split("_")[1]] = item.value;
			flag = true;
		});
		if(!flag){
			return "";
		}else{
			return returnObj;
		}
	}
	
	/**
	 * 深复制指定对象
	 * 
	 * @param obj
	 *            要复制的对象
	 * @return 返回复制后的新对象
	 */
	baseUtil.cloneObject = function(obj) {
		var cloneObj = $.extend(true, {}, obj);
		return cloneObj;
	};

	/**
	 * 深复制一个数组
	 * 
	 * @param arr
	 *            要复制的数组
	 * @return 返回复制后的新数组
	 */
	baseUtil.cloneArray = function(arr) {
		var that = this;
		var cloneArr = $.map(arr, function(obj) {
			return that.cloneObject(obj);
		});
		return cloneArr;
	};
	/**
	 * 删除对象中所有属性
	 * 
	 * @param obj
	 *            要删除属性的对象
	 */
	baseUtil.deleteAllProps = function(obj) {
		if(obj == null) {
			return;
		}
		for(var prop in obj) {
			delete obj[prop];
		}
	};
	/**
	 * 获取选中的单选框的值
	 * 
	 * @param name
	 *            单选框名称
	 * @return 返回选中的单选框的值
	 */
	baseUtil.getCheckedRadioVal = function(name) {
		return $(":radio[name='" + name + "']:checked").val();
	};
	/**
	 * 根据名称与值将单选框选中
	 * 
	 * @param name
	 *            单选框名称
	 * @param value
	 *            单选框的值
	 */
	baseUtil.setRadioChecked = function(name, value) {
		$(":radio[name='" + name + "'][value='" + value + "']").prop("checked",
			true);
	};
	/**
	 * 检查名称是否合法
	 * 
	 * @param str
	 *            要检查的名称字符串
	 * @param text
	 *            用于不合法时提示的信息
	 * @return 返回一个值，该值标识输入字符串是否合法
	 */
	baseUtil.checkNameValid = function(str, text) {
		return baseUtil.checkValid(str, text || "名称");
	};
	/**
	 * 通过正则检查输入字符串是否合法
	 * 
	 * @param nameStr
	 *            要检查的字符串
	 * @param text
	 *            用于不合法时提示的信息
	 * @return 返回一个值，该值标识输入字符串是否合法
	 */
	baseUtil.checkValid = function(nameStr, text) {
		if(!nameStr || "") {
			mui.toast(text + "输入不允许为空。");
			return false;
		}
		var x = eval("/[\\\\/'\"*?%.><=:;()\\[\\]|]/");
		var ret = nameStr.match(x);
		if(ret) {
			if(ret == " ") {
				mui.alert(text +
					"输入中不允许包含非法字符' []()\*?%<>=:;|/\" '，该输入中包含非法字符： 。请更换为合法字符。");
			} else {
				mui.alert(text + "输入中不允许包含非法字符' []()\*?%<>=:;|/\" '，该输入中包含非法字符：" + ret +
					"。请更换为合法字符。");
			}
			return false;
		}
		return true;
	};
	/**
	 * 扩展数组方法，在指定位置插入对象
	 * 
	 * @param index
	 *            要插入的索引位置
	 * @param item
	 *            要插入的对象
	 */
	Array.prototype.insert = function(index, item) {
		this.splice(index, 0, item);
	};
	/**
	 * 扩展字符串方法，判断是否以指定字符串结尾
	 * 
	 * @param s
	 *            字符串结尾
	 */
	String.prototype.endWith = function(s) {
		if(s == null || s == "" || this.length == 0 || s.length > this.length) {
			return false;
		}
		if(this.substring(this.length - s.length) == s) {
			return true;
		} else {
			return false;
		}
		return true;
	};
	/**
	 * 扩展字符串方法，判断是否以指定字符串开头
	 * 
	 * @param s
	 *            字符串开头
	 */
	String.prototype.startWith = function(s) {
		if(s == null || s == "" || this.length == 0 || s.length > this.length) {
			return false;
		}
		if(this.substr(0, s.length) == s) {
			return true;
		} else {
			return false;
		}
		return true;
	};


	exports("baseUtil", baseUtil);
});
