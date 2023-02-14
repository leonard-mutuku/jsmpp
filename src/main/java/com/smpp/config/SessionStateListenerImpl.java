/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.config;

import com.smpp.utils.Constants;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

/**
 *
 * @author leonard
 */
public class SessionStateListenerImpl implements SessionStateListener {

    @Override
    public void onStateChange(SessionState newState, SessionState oldState, Session sn) {
        Constants.SMPP_LOGGER.info("Session {} changed from {} to {}", sn.getSessionId(), oldState, newState);
    }

}
