/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.config;

import com.smpp.config.ApplicationProperties.SMPP;
import com.smpp.utils.Constants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author leonard
 */
@EnableScheduling
@EnableAsync
@Component
public class SmppSessionListener implements ApplicationContextAware {

    private final ApplicationProperties properties;

    private ApplicationContext ctx;

    @Autowired
    public SmppSessionListener(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Scheduled(initialDelayString = "${configs.smpp.enquire-link-timer}", fixedDelayString = "${configs.smpp.enquire-link-timer}")
    public void enquireLinkJob() {
        SMPPSession session = SmppSessionDelegate.getSession();
        SessionState sessionState = session.getSessionState();
        if (sessionState.isBound() && sessionState.isNotClosed()) {
            Constants.SMPP_LOGGER.info("Session {} in state {}", session.getSessionId(), sessionState);
        } else {
            Constants.SMPP_LOGGER.error("enquire link running while session is not connected");
            //publish event to reshesh session, since session is in closed state
        }
    }

    @PostConstruct
    public void initSmppSession() {
        createSmppSession();
    }

    //overriding default connection factory to add connect timeot
    private ConnectionFactory getConnectionFactory(Long timeout) {
        return (String host, int port) -> {
            Socket so = new Socket();
            so.connect(new InetSocketAddress(host, port), timeout.intValue());
            return new SocketConnection(so);
        };
    }

    private void createSmppSession() {
        SMPP smpp = properties.getSmpp();
        ConnectionFactory cf = getConnectionFactory(smpp.getBindTimeout());
        SMPPSession session = new SMPPSession(cf);
        BindType bindType = getSmppBindType(smpp.getBindtype());
        try {
            if (bindType.equals(BindType.BIND_RX) || bindType.equals(BindType.BIND_TRX)) {
                session.setMessageReceiverListener(ctx.getBean(MessageReceiverListenerImpl.class));
            }
            session.addSessionStateListener(new SessionStateListenerImpl());
            session.setInterfaceVersion(InterfaceVersion.IF_50);
            session.setEnquireLinkTimer(smpp.getEnquireLinkTimer()); //smpp session enquire link timer
            session.setTransactionTimer(smpp.getTransactionTimer()); //maximum waiting time for each submit request
            session.setPduProcessorDegree(properties.getClient().getNumberOfClientSessions());
            session.setQueueCapacity(properties.getClient().getSmppWorkQueueCapacity());

            BindParameter bp = new BindParameter(bindType, smpp.getUserId(), smpp.getPassword(), "smpp",
                    TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null);
            String systemId = session.connectAndBind(smpp.getHost(), smpp.getPort(), bp, smpp.getBindTimeout());

            SmppSessionDelegate.setSession(session);
            Constants.SMPP_LOGGER.info("SMPP session connected with system id {}", systemId);
        } catch (IOException e) {
            Constants.SMPP_LOGGER.error("SMPP session connection failed. Error is; {}", e.toString());
        }
    }

    private BindType getSmppBindType(String type) {
        return switch (type) {
            case "TX" -> BindType.BIND_TX;
            case "RX" -> BindType.BIND_RX;
            default -> BindType.BIND_TRX;
        };
    }

    @PreDestroy
    public void destroySmppSession() {
        SmppSessionDelegate.destroySession();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

}
