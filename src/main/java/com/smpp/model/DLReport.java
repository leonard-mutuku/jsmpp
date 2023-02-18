/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author leonard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DLReport implements Serializable {

    private static final long serialVersionUID = 694185737655698496L;

    private String messageId;
    private String msisdn;
    private String sender;
    private Integer delivered;
    private String status;
    private String date;

}
