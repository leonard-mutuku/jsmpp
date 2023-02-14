/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.config;

import com.smpp.model.DLReport;
import com.smpp.utils.Constants;
import com.smpp.utils.Utils;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.MessageType;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.springframework.stereotype.Component;

/**
 *
 * @author leonard
 */
@Component
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
//            dlrService.processDLR(deliverSm);
            try {
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();

                String messageId = delReceipt.getId();
                String msisdn = deliverSm.getSourceAddr();
                String sender = deliverSm.getDestAddress();
                Integer statusId = delReceipt.getDelivered();
                String status = delReceipt.getFinalStatus().name();
                String date = Utils.formatEATDate(delReceipt.getDoneDate());

                DLReport dlr = new DLReport(messageId, msisdn, sender, statusId, status, date);
                Constants.SMS_LOGGER.info("Receiving DLR => Dlr : {}", Utils.toJson(dlr));
            } catch (InvalidDeliveryReceiptException e) {
                Constants.SMS_LOGGER.error("Failed getting DLR. Error is {}", e);
            }
        } else {
//            incomingService.processMo(deliverSm);
        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification an) {
        Constants.SMPP_LOGGER.info("AlertNotification not implemented");
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm datasm, Session sn) throws ProcessRequestException {
        Constants.SMPP_LOGGER.info("DataSm not implemented");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAcceptEnquireLink(EnquireLink enquireLink, Session source) {
        Constants.SMPP_LOGGER.info("on accept enquire Link; EQ : {}, Se : {}", enquireLink, source);
        MessageReceiverListener.super.onAcceptEnquireLink(enquireLink, source);
    }

}
