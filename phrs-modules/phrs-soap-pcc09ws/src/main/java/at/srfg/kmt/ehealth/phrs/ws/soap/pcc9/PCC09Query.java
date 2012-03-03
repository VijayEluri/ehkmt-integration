package at.srfg.kmt.ehealth.phrs.ws.soap.pcc9;


import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.QUPCIN043100UV01;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class PCC09Query {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PCC09Query.class);
    private final String CONFIG_FILE = "pcc-9.properties";

    private String endpointURI;
    private String responseEndpointURI;
    private String keystoreFile;
    private String keystorePassword;
     /*
         private final String keystoreFile;
    private final String keystorePassword;
      */
    public void SendPcc09Client(String endpointURI, String responseEndpointURI, String keystoreFile, String password) {
        this.endpointURI = endpointURI;
        this.responseEndpointURI = responseEndpointURI;
        this.keystoreFile = keystoreFile;
        this.keystorePassword = password;

    }

    public void SendPcc09Client() {
        final ClassLoader classLoader = PCC09Query.class.getClassLoader();
        final InputStream resourceAsStream =
                classLoader.getResourceAsStream(CONFIG_FILE);

        if (resourceAsStream == null) {
            LOGGER.warn("The PCC09 configunration file named {} must be placed in the classpath", CONFIG_FILE);
            LOGGER.warn("NO PCC09 MESSAGES CAN BE SENT!");
            return;
        }

        try {
            final Properties config = new Properties();
            config.load(resourceAsStream);
            //https://localhost:8089/testws/pcc9 https://localhost:8989/testws/pcc10

            endpointURI = config.getProperty("endpointURI", "https://icardea-server.lksdom21.lks.local/ehrif/pcc/").trim();
            responseEndpointURI = config.getProperty("responseEndpointURI", "https://icardea-server.lksdom21.lks.local:8989/testws/pcc10").trim();

            keystoreFile = config.getProperty("keystore", "srfg-phrs-core-keystore.ks").trim();
            keystorePassword = config.getProperty("password", "icardea").trim();

        } catch (Exception exception) {
            LOGGER.warn("NO pcc09 MESSAGES CAN BE SENT!");
            LOGGER.warn(exception.getMessage(), exception);
        }
    }

    public String getEndpointURI() {
        return endpointURI;
    }

    public String getResponseEndpointURI() {
        return responseEndpointURI;
    }

    public String getKeystoreFile() {
        return keystoreFile;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public MCCIIN000002UV01 sendPcc09Message(final String patientID, final String careProvisionCode) {
        return sendPcc09Message(patientID, careProvisionCode, getEndpointURI(), getResponseEndpointURI(), getKeystoreFile(), getKeystorePassword());
    }

    public MCCIIN000002UV01 sendPcc09Message(final String patientID, final String careProvisionCode,
                                             final String endpointURI, final String responseURI, final String keystore, final String password) {
        //final String careProvisionReason = "";
        //final String includeCarePlanAttachment = "true";
        final String maximumHistoryStatements = "30";

        //final String patientBirthTime = "197903111010";

        String patientName = "";
        String patientSurname = "";

        MCCIIN000002UV01 ack = null;
        try {
            /// + " careProvisionCode: " + careProvisionCode + " patientID: " + patientID
            LOGGER.info("Tries to send a PCC9 query ({}) to {}", endpointURI, responseURI);

            final QUPCIN043100UV01 pcc9Request =
                    QueryFactory.buildPCC9Request(careProvisionCode,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            maximumHistoryStatements,
                            null,
                            null,
                            patientID,
                            patientName,
                            patientSurname);

            LOGGER.info("Tries to send a PCC9 query patientId  careProvisionCode to  " + endpointURI + "  " + careProvisionCode + " patientID: " + patientID + " keystore=" + keystore);

            if (endpointURI.startsWith("https")) {
                ack = SendPcc09Message.sendSecureMessage(pcc9Request, endpointURI, responseURI, keystore, password);
            } else {
                ack = SendPcc09Message.sendMessage(pcc9Request, endpointURI, responseURI);
            }
            LOGGER.info("Acknowledge (response) is : {}.", ack);

        } catch (JAXBException e) {
            LOGGER.info("JAXBException", e);
        } catch (MalformedURLException e) {
            LOGGER.info("JAXBException", e);
        } catch (Exception e) {
            LOGGER.info("Exception", e);
        }

        return ack;

    }

}
