package com.mejesticpay.iso20022.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Reference: https://docs.oracle.com/javase/8/docs/api/javax/xml/validation/package-summary.html
 */
public class XmlValidationUtil
{

    private static final Logger logger = LogManager.getLogger(XmlValidationUtil.class);
    // create a SchemaFactory capable of understanding WXS schemas
    private static SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    private static DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();

    public static boolean validate(String xmlAsString, String schemaFileLocation) throws IOException
    {

        InputStream inputStream = null;
        try {
            // parse an XML document into a DOM tree
            DocumentBuilder parser = DBF.newDocumentBuilder();
            //use ByteArrayInputStream to get the bytes of the String and convert them to InputStream.
            inputStream = new ByteArrayInputStream(xmlAsString.getBytes(Charset.forName("UTF-8")));

            Document document = parser.parse(inputStream);

            // load a WXS schema, represented by a Schema instance
            URL url = XmlValidationUtil.class.getClassLoader().getResource(schemaFileLocation);
            Source schemaFile = new StreamSource(new File(url.getFile()));
            Schema schema = factory.newSchema(schemaFile);

            // create a Validator instance, which can be used to validate an instance document
            Validator validator = schema.newValidator();

            // validate the DOM tree

            validator.validate(new DOMSource(document));
            return true;
        }
        catch (ParserConfigurationException | SAXException e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if(inputStream != null)
                inputStream.close();
        }
        return false;
    }

}
