package com.jhs.wiken.controller

import com.jhs.wiken.service.ArticleService
import com.jhs.wiken.vo.Article
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrArticleController(private val articleService: ArticleService) {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/article/getArticles")
    @ResponseBody
    fun getArticles(): List<Article> {
        return articleService.getArticles()
    }

    @RequestMapping("/article/list")
    fun showList(model: Model): String {
        val articles = articleService.getArticles()

        model.set("articles", articles)

        return "usr/article/list"
    }
}