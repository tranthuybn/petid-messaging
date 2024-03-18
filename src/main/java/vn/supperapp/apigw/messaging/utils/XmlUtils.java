/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author truonglq
 */
public class XmlUtils {
    final static Logger logger = LogManager.getLogger(XmlUtils.class.getName());
    
    public static void main(String[] args) {
        String xml = "<firstName></firstName><lastName></lastName><message>Success</message><result>1</result><accountCIF>XPAYPD62+1cdHTev4PkHcpvE5gZRou4=</accountCIF><accountCurrency>KHR</accountCurrency><accountName>Bui Quang Vu</accountName><accountStatus>0</accountStatus><aclErrorCode>0</aclErrorCode><tokenType>2</tokenType><validateTransId>00010060123002_1564557907570</validateTransId>";
        
        XStream xstream = new XStream();
//        xstream.alias("return", MbgUserAccount.class);
        
//        MbgUserAccount mbgUser = convertXmlToObject(MbgUserAccount.class, xml);
        System.out.println("TEST");
    }
    
    public static <T> T convertXmlToObject(Class<T> clazz, String xml, String rootElement) {
        try {
            XStream xstream = new XStream();
            xstream.alias(rootElement, clazz);
            return (T) xstream.fromXML(xml);
        } catch (Exception ex) {
            logger.error("#convertXmlToObject - ERROR: ", ex);
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    public static <T> T convertXmlToObject(Class<T> clazz, String xml) {
        try {
            StringBuilder strXml = new StringBuilder();
            strXml.append("<alias>").append(xml).append("</alias>");
            XStream xstream = new XStream();
//            xstream.ignoreUnknownElements();
            xstream.autodetectAnnotations(true);
            xstream.alias("alias", clazz);
            return (T) xstream.fromXML(strXml.toString());
        } catch (Exception ex) {
            logger.error("#convertXmlToObject - ERROR: ", ex);
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static String toXmlString(Object obj) {
        try {
            XStream xstream = new XStream();
//            xstream.processAnnotations(Pain00100105.class);
            xstream.ignoreUnknownElements();
            xstream.autodetectAnnotations(true);
            return xstream.toXML(obj);
        } catch (Exception ex) {
            logger.error("#toXmlString - ERROR: ", ex);
        }
        return null;
    }
}
