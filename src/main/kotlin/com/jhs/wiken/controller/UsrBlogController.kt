package com.jhs.wiken.controller

import com.jhs.wiken.service.BlogService
import com.jhs.wiken.service.KenService
import com.jhs.wiken.vo.KenSourceInterpreter
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrBlogController(
    private val kenService: KenService,
    private val blogService: BlogService
) {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/b/{id}")
    fun showArticles(@PathVariable("id") id: Int, model: Model): String {
        rq.siteHeaderType = "blog"

        val ken = kenService.getKen(id) ?: return rq.historyBackJsOnTemplate("존재하지 않는 blog 입니다.")
        val kenSourceInterpreter = ken.genSourceInterpreter()
        val blogCss = kenSourceInterpreter.getCssSource(0)
        val kenConfig = ken.getKenConfig()

        val blogName = kenConfig.title
        val articleIds = kenConfig.articles

        val articles = blogService.getArticlesByKenIds(articleIds)

        model.addAttribute("blogCss", blogCss)
        model.addAttribute("blogName", blogName)
        model.addAttribute("ken", ken)
        model.addAttribute("articles", articles)

        rq.setCurrentPageCanGoEditCurrentKen(true)

        return "usr/blog/article-list"
    }
}