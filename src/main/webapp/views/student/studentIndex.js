layui.define(['util','laydate','layer','element','form'], function(exports) {
	var $ = layui.jquery
	,setter = layui.setter
	,element = layui.element//Tab的切换功能，切换事件监听等，需要依赖element模块
	,form = layui.form
	,util = layui.util
	,laydate = layui.laydate
	,layer = layui.layer;
	
	var loginUser = layui.data(setter.userInfo)[setter.userInfo];
	
	var studentExam = function(){
		this.sjid = loginUser.sjid;//试卷id
		this.userid = loginUser.id;//userid
		this.nowTmxhBtnid = "tm_1";//记录当前选择的题目序号
		
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
		
		this.submitexam = "submitexam";
		this.warmTmBtn = "warmTm";//标记按钮
		this.upTmBtn = "upTm";//上一题按钮
		this.downTmBtn = "downTm";//下一题按钮
		
//		this.getAllTm();
		this.initEvent();
		this.initView();
	}
	/**
	 * 获取所有题目
	 */
	studentExam.prototype.getAllTm = function(){
		var that = this;
		$.ajax({
			url:'subject/selectBySjid',//HLTODO 获取所有题目
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
	 * 试图初始化
	 */
	studentExam.prototype.initView = function(){
		var that = this;
		that.tmChange(that.nowTmxhBtnid);
	}
	
	/**
	 * 事件初始化
	 */
	studentExam.prototype.initEvent = function(){
		var that = this;
		$("button[name='tm']").on('click',function(){
			var buttonid = $(this).attr("id");
			that.tmChange(buttonid);
		});
		//单选
		form.on('radio(dxxzoption)', function(data){
			that.checkAnswer("danxuan",data.value);
			console.log(data.value); //被点击的radio的value值
		})
		//多选
		form.on('checkbox(duoxxzoption)', function(data){
			that.checkAnswer("duoxuan",data.value,data.elem.checked);
			console.log(data.value); //被点击的radio的value值
		})
		//上一题
		$("#"+this.upTmBtn).on('click',function(){
			var tmid = that.nowTmxhBtnid.split("_")[1];
			var willTmBtnid = "tm_"+(parseInt(tmid)-1);
			$("#"+willTmBtnid).click();
		});
		//下一题
		$("#"+this.downTmBtn).on('click',function(){
			var tmid = that.nowTmxhBtnid.split("_")[1];
			var willTmBtnid = "tm_"+(parseInt(tmid)+1);
			$("#"+willTmBtnid).click();
		});
		//标记
		$("#"+this.warmTmBtn).on('click',function(){
			that.setTmWarm(that.nowTmxhBtnid);
		});
		//交卷
		$("#"+this.submitexam).on('click',function(){
			that.endExam();
		});
	}
	
	/**
	 * 交卷
	 */
	studentExam.prototype.endExam = function(){
		var that = this;
		var paramdata = {
				userid:123,
				sjid:that.sjid,
				answerMap:{
					'1':'B',
					'2':'c'
				}
		};
		$.ajax({
			url:'/houdaexam/rest/answer/submitAnswer',//HLTODO 获取所选题目
			type:'post',
			data:paramdata,
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
				
				}else{
					
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				layer.msg("题目获取失败");
			}
		});
	}
	/**
	 * 设置标记按钮
	 * @param tmid
	 */
	studentExam.prototype.setTmWarm = function(tmid){
		var hasblue = $('#'+tmid).hasClass('layui-btn-blue');
		var hasnormal = $('#'+tmid).hasClass('layui-btn-normal');
		var haswarm = $('#'+tmid).hasClass('layui-btn-warm');
		if(haswarm){
			$("#"+tmid).removeClass("layui-btn-warm");
			var tmAnswer = $("#"+tmid).val();
//			if(tmAnswer == "" || tmAnswer == null || tmAnswer == undefined){
//				$("#"+tmid).addClass("layui-btn-blue");
//			}else{
				$("#"+tmid).addClass("layui-btn-normal");
//			}
			$("#warmspanid").text("标记");
		}else{
			$("#"+tmid).removeClass("layui-btn-blue");
			$("#"+tmid).removeClass("layui-btn-normal");
			$("#"+tmid).addClass("layui-btn-warm");
			$("#warmspanid").text("取消标记");
		}
		
	}
	/**
	 * 单选、多选按钮显示
	 */
	studentExam.prototype.checkTypeChange = function(showDiv){
		var that = this;
		if(showDiv == that.dxCheckDiv){
			$("#"+that.dxCheckDiv).show();
			$("#"+that.duoxCheckDiv).hide();
			$('input:radio[name=dxxzoption]').attr('checked',false);
		}else{
			$("#"+that.dxCheckDiv).hide();
			$("#"+that.duoxCheckDiv).show();
			$("input[name='duoxxzoption']").each(function(){this.checked=false;}); 
		}
	}
	
	/**
	 * 更换题目
	 * @param showDiv
	 */
	studentExam.prototype.tmChange = function(buttonid){
		var that = this;
		/**
		 * 0.判断标记按钮显示：标记、取消标记
		 */
		var haswarm = $('#'+buttonid).hasClass('layui-btn-warm');
		if(haswarm){
			$("#warmspanid").text("取消标记");
		}else{
			$("#warmspanid").text("标记");
		}
		/**
		 * 1.改变按钮颜色
		 */
		//判断上一个题是否已做，颜色是否变化。
		if(that.nowTmxhBtnid != "" && that.nowTmxhBtnid != null){
			var nowTmAns = $("#"+that.nowTmxhBtnid).val();
			if(nowTmAns == "" || nowTmAns == null){
				$("#"+that.nowTmxhBtnid).addClass("layui-btn-blue");
				$("#"+that.nowTmxhBtnid).removeClass("layui-btn-normal");
			}
		}
		//当前题目的操作
	    that.nowTmxhBtnid = buttonid;
	    var tmxh = buttonid.split("_")[1];
	    
	    $("#"+buttonid).removeClass("layui-btn-blue");
		$("#"+buttonid).addClass("layui-btn-normal");
		/**
		 * 2.获取题目，改变题目显示内容
		 */
	    $.ajax({
			url:'/houdaexam/rest/subject/selectByTmxh?sjid='+1+'&tmxh='+tmxh,//HLTODO 获取所选题目
			type:'get',
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					var tmdata = returnData.obj;
					
					$("#"+that.tmxhSpan).text(tmdata.tmxh);
					var nowTmlx = "";
					if(tmdata.type=="1"){
						nowTmlx= "单选题";
						that.checkTypeChange(that.dxCheckDiv);//显示单选按钮
					}else if(tmdata.type=="2"){
						nowTmlx= "多选题";
						that.checkTypeChange(that.duoxCheckDiv);//显示多选按钮
					}else{
						nowTmlx= "不定项";
						that.checkTypeChange(that.duoxCheckDiv);//显示多选按钮
					}
					$("#"+that.tmTypeSpan).text(nowTmlx);
					$("#"+that.tmtitle).text(tmdata.tmtitle);
					$("#optionATitle").text(tmdata.optionA);
					$("#optionBTitle").text(tmdata.optionB);
					$("#optionCTitle").text(tmdata.optionC);
					$("#optionDTitle").text(tmdata.optionD);
					
					 /**
				     * 3.检查题目是否已做。选项清空还是回显
				     */
					that.setRadioOrCheckbox(buttonid);
				}else{
					$("#"+that.tmxhSpan).text("");
					$("#"+that.tmTypeSpan).text("");
					$("#"+that.tmtitle).text("该题目不存在");
					$("#optionATitle").text('');
					$("#optionBTitle").text('');
					$("#optionCTitle").text('');
					$("#optionDTitle").text('');
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				layer.msg("题目获取失败");
			}
		});
	   
	}
	
	studentExam.prototype.setRadioOrCheckbox = function(nowTmBtnid){
		var that = this;
		 var tmAnswer = $("#"+nowTmBtnid).val();
		    if(tmAnswer == "" || tmAnswer == null){
		    	$("#dxCheckDiv").html(dxCheckDivHtml);
		    	$("#duoxCheckDiv").html(duoxCheckDivHtml);
		    	form.render('checkbox', 'tmoptionform');
		    	form.render('radio', 'tmoptionform');
		    }else{
		    	$("#dxCheckDiv").html(dxCheckDivHtml);
		    	$("#duoxCheckDiv").html(duoxCheckDivHtml);
		    	var arr = tmAnswer.split("");
		    	$.each(arr,function(i,val){
		    		$("#dxxzoption"+val).attr("checked",true);
		    		$("#duoxxzoption"+val).attr("checked",true);
		    	});
		    	form.render('checkbox', 'tmoptionform');
		    	form.render('radio', 'tmoptionform');
		    }
	}
	/**
	 * 选择答案 赋值
	 */
	studentExam.prototype.checkAnswer = function(nowTmlx,xzvalue,flag){
		var that = this;
		var theEndValue = "";
		if(nowTmlx == "danxuan"){
			
		}else{
			var tmAnswer = $("#"+that.nowTmxhBtnid).val();
			var arr =[];
			if(flag){//选中的
				if(tmAnswer.indexOf(xzvalue)!=-1){
				    return;
				}
				xzvalue = xzvalue + tmAnswer;
			}else{
				if(tmAnswer.indexOf(xzvalue)!=-1){
					xzvalue = tmAnswer.replace(xzvalue,'');
				}else{
					return;
				}
			}
			
			arr = xzvalue.split("");
			arr.sort();
			$.each(arr,function(i,val){
				theEndValue = theEndValue + val;
			});
		}
		$("#"+that.nowTmxhBtnid).val(theEndValue)
	};
	
	var studentIndex = function(){
		var serverTime = new Date(); //假设为当前服务器时间，这里采用的是本地时间，实际使用一般是取服务端的
		var year =serverTime.getFullYear();
		var yue = serverTime.getMonth();
		var ri = serverTime.getDate();
		var shi = serverTime.getHours();
		var fen = serverTime.getMinutes();
		var miao = serverTime.getSeconds();
		
		var endTime = layui.data("examendtime")['endtime'];
		if(endTime == undefined || endTime == ""){
			endTime = new Date(year,yue,ri,parseInt(shi)+3,fen,miao); //结束日期
			var loginUser = layui.data("examendtime",{
				key:"endtime",
				value:endTime
			})
		}
		
		util.countdown(endTime, serverTime, function(date, serverTime, timer){
			var str = date[1] + '时' +  date[2] + '分' + date[3] + '秒';
			lay('#examTime').html(str);
			if(date[1] == 0 && date[2] == 0 && date[3] == 0){
				studentExamObj.endExam();
			}
		});
		
	}
	var studentExamObj = new studentExam();
	
	var dxCheckDivHtml = '<input type="radio" id="dxxzoptionA" name="dxxzoption" lay-filter="dxxzoption" value="A" title="A">'+
							'<input type="radio" id="dxxzoptionB" name="dxxzoption" lay-filter="dxxzoption" value="B" title="B">'+
							'<input type="radio" id="dxxzoptionC" name="dxxzoption" lay-filter="dxxzoption" value="C" title="C">'+
							'<input type="radio" id="dxxzoptionD" name="dxxzoption" lay-filter="dxxzoption" value="D" title="D">';
	
	var duoxCheckDivHtml = '<input type="checkbox" id="duoxxzoptionA" name="duoxxzoption" lay-filter="duoxxzoption" value="A" title="A">'+
							'<input type="checkbox" id="duoxxzoptionB" name="duoxxzoption" lay-filter="duoxxzoption" value="B" title="B">'+
							'<input type="checkbox" id="duoxxzoptionC" name="duoxxzoption" lay-filter="duoxxzoption" value="C" title="C">'+
							'<input type="checkbox" id="duoxxzoptionD" name="duoxxzoption" lay-filter="duoxxzoption" value="D" title="D">';
	
	exports('studentIndex',studentIndex());
});