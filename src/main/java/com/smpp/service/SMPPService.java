/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.service;

import com.smpp.config.SmppSessionDelegate;
import com.smpp.model.MessageParams;
import com.smpp.utils.Constants;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SubmitMultiResult;
import org.jsmpp.session.SubmitSmResult;
import org.springframework.stereotype.Service;

/**
 *
 * @author leonard
 */
@Service
public class SMPPService {

    private final Charset ISO_CHARSET = StandardCharsets.ISO_8859_1;
    private final String SERVICE_TYPE = "CMT";

    public String sendTextMessage(String sourceAddress, String message, List<String> destinationAddress, boolean requestDlr) {
        SMPPSession session = SmppSessionDelegate.getSession();
        SessionState sessionState = session.getSessionState();
        String messageId = null;

        if (sessionState.isBound() && sessionState.isNotClosed()) {

            TypeOfNumber sourceAddTon = getSourceAddTon(sourceAddress);
            RegisteredDelivery RD = getRegisteredDelivery(requestDlr);
            MessageParams mps = getMessageParams(message);

            try {
                if (destinationAddress.size() > 1) {
                    Address[] addresses = prepareAddress(destinationAddress);
                    SubmitMultiResult result = session.submitMultiple(SERVICE_TYPE,
                            sourceAddTon, NumberingPlanIndicator.UNKNOWN, sourceAddress, addresses,
                            new ESMClass(), (byte) 0, (byte) 1, null, null, RD, ReplaceIfPresentFlag.REPLACE,
                            new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                            mps.getMessage(), mps.getOptionalParameter());
                    messageId = result.getMessageId();
                } else {
                    SubmitSmResult result = session.submitShortMessage(SERVICE_TYPE,
                            sourceAddTon, NumberingPlanIndicator.UNKNOWN, sourceAddress,
                            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, destinationAddress.get(0),
                            new ESMClass(), (byte) 0, (byte) 1, null, null, RD, (byte) 0,
                            new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                            mps.getMessage(), mps.getOptionalParameter());
                    messageId = result.getMessageId();
                }
                Constants.SMS_LOGGER.info("Message Submitted successful, Message ID : {}", messageId);
            } catch (PDUException | NegativeResponseException | ResponseTimeoutException | IOException | InvalidResponseException e) {
                Constants.SMS_LOGGER.error(e.getMessage());
            } catch (Exception e) {
                Constants.SMS_LOGGER.error(e.getMessage());
            }
        } else {
            Constants.SMS_LOGGER.error("SMPP session is not connected");
        }

        return messageId;
    }

    private TypeOfNumber getSourceAddTon(String sourceAddress) {
        if (sourceAddress.matches("[0-9]+")) {
            return TypeOfNumber.UNKNOWN;
        } else {
            return TypeOfNumber.ALPHANUMERIC;
        }
    }

    private RegisteredDelivery getRegisteredDelivery(boolean requestDlr) {
        SMSCDeliveryReceipt SDLR = requestDlr ? SMSCDeliveryReceipt.SUCCESS_FAILURE : SMSCDeliveryReceipt.DEFAULT;
        return new RegisteredDelivery(SDLR);
    }

    private Address[] prepareAddress(List<String> numbers) {
        Address[] addresses = new Address[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            addresses[i] = new Address(TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, numbers.get(i));
        }
        return addresses;
    }

    private MessageParams getMessageParams(String message) {
        byte[] msgBytes = message.getBytes(ISO_CHARSET);
        if (msgBytes.length > Constants.MSG_PAYLOAD_MIN_LEN) {
            return new MessageParams(new OptionalParameter.OctetString(Tag.MESSAGE_PAYLOAD, message), null);
        } else {
            return new MessageParams(new OptionalParameter.Null(Tag.MESSAGE_PAYLOAD), msgBytes);
        }
    }
}
