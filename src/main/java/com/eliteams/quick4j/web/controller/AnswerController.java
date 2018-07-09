package com.eliteams.quick4j.web.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.ExcelCallbackInterfaceUtil;
import com.eliteams.quick4j.core.util.ExcelExportUtil;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.AnswerDisp;
import com.eliteams.quick4j.web.service.AnswerService;

import jxl.write.WritableSheet;

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
    
    /**
     * 订单发票列表导出
     * @param request
     * @return
     */
    @RequestMapping(value = { "/exportAnswers" })
    void exportAnswers(String sjid,String userid,
                                    HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("m", 0);
        params.put("n", 5000);
        //orderStatus
        Map<String , Object> map = new HashMap<String, Object>();      	 
        List<Answer> answerList=null;
        int count =0;
        if(sjid!=null&&!"".equals(sjid)) {
        	params.put("sjid", Long.parseLong(sjid));
        }
        
        if(userid!=null&&!"".equals(userid)) {
        	params.put("userid", Long.parseLong(userid));
        }
     
        map  = answerService.selectList(params); 	
        answerList = (List<Answer>)map.get("list");
        
        final  List<Map<String, Object>> list = new ArrayList<>();
        for (Answer answer : answerList) {
            Map<String, Object> one = new HashMap<>();
            one.put("username", answer.getUsername());
            one.put("sjName", answer.getSjName());
            one.put("score",answer.getScore());
            one.put("answerTime", answer.getAnswerTime());
            one.put("submitTime", answer.getSubmitTime());
            one.put("answer", answer.getAnswer());
            list.add(one);
        }
        
        String fileName = "成绩列表" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sheetName = "成绩数据导出";

        String[] sheetHead = new String[] { "序号",  "用户姓名", "试卷名称", "成绩", "答题用时","交卷时间","答题情况"};

        final String[] dataKey = new String[] { "username", "sjName", "score", "answerTime", "submitTime" ,"answer"};

        try {
            ExcelExportUtil.exportEntity(request, response, fileName, sheetName, sheetHead,
                    new ExcelCallbackInterfaceUtil() {
                        @Override
                        public void setExcelBodyTotal(OutputStream os, WritableSheet sheet, int temp)
                                throws Exception {
                            ExcelExportUtil.setExcelBody(sheet, temp, dataKey, list);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("成绩导出失败");
        }
    }
    
    
    
    
	
}
