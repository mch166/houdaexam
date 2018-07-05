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
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.AnswerDisp;
import com.eliteams.quick4j.web.service.AnswerService;

@Controller
@RequestMapping(value = "/answer")
public class AnswerController {


	
	@Resource
	private AnswerService answerService;
	
	

/**
 * 根据用户id，试卷id查询答题卷
 * @param request
 * @param page
 * @param sjid
 * @param userid
 * @return
 */
	@RequestMapping(value = "/selectList")
    @ResponseBody
	public Page selectList(HttpServletRequest request,Page<Answer> page,String sjid,String userid) {
		
		 Map<String , Object> map = new HashMap<String, Object>();      	 
         Map<String, Object> paramMap = new HashMap<String, Object>();
         List<Answer> answerList=null;
         int count =0;
         if(sjid!=null&&!"".equals(sjid)) {
             paramMap.put("sjid", Long.parseLong(sjid));
         }
         
         if(userid!=null&&!"".equals(userid)) {
             paramMap.put("userid", Long.parseLong(userid));
         }

         paramMap.put("m", (page.getPage() - 1) * page.getLimit());
         paramMap.put("n", page.getLimit());        
         map  = answerService.selectList(paramMap); 	
         answerList = (List<Answer>)map.get("list");
    	 count = (int)map.get("total");   	 
  
    	page.setData(answerList);
    	page.setCount(count);
    	page.setCode(0);       
        return page;
}

	
	
	 
    /**
     * 根据主键查
     *
     * @return
     */
    @RequestMapping(value = "/getAnswerByid")
    @ResponseBody
    public AjaxJson getAnswerByid(HttpServletRequest request,Answer answerParam) {
    	AjaxJson j = new AjaxJson();
    	Answer answer = answerService.selectById(answerParam.getId());
    	j.setSuccess(true);
    	j.setObj(answer);
    	return j;
    }
    
   
    
  //删除题目
    @RequestMapping(value = "/deleteAnswer")
    @ResponseBody
    public AjaxJson deleteAnswer( Answer answer ,HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	answerService.delete(answer.getId());
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    //删除题目
    @RequestMapping(value = "/submitAnswer")
    @ResponseBody
    public AjaxJson submitAnswer( AnswerDisp answerDisp ,HttpServletRequest request) {
        try {
        	AjaxJson j = new AjaxJson();
        	answerService.submitAnswer(answerDisp);
        	j.setSuccess(true);
        	j.setMsg("执行成功");
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
	
}
