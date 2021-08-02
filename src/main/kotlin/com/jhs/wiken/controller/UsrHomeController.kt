package com.jhs.wiken.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrHomeController {
    @RequestMapping("/usr/home/main")
    @ResponseBody
    fun showMain(): String {
        return "ㅋㅋㅋㅋ"
    }

    @RequestMapping("/usr/home/main2")
    @ResponseBody
    fun showMain2(): String {
        return "하하하하"
    }
}