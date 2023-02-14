/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.utils;

import org.slf4j.LoggerFactory;

/**
 *
 * @author leonard
 */
public class Constants {

    public static final org.slf4j.Logger SMPP_LOGGER = LoggerFactory.getLogger("SMPP_LOG");
    public static final org.slf4j.Logger SMS_LOGGER = LoggerFactory.getLogger("SMS_LOG");

    public static final String CX_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
    public static final String CX_FORMAT_TS = "yyyyMMddHHmmss";
    public static final String CX_FORMAT_ISO_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final Integer MSG_PAYLOAD_MIN_LEN = 254;

}
