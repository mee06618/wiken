package com.jhs.wiken.service

import com.jhs.wiken.repository.ArticleRepository
import com.jhs.wiken.vo.Article
import org.springframework.stereotype.Service

@Service("articleService")
class ArticleService(private val articleRepository: ArticleRepository) {
    fun getArticles(): List<Article> {
        return articleRepository.getArticles()
    }
}
