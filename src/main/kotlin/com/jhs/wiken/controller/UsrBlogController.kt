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

    @RequestMapping("/b/{blogId}")
    fun showArticles(@PathVariable("blogId") blogId: Int, model: Model): String {
        rq.currentPageSiteHeaderType = "blog"

        val blogKen = kenService.getKen(blogId) ?: return rq.historyBackJsOnTemplate("존재하지 않는 blog 입니다.")
        val blogKenSourceInterpreter = blogKen.genSourceInterpreter()
        val blogCss = blogKenSourceInterpreter.getCssSource(0)
        val blogKenConfig = blogKen.getKenConfig()

        val blogName = blogKenConfig.title
        val articleIds = blogKenConfig.articles

        val articles = blogService.getArticlesByKenIds(articleIds)

        model["blogId"] = blogId
        model["blogCss"] = blogCss
        model["blogName"] = blogName
        model["blogKen"] = blogKen
        model["articles"] = articles

        rq.currentPageCanGoEditCurrentKen = true

        return "usr/blog/articleList"
    }

    @RequestMapping("/b/{blogId}/{id}")
    fun showArticle(@PathVariable("blogId") blogId: Int, @PathVariable("id") id: Int, model: Model): String {
        rq.currentPageUseToastUiEditor = true
        rq.currentPageCanGoEditCurrentKen = true
        rq.currentPageSiteHeaderType = "blog"

        val blogKen = kenService.getKen(blogId) ?: return rq.historyBackJsOnTemplate("존재하지 않는 blog 입니다.")
        val blogKenSourceInterpreter = blogKen.genSourceInterpreter()
        val blogCss = blogKenSourceInterpreter.getCssSource(0)
        val blogKenConfig = blogKen.getKenConfig()

        val blogName = blogKenConfig.title
        val articleIds = blogKenConfig.articles

        val articles = blogService.getArticlesByKenIds(articleIds)

        model["blogId"] = blogId
        model["blogCss"] = blogCss
        model["blogName"] = blogName
        model["blogKen"] = blogKen
        model["articles"] = articles

        val ken = kenService.getKen(id) ?: return rq.historyBackJsOnTemplate("존재하지 않는 ken 입니다.")
        model["ken"] = ken

        return "usr/blog/articleDetail"
    }
}