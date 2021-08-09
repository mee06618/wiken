package com.jhs.wiken.controller

import com.jhs.wiken.service.KenService
import com.jhs.wiken.vo.KenSourceInterpreter
import com.jhs.wiken.vo.Rq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrKenController(private val kenService: KenService) {
    @Autowired
    private lateinit var rq: Rq;

    // ken 작성/편집 화면 보여줌
    @RequestMapping("/ken")
    fun showWrite(): String {
        rq.setCurrentPageCanSaveKen(true)
        return "usr/ken/write"
    }

    // ken 작성/편집 처리
    @RequestMapping("/ken/doWrite")
    @ResponseBody
    fun doWrite(id: Int, source: String, result: String): String {
        // 켄 소스 해석기 생성
        val kenSourceInterpreter = KenSourceInterpreter.from(source)
        // 해석기에서 제목 가져옴
        val title = kenSourceInterpreter.getTitle()
        val resultData = kenService.write(rq.getLoginedMemberId(), title, source, result)
        val id = resultData.getData()

        return rq.replaceJs("", "../ken/${id}/edit")
    }

    @RequestMapping("/ken/{id}/edit")
    fun showEdit(@PathVariable("id") id: Int, model: Model): String {
        rq.setCurrentPageCanGoViewCurrentKen(true)
        rq.setCurrentPageCanSaveKen(true)
        val ken = kenService.getKen(id)
        model.addAttribute("ken", ken)
        return "usr/ken/write"
    }

    @RequestMapping("/ken/{id}")
    fun showDetail(@PathVariable("id") id: Int, model: Model): String {
        rq.setCurrentPageCanGoEditCurrentKen(true)
        val ken = kenService.getKen(id)
        model.addAttribute("ken", ken)
        return "usr/ken/detail"
    }
}