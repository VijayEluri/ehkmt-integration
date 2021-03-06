package at.srfg.kmt.ehealth.phrs.security.services.login;

import at.srfg.kmt.ehealth.phrs.presentation.services.ConfigurationService;
import at.srfg.kmt.ehealth.phrs.presentation.services.UserSessionService;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

//import flex.messaging.FlexContext;
//import flex.messaging.FlexSession;

/**
 * Consolidates business logic from the UI code for Registration activities.
 * <p/>
 * Most of this code modeled after ConsumerServlet, part of the openid4java
 * sample code available at
 * http://code.google.com/p/openid4java/wiki/SampleConsumer. Some of this code
 * was outright copied :->.
 *
 * @author J Steven Perry
 * @author http://makotoconsulting.com
 */
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class.getName());

    public String createRedirect(String username) {
        return createRedirect(username, RegistrationService.getReturnToUrl());
    }

    /**
     *
     * @param username     shortName
     * @param returnUrl
     * @return
     */

    public String createRedirect(String username, String returnUrl) {

        LOGGER.debug("LoginServiceImpl createRedirect(username,loginType) special openId usage for " + username + " returnUrl " + returnUrl);
        ResourceBundle properties = ResourceBundle.getBundle("icardea");
        String salkServer = properties.getString("salk.server");

        //FIXXME phrs use profiles
        boolean specialUsage = Boolean.parseBoolean(ResourceBundle.getBundle("icardea").getString("salk.usage"));
        // boolean specialUsage = new Boolean(ResourceBundle.getBundle("icardea").getString("salk.usage")).booleanValue();


        if (specialUsage) {
            username = salkServer + "/idp/u=" + username; //only valid for SALK server
        }
        LOGGER.debug("LoginServiceImpl createRedirect ##############AT Discovery for: username="
                + username + " returnUrl=" + returnUrl
                + " salkServer=" + salkServer + "  special openId usage=" + specialUsage);

        DiscoveryInformation discovery = RegistrationService.performDiscoveryOnUserSuppliedIdentifier(username);


        if (returnUrl == null) {
            returnUrl = LoginUtils.getOpenIdReturnToUrl();
        }

        LOGGER.debug("LoginServiceImpl  before authRequest ##############AT return url:" + returnUrl);

        AuthRequest authRequest = RegistrationService.createOpenIdAuthRequest(discovery, returnUrl);
        LOGGER.debug("LoginServiceImpl authRequest created redirectUrl:" + returnUrl);
        String redirectUrl = authRequest.getDestinationUrl(true);
        LOGGER.debug("LoginServiceImpl authRequest.getDestinationUrl(true) redirectUrl:" + redirectUrl);
        return redirectUrl;
    }
    //openid.provider.any

    /**
     * Specialize for system openid provider
     * @param loginId
     * @param openIdProviderId - the key from the UI e.g. openid.provider.1
     * @return
     */
    //TODO if username contains '@' check if yahoo or google and add url, otherwise if no http then try default server if fail, try alt server
    //TODO handle short names and emails
    public String createRedirectForLoginType(String loginId, String openIdProviderId) throws Exception {
        String redirectUrl = null;
        String username = loginId;
        try {


            String server = ConfigurationService.getInstance().getProperty(openIdProviderId);

            if (server == null) {
                server = ConfigurationService.getInstance().getProperty("openid.provider.1", "https://icardea-server.lksdom21.lks.local/idp/");
            }
            String returnUrl = ConfigurationService.getInstance().getProperty("openid.returnurl.default", "http://icardea-server.lksdom21.lks.local:6060/phrweb/openid");


            //FIXXME phrs use profiles
            server = server.trim();
            returnUrl = returnUrl.trim();
            //TODO use profile for openID provider see key   openid.provider.1.profile

            boolean specialUsage = Boolean.parseBoolean(ResourceBundle.getBundle("icardea").getString("salk.usage"));
            // boolean specialUsage = new Boolean(ResourceBundle.getBundle("icardea").getString("salk.usage")).booleanValue();
            LOGGER.debug("createRedirect special openId usage " + specialUsage + " " + username + " returnUrl=" + returnUrl + " openIdProviderId=" + openIdProviderId + " server=" + server);

            if (specialUsage) {
                //includes .../idp
                if (server.endsWith("/")) {
                    username = server + "u=" + username;
                } else {
                    username = server + "/u=" + username; //only valid for SALK server
                }
            }


            LOGGER.debug("LoginServiceImpl " + "##############AT Discovery for: " + username);
            DiscoveryInformation discovery = RegistrationService.performDiscoveryOnUserSuppliedIdentifier(username);
            if (discovery == null) {
                LOGGER.error("Discovery FAILED (null). Username=" + username);
                throw new Exception("Discovery FAILED (null), username=" + username);
            }
            //String url = RegistrationService.getReturnToUrl();
            if (returnUrl == null) {
                returnUrl = LoginUtils.getOpenIdReturnToUrl();
            }


            LOGGER.debug("LoginServiceImpl  before authRequest ##############AT return url:" + returnUrl);
            AuthRequest authRequest = RegistrationService.createOpenIdAuthRequest(discovery, returnUrl);

            if (authRequest == null) {
                LOGGER.error("AuthRequest FAILED (null). Username=" + username);
                throw new Exception("AuthRequest FAILED,(null). Username= " + username);
            }
            LOGGER.debug("LoginServiceImpl authRequest created redirectUrl:" + returnUrl);
            redirectUrl = authRequest.getDestinationUrl(true);

        } catch (Exception e) {
            LOGGER.error("OpenId error createRedirect username=" + username, e);
        }

        return redirectUrl;
    }

    /**
     *  Support full open ID
     * @param openId
     * @return
     * @throws Exception
     */
    public String createRedirectForOpenID(String openId) throws Exception {
        String redirectUrl = null;

        String username = openId;
        try {
            String returnUrl = ConfigurationService.getInstance().getProperty("openid.returnurl.default", "http://icardea-server.lksdom21.lks.local:6060/phrweb/openid");

            returnUrl = returnUrl.trim();
            //TODO use profile for openID provider see key   openid.provider.1.profile

            // boolean specialUsage = new Boolean(ResourceBundle.getBundle("icardea").getString("salk.usage")).booleanValue();
            LOGGER.debug("createRedirectForOpenID special openId  " + " " + username + " returnUrl=" + returnUrl);

            LOGGER.debug("LoginServiceImpl createRedirectForOpenID " + "##############AT Discovery for: " + username);
            DiscoveryInformation discovery = RegistrationService.performDiscoveryOnUserSuppliedIdentifier(username);
            if (discovery == null) {
                LOGGER.error("Discovery FAILED (null). Username=" + username);
                throw new Exception("Discovery FAILED (null), username=" + username);
            }
            //String url = RegistrationService.getReturnToUrl();
            if (returnUrl == null) {
                returnUrl = LoginUtils.getOpenIdReturnToUrl();
            }

            LOGGER.debug("LoginServiceImpl createRedirectForOpenID before authRequest ##############AT return url:" + returnUrl);
            AuthRequest authRequest = RegistrationService.createOpenIdAuthRequest(discovery, returnUrl);

            if (authRequest == null) {
                LOGGER.error("AuthRequest FAILED (null). Username=" + username);
                throw new Exception("AuthRequest FAILED,(null). Username= " + username);
            }
            LOGGER.debug("LoginServiceImpl createRedirectForOpenID authRequest created redirectUrl:" + returnUrl);
            redirectUrl = authRequest.getDestinationUrl(true);

        } catch (Exception e) {
            LOGGER.error("OpenId createRedirectForOpenID error createRedirect username=" + username, e);
        }

        return redirectUrl;
    }

    public RegistrationModel handleValidation() {
        LOGGER.debug("LoginServiceImpl " + "##############AT HANDLEVALIDATION");
        RegistrationModel model = new RegistrationModel();
//		FlexSession mySession= FlexContext.getFlexSession();
//		model.setIs_verified((String)mySession.getAttribute("is_verified"));
//		model.setEmailAddress((String)mySession.getAttribute("user_email"));
//		model.setOpenId((String)mySession.getAttribute("user_openid"));
//		model.setFullName((String)mySession.getAttribute("user_fullname"));
//		model.setRole((String)mySession.getAttribute("user_role"));

        model.setIs_verified(("is_verified"));

        model.setEmailAddress(("user_email"));
        model.setOpenId(("user_openid"));
        model.setFullName(("user_fullname"));
        model.setRole(("user_role"));


        //TODO sign and encrypt model
        return model;

    }

    /**
     * @deprecated Logout handled by JSF LoginBean This would actually fail in
     *             the servlet with JSF context unless we have the request or JSF context
     *             can be used Must set attribute of new session with is_verified false
     */
    public void doLogout() {
//		FlexSession mySession= FlexContext.getFlexSession();
        Map<String, String> params = new HashMap<String, String>();
        params.put("is_verified", "false");
        //init the new session and with the following params

        UserSessionService.sessionInit(true, params);

        //TODO control is_verified variable from Registration Model
    }

    public String getCurrentIP() {
        String hostname = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostAddress();

        } catch (UnknownHostException e) {
        }
        return hostname;
    }
    //TODO: TestLogin

    void main() {
        LoginServiceImpl logserv = new LoginServiceImpl();
        String openIdTest = ConfigurationService.getInstance().getProperty("openid.test.url", "http://kmt23.salzburgresearch.at:4545/idp/u=bob");
        System.out.println("LoginServiceImpl  openid.test.url  " + openIdTest);
        System.out.println("LoginServiceImpl  getEndpointApplicationHome " + LoginUtils.getEndpointApplicationHome());
        System.out.println("LoginServiceImpl  getEndpointLoginPage " + LoginUtils.getEndpointLoginPage());
        String redirect = logserv.createRedirect(openIdTest);
        System.out.println("LoginServiceImpl " + redirect);

    }
}
