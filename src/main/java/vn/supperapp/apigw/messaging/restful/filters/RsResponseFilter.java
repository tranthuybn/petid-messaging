/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.restful.filters;

import com.google.gson.Gson;
import vn.supperapp.apigw.messaging.configs.RsResponseFilterMapping;
import vn.supperapp.apigw.messaging.restful.models.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

/**
 *
 * @author luandv
 */
@RsResponseFilterMapping
public class RsResponseFilter implements ContainerResponseFilter {

    static Logger logger = LoggerFactory.getLogger(RsResponseFilter.class);
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.info("############################## [ RsResponseFilter - Log response ########################");
        logger.info("Api: {}", requestContext.getUriInfo().getRequestUri().getPath());
        logger.info("HTTP Status: {}", responseContext.getStatus());

        Gson gson = new Gson();
        UriInfo uri = requestContext.getUriInfo();
        String path = uri.getPath();
        if (!path.startsWith("/")) {
            path = String.format("/%s", path);
        }

        String res = "";
        BaseResponse baseRes = null;
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#RsResponseFilter - EXCEPTION: ", ex);
        }
        logger.info("############################## ] RsResponseFilter - Log response ########################");
    }
    
}
