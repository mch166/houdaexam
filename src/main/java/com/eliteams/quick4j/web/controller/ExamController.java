package com.eliteams.quick4j.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.SubjectService;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {
    public static Logger log=Logger.getLogger(ExamController.class);

	@Resource
	private ExamService examService;
	
	@Resource
	private SubjectService subjectService;

	/**
	 * 获取试卷列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/getExamList")
    @ResponseBody
	public Page getExamList(HttpServletRequest request,Page<Exam> page) {
		try {
		 Map<String , Object> map = new HashMap<String, Object>();      	 
         Map<String, Object> paramMap = new HashMap<String, Object>();
         paramMap.put("m", (page.getPage() - 1) * page.getLimit());
         paramMap.put("n", page.getLimit());        
         map  = examService.selectList(paramMap);        	
         List<Exam> examList = (List<Exam>)map.get("list");
    	int count = (int)map.get("total");   
    	page.setData(examList);
    	page.setCount(count);
    	page.setCode(0);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("获取试卷列表失败", e);
		}  
        return page;
}
	
	/**
	 * 获取所有试卷list
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllExam")
    @ResponseBody
	public AjaxJson getAllExam(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
        try {
		Map<String , Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
			map  = examService.selectList(paramMap);   	
        List<Exam> examList = (List<Exam>)map.get("list");
        j.setSuccess(true);
    	j.setObj(examList);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取所有试卷list失败", e);
			j.setSuccess(false);
		    j.setObj(null);
		}   
    	return j;
}
	/**
	 * 设置试卷是否可用
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setExamSfky")
    @ResponseBody
	public AjaxJson setExamSfky(HttpServletRequest request,String sjid) {
		AjaxJson j = new AjaxJson();
		examService.setExamSfky(Integer.parseInt(sjid));;
        j.setSuccess(true);
    	return j;
	}

	 
    /**
     * 根据主键查试卷 
     *
     * @return
     */
    @RequestMapping(value = "/getExamByid")
    @ResponseBody
    public AjaxJson getUserByid(HttpServletRequest request,Exam examParam) {
    	AjaxJson j = new AjaxJson();
    	Exam user = examService.selectById(examParam.getId());
    	j.setSuccess(true);
    	j.setObj(user);
    	return j;
    }
	
    @RequestMapping(value = "/InsertExam")
    @ResponseBody
    public AjaxJson InsertExam( Exam exam, HttpServletRequest request) {
    	AjaxJson j = new AjaxJson();
        try {
        	examService.insert(exam);
        	j.setSuccess(true);
        	j.setObj(exam);
        	j.setMsg("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
        	j.setMsg("执行失败");
        }
        return j;

    }
    
    @RequestMapping(value = "/deleteExam")
    @ResponseBody
    public AjaxJson deleteExam( String json, HttpServletRequest request) {
    AjaxJson j = new AjaxJson();
    	try {
        	json = request.getParameter("json");
        	String[] ids = json.split(",");
        	for(int i = 0; i<ids.length; i++){
        		examService.delete(Long.parseLong(ids[i]));
        		subjectService.deleteBySjid(ids[i].toString());
        	}
        	//删除对应的题目
        	j.setSuccess(true);
        	j.setMsg("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
        	j.setMsg("执行失败");       
        	}
        return j;

    }

    
    @RequestMapping(value = "/updateExam")
    @ResponseBody
    public AjaxJson updateExam( Exam exam, HttpServletRequest request) {
    	AjaxJson j = new AjaxJson();
        try {
        	examService.update(exam);
        	j.setSuccess(true);
        	j.setObj(exam);
        	j.setMsg("执行成功");
           
        } catch (Exception e) {
        	j.setSuccess(false);
        	j.setMsg("执行失败");
            e.printStackTrace();
        }
        return j;
    }

	
}
