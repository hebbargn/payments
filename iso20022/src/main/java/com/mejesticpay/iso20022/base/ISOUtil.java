package com.mejesticpay.iso20022.base;

import com.mejesticpay.iso20022.type.BusinessApplicationHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISOUtil
{
    private static final Logger logger = LogManager.getLogger(ISOUtil.class);

    private  static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    // Support %R1% to %R9%.  For  example: %R7% will randomize 7 characters.
    private static Pattern pattern = Pattern.compile(".*%R[1-9]%.*");

    /**
     * Generic utility to randomize string to avoid duplicate detection.
     * Supported usage is %R1% (randomize just 1 character) to %R9% (randomize 9 characters)
     *
     * @param input
     * @return
     */
    public static String randomizeString(String input)
    {
        Matcher m = pattern.matcher(input);
        if(m.matches())
        {
            int number = Character.getNumericValue(input.charAt(input.indexOf("%R")+2));
            String randomStr = String.format("%010d", new Random().nextInt(Integer.MAX_VALUE));
            return input.replaceFirst("%R" + number + "%", randomStr.substring(randomStr.length()-number, randomStr.length()));
        }
        return input;
    }

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


    public static BusinessApplicationHeader getBusinessAppHeader(String inputMessage) throws XMLStreamException
    {
        Reader reader = new StringReader(inputMessage);
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(reader);
        try
        {
            while (xmlStreamReader.hasNext())
            {
                int eventType = xmlStreamReader.next();
                if (XMLStreamReader.START_ELEMENT == eventType)
                {
                    if (xmlStreamReader.getLocalName().contains("AppHdr"))
                    {
                        return BusinessApplicationHeader.getBussAppHdr(xmlStreamReader);
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
