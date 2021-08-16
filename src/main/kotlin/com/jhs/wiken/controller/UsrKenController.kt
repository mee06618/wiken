package com.jhs.wiken.controller

import com.jhs.wiken.service.KenService
import com.jhs.wiken.vo.KenSourceInterpreter
import com.jhs.wiken.vo.Rq
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UsrKenController(
    private val kenService: KenService,
    private val rq: Rq
) {
    // ken 작성/편집 화면 보여줌
    @RequestMapping("/ken")
    fun showWrite(): String {
        rq.currentPageUseToastUiEditor = true
        rq.currentPageCanSaveKen = true
        return "usr/ken/write"
    }

    // ken 작성/편집 화면 보여줌
    @RequestMapping("/ken/mine")
    fun showMyList(model: Model): String {
        val kens = kenService.getKensByMemberId(rq.loginedMemberId)

        model["kens"] = kens

        return "usr/ken/my-list"
    }

    // ken 작성 처리
    @RequestMapping("/ken/doWrite")
    @ResponseBody
    fun doWrite(source: String, result: String): String {
        // 켄 소스 해석기 생성
        val kenSourceInterpreter = KenSourceInterpreter.from(source)
        // 해석기에서 제목 가져옴
        val title = kenSourceInterpreter.title

        val resultData = kenService.write(rq.loginedMemberId, title, source, result)
        val id = resultData.data

        return rq.replaceJs("", "../ken/${id}/edit")
    }

    // ken 편집 처리
    @RequestMapping("/ken/doModify")
    @ResponseBody
    fun doModify(id: Int, source: String, result: String): String {
        val ken = kenService.getKen(id) ?: return rq.historyBackJs("존재하지 않는 ken 입니다.")

        if (ken.memberId != rq.loginedMemberId) {
            return rq.historyBackJs("권한이 없습니다.")
        }

        // 입력받은 소스를 새 소스로 설정한다.
        var newSource = source

        // 켄 소스 해석기 생성
        var kenSourceInterpreter = KenSourceInterpreter.from(source)

        // 만약 사용자가 깜빡하고 config을 만들지 않았다면, 기존 config을 재사용한다.
        if (!kenSourceInterpreter.hasConfig) {
            newSource = "$" + "$" + "config\n"
            newSource += ken.genKenConfigSource()
            newSource += "\n" + "$" + "$" + "\n"
            newSource += source.trim()

            kenSourceInterpreter = KenSourceInterpreter.from(newSource)
        }

        // 해석기에서 제목 가져옴
        val title = kenSourceInterpreter.title

        kenService.modify(id, title, newSource, result)

        return rq.replaceJs("", "../ken/${id}/edit")
    }

    // ken 삭제
    @RequestMapping("/ken/doDelete")
    @ResponseBody
    fun doDelete(id: Int): String {
        val ken = kenService.getKen(id) ?: return rq.historyBackJs("존재하지 않는 ken 입니다.")

        if (ken.memberId != rq.loginedMemberId) {
            return rq.historyBackJs("권한이 없습니다.")
        }

        val resultData = kenService.delete(id)

        return rq.replaceJs(resultData.msg, "/ken")
    }

    @RequestMapping("/ken/{id}/edit")
    fun showModify(@PathVariable("id") id: Int, model: Model): String {
        val ken = kenService.getKen(id) ?: return rq.historyBackJsOnTemplate("존재하지 않는 ken 입니다.")

        if (ken.memberId != rq.loginedMemberId) {
            return rq.historyBackJsOnTemplate("수정권한이 없습니다.")
        }

        rq.currentPageUseToastUiEditor = true
        rq.currentPageCanDeleteCurrentKen = true
        rq.currentPageCanGoViewCurrentKen = true
        rq.currentPageCanSaveKen = true

        model["ken"] = ken

        return "usr/ken/write"
    }

    @RequestMapping("/ken/{id}")
    fun showDetail(@PathVariable("id") id: Int, model: Model): String {
        rq.currentPageUseToastUiEditor = true
        rq.currentPageCanGoEditCurrentKen = true
        val ken = kenService.getKen(id) ?: return rq.historyBackJsOnTemplate("존재하지 않는 ken 입니다.")
        model["ken"] = ken
        return "usr/ken/detail"
    }
}