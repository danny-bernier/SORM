package dev.config;

import dev.model.exception.XMLConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class to gather connection information
 */
public abstract class ConnectionConfiguration {

    private ConnectionConfiguration(){}

    /**
     * Gathers database connection configuration information from DB_Connection.xml
     * and populates a map of elements to their values
     * @param XMLFilePath Path to the DB_Connection.xml file
     * @return Returns Map of tag names to tag body
     * @throws XMLConfigurationException Thrown when getDBConnectionInformation() fails any aspect of reading from DB_Connection.xml and populating returned map
     * @throws IllegalArgumentException Thrown when XMLFilePath is null
     */
    public static Map<String, String> getDBConnectionInformation(String XMLFilePath) throws XMLConfigurationException, IllegalArgumentException {

        if(XMLFilePath == null)
            throw new IllegalArgumentException("Parameter XMLFilePath cannot be null");

        File connectionXMLFile = new File(XMLFilePath);

        try {
            Map<String, String> connectionInformation = new HashMap<>();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            //reading & parsing xml from file
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionXMLFile);

            if(document == null)
                throw new XMLConfigurationException("Unable to parse DB_Connection.xml. " +
                        "\nPlease refer to README.md when setting up DB_Connection.xml");

            //getting elements
            NodeList url = document.getElementsByTagName("url");
            NodeList schema = document.getElementsByTagName("schema");
            NodeList user = document.getElementsByTagName("user");
            NodeList password = document.getElementsByTagName("password");

            //checking if any elements are missing from connection
            if(url.item(0) == null || schema.item(0) == null || user.item(0) == null || password.item(0) == null)
                throw new XMLConfigurationException("One or more elements are missing from DB_Connection.xml. " +
                        "\nPlease refer to README.md when setting up DB_Connection.xml");

            //populating map
            connectionInformation.put("url", url.item(0).getTextContent());
            connectionInformation.put("schema", schema.item(0).getTextContent());
            connectionInformation.put("user", user.item(0).getTextContent());
            connectionInformation.put("password", password.item(0).getTextContent());

            if(connectionInformation.get("url").equals(""))
                throw new XMLConfigurationException("Element '<url>' must be specified/cannot be empty in DB_Connection.xml" +
                        "\nPlease refer to README.md when setting up DB_Connection.xml");

            if(connectionInformation.get("user").equals(""))
                throw new XMLConfigurationException("Element '<user>' must be specified/cannot be empty in DB_Connection.xml" +
                        "\nPlease refer to README.md when setting up DB_Connection.xml");

            return connectionInformation;

        } catch (ParserConfigurationException | SAXException e){
            throw new XMLConfigurationException("Failed to parse xml from DB_Connection.xml: " +
                    e.getMessage() +
                    "\nDB_Connection.xml may not be configured correctly." +
                    "\nPlease refer to README.md when setting up DB_Connection.xml");

        } catch (IOException e) {
            throw new XMLConfigurationException("Could not access file DB_Connection.xml: " +
                    e.getMessage() +
                    "\nEnsure DB_Connection.xml exists, path: SORM/src/main/resources/DB_Connection.xml" +
                    "\nPlease refer to README.md when setting up DB_Connection.xml");
        }
    }
}
