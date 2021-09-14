package com.demidovn.fruitbounty.server.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Slf4j
@Controller
public class HttpMainController {

    private static final String FB_APP_URL = "/fb-app";
    private static final String VK_APP_URL = "/vk-app";
    private static final String MVC_REDIRECT = "redirect:/";
    private static final String APP_HTML_VIEW = "app";
    private static final String VK_SOCIAL_NETWORK_TYPE = "vk";
    private static final String SOCIAL_NETWORK_TYPE_PARAM = "socialNetworkType";
    private static final String SOCIAL_NETWORK_APP_ID_PARAM = "socialNetworkAppId";

    @Value("${game-server.facebook.application-id}")
    private String FACEBOOK_APP_ID;

    @RequestMapping(value = FB_APP_URL, method = RequestMethod.GET)
    public String facebookAppGet(Map<String, Object> model) {
        model.put(SOCIAL_NETWORK_APP_ID_PARAM, FACEBOOK_APP_ID);
        return APP_HTML_VIEW;
    }

    @RequestMapping(value = FB_APP_URL, method = RequestMethod.POST)
    public String facebookAppPost(Map<String, Object> model) {
        model.put(SOCIAL_NETWORK_APP_ID_PARAM, FACEBOOK_APP_ID);
        return APP_HTML_VIEW;
    }

    @RequestMapping(value = VK_APP_URL, method = RequestMethod.GET)
    public String vkAppGet(Map<String, Object> model) {
        model.put(SOCIAL_NETWORK_TYPE_PARAM, VK_SOCIAL_NETWORK_TYPE);
        return APP_HTML_VIEW;
    }

}
