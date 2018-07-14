package com.eliteams.quick4j.web.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eliteams.quick4j.core.util.AjaxJson;
import com.eliteams.quick4j.core.util.ImportExcelUtil;
import com.eliteams.quick4j.core.util.Page;
import com.eliteams.quick4j.web.model.Exam;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.service.ExamService;
import com.eliteams.quick4j.web.service.SubjectService;

@Controller
@RequestMapping(value = "/subject")
public class SubjectController {

    public static Logger log=Logger.getLogger(SubjectController.class);

	
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
		try {
			
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
	    	Map resultMap = examService.selectList(paramMap);

	        Long randomsjid=7L;
	    	if(resultMap!=null&&resultMap.get("list")!=null) {
	    		List<Exam> resultList=(List<Exam>) resultMap.get("list");
//	    		//随机数
//	    		int randomNumber = (int) Math.round(Math.random()*resultList.size()-1);  
//	     		if(randomNumber==-1) {
//	     			randomNumber=0;
//	     		}
//	    		Long randomsjid=resultList.get(randomNumber).getId();
	    		
	    		for (Exam exam : resultList) {
					if("是".equals(exam.getSfky())) {
						randomsjid=exam.getId();
						break;
					}
				}
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
	    	
		} catch (Exception e) {
			e.printStackTrace();
			log.error("根据试卷id查询试卷题异常 ",e);
		}
        return page;
}

	
	@RequestMapping(value = "/selectAll")
    @ResponseBody
	public Page selectAll(HttpServletRequest request,Page<Subject> page,Subject subject) {
		
		 Map<String , Object> map = new HashMap<String, Object>();      	 
         Map<String, Object> paramMap = new HashMap<String, Object>();
         List<Subject> examList=null;
         int count =0;
         paramMap.put("m", (page.getPage() - 1) * page.getLimit());
         paramMap.put("n", page.getLimit());
         if(null != subject && null != subject.getSjid() && subject.getSjid()>0){
        	 paramMap.put("sjid", subject.getSjid());
         }
         if(null != subject && null != subject.getTmxh() && "" != subject.getTmxh() && !StringUtils.isEmpty(subject.getTmxh())){
        	 paramMap.put("tmxh", subject.getTmxh());
         }
         map  = subjectService.selectAll(paramMap);
          examList = (List<Subject>)map.get("list");
    	 count = (int)map.get("total");   	 
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
    	try {
    	Subject subject = subjectService.selectByTmxh(sjid, tmxh);
    	j.setSuccess(true);
    	j.setObj(subject);
    	}catch (Exception e) {
    		j.setSuccess(false);
        	j.setObj("获取试题失败");
    		e.printStackTrace();
    		log.error("根据试卷和题目序号查试题异常",e);
    	}

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
       
    /** 
     * 描述：通过 jquery.form.js 插件提供的ajax方式上传文件 
     * @param request 
     * @param response 
     * @throws Exception 
     */  
    @ResponseBody  
    @RequestMapping(value="importSubject",method={RequestMethod.GET,RequestMethod.POST})  
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
        
        Exam exam=new Exam();
        String sjCode=String.valueOf(listob.get(0).get(0));
        String sjName=String.valueOf(listob.get(0).get(1));
        exam.setName(sjName);
        exam.setCode(sjCode);
        examService.insertExam(exam);
        List<Subject> subjectList=new ArrayList<Subject>();
        int k =0;
        for (int i = 2; i < listob.size()-1; i++) {  
        	k++;
            List<Object> lo = listob.get(i);  
            Subject subject = new Subject();  
        	try {
        		if(lo==null||"".equals(lo)){
        			continue;
        		}
            subject.setTmtitle(String.valueOf(lo.get(0)));
            subject.setOptionA(String.valueOf(lo.get(1)));
            subject.setOptionB(String.valueOf(lo.get(2)));
            subject.setOptionC(String.valueOf(lo.get(3)));
            subject.setOptionD(String.valueOf(lo.get(4)));
            subject.setAnswer(String.valueOf(lo.get(5)));
            subject.setTmxh(k+"");
            subject.setParse(String.valueOf(lo.get(6))); 
        	}catch (Exception e) {
				e.printStackTrace();
				System.out.println(i);
			}
            if(k<51) {
            	subject.setType(Subject.TYPE_DANXUAN);
            }else if(50<k&&k<86) {
            	subject.setType(Subject.TYPE_DUOXUAN);
            }else {
            	subject.setType(Subject.TYPE_BUDINGXIANG);
            }
            subject.setSjid(exam.getId());
            subjectList.add(subject);
            subjectService.insert(subject);
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
