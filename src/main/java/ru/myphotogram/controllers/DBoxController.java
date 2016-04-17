package ru.myphotogram.controllers;

import com.dropbox.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Роман on 17.04.2016.
 */
@Controller
public class DBoxController {

    private final DbxRequestConfig config = new DbxRequestConfig("myphotogram", "en_US");
    private final DbxAppInfo dbxAppInfo = new DbxAppInfo("0mhdx4iiel9uejm", "ejfpqu8pn13zyli");

    @RequestMapping(path = "dropbox/signin", method = RequestMethod.GET)
    public RedirectView singin(HttpServletRequest request) {
        DbxWebAuth dbxWebAuth =  new DbxWebAuth(config, dbxAppInfo, "https://myphotogramru.herokuapp.com/dropbox/finishauth", new DbxStandardSessionStore(request.getSession(), "dropbox_token"));
        return new RedirectView(dbxWebAuth.start());
    }

    @RequestMapping(path = "dropbox/finishauth", method = RequestMethod.GET)
    public String finish(HttpServletRequest request) {
        DbxWebAuth dbxWebAuth =  new DbxWebAuth(config, dbxAppInfo, "https://myphotogramru.herokuapp.com/dropbox/finishauth", new DbxStandardSessionStore(request.getSession(), "dropbox_token"));
        DbxAuthFinish authFinish = null;
        try {
            authFinish = dbxWebAuth.finish(request.getParameterMap());
        } catch (DbxWebAuth.BadRequestException | DbxException | DbxWebAuth.NotApprovedException | DbxWebAuth.ProviderException | DbxWebAuth.CsrfException | DbxWebAuth.BadStateException e) {
            e.printStackTrace();
        }
        String accessToken = authFinish.getAccessToken();
        return accessToken;
    }


}
