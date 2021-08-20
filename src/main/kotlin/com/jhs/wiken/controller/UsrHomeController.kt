package com.jhs.wiken.controller

import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

// 점검완료, 2021-08-20 기준
@Controller
class UsrHomeController(
    private val rq: Rq
) {
    @RequestMapping("/")
    @ResponseBody
    fun showRoot(): String {
        return rq.replaceJs("", "/ken")
    }

    @RequestMapping("/home/main")
    fun showMain(model: Model): String {
        return "usr/home/main"
    }
}