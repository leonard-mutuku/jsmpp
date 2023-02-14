/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.smpp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author leonard
 */
public class Utils {

    public static String generateTimestamp() {
        DateFormat df = new SimpleDateFormat(Constants.CX_FORMAT_TS);
        df.setTimeZone(TimeZone.getTimeZone("EAT"));
        String ts = df.format(new Date());
        return ts;
    }

    public static String generateEATDate() {
        DateFormat df = new SimpleDateFormat(Constants.CX_FORMAT_TIMESTAMP);
        df.setTimeZone(TimeZone.getTimeZone("EAT"));
        String eatDate = df.format(new Date());
        return eatDate;
    }

    public static String formatEATDate(Date date) {
        String fm_date = "";
        try {
            DateFormat df = new SimpleDateFormat(Constants.CX_FORMAT_TIMESTAMP);
            df.setTimeZone(TimeZone.getTimeZone("EAT"));
            fm_date = df.format(date);
        } catch (Exception e) {
            Constants.SMS_LOGGER.error(e.toString());
        }
        return fm_date;
    }

    public static String generateISODate() {
        DateFormat df = new SimpleDateFormat(Constants.CX_FORMAT_ISO_TIMESTAMP);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String isoDate = df.format(new Date());
        return isoDate;
    }

    public static String toJson(Object obj) {
        String jsonStr = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonStr = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Constants.SMS_LOGGER.error(e.toString());
        }
        return jsonStr;
    }

}
