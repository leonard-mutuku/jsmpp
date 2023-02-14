/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsmpp.bean.OptionalParameter;

/**
 *
 * @author leonard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageParams {

    private OptionalParameter optionalParameter;
    private byte[] message;

}
