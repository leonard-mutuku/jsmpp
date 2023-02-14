/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.config;

import com.smpp.utils.Constants;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;

/**
 *
 * @author leonard
 */
public class SmppSessionDelegate {

    private static SMPPSession session;

    public static void setSession(SMPPSession session) {
        SmppSessionDelegate.session = session;
    }

    public static SMPPSession getSession() {
        if (session == null) {
            SmppSessionDelegate.session = new SMPPSession();
        }
        return session;
    }

    public static void destroySession() {
        try {
            SessionState sessionState = getSession().getSessionState();
            if (sessionState.isBound() && sessionState.isNotClosed()) {
                Constants.SMPP_LOGGER.info("Cleaning up SMPP session ...");
                session.unbindAndClose();
            } else {
                Constants.SMPP_LOGGER.info("SMPP session already unbound and closed ...");
            }
        } catch (Exception e) {
            Constants.SMPP_LOGGER.error("SMPP session destroy error; {}", e.toString());
        }
    }

}
