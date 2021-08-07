package com.jhs.wiken.controller

import com.jhs.wiken.service.KenService
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrKenController(private val kenService: KenService) {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/ken")
    fun showWrite(): String {
        return "usr/ken/write"
    }

    @RequestMapping("/ken/doWrite")
    @ResponseBody
    fun doWrite(title: String, source: String, result: String): String {
        val resultData = kenService.write(1, title, source, result)

        val id = resultData.getData()

        return rq.replaceJs("", "../ken/${id}")
    }
}