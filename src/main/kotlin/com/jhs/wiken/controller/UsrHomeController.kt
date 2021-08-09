package com.jhs.wiken.controller

import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrHomeController {
    @Autowired
    private lateinit var rq: Rq;

    @RequestMapping("/")
    @ResponseBody
    fun showRoot(): String {
        return rq.replaceJs("", "/ken")
    }

    @RequestMapping("/home/main")
    fun showMain(model: Model): String {
        model.addAttribute("rq", Rq())
        model.addAttribute("name", "홍길동")

        return "usr/home/main"
    }
}