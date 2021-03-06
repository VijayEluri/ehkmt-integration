/*
 * Project :iCardea
 * File : Util.java
 * Encoding : UTF-8
 * Date : Dec 22, 2011
 * User : Mihai Radulescu
 */
package at.srfg.kmt.ehealth.phrs.ws.soap.pcc9;


import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.hl7.v3.CD;
import org.hl7.v3.II;
import org.hl7.v3.PN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Contains a set of common purposed methods related with the SOAP message.
 * <br/> This class is not designed to be extend.
 *
 * @author Miahai
 * @version 0.1
 * @since 0.1
 */
final class Util {

    /**
     * The XML Tag Name for the Care Provision Code XML Element.
     */
    private static final String CARE_PROVISION_CODE_TAG_NAME = "careProvisionCode";

    /**
     * The XML Element Attribute Name for the Patient Name XML Element.
     */
    private static final String PATIENTNAME_TAG_NAME = "patientName";

    /**
     * The XML Element Attribute Name for the Patient ID XML Element.
     */
    private static final String PATIENT_ID_TAG_NAME = "patientId";

    /**
     * The XML Tag name for the Value XML Element.
     */
    private static final String VALUE_TAG_NAME = "value";

    /**
     * The XML Tag name for the Parameter List XML Element.
     */
    private static final String PARAMETER_LIST_TAG_NAME = "parameterList";

    /**
     * The XML Element Attribute Name for the Code values.
     */
    private static final String CODE_ATTRIBUTE_NAME = "code";

    /**
     * The XML Element Attribute Name for the Extension values.
     */
    private static final String EXTENSION_ATTRIBUTE_NAME = "extension";

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger name space is
     * <code>at.srfg.kmt.ehealth.phrs.ws.soap.XMLUtil</code>.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Util.class);

    /**
     * Don't let anyone to instantiate this class.
     */
    private Util() {
        // UNIMPLEMENTED
    }

    /**
     * Serialize a
     * <code>org.w3c.dom.Node</code> in to a string, this string represents a
     * well formated XML. This methods can be applied on the SOPA header and
     * SOAP body (because both are extending the
     * <code>org.w3c.dom.Node</code>) node interface.
     *
     *
     * @param node the note to be serialize, it can not be null.
     * @return
     * @throws ParserConfigurationException if the header can not be (XML)
     * parsed.
     * @throws TransformerConfigurationException by any XML transformation
     * related error.
     * @throws TransformerException by any XML transformation related error.
     * @throws NullPointerException if the
     * <code>header</code> argument is null.
     */
    static String toString(Node node) throws
            ParserConfigurationException, TransformerConfigurationException,
            TransformerException {

        if (node == null) {
            throw new NullPointerException("The node argument can not be null.");
        }

        final StringWriter stringWriter = new StringWriter();
        final StreamResult streamResult = new StreamResult(stringWriter);
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.transform(new DOMSource(node), streamResult);

        final String result = stringWriter.toString();
        return result;
    }

    /**
     * Transforms a
     * <code>org.hl7.v3.II</code> in to a human readable string.
     *
     * @param ii the involved
     * <code>org.hl7.v3.II</code> instance.
     * @return a human readable string for the given instance.
     */
    static String toString(II ii) {
        final StringBuffer result = new StringBuffer();
        final String extension = ii.getExtension();

        if (extension != null) {
            result.append("Extension (UUID) : ");
            result.append(extension);
        }

        return result.toString();
    }

    /**
     * Transforms a
     * <code>org.hl7.v3.CD</code> in to a human readable string.
     *
     * @param cd the involved
     * <code>org.hl7.v3.CD</code> instance.
     * @return a human readable string for the given instance.
     */
    static String toString(CD cd) {
        final StringBuffer result = new StringBuffer();

        if (cd.getCode() != null) {
            result.append("Code : ");
            result.append(cd.getCode());
            result.append(", ");
        }

        if (cd.getCodeSystem() != null) {
            result.append("CodeSystem : ");
            result.append(cd.getCodeSystem());
            result.append(", ");
        }


        if (cd.getCodeSystemName() != null) {
            result.append("CodeSystemName : ");
            result.append(cd.getCodeSystemName());
            result.append(", ");
        }

        if (cd.getCodeSystemVersion() != null) {
            result.append("CodeSystemVersion : ");
            result.append(cd.getCodeSystemVersion());
            result.append(", ");
        }

        final int length = result.length();
        if (length > 2) {
            result.delete(length - 2, length - 1);
        }

        return result.toString();
    }

    /**
     * Transforms a
     * <code>org.hl7.v3.PN</code> in to a human readable string.
     *
     * @param pn the involved
     * <code>org.hl7.v3.PN</code> instance.
     * @return a human readable string for the given instance.
     */
    static String toString(PN pn) {
        final StringBuffer result = new StringBuffer();
        final List<Serializable> content = pn.getContent();
        if (content.isEmpty()) {
            return "No Content Found.";
        }

        for (Serializable item : content) {
            result.append(item);
            result.append(", ");
        }
        final int length = result.length();
        if (length > 2) {
            result.delete(length - 2, length - 1);
        }

        return result.toString();
    }

    /**
     * Extracts the Care Provision Code information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Care Provision Code information.
     *
     * @param paramter the
     * <code>org.w3c.dom.Element</code> instance that contains the Care
     * Provision Code information.
     * @return the Care Provision Code information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Care Provision Code information.
     * @throws IllegalStateException if the
     * <code>paramter</code> argument does not contains the right information
     * for the Care Provision Code.
     * @throws NullPointerException if the
     * <code>paramter</code> argument is null.
     */
    static String getCareProvisionCode(Element paramter) {
        if (paramter == null) {
            final NullPointerException exception =
                    new NullPointerException("The parameter argument can not be null");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        // TODO : use XPATH !
        final NodeList careProvisionList =
                paramter.getElementsByTagName(CARE_PROVISION_CODE_TAG_NAME);
        if (careProvisionList.getLength() == 0) {
            return null;
        }
        if (careProvisionList.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount careProvisionCode element. Only one careProvisionCode element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element careProvisionCode = (Element) careProvisionList.item(0);


        final NodeList values = careProvisionCode.getElementsByTagName(VALUE_TAG_NAME);
        if (values.getLength() == 0) {
            return null;
        }
        if (values.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount values element. Only one values element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element value = (Element) values.item(0);
        final String result = value.getAttribute(CODE_ATTRIBUTE_NAME);
        return result;
    }

    /**
     * Extracts the Patient ID information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Patient ID information.
     *
     * @param paramter the
     * <code>org.w3c.dom.Element</code> instance that contains the Patient ID
     * information.
     * @return the Patient ID information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Patient ID information.
     * @throws IllegalStateException if the
     * <code>paramter</code> argument does not contains the right information
     * for the Patient ID.
     * @throws NullPointerException if the
     * <code>paramter</code> argument is null.
     */
    static String getPatientId(Element paramter) {
        if (paramter == null) {
            final NullPointerException exception =
                    new NullPointerException("The parameter argument can not be null");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        // TODO : use XPATH !
        final NodeList ids =
                paramter.getElementsByTagName(PATIENT_ID_TAG_NAME);
        if (ids.getLength() == 0) {
            return null;
        }
        if (ids.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount patientId element. Only one patientId element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element careProvisionCode = (Element) ids.item(0);

        final NodeList values = careProvisionCode.getElementsByTagName(VALUE_TAG_NAME);
        if (values.getLength() == 0) {
            return null;
        }
        if (values.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount value element. Only one value element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element value = (Element) values.item(0);
        final String result = value.getAttribute(EXTENSION_ATTRIBUTE_NAME);
        return result;
    }

    /**
     * Extracts the Patient Name information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Patient Name information.
     *
     * @param paramter the
     * <code>org.w3c.dom.Element</code> instance that contains the Patient Name
     * information.
     * @return the Patient Name information from the given
     * <code>org.w3c.dom.Element</code> instance or null if the given element
     * does not contains any Patient Name information.
     * @throws IllegalStateException if the
     * <code>paramter</code> argument does not contains the right information
     * for the Patient Name.
     * @throws NullPointerException if the
     * <code>paramter</code> argument is null.
     */
    static String getPatientName(Element paramter) {
        if (paramter == null) {
            final NullPointerException exception =
                    new NullPointerException("The parameter argument can not be null");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        // TODO : use XPATH !
        final NodeList names =
                paramter.getElementsByTagName(PATIENTNAME_TAG_NAME);
        if (names.getLength() == 0) {
            return null;
        }
        if (names.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount patientName element. Only one patientName element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element careProvisionCode = (Element) names.item(0);

        final NodeList values =
                careProvisionCode.getElementsByTagName(VALUE_TAG_NAME);
        if (values.getLength() == 0) {
            return null;
        }
        if (values.getLength() != 1) {
            final IllegalStateException exception =
                    new IllegalStateException("Wrong amount values element. Only one values element expected.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        final Element value = (Element) values.item(0);
        final String result = value.getTextContent();
        return result;
    }

    /**
     * Check   wsa:Address in header
     * @param header
     * @param otherUri
     * @return
     */
    public static boolean isReponseEndPointInHeader(SOAPHeader header, String otherUri){
        if(otherUri == null) {
            return false;
        }

        String headerResponseUri=  Util.getResponseEndPointUri(header);
        if(headerResponseUri == null )   {
           return false;
        }

        if(headerResponseUri.equalsIgnoreCase(otherUri)){
            return true;
        }
        return false;
    }
    public  static String getResponseEndPointUri(SOAPHeader header){
        String responseEndpointURI = null;
        try {
            if(header==null) {
                LOGGER.error("header is null");
                return null;
            }

            final NodeList nodes = header.getElementsByTagName("wsa:Address");
            final int length = nodes.getLength();
            if (length != 1) {
                final IllegalStateException exception =
                        new IllegalStateException("The SOAP Header must contain only one WSA Address element.");
                LOGGER.error(exception.getMessage(), exception);
                throw exception;
            }

            final javax.xml.soap.Node wsAdressNode = (javax.xml.soap.Node) nodes.item(0);
            responseEndpointURI = wsAdressNode.getTextContent();

            LOGGER.debug("getResponseEndPointUri Response end point address is {}", responseEndpointURI);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return    responseEndpointURI;
    }
}
