package com.jhs.wiken.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class UsrHomeController {
    @RequestMapping("/usr/home/main")
    fun showMain(model: Model): String {
        model.addAttribute("name", "홍길동")

        return "usr/home/main"
    }
}