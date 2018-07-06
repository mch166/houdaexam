package com.eliteams.quick4j.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.SubjectService;

@Controller
@RequestMapping(value = "/subject")
public class SubjectController {

	@Resource
	private SubjectService subjectService;
	
	@Resource
	private ExamService examService;

	/**
	 * 根据试卷id得到 试卷题 
	 * 若试卷id为空，随机一个试卷id
	 * 使用场景：①后台管理维护界面，由试卷列表点击进入
	 * 					  ②考生进入答题界面（刚进入时，未分配试卷，试卷id为空）
	 * 					  ③考生进入答题界面（进入后，已分配试卷，分页请求时）
	 * @param request
	 * @param page
	 * @param sjid
	 * @return
	 */
	@RequestMapping(value = "/selectBySjid")
    @ResponseBody
	public Page selectBySjid(HttpServletRequest request,Page<Subject> page,String sjid) {
		
		 Map<String , Object> map = new HashMap<String, Object>();      	 
         Map<String, Object> paramMap = new HashMap<String, Object>();
         List<Subject> examList=null;
         int count =0;
         if(sjid!=null&&!"".equals(sjid)) {
         paramMap.put("sjid", Long.parseLong(sjid));
         paramMap.put("m", (page.getPage() - 1) * page.getLimit());
         paramMap.put("n", page.getLimit());        
         map  = subjectService.selectBySjid(paramMap);     	
          examList = (List<Subject>)map.get("list");
    	 count = (int)map.get("total");   	 
    }else {
    	Map resultMap=examService.selectList(paramMap);
    	if(resultMap!=null&&resultMap.get("list")!=null) {
    		List<Exam> resultList=(List<Exam>) resultMap.get("list");
    		//随机数
    		int randomNumber = (int) Math.round(Math.random()*resultList.size()-1);  
    		Long randomsjid=resultList.get(randomNumber).getId();
    		paramMap.put("sjid", randomsjid);
            paramMap.put("m", (page.getPage() - 1) * page.getLimit());
            paramMap.put("n", page.getLimit());        
            map  = subjectService.selectBySjid(paramMap);     	
             examList = (List<Subject>)map.get("list");
             count = (int)map.get("total");  
    	}
    }
    	page.setData(examList);
    	page.setCount(count);
    	page.setCode(0);       
        return page;
}

	
	
	 
    /**
     * 根据主键查试题 
     *
     * @return
     */
    @RequestMapping(value = "/getSubjectByid")
    @ResponseBody
    public AjaxJson getSubjectByid(HttpServletRequest request,Subject subjectParam) {
    	AjaxJson j = new AjaxJson();
    	Subject subject = subjectService.selectById(subjectParam.getId());
    	j.setSuccess(true);
    	j.setObj(subject);
    	return j;
    }
    
    /**
     * 根据试卷和题目序号查试题 
     *
     * @return
     */
    @RequestMapping(value = "/selectByTmxh")
    @ResponseBody
    public AjaxJson selectByTmxh(HttpServletRequest request,Long sjid,String tmxh) {
    	AjaxJson j = new AjaxJson();
    	Subject subject = subjectService.selectByTmxh(sjid, tmxh);
    	j.setSuccess(true);
    	j.setObj(subject);
    	return j;
    }
	
    //插入题目
    @RequestMapping(value = "/InsertSubject")
    @ResponseBody
    public AjaxJson InsertSubject( Subject subject, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	subjectService.insert(subject);
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
  //更新题目
    @RequestMapping(value = "/updateSubject")
    @ResponseBody
    public AjaxJson updateSubject( Subject subject, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	subjectService.update(subject);
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
  //删除题目
    @RequestMapping(value = "/deleteSubject")
    @ResponseBody
    public AjaxJson deleteSubject( String json, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	json = request.getParameter("json");
        	String[] ids = json.split(",");
        	for(int i = 0; i<ids.length; i++){
        		subjectService.delete(Long.parseLong(ids[i]));
        	}
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
	
}
