layui.define(["form"],function(exports){
	 var $ = layui.$
	 ,layer = layui.layer
	 ,setter = layui.setter
	 ,form = layui.form;
	 
	form.verify({
		pass:[/^[\S]{6,12}$/,"密码必须6到12位，且不能出现空格"]
		,repass:function(repass){
			if(repass!==$("#LAY_password").val())
			return"两次密码输入不一致";
			}
	});
	 
	  //监听提交
	form.on('submit(setmypass)', function(data){
		var loginUser = layui.data(setter.userInfo)[setter.userInfo];
		data.field.id = loginUser.id;
		$.ajax({
			url:'/houdaexam/rest/user/updateUserPassword',//HLTODO 密码修改
			data:data.field,
			type:'post',
			dataType : "json",
			success:function(returnData){
				if(returnData.success){
					if(loginUser.type == 0){
						layer.closeAll();
					}else{
						location.href = '../../../login.html';
					}
				}else{
					layer.msg(returnData.msg);
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				layer.msg("密码修改失败");
			}
		});
	});
	
	exports("password",{});
})