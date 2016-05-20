package com.binghz.control.news;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.binghz.control.common.BaseControl;
import com.binghz.service.news.NewsService;
import com.binghz.service.user.UserService;
import com.binghz.yy.consts.common.CommonConstant;
import com.binghz.yy.consts.common.HttpState;
import com.binghz.yy.entity.common.news.NewsEntity;
import com.binghz.yy.entity.common.user.UserEntity;
import com.binghz.yy.session.entity.SessionEntity;
import com.binghz.yy.session.service.SessionService;
import com.binghz.yy.utils.CSDNUtils;
import com.binghz.yy.utils.FreemarkerUtils;
import com.binghz.yy.utils.JsonMessage;
import com.qiniu.common.QiniuException;

@Controller
@RequestMapping("news")
public class NewsConrol extends BaseControl {

	@Autowired
	private NewsService newsService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping("save/{token}")
	public JsonMessage saveNews(HttpServletRequest request,
			@PathVariable(value = "token") String token, String title,
			String content, String briefContent) {
		JsonMessage result = new JsonMessage();
		System.out.println(token + " " + title + " " + content + " "
				+ briefContent);
		if (token == null || title == null || content == null
				|| briefContent == null) {
			return result.fill(HttpState.HTTP_PARAME_NORMAL,
					HttpState.HTTP_PARAME_NORMAL_STR); // 参数错误
		}
		SessionEntity sessionEntity = sessionService.findByToken(token);
		UserEntity userEntity = userService.findByUserName(sessionEntity
				.getUsername());
		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setAuthorid(userEntity.getId());
		newsEntity.setBriefContent(briefContent);
		newsEntity.setClassification(1);
		newsEntity.setCreateDate(new Date());
		newsEntity.setStatus(1); ///状态暂定为1
		newsEntity.setContent(content);
		newsEntity.setUpdateDate(new Date());
		newsEntity.setTitle(title);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		map.put("content", content);
		map.put("briefContent", briefContent);
		map.put("classification", 1);
		map.put("id", userEntity.getId());
		map.put("nickname", userEntity.getNickname());
		map.put("date", new Date().toString());
		map.put("imgSrc", userEntity.getImgSrc());
		ServletContext servletContext = request.getSession()
				.getServletContext();
		map.put("ctx", servletContext.getContextPath());
		NewsEntity newsCreate = newsService.save(newsEntity);
		File htmlFile = FreemarkerUtils.createNewsHtml(servletContext, map,
				newsCreate.getId().toString());
		newsCreate.setNewsUrl(CommonConstant.CSDN_MIRRO_LOCATION+"/"+newsCreate.getId().toString());
		newsService.update(newsCreate);
		List<Map<String, Object>> newsList = newsService.findNewsByStatus(1);
		map = new HashMap<String, Object>();
		for(Map<String,Object> news:newsList){
			UserEntity authorEntity = userService.findOne(NumberUtils.toLong(news.get("authorid").toString()));
			news.put("imgSrc", authorEntity.getImgSrc());
		}
		map.put("newsList", newsList);
		FreemarkerUtils.createIndexNewsContent(servletContext, map);
		try {
			CSDNUtils.getFtpQiNiu().upFile(htmlFile,
					newsCreate.getId().toString());
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.fill(HttpState.HTTP_CHANNEL_SUCCESS,
				HttpState.HTTP_CHANNEL_SUCCESS_STR);
	}

	@ResponseBody
	@RequestMapping("newsBrief")
	public JsonMessage newsBrief(Integer page) {
		JsonMessage result = new JsonMessage();
		List<Map<String, Object>> newsList = newsService.findNewsByStatus(1);
		result.fill(0, "", null, newsList);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/newsCreateView")
	public ModelAndView newsCreateViews() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/custom/login");
		return mv;
	}

	@ResponseBody
	@RequestMapping("newsCreateView/{token}")
	public ModelAndView newsCreateView(
			@PathVariable(value = "token") String token) {
		ModelAndView mv = new ModelAndView();
		if (!isLogin(token) || !isLogin(token)) {
			mv.setViewName("/custom/login");
			return mv;
		}
		SessionEntity sessionEntity = sessionService.findByToken(token);
		mv.addObject("isLogin", isLogin(token));
		mv.addObject("isAlive", isAlive(token));
		mv.addObject("imgSrc", sessionEntity.getImgSrc());
		mv.setViewName("/custom/newsCreate");
		mv.addObject("token", token);
		return mv;
	}
}
