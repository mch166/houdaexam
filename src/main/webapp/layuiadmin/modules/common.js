/** layuiAdmin.std-v1.0.0 LPPL License By http://www.layui.com/admin/ */
 layui.define(function(e){
	 var i=(layui.$,layui.layer,layui.laytpl,layui.setter,layui.view,layui.admin);
	 i.events.logout=function(){
		 i.req({
			 //HLTODO 退出的接口
			 url:"/houdaexam/rest/user/logout",
			 type:"get",
			 data:{},
			 done:function(e){
				 i.exit(function(){
					 location.href="../login.html"
				})
			}
		})
	};
e("common",{})
});