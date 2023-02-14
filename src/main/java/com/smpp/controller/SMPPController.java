/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.controller;

import com.smpp.model.Sms;
import com.smpp.service.SMPPService;
import com.smpp.utils.Constants;
import com.smpp.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author leonard
 */
@RestController
@RequestMapping("/smpp")
@ConditionalOnExpression("{'TRX', 'TX'}.contains('${configs.smpp.bind-type}')")
public class SMPPController {

    @Autowired
    private SMPPService smppService;

    @RequestMapping(path = "/sms", method = RequestMethod.POST)
    public ResponseEntity sentOtpSMS(@RequestBody Sms sms, HttpServletRequest request) {

        String ip_address = request.getRemoteAddr();

        String response;
        String messageId = smppService.sendTextMessage(sms.getSender(), sms.getMessage(), sms.getAddresses(), sms.isRequest_for_dlr());
        if (messageId != null) {
            response = "Message Submitted successful, Message ID : " + messageId;
        } else {
            response = "Message Submit failed!.";
        }
        Constants.SMS_LOGGER.info("Request ip: {}, Message : {}, Response : {}", ip_address, Utils.toJson(sms), response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
