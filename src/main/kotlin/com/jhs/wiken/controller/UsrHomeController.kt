package com.jhs.wiken.controller

import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class UsrHomeController {
    @RequestMapping("/home/main")
    fun showMain(model: Model): String {
        model.addAttribute("rq", Rq())
        model.addAttribute("name", "홍길동")

        return "usr/home/main"
    }
}