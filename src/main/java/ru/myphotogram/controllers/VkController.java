package ru.myphotogram.controllers;

import com.google.common.collect.ImmutableMap;
import org.springframework.core.env.Environment;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.social.support.URIBuilder;
import org.springframework.social.vkontakte.api.VKGenericResponse;
import org.springframework.social.vkontakte.api.attachment.Photo;
import org.springframework.social.vkontakte.api.impl.json.VKArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Роман on 16.04.2016.
 */
@Controller
public class VkController {

    @Inject
    Environment environment;

    @RequestMapping(path = "vkontakte/signin", method = RequestMethod.GET)
    public RedirectView signin() {
        return new RedirectView(URIBuilder
            .fromUri("https://oauth.vk.com/authorize")
            .queryParam("client_id", "5417613")
            .queryParam("client_secret", "cHuT2M67a4u93hhecNCq")
            .queryParam("redirect_uri", "http://localhost:8080/vkontakte/getphotos")
            .queryParam("scope", "photos email")
            .queryParam("display", "page")
            .queryParam("state", "test")
            .queryParam("response_type", "code")
            .queryParam("v", "5.50")
            .build().toString());
    }

    @RequestMapping(path = "vkontakte/getphotos", method = RequestMethod.GET)
    public RedirectView getPhotos(@RequestParam String code) {
        return new RedirectView(
            URIBuilder
                .fromUri("https://oauth.vk.com/access_token")
                .queryParam("client_id", "5417613")
                .queryParam("client_secret", "cHuT2M67a4u93hhecNCq")
                .queryParam("redirect_uri", "http://localhost:8080/vkontakte/getphotos")
                .queryParam("code", code)
                .build().toString()
        );
    }

    @RequestMapping(path = "vkontakte/realygetallphotos")
    @ResponseBody
    public Map realygetallphotos() {
        RestTemplate restTemplate = new RestTemplate();
        String url = URIBuilder
            .fromUri("https://api.vk.com/method/photos.getAll")
            .queryParam("access_token", "e20945e8e4731af7053fadcac0e14d966c304073768ccd3aaea577341609884bb57000d3b2778db8160b6")
            .queryParam("owner_id", "360697717")
            .build().toString();
        Map vkArray = restTemplate.getForObject(url, Map.class);
        return vkArray;
    }


}
