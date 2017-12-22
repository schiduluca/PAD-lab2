package com.sckeedoo.broker.domain.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlConverter {

    public static <T> String convertToXMLString(T object, Class<T> tClass) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(tClass);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(object, sw);

            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T convertToDto(String xml, Class<T> classType) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gsonToObj = new Gson();
        return gsonToObj.fromJson(xml, classType);
    }
}
