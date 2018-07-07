package com.eliteams.quick4j.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.ApplicationUtils;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.security.PermissionSign;
import com.eliteams.quick4j.web.security.RoleSign;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.UserService;

/**
 * 用户控制器
 * 
 * @author StarZou
 * @since 2014年5月28日 下午3:54:00
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;
    
    @Resource
	private ExamService examService;
    
    private int rows = 10;
    
    /**
     * 用户登录
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map login(@Valid User user, BindingResult result,HttpServletRequest request) {
        Map<String , Object> map = new HashMap<String, Object>();
        try {       	          
            if (result.hasErrors()) {
                map.put("code", "1");
                map.put("msg", "参数错误");
                return map;
            }
            Subject subject = SecurityUtils.getSubject();
            
            // 已登陆
            map.put("code", "0");
            map.put("msg", "登入成功");
            map.put("access_token", "c262e61cd13ad99fc650e6908c7e5e65b63d2f32185ecfed6b801ee3fbdd5c0a");
            if (subject.isAuthenticated()) {  
            }else {
            	 // 身份验证
                subject.login(new UsernamePasswordToken(user.getUsername(), ApplicationUtils.sha256Hex(user.getPassword())));                        
            }
            // 验证成功在Session中保存用户信息
             User userInfo = userService.selectByUsername(user.getUsername());
             Map<String, Object> paramMap = new HashMap<String, Object>();
             Map resultMap=examService.selectList(paramMap);
         	if(resultMap!=null&&resultMap.get("list")!=null) {
         		List<Exam> resultList=(List<Exam>) resultMap.get("list");
         		//随机数
         		int randomNumber = (int) Math.round(Math.random()*resultList.size()-1);  
         		if(randomNumber==-1) {
         			randomNumber=0;
         		}
         		Long randomsjid=resultList.get(randomNumber).getId();
             	userInfo.setSjid(randomsjid+"");

         	}
             request.getSession().setAttribute("userInfo", userInfo);
             map.put("userInfo", userInfo);
             return  map;
        } catch (AuthenticationException e) {
            // 身份验证失败
        	  map.put("code", "1");
              map.put("msg", "用户名或密码错误 ！");
            return map;
        }
        //return "student/showAnswer";
    }

    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Map logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "退出成功");
        map.put("data", null);
        return map;
    }

   
   


    /**
     *更新用户
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public AjaxJson updateUser( User user, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
            	userService.update(user);
        	j.setSuccess(true);
        	j.setObj(user);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    /**
     *删除用户
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public AjaxJson deleteUser(String json, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	json = request.getParameter("json");
        	String[] ids = json.split(",");
        	for(int i = 0; i<ids.length; i++){
        		userService.delete(Long.parseLong(ids[i]));
        	}
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    

    /**
     *插入用户
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/InsertUser")
    @ResponseBody
    public AjaxJson InsertUser( User user, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	user.setPassword(ApplicationUtils.sha256Hex("123456"));
            userService.insert(user);
        	j.setSuccess(true);
        	j.setObj(user);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据主键查用户 
     *
     * @return
     */
    @RequestMapping(value = "/getUserByid")
    @ResponseBody
    public AjaxJson getUserByid(HttpServletRequest request,User userParam) {
    	AjaxJson j = new AjaxJson();
    	User user = userService.selectById(userParam.getId());
    	j.setSuccess(true);
    	j.setObj(user);
    	return j;
    }
    
    /**
     * 重置密码
     *
     * @return
     */
    @RequestMapping(value = "/ResetPassword")
    @ResponseBody
    public AjaxJson ResetPassword(HttpServletRequest request,User userParam) {
    	AjaxJson j = new AjaxJson();
    	userParam.setPassword(ApplicationUtils.sha256Hex("123456"));
    	userService.update(userParam);
    	j.setSuccess(true);
    	j.setObj(userParam);
    	return j;
    }
    
    /**
     * 用户列表
     * @return
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public Page<User> getUserList(HttpServletRequest request,Page<User> page) {      	
        	 Map<String , Object> map = new HashMap<String, Object>();      	 
             Map<String, Object> paramMap = new HashMap<String, Object>();
             paramMap.put("m", (page.getPage() - 1) * page.getLimit());
             paramMap.put("n", page.getLimit());        
             map  = userService.selectList(paramMap);        	
             List<User> userList = (List<User>)map.get("list");
        	int count = (int)map.get("total");             
        	page.setData(userList);
        	page.setCount(count);
        	page.setCode(0);       
            return page; 
    }
    
    /**
     * 根据条件查用户列表
     * @return
     */
    @RequestMapping(value = "/getUserByOther")
    @ResponseBody
    public Page<User> getUserByOther(HttpServletRequest request,Page<User> page,String name,String username,String state,String type) {      	
        	 Map<String , Object> map = new HashMap<String, Object>();      	 
             Map<String, Object> paramMap = new HashMap<String, Object>();
             if(name!=null&&!"".equals(name.trim())) {
            	 paramMap.put("name", name);
             }
             if(username!=null&&!"".equals(username.trim())) {
            	 paramMap.put("username", username);
             }
             if(state!=null&&!"".equals(state.trim())) {
            	 paramMap.put("state", state);
             }
             if(type!=null&&!"".equals(type.trim())) {
            	 paramMap.put("type", type);
             }
             paramMap.put("m", (page.getPage() - 1) * page.getLimit());
             paramMap.put("n", page.getLimit());        
             map  = userService.selectList(paramMap);        	
             List<User> userList = (List<User>)map.get("list");
        	int count = (int)map.get("total");             
        	page.setData(userList);
        	page.setCount(count);
        	page.setCode(0);       
            return page;
     
    }

}
