package com.akshay.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akshay.exception.ServiceException;
import com.akshay.model.Article;
import com.akshay.model.Category;
import com.akshay.model.Comment;
import com.akshay.model.User;
import com.akshay.service.ArticleService;
import com.akshay.service.CategoryService;
import com.akshay.service.CommentService;
import com.akshay.service.UserService;

@RestController
@RequestMapping("/articles")
public class ArticleController {
	private Article article = new Article();

	private ArticleService articleService = new ArticleService();

	@GetMapping
	public List<Article> index() {
		List<Article> articleList;
		articleList = articleService.listService();
		return articleList;
	}

	@GetMapping("viewbycategory")
	public List<Article> indexByCategory(ModelMap modelMap, @RequestParam("category") String category) {
		CategoryService categoryService = new CategoryService();
		List<Article> articleList = categoryService.viewByCategoryService(category);
		return articleList;
	}

	@GetMapping("comments")
	public List<Comment> indexComments(ModelMap modelMap, @RequestParam("articleId") int articleId) {
		CommentService commentService = new CommentService();
		List<Comment> commentList = commentService.listByArticleIdService(articleId);
		return commentList;
		}
	
	

	@GetMapping("/other")
	public List<Article> indexOtherUsers(ModelMap modelMap, @RequestParam("userId") int userId) {
		List<Article> articleList = null;
		try {
			articleList = articleService.listOtherUserService(userId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return articleList;
		
	}
		

	@GetMapping("/user")
	public List<Article> index(@RequestParam("userName") String name) {
		UserService userService = new UserService();
		List<Article> articleList = null;
		 int roleId=0;
		 int userId=0;
		try {
		 roleId = userService.functionGetRoleId(name);
		 userId = userService.functionGetUserId(name);
			articleList = articleService.listByUserService(userService.functionGetUserId(name));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return articleList;
		
	}
	

	@PostMapping("/publish")
	public String publish(@RequestBody Article a) {
		article.setTitle(a.getTitle());
		article.setContent(a.getContent());

		User user = new User();
		user.setId(a.getUserId().getId());
		article.setUserId(user);
		try {

			articleService.publishArticleService(article, user);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return "Successfully published";
		}
		

	@GetMapping("/update")
	public String update(@RequestParam("id") int id,HttpSession httpSession) {
		User userSession = (User) httpSession.getAttribute("LOGGED_USER");
		if (userSession.getRoleId().getId() != 1) {
		article.setId(id);
		article.getId();
		return "../updatearticle.jsp";}
		else
			return "redirect:/";
	}

	@PostMapping("/updateArticle")
	public String update(@RequestBody Article a) {
		article.setTitle(a.getTitle());
		article.setContent(a.getContent());
		article.setId(a.getId());
		String userName = articleService.getUserIdByArticleId(a.getId());
		try {
			articleService.updateByIdService(article);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return "Successully updated article" + userName;}
		
	

	@PostMapping("/delete")
	public String delete(@RequestBody Article a) {
		article.setId(a.getId());
		String userName = articleService.getUserIdByArticleId(a.getId());
		try {
			articleService.deleteArticleService(article);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return "Successfully Deleted" + userName;}
		

}
