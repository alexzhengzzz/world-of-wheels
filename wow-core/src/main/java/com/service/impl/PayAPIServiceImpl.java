package com.service.impl;

import org.springframework.web.client.RestTemplate;

/**
 * @author alexzhengzzz
 * @date 5/5/22 06:37
 */
public class PayAPIServiceImpl {
    private RestTemplate restTemplate = new RestTemplate();
    private String URL = "http://localhost:8081/pay/";

    public void pay(String orderId) {
        String url = "http://localhost:8080/pay?orderId=" + orderId;
        restTemplate.getForObject(url, String.class);
    }
}
