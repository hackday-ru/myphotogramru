package ru.myphotogram.controllers;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v1.DbxThumbnailFormat;
import com.dropbox.core.v1.DbxThumbnailSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class DropboxController {

    private static final int BUFFER_SIZE = 4096;

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxController.class);
    private DbxRequestConfig config = new DbxRequestConfig("myphotogram", "en_US");
    private DbxClientV1 clientV1 = new DbxClientV1(config, "1LffmceDYhAAAAAAAAAAGnq29CjJCm2jefXwEDOMv0U75AKI-ds_-KrOKCgJ1kAB");

    @RequestMapping(value = "dropbox/thumbnail/{path}", method = RequestMethod.GET)
    public void dropboxThumbnail(@PathVariable("path") String path, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, DbxException {
        String url = "/" + path + ".jpg";

        try (OutputStream os = response.getOutputStream()) {
            clientV1.getThumbnail(DbxThumbnailSize.w128h128, DbxThumbnailFormat.JPEG, url, null, os);
            response.setContentType("image/jpg");
            response.flushBuffer();
        }
    }

    @RequestMapping(value = "dropbox/img/{path}", method = RequestMethod.GET)
    public void dropbox(@PathVariable("path") String path, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, DbxException {
        String url = "/" + path + ".jpg";
        DbxRequestConfig config = new DbxRequestConfig("myphotogram", "en_US");
        DbxClientV1 clientV1 = new DbxClientV1(config, "1LffmceDYhAAAAAAAAAAGnq29CjJCm2jefXwEDOMv0U75AKI-ds_-KrOKCgJ1kAB");

        try (OutputStream os = response.getOutputStream()) {
            DbxEntry.File file = clientV1.getFile(url, null, os);
            response.setContentType("image/jpeg");
            response.setContentLengthLong(file.numBytes);
            response.flushBuffer();
        }
    }
}
