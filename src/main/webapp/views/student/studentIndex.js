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
		this.viewmyexam = "viewmyexam";
		this.warmTmBtn = "warmTm";//标记按钮
		this.upTmBtn = "upTm";//上一题按钮
		this.downTmBtn = "downTm";//下一题按钮
		
		this.timer = "";
		this.answerMap = layui.sessionData("answerMap"+loginUser.id).answerMap;//保存学生最后做题情况，展示结果用
		if(this.answerMap == undefined || typeof(this.answerMap) == "undefined"){
			this.answerMap = {};
		}
		this.examisend = null;
		
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
//		that.examisend = layui.sessionData("examisend").examisend;
//		if(that.examisend || that.examisend == "true"){
//			clearTimeout(that.timer);
//			$("#submitexam").hide();
//		}
		//刷新操作，从缓存中读取做题情况并重新赋值
		var pagetmbtnid = "";
		$.each(this.answerMap, function(i, val) {
			pagetmbtnid = "tm_"+i;
			$("#"+pagetmbtnid).val(val);
			if(val != null && val!=""){
				$("#"+pagetmbtnid).addClass("layui-btn-normal");
				$("#"+pagetmbtnid).removeClass("layui-btn-blue");
			}
		});
		
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
			layer.open({
		        type: 1
		        ,title: false //不显示标题栏
		        ,closeBtn: false
		        ,area: '300px;'
		        ,shade: 0.3
		        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		        ,btn: ['确定', '取消']
		        ,btnAlign: 'c'
		        ,moveType: 1 //拖拽模式，0或者1
		        ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">是否确认提交试卷<br>试卷提交后将自动退出系统</div>'
		        ,yes: function(index){
		        	that.endExam();
		        	layer.closeAll();
		        }
		      });
		});
		//查看答卷情况
		$("#"+this.viewmyexam).on('click',function(){
			that.studentViewExam();
		});
		
		//修改密码
		$("#resetpassword").on('click',function(){
			layer.open({
		        type: 2
		        ,title: '修改密码' //不显示标题栏
		        ,area: ['550px','370px']
		        ,shade: 0.3
		        ,id: 'layer_resetpassword' //设定一个id，防止重复弹出
		        ,content: '../set/user/password.html'
		      });
		});
	}
	
	/**
	 * 交卷
	 */
	studentExam.prototype.endExam = function(){
		var that = this;
		var paramdata = {};
		paramdata.userid = that.userid;
		paramdata.sjid = that.sjid;
		
		var serverTime = new Date(); //假设为当前服务器时间，这里采用的是本地时间，实际使用一般是取服务端的
		var year =serverTime.getFullYear();
		var yue = parseInt(serverTime.getMonth())+1;
		var ri = serverTime.getDate();
		var shi = serverTime.getHours();
		var fen = serverTime.getMinutes();
		var miao = serverTime.getSeconds();
		
		var startTime = layui.sessionData("startServerTime").startServerTime;
		
		var date3=serverTime.getTime()-Date.parse(layui.sessionData("startServerTime").startServerTime);
		var leave1=date3%(24*3600*1000);    //计算天数后剩余的毫秒数
		var hours=Math.floor(leave1/(3600*1000));
		
		var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
		var minutes=Math.floor(leave2/(60*1000));
		
		var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
		var seconds=Math.round(leave3/1000);
		
		paramdata.answerTime = hours+":"+minutes+":"+seconds;
		paramdata.submitTime = year+"-"+yue+"-"+ri+" "+shi+":"+fen+":"+miao;
		var blTmxh = "";
		var blTmval = "";
		var answers = {};
		$("button[name=tm]").each(function(i){
			blTmxh = $(this).attr("id").split("_")[1];
			blTmval = $(this).val();
			answers[blTmxh] = blTmval;
		});
		paramdata.answerMap = answers;
		layui.sessionData("answerMap"+loginUser.id,{
			key:'answerMap',
			value:answers
		});
//		that.answerMap = answers;
		$.ajax({
			url:'/houdaexam/rest/answer/submitAnswer',
			type:'post',
			data:paramdata,
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					clearTimeout(that.timer)
					layer.msg("试卷提交成功");
					that.studentViewExam("end");
//					that.studentExamTheEnd();
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				that.submitAnswerAgain();
			}
		});
	}
	
	/**
	 * 第一次提交失败后
	 * 再次提交（无时间）
	 */
	studentExam.prototype.submitAnswerAgain = function(){
		var that = this;
		var paramdata = {};
		paramdata.userid = that.userid;
		paramdata.sjid = that.sjid;
		var blTmxh = "";
		var blTmval = "";
		var answers = {};
		$("button[name=tm]").each(function(i){
			blTmxh = $(this).attr("id").split("_")[1];
			blTmval = $(this).val();
			answers[blTmxh] = blTmval;
		});
		paramdata.answerMap = answers;
		$.ajax({
			url:'/houdaexam/rest/answer/submitAnswer',
			type:'post',
			data:paramdata,
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					clearTimeout(that.timer)
					layer.msg("试卷提交成功");
					that.studentViewExam("end");
//					that.studentExamTheEnd();
					
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				layer.msg("试卷提交失败:textStatus:"+textStatus);
			}
		});
	}
	/**
	 * 试卷点击提交后提交成功后
	 * 删除各个缓存、退出
	 */
	studentExam.prototype.studentExamTheEnd = function(){
		layui.sessionData("answerMap"+loginUser.id,null);//答题结果
		layui.sessionData("examisend"+loginUser.id,null);//试卷提交成功标志
		layui.sessionData("startServerTime"+loginUser.id,null);//试卷开始时间
		layui.sessionData("examendtime"+loginUser.id,null);//试卷结束时间
		$("#logoutBtnid").click();
	}
	/**
	 * 交卷后查看卷子做题情况
	 */
	studentExam.prototype.studentViewExam = function(isEnd){
		var that = this;
		
		var blTmxh = "";
		var blTmval = "";
		var answers = {};
		$("button[name=tm]").each(function(i){
			blTmxh = $(this).attr("id").split("_")[1];
			blTmval = $(this).val();
			answers[blTmxh] = blTmval;
		});
		var viewanswerMap = answers;
		
		layer.open({
	        type: 1
	        ,id: 'layerDemoStudentendview' //防止重复弹出
	        ,area : ['1010px', '500px']
	        ,content: '<div id="studentendviewanswer"></div>'
	        ,btn: ['确定']
	        ,shade: 0.3 //不显示遮罩
	        ,yes: function(index,layero){
	        	layer.close(index);
	        }
			,success:function(){
				var strHtml = '<button class="layui-btn layui-btn-disabled" style="width:90px;color: black;"><span>{tmxh}</span>:<span>{tmanswer}</span></button>';
				var endStrHtml = "";
				$.each(viewanswerMap, function(i, val) {
					endStrHtml += strHtml.replace(/\{tmxh\}/g,i)
										.replace(/\{tmanswer\}/g,val);
					if(parseInt(i)%10 == 0){
						endStrHtml += '<br/>';
					}
				});
				$("#studentendviewanswer").html(endStrHtml);
			}
			,end:function(){
				if(isEnd=="end"){
	        		that.studentExamTheEnd();
	        	}
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
			$("#"+tmid).addClass("layui-btn-normal");
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
			}else{
				//缓存学生做题记录
				var answers = layui.sessionData("answerMap"+loginUser.id).answerMap;
				var xsDtTmxh = that.nowTmxhBtnid.split("_")[1];//学生答题题目序号
				if(answers=="undefined" || typeof(answers)=="undefined"){
					answers = {};
				}
				answers[xsDtTmxh] = nowTmAns;
				layui.sessionData("answerMap"+loginUser.id,{
					key:'answerMap',
					value:answers
				});
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
			url:'/houdaexam/rest/subject/selectByTmxh?sjid='+that.sjid+'&tmxh='+tmxh,//HLTODO 获取所选题目
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
			theEndValue = xzvalue;
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
		var startTime = layui.sessionData("startServerTime"+loginUser.id).startServerTime;
		if(startTime == undefined || startTime == ""){
			layui.sessionData("startServerTime"+loginUser.id,{
				key:'startServerTime',
				value:serverTime
			});
		}
		
		var endTime = layui.sessionData("examendtime"+loginUser.id)['endtime'];
		if(endTime == undefined || endTime == ""){
			endTime = new Date(year,yue,ri,parseInt(shi)+3,fen,miao); //结束日期
			layui.sessionData("examendtime"+loginUser.id,{
				key:"endtime",
				value:endTime
			})
		}
		var timeStartFlag = true;
		util.countdown(endTime, serverTime, function(date, serverTime, timer){
			var str = date[1] + '时' +  date[2] + '分' + date[3] + '秒';
			if((date[2]=="0" || date[2]=="15" || date[2]=="30" || date[2]=="45") && date[3]=="0"  && !timeStartFlag){
				fifAutoSubmit();
			}
			studentExamObj.timer = timer;
			lay('#examTime').html(str);
			if(date[1] == 0 && date[2] == 0 && date[3] == 0 && !timeStartFlag){
				clearTimeout(studentExamObj.timer);
				layer.open({
			        type: 1
			        ,offset: 'rt' 
			        ,title: false
			        ,id: 'layerDemoTimeOver' //防止重复弹出
			        ,content: '<div style="padding: 20px 100px;">答卷时间已到，系统已自动交卷。 </div>'
			        ,btn: '确定'
			        ,btnAlign: 'c' //按钮居中
			        ,shade: 0.3 
			        ,yes: function(index, layero){
			        	layer.close(index);
			        	studentExamObj.endExam();
			        }
			      });
			}
			timeStartFlag = false;
		});
	}
	/**
	 * 15分钟自动提交一次
	 */
	var fifAutoSubmit = function(){
		var that = studentExamObj;
		var paramdata = {};
		paramdata.userid = that.userid;
		paramdata.sjid = that.sjid;
		var blTmxh = "";
		var blTmval = "";
		var answers = {};
		$("button[name=tm]").each(function(i){
			blTmxh = $(this).attr("id").split("_")[1];
			blTmval = $(this).val();
			answers[blTmxh] = blTmval;
		});
		paramdata.answerMap = answers;
		$.ajax({
			url:'/houdaexam/rest/answer/submitAnswer',
			type:'post',
			data:paramdata,
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					console.log("试卷自动提交成功");
				}
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
	
//	var examisend = layui.sessionData("examisend"+loginUser.id).examisend;
//	if(studentExamObj.examisend || studentExamObj.examisend == "true"){
//		clearTimeout(studentExamObj.timer);
//		$("#submitexam").hide();
//	}else{
//		studentIndex();
//	}
	studentIndex();
	exports('studentIndex',{});
});