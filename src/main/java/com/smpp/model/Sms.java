/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.model;

import java.io.Serializable;
import java.util.List;
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
public class Sms implements Serializable {

    private static final long serialVersionUID = 4758880159210086960L;

    private List<String> addresses;
    private String message;
    private boolean request_for_dlr;
    private String sender;
}
