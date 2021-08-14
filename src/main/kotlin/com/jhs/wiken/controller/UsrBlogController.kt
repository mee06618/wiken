package com.jhs.wiken.controller

import com.jhs.wiken.service.BlogService
import com.jhs.wiken.service.KenService
import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class UsrBlogController(
    private val kenService: KenService,
    private val blogService: BlogService,
    private val rq: Rq
) {

    @RequestMapping("/b/{id}")
    fun showArticles(@PathVariable("id") id: Int, model: Model): String {
        rq.currentPageSiteHeaderType = "blog"

        val ken = kenService.getKen(id) ?: return rq.historyBackJsOnTemplate("존재하지 않는 blog 입니다.")
        val kenSourceInterpreter = ken.genSourceInterpreter()
        val blogCss = kenSourceInterpreter.getCssSource(0)
        val kenConfig = ken.getKenConfig()

        val kenConfigBlog = kenConfig.blog

        val blogName = kenConfig.title
        val articleIds = kenConfig.articles

        val articles = blogService.getArticlesByKenIds(articleIds)

        model["blogCss"] = blogCss
        model["blogName"] = blogName
        model["ken"] = ken
        model["articles"] = articles
        model["kenConfigBlog"] = kenConfigBlog

        rq.currentPageCanGoEditCurrentKen = true

        return "usr/blog/article-list"
    }
}