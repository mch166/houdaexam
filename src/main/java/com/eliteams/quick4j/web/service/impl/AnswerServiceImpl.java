package com.eliteams.quick4j.web.service.impl;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.core.generic.GenericServiceImpl;
import com.eliteams.quick4j.web.dao.AnswerMapper;
import com.eliteams.quick4j.web.dao.SubjectMapper;
import com.eliteams.quick4j.web.model.Answer;
import com.eliteams.quick4j.web.model.AnswerDisp;
import com.eliteams.quick4j.web.model.Subject;
import com.eliteams.quick4j.web.service.AnswerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 答题实现类
 */
@Service
public class AnswerServiceImpl extends GenericServiceImpl<Answer, Long> implements AnswerService {

	@Resource
	private AnswerMapper answerMapper;
	
	 @Resource
	    private SubjectMapper subjectMapper;
	
	@Override
	public GenericDao<Answer, Long> getDao() {
		// TODO Auto-generated method stub
		return answerMapper;
	}

	@Override
	public Map selectList(Map paramMap) {
		Map<String, Object> retMap = new HashMap<String, Object>();
        List<Answer> result = answerMapper.selectByOther(paramMap);
    	int rowCnts = answerMapper.getRowCnts();
        retMap.put("total", rowCnts);
        retMap.put("list", result);
        return retMap;
	}

	@Override
	public Answer selectById(Long id) {
		// TODO Auto-generated method stub
		return answerMapper.selectByPrimaryKey(id);
	}
	
	
	@Override
	public String  submitAnswer(AnswerDisp answerDisp) {
		String score = marking(answerDisp);
		Map answerMap = answerDisp.getAnswerMap();		
		 Type mapType = new TypeToken<Map>() {}.getType();		
		String answerJson=new Gson().toJson(answerMap,mapType);
		Answer answer=new Answer();
		answer.setAnswer(answerJson);
		answer.setAnswerTime(answerDisp.getAnswerTime());
		answer.setScore(score);
		answer.setSjid(answerDisp.getSjid());
		answer.setSubmitTime(answerDisp.getSubmitTime());
		answer.setUserid(answerDisp.getUserid());
		//查询学员在一定时间内是否有提交过答题
		Map paramMap=new HashMap();
		paramMap.put("sjid", answerDisp.getSjid());
		paramMap.put("userid", answerDisp.getUserid());
		paramMap.put("m", "0");
		paramMap.put("n", "1");
		List<Answer> resultList= answerMapper.selectByOther(paramMap);
		if(resultList==null||compareSubmitTime(resultList.get(0).getSubmitTime())) {
			answerMapper.insert(answer);
		}else {
			answer.setId(resultList.get(0).getId());
			answerMapper.updateScore(answer);
		}		
		return score;
	}
	
	@Override
	public void  ReSubmitAnswer(String sjid) {
		
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sjid", Long.parseLong(sjid));
        paramMap.put("m", 0);
        paramMap.put("n",1000);        
        Map<String, Object>  map  = selectList(paramMap); 	
        List<Answer> answerList = (List<Answer>)map.get("list");	
        for (Answer answer : answerList) {
		String score = marking(answer);
		answer.setScore(score);
		answerMapper.updateScore(answer);	
	}
	}
	
	
   
	
	  @Override
	    public int updateScore(Answer answer) {
	        return answerMapper.updateScore(answer);
	    }

	    @Override
	    public int delete(Long id) {
	        return answerMapper.deleteByPrimaryKey(id);
	    }
	    
	    /**
	     * 打分
	     * @param answerDisp
	     * @return
	     */
	    public String marking(AnswerDisp answerDisp){
	    	String sjid = answerDisp.getSjid();
	    	Map<String,String> answerMap = answerDisp.getAnswerMap();
	    	Map params=new HashMap();
	    	params.put("sjid", sjid);
	    	List<Subject> subjectList = subjectMapper.selectBySjid(params);
	    	int score=0;
	    	for (Subject subject : subjectList) {
	    		String tmxh = subject.getTmxh();
	    		String type = subject.getType();
	    		String answer = subject.getAnswer();
	    		String userAnswer = answerMap.get(tmxh);
	    		//TODO 计算规则待定
    		if(answer!=null&&!"".equals(answer)&&userAnswer!=null&&!"".equals(userAnswer)) {
    			
				if(answer.equals(userAnswer)) {
    			if(Subject.TYPE_DANXUAN.equals(type)) {//单选
    						score=score+Subject.VALUE_DANXUAN;    					 		
	    		}else if (Subject.TYPE_DUOXUAN.equals(type)) {	//多选
						score=score+Subject.VALUE_DUOXUAN;    		
	    		}else if (Subject.TYPE_BUDINGXIANG.equals(type)) {//不定项
						score=score+Subject.VALUE_BUDINGXIANG;					
	    		}
	    	}
    	}
	  }
	    	return score+"";
 }
	    
	    /**
	     * 打分
	     * @param answerDisp
	     * @return
	     */
	    public String marking(Answer answer){
	    	String sjid = answer.getSjid();
	    	String answerjson = answer.getAnswer();
	    	Map params=new HashMap();
			 Type mapType = new TypeToken<Map>() {}.getType();
	    	Map<String,String> answerMap=new Gson().fromJson(answerjson,mapType);
	    	params.put("sjid", sjid);
	    	List<Subject> subjectList = subjectMapper.selectBySjid(params);
	    	int score=0;
	    	for (Subject subject : subjectList) {
	    		String tmxh = subject.getTmxh();
	    		String type = subject.getType();
	    		String rightAnswer = subject.getAnswer();
	    		String userAnswer = answerMap.get(tmxh);
	    		//TODO 计算规则待定
    		if(rightAnswer!=null&&!"".equals(rightAnswer)&&userAnswer!=null&&!"".equals(userAnswer)) {
    			
				if(rightAnswer.equals(userAnswer)) {
    			if(Subject.TYPE_DANXUAN.equals(type)) {//单选
    						score=score+Subject.VALUE_DANXUAN;    					 		
	    		}else if (Subject.TYPE_DUOXUAN.equals(type)) {	//多选
						score=score+Subject.VALUE_DUOXUAN;    		
	    		}else if (Subject.TYPE_BUDINGXIANG.equals(type)) {//不定项
						score=score+Subject.VALUE_BUDINGXIANG;					
	    		}
	    	}
    	}
	  }
	    	return score+"";
 }

	    public static void main(String[] args) {
			Answer answer=new Answer();
			Map map=new HashMap();
			map.put("1", "A");
			map.put("2", "B");
			map.put("3", "BD");
			String jsonMap="";
			 Type mapType = new TypeToken<Map>() {}.getType();
		       Type answerType=new TypeToken<Answer>() {}.getType();

		        try {
		        	 jsonMap = new Gson().toJson(map, mapType);
		        } catch (Exception e) {
		        	e.printStackTrace();
		            throw new RuntimeException(e);
		        }
			answer.setAnswer(jsonMap);
			answer.setAnswerTime("2018-07-05 22:00:00");
			answer.setSjid("1");
			answer.setScore("80");
			answer.setUserid("2");
			String answerJson = new Gson().toJson(answer,answerType);
			System.out.println("======="+answerJson);
			
			Answer answers = new Gson().fromJson(answerJson, answerType);
			System.out.println("======="+answers);
			String answer2 = answers.getAnswer();
			Map answerMap=new Gson().fromJson(answer2,mapType);
			System.out.println("======="+answerMap);
		}
	    
	    
public boolean  compareSubmitTime(String submitTimeStr) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
		Date submitTime = df.parse(submitTimeStr);
	    Date d=new Date();   
	    Date lastHour = new Date(d.getTime() -  1* 60 * 60 * 1000);
		 System.out.println("1个小时前的日期：" +df.format( lastHour));  
		boolean after = submitTime.before(lastHour);
		return after;
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	
	
}
}
