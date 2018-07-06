layui.define(['util','laydate','layer','element','form'], function(exports) {
	var $ = layui.jquery
	,element = layui.element//Tab的切换功能，切换事件监听等，需要依赖element模块
	,form = layui.form
	,util = layui.util
	,laydate = layui.laydate
	,layer = layui.layer;
	
	var studentExam = function(){
		this.sjid = "";//试卷id
		this.userid = "";//userid
		
		this.dxdiv = "dxdiv";//单选div
		this.duoxdiv = "duoxdiv";//多选div
		this.bdxdiv = "bdxdiv";//不定项div
		
		this.tmxhSpan = "tmxhSpan";//题目序号span的id
		this.tmTypeSpan = "tmTypeSpan";//题目类型span的id
		this.tmtitle = "tmtitle";//题目名称span的id
		this.optionName="optionTitle"//选项span的name。
							//id分别为:optionATitle、optionBTitle、optionCTitle、optionDTitle
		
		this.dxCheckDiv = "dxCheckDiv";//单选按钮div
		this.duoxCheckDiv = "duoxCheckDiv";//多选按钮div
		
		this.warmTmBtn = "warmTm";//标记按钮
		this.upTmBtn = "upTm";//上一题按钮
		this.downTmBtn = "downTm";//下一题按钮
	}
	/**
	 * 获取所有题目
	 */
	studentExam.prototype.getAllTm = function(){
		var that = this;
		$.ajax({
			url:'',//HLTODO 获取所有题目
			data:that.sjid,
			type:'post',
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					
					
				}else{
					
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				layer.alert(jqXHR.responseText);
			}
		});
	}
	
	/**
	 * 选择题目
	 */
	studentExam.prototype.checkTm = function(){
		
	}
	
	/**
	 * 选择答案
	 */
	studentExam.prototype.checkAnswer = function(){
		 
	}
	
	var index = function(){
		debugger;
		var serverTime = new Date(); //假设为当前服务器时间，这里采用的是本地时间，实际使用一般是取服务端的
		var year =serverTime.getFullYear();
		var yue = serverTime.getMonth();
		var ri = serverTime.getDate();
		var shi = serverTime.getHours();
		var fen = serverTime.getMinutes();
		var miao = serverTime.getSeconds();
		
		var endTime = new Date(year,yue,ri,parseInt(shi)+3,fen,miao); //结束日期
		
		util.countdown(endTime, serverTime, function(date, serverTime, timer){
			var str = date[1] + '时' +  date[2] + '分' + date[3] + '秒';
			lay('#examTime').html(str);
			if(date[1] == 0 && date[2] == 0 && date[3] == 0){
				//TODO submit
			}
		});
	}
	
	
	exports('index',index());
});