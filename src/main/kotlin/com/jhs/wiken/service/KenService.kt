package com.jhs.wiken.service

import com.jhs.wiken.repository.KenRepository
import com.jhs.wiken.vo.Article
import com.jhs.wiken.vo.Ken
import com.jhs.wiken.vo.ResultData
import org.springframework.stereotype.Service

@Service("kenService")
class KenService(private val kenRepository: KenRepository) {
    fun write(memberId: Int, title: String, source: String, result: String): ResultData<Int> {
        kenRepository.write(memberId, title, source, result)
        val id = kenRepository.getLastInsertId()

        return ResultData.from("S-1", "${id}번 켄이 생성되었습니다.", "id", id)
    }

    fun getKen(id: Int): Ken? {
        return kenRepository.getKen(id)
    }

    fun modify(id: Int, title: String, source: String, result: String): ResultData<Int> {
        kenRepository.modify(id, title, source, result)

        return ResultData.from("S-1", "${id}번 켄이 생성되었습니다.", "id", id)
    }

    fun getKensByMemberId(memberId: Int): List<Ken> {
        return kenRepository.getKensByMemberId(memberId)
    }
}
