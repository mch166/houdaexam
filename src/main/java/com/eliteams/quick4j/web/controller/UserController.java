package com.eliteams.quick4j.web.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.ApplicationUtils;
import com.eliteams.quick4j.core.util.ImportExcelUtil;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.core.util.RedisUtil;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.security.PermissionSign;
import com.eliteams.quick4j.web.security.RoleSign;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.UserService;

import redis.clients.jedis.Jedis;

/**
 * 用户控制器
 * 
 * @author StarZou
 * @since 2014年5月28日 下午3:54:00
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    public static Logger log=Logger.getLogger(UserController.class);

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
		     RedisUtil redisUtil = new RedisUtil();
            Jedis jedis = redisUtil.getJedis();
            Boolean exists = jedis.exists("user_"+user.getUsername());
            if(exists){
            	  map.put("code", "1");
                  map.put("msg", "退出后30分钟内不允许登录，请稍后再试！");
                  return map;
            }          
            Subject subject = SecurityUtils.getSubject();                     
            if (subject.isAuthenticated()) {  
            }else {
            	 // 身份验证
            	System.out.println(ApplicationUtils.sha256Hex(user.getPassword()));
                subject.login(new UsernamePasswordToken(user.getUsername(), ApplicationUtils.sha256Hex(user.getPassword())));                        
            }
            // 验证成功在Session中保存用户信息
             User userInfo = userService.selectByUsername(user.getUsername());
             Map<String, Object> paramMap = new HashMap<String, Object>();
             Map resultMap=examService.selectList(paramMap);
             Long randomsjid=7L;
         	if(resultMap!=null&&resultMap.get("list")!=null) {
         		List<Exam> resultList=(List<Exam>) resultMap.get("list");
//         		//随机数
//         		int randomNumber = (int) Math.round(Math.random()*resultList.size()-1);  
//         		if(randomNumber==-1) {
//         			randomNumber=0;
//         		}
//             	userInfo.setSjid(randomsjid+"");
         		for (Exam exam : resultList) {
					if("是".equals(exam.getSfky())) {
						randomsjid=exam.getId();
						break;
					}
				}

         	}
         	// 已登陆
            map.put("code", "0");
            map.put("msg", "登入成功");
         	userInfo.setSjid(randomsjid+"");       	
             request.getSession().setAttribute("userInfo", userInfo);
             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
     	    Date d=new Date();   
     	    log.info("用户登录user:"+user.getUsername()+";时间:"+df.format(d)); 
             request.getSession().setAttribute("loginTime", df.format(d));
             map.put("userInfo", userInfo);
             return  map;
        } catch (AuthenticationException e) {
            // 身份验证失败
        	  map.put("code", "1");
              map.put("msg", "用户名或密码错误 ！");
              log.error("用户验证失败,username:"+user.getUsername()+"密码:"+user.getPassword(), e);
        }catch (Exception e) {
			e.printStackTrace();
			  map.put("code", "1");
              map.put("msg", "登录失败！");
            log.error("用户登录失败,username:"+user.getUsername()+"密码:"+user.getPassword(), e);
		}
        return map;
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
    	User user = (User) session.getAttribute("userInfo");
		if(user!=null&&user.getType()!=1) {
		     RedisUtil redisUtil = new RedisUtil();
		        Jedis jedis = redisUtil.getJedis();
		        jedis.setex("user_"+user.getUsername(), 30*60, "user_"+user.getUsername());
		}
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
     * 重置用户登录状态
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/reLogin")
    @ResponseBody
    public AjaxJson reLogin(HttpSession session,String username) {
    	 try {
         	AjaxJson j = new AjaxJson();
         	 	RedisUtil redisUtil = new RedisUtil();
		        Jedis jedis = redisUtil.getJedis();
		        jedis.del("user_"+username);
         	j.setSuccess(true);
         	j.setMsg("执行成功");
             return j;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
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
    	try {
    	userParam.setPassword(ApplicationUtils.sha256Hex("123456"));
    	userService.update(userParam);
    	j.setSuccess(true);
    	j.setObj(userParam);
    	}catch (Exception e) {
    		e.printStackTrace();
    		j.setSuccess(false);
        	j.setObj("重置失败");
    		log.error("重置密码失败，"+userParam.toString(), e);
		}
    	return j;
    }
    
    /**
     * 用户更新密码
     *
     * @return
     */
    @RequestMapping(value = "/updatePwd4user")
    @ResponseBody
    public AjaxJson updatePwd4user(HttpServletRequest request,String username,String oldpwd,String newpwd) {
    	AjaxJson j = new AjaxJson();
    	try {
    	if(username==null||"".equals(username)
    			||oldpwd==null||"".equals(oldpwd)
    			 	||newpwd==null||"".equals(newpwd)) {
    		j.setSuccess(false);
    		j.setMsg("参数有误！");
    		return j;
    	}
    	User user =new User();
    	user.setUsername(username);
    	user.setPassword(ApplicationUtils.sha256Hex(oldpwd));
    	User authentication = userService.authentication(user);
    	if(authentication==null) {
    		j.setSuccess(false);
    		j.setMsg("原密码不正确！");
    		return j;
    	}
    	user.setPassword(ApplicationUtils.sha256Hex(newpwd));
    	userService.updatePwd(user);
    	j.setSuccess(true);
    	j.setMsg("修改成功");
    	}catch (Exception e) {
    		e.printStackTrace();
    		j.setSuccess(false);
        	j.setMsg("修改失败");
    		log.error("用户更新密码失败，username="+username+";oldpwd="+oldpwd+";newpwd="+newpwd, e);
    	}
    	return j;
    }
    
    /**
     * 用户列表
     * @return
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public Page<User> getUserList(HttpServletRequest request,Page<User> page,User user) {      	
        try {
        	 Map<String , Object> map = new HashMap<String, Object>();      	 
             Map<String, Object> paramMap = new HashMap<String, Object>();
             paramMap.put("m", (page.getPage() - 1) * page.getLimit());
             paramMap.put("n", page.getLimit());
             if(null!=user && null != user.getName() && !"".equals(user.getName())){
            	 paramMap.put("name", user.getName());
             }

				map  = userService.selectList(paramMap);
       	
             List<User> userList = (List<User>)map.get("list");
        	int count = (int)map.get("total");             
        	page.setData(userList);
        	page.setCount(count);
        	page.setCode(0);    
		} catch (Exception e) {
			page.setData(null);
        	page.setCount(0);
        	page.setCode(1);    
			e.printStackTrace();
			log.error("查看用户列表失败", e);
		} 
            return page; 
    }
    
    /**
     * 根据条件查用户列表
     * @return
     */
    @RequestMapping(value = "/getUserByOther")
    @ResponseBody
    public Page<User> getUserByOther(HttpServletRequest request,Page<User> page,String name,String username,String state,String type,String phone) {      	
        try {	
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
             if(phone!=null&&!"".equals(phone.trim())) {
            	 paramMap.put("phone", phone);
             }
             paramMap.put("m", (page.getPage() - 1) * page.getLimit());
             paramMap.put("n", page.getLimit());        
				map  = userService.selectList(paramMap);
  	
             List<User> userList = (List<User>)map.get("list");
        	int count = (int)map.get("total");             
        	page.setData(userList);
        	page.setCount(count);
        	page.setCode(0);   
			} catch (Exception e) {
				log.error("根据条件查用户列表失败", e);
				e.printStackTrace();
				page.setData(null);
	        	page.setCount(0);
	        	page.setCode(1);  
			}      
            return page;
     
    }
    
    /**
     * 更改密码
     * @return
     */
    @RequestMapping(value = "/updateUserPassword")
    @ResponseBody
    public AjaxJson updateUserPassword(HttpServletRequest request,User userParam) {      	
    	AjaxJson ajaxJson=new AjaxJson();
    	User user = userService.selectById(userParam.getId());
    	if(user.getPassword().equals(ApplicationUtils.sha256Hex(userParam.getNewpassword()))){
    		int result = userService.updatePwd(userParam);
    		if(result > 0){
    			ajaxJson.setSuccess(true);
    		}else{
    			ajaxJson.setSuccess(false);
        		ajaxJson.setMsg("密码修改失败");
    		}
    	}else{
    		ajaxJson.setSuccess(false);
    		ajaxJson.setMsg("原密码不正确");
    	}
    	return ajaxJson;
    }

    /** 
     * 描述：通过 jquery.form.js 插件提供的ajax方式上传文件 
     * @param request 
     * @param response 
     * @throws Exception 
     */  
    @ResponseBody  
    @RequestMapping(value="/importUser",method={RequestMethod.GET,RequestMethod.POST})  
    public  AjaxJson  importSubject(HttpServletRequest request,HttpServletResponse response) throws Exception {  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
          
        System.out.println("通过 jquery.form.js 提供的ajax方式上传文件！");  
        AjaxJson ajaxJson=new AjaxJson();
        InputStream in =null;  
        List<List<Object>> listob = null;  
        MultipartFile file = multipartRequest.getFile("file");  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
          
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());  
        
        List<User> userList=new ArrayList<User>();
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            User user = new User();  
            user.setUsername(String.valueOf(lo.get(0)));
            user.setName(String.valueOf(lo.get(1)));
            user.setPhone(String.valueOf(lo.get(2)));
            user.setPassword(ApplicationUtils.sha256Hex("123456"));
            user.setType(0);
            user.setState("0");
            user.setCreateTime("2018");
            userService.insert(user);
            userList.add(user);
        }  
          
//        PrintWriter out = null;  
        response.setCharacterEncoding("utf-8");  //防止ajax接受到的中文信息乱码  
//        out = response.getWriter();  
//        out.print("文件导入成功！");  
//        out.flush();  
//        out.close();  
        ajaxJson.setSuccess(true);
    	//j.setObj(subject);
    	return ajaxJson;
    }  
  
  

}
