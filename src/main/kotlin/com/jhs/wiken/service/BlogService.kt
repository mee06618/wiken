package com.jhs.wiken.service

import com.jhs.wiken.repository.BlogRepository
import com.jhs.wiken.repository.KenRepository
import com.jhs.wiken.vo.Ken
import org.springframework.stereotype.Service

@Service("blogService")
class BlogService(private val blogRepository: BlogRepository) {
    fun getArticlesByKenIds(kenIds: List<Int>): List<Ken> {
        return blogRepository.getArticlesByKenIds(kenIds)
    }
}
