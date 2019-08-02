package com.mejesticpay.iso20022.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class XMLParser
{

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

    private static void printEvent(XMLStreamReader xmlr) {
        logger.debug("EVENT: Type= " + xmlr.getEventType()+ " ["+xmlr.getLocation().getLineNumber()+"]["+
                xmlr.getLocation().getColumnNumber()+"] ");
        logger.debug(" [");
        switch (xmlr.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                logger.debug("<");
                printName(xmlr);
                printNamespaces(xmlr);
                printAttributes(xmlr);
                logger.debug(">");
                printElementText(xmlr);
                break;
            case XMLStreamConstants.END_ELEMENT:
                logger.debug("</");
                printName(xmlr);
                logger.debug(">");
                break;
            case XMLStreamConstants.SPACE:
            case XMLStreamConstants.CHARACTERS:
                int start = xmlr.getTextStart();
                int length = xmlr.getTextLength();
                logger.debug(new String(xmlr.getTextCharacters(),
                        start,
                        length));
                break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                logger.debug("<?");
                if (xmlr.hasText())
                    logger.debug(xmlr.getText());
                logger.debug("?>");
                break;
            case XMLStreamConstants.CDATA:
                logger.debug("<![CDATA[");
                start = xmlr.getTextStart();
                length = xmlr.getTextLength();
                logger.debug(new String(xmlr.getTextCharacters(),
                        start,
                        length));
                logger.debug("]]>");
                break;
            case XMLStreamConstants.COMMENT:
                logger.debug("<!--");
                if (xmlr.hasText())
                    logger.debug(xmlr.getText());
                logger.debug("-->");
                break;
            case XMLStreamConstants.ENTITY_REFERENCE:
                logger.debug(xmlr.getLocalName()+"=");
                if (xmlr.hasText())
                    logger.debug("["+xmlr.getText()+"]");
                break;
            case XMLStreamConstants.START_DOCUMENT:
                logger.debug("<?xml");
                logger.debug(" version='"+xmlr.getVersion()+"'");
                logger.debug(" encoding='"+xmlr.getCharacterEncodingScheme()+"'");
                if (xmlr.isStandalone())
                    logger.debug(" standalone='yes'");
                else
                    logger.debug(" standalone='no'");
                logger.debug("?>");
                break;
        }
        logger.debug("]");
    }
    private static void printName(XMLStreamReader xmlr){
        if(xmlr.hasName()){
            String prefix = xmlr.getPrefix();
            String uri = xmlr.getNamespaceURI();
            String localName = xmlr.getLocalName();
            printName(prefix,uri,localName);
        }
    }
    private static void printName(String prefix,
                                  String uri,
                                  String localName) {
        if (uri != null && !("".equals(uri)) ) logger.debug("['"+uri+"']:");
        if (prefix != null) logger.debug(prefix+":");
        if (localName != null) logger.debug(localName);
    }
    private static void printAttributes(XMLStreamReader xmlr){
        for (int i=0; i < xmlr.getAttributeCount(); i++) {
            printAttribute(xmlr,i);
        }
    }
    private static void printAttribute(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getAttributePrefix(index);
        String namespace = xmlr.getAttributeNamespace(index);
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        logger.debug(" ");
        printName(prefix,namespace,localName);
        logger.debug("='"+value+"'");
    }
    private static void printNamespaces(XMLStreamReader xmlr){
        for (int i=0; i < xmlr.getNamespaceCount(); i++) {
            printNamespace(xmlr,i);
        }
    }
    private static void printNamespace(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getNamespacePrefix(index);
        String uri = xmlr.getNamespaceURI(index);
        logger.debug(" ");
        if (prefix == null)
            logger.debug("xmlns='"+uri+"'");
        else
            logger.debug("xmlns:"+prefix+"='"+uri+"'");
    }

    private static void printElementText(XMLStreamReader xmlr){
        if(xmlr.hasText()){
            logger.debug(xmlr.getText());
        }
    }

    private static void printText(XMLStreamReader xmlr){
        if(xmlr.hasText()){
            logger.debug(xmlr.getText());
        }
    }
}
