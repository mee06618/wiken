package com.jhs.wiken.service

import com.jhs.wiken.repository.KenRepository
import com.jhs.wiken.vo.ResultData
import org.springframework.stereotype.Service

@Service("kenService")
class KenService(private val kenRepository: KenRepository) {
    fun write(memberId: Int, title: String, source: String, result: String): ResultData<Int> {
        kenRepository.write(memberId, title, source, result)
        val id = kenRepository.getLastInsertId()

        println("id : ${id}")

        return ResultData.from("S-1", "${id}번 켄이 생성되었습니다.", "id", id)
    }
}
