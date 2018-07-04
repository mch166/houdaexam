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
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.SubjectService;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {

	@Resource
	private ExamService examService;
	
	@Resource
	private SubjectService subjectService;

	@RequestMapping(value = "/getExamList")
    @ResponseBody
	public Page getExamList(HttpServletRequest request,Page<Exam> page) {
		
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
        return page;
}

	 
    /**
     * 根据主键查用户 
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
        try {
        	AjaxJson j = new AjaxJson();
        	examService.insert(exam);
        	j.setSuccess(true);
        	j.setObj(exam);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @RequestMapping(value = "/deleteExam")
    @ResponseBody
    public AjaxJson deleteExam( Exam exam, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	examService.delete(exam.getId());
        	subjectService.deleteBySjid(exam.getId().toString());
        	//删除对应的题目
        	j.setSuccess(true);
        	j.setObj(exam);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    @RequestMapping(value = "/updateExam")
    @ResponseBody
    public AjaxJson updateExam( Exam exam, HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	examService.update(exam);
        	j.setSuccess(true);
        	j.setObj(exam);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	
}
