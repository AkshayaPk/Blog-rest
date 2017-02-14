package com.akshay.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akshay.exception.ServiceException;
import com.akshay.model.Article;
import com.akshay.model.Category;
import com.akshay.model.User;
import com.akshay.service.CategoryService;
import com.akshay.service.UserService;
import com.mysql.cj.x.json.JsonArray;

@RestController
@RequestMapping("/category")
public class CategoryController {
	CategoryService categoryService = new CategoryService();

	@GetMapping("/list")
	public List<Category> index(ModelMap modelMap, @RequestParam("userId") int userId) {
		List<Category> categoryrList = categoryService.listByUserIdService(userId);
		modelMap.addAttribute("CATEGORY_LIST", categoryrList);
		return categoryrList;
	}

	@GetMapping("/publish")
	public String publish(@RequestParam("category") String category, @RequestParam("userId") int userId,
			@RequestParam("title") String title) {
		UserService service = new UserService();
		String userName = null;
		Category cate = new Category();
		cate.setName(category);
		User user = new User();
		user.setId(userId);
		cate.setUserId(user); 
		Article article = new Article();
		article.setTitle(title);
		article.setUserId(user);
		try {
			userName = service.functionGetUserName(userId);
			categoryService.insertCategory(article, cate);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "../publishcategory.jsp?success=0&title="+title+"&userId="+userId;
		}
		return "../articles/user?userName=" + userName;
	}
	
	@PostMapping("/post")
	public String post(@RequestParam("list") String categoryList,HttpSession httpSession) throws JSONException{
		UserService service = new UserService();
		String userName = null;
		User u=(User) httpSession.getAttribute("LOGGED_USER");
		JSONObject category=new JSONObject(categoryList);
		JSONArray tags=category.getJSONArray("category");
		 System.out.println(tags);
		for(int i=0;i<tags.length();i++){
			JSONObject obj=tags.getJSONObject(i);
			Category cate = new Category();
			cate.setName(obj.get("tag").toString());
			
			cate.setUserId(u); 
			Article article = new Article();
			article.setTitle(category.getString("title"));
			article.setUserId(u);
			try {
				userName = service.functionGetUserName(u.getId());
				categoryService.insertCategory(article, cate);
			} catch (ServiceException e) {
				e.printStackTrace();
			
			}
			System.out.println(obj.get("tag"));
		}
		return "../articles/user?userName=" + userName;
	}
	@PostMapping("/postTest")
	public void postTest(@RequestParam("categoryList") String categoryList) throws JSONException{
		
		JSONObject category=new JSONObject(categoryList);
		JSONArray tags=category.getJSONArray("category");
		for(int i=0;i<tags.length();i++){
			JSONObject obj=tags.getJSONObject(i);
			System.out.println(obj.get("tag"));
		}
		
	}

}
