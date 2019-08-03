package com.mejesticpay.iso20022.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class ISOUtil
{
    private static final Logger logger = LogManager.getLogger(ISOUtil.class);

    private  static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    /**
     * Quickly look up and return message type based on NamespaceURI.
     * @param inputMessage, input message with NamespaceURI.
     * @return
     * @throws XMLStreamException
     */
    public static String getMessageType(String inputMessage) throws XMLStreamException
    {
        Reader reader = new StringReader(inputMessage);
        //InputStream inputStream = new ByteArrayInputStream(inputMessage.getBytes(Charset.forName("UTF-8")));
        return getMessageType(reader);
    }

    /**
     * Quickly look up and return message type based on NamespaceURI.
     *
     * @param reader, reader containing input message.
     * @return
     * @throws XMLStreamException
     */
    public static String getMessageType(Reader reader) throws XMLStreamException
    {
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(reader);
        try
        {
            while (xmlStreamReader.hasNext())
            {
                int eventType = xmlStreamReader.next();
                if (XMLStreamReader.START_ELEMENT == eventType)
                {
                    if (xmlStreamReader.getLocalName().contains("Message"))
                    {
                        // Count attribute before reading.
                        int nameSpaceCount = xmlStreamReader.getNamespaceCount();
                        for (int i = 0; i < nameSpaceCount; i++)
                        {
                            String nameSpaceURI = xmlStreamReader.getNamespaceURI(i);
                            if(nameSpaceURI != null && nameSpaceURI.contains("pacs.008.001.06"))
                            {
                                return "pacs.008.001.06";
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            xmlStreamReader.close();
        }
        return null;
    }

}
