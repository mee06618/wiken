package com.jhs.wiken.service

import com.jhs.wiken.vo.ResultData
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailSender: JavaMailSender) {
    @Value("\${custom.notifyEmailFrom}")
    private val notifyEmailFrom: String = ""

    fun send(to: String, subject: String, body: String): ResultData<String> {
        val message = emailSender.createMimeMessage()

        val helper = MimeMessageHelper(message)

        helper.setFrom(notifyEmailFrom)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(body, true)

        try {
            emailSender.send(message)
        }
        catch ( e: Exception ) {
            return ResultData.from("F-1", "이메일 발송에 실패하였습니다.", "email", to)
        }

        return ResultData.from("S-1", "이메일이 발송되었습니다.", "email", to)
    }
}