package com.eliteams.quick4j.web.controller;

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

import com.eliteams.quick4j.core.util.ApplicationUtils;
import com.eliteams.quick4j.web.model.User;
import com.eliteams.quick4j.web.security.PermissionSign;
import com.eliteams.quick4j.web.security.RoleSign;
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
    
    public final int rows=20;

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
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    /**
     * 基于角色 标识的权限控制案例
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
    @RequiresRoles(value = RoleSign.ADMIN)
    public String admin() {
        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = PermissionSign.USER_CREATE)
    public String create() {
        return "拥有user:create权限,能访问";
    }
    
    /**
     * 新增用户
     * 
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/insertUser")
    @ResponseBody
    public Map<String,Object> insertUser(@Valid User user,HttpServletRequest request) {
        try {

            Map<String , Object> map = new HashMap<String, Object>();
        	userService.insert(user);
        	 Map<String, Object> paramMap = new HashMap<String, Object>();
             //paramMap.put("m", (page - 1) * rows);
             paramMap.put("m", 0);
             paramMap.put("n", rows);
             map  = userService.selectList(paramMap);
             //request.getSession().setAttribute("userInfoList", selectList);
            return map;
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
    public Map<String,Object> updateUser(@Valid User user, HttpServletRequest request) {
        try {

            Map<String , Object> map = new HashMap<String, Object>();
        	userService.update(user);
        	 Map<String, Object> paramMap = new HashMap<String, Object>();
             //paramMap.put("m", (page - 1) * rows);
             paramMap.put("m", 0);
             paramMap.put("n", rows);
             map  = userService.selectList(paramMap);
             //request.getSession().setAttribute("userInfoList", selectList);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/selectUser")
    @ResponseBody
    public Map<String,Object> selectUser(HttpServletRequest request,int page) {
        try {
            Map<String , Object> map = new HashMap<String, Object>();
            Map<String, Object> paramMap = new HashMap<String, Object>();
            //paramMap.put("m", (page - 1) * rows);
            paramMap.put("m", (page - 1) * rows);
            paramMap.put("n", rows);
            map  = userService.selectList(paramMap);
            //request.getSession().setAttribute("userInfoList", selectList);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
