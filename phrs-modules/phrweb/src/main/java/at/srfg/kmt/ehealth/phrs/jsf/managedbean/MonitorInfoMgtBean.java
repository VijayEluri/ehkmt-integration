package at.srfg.kmt.ehealth.phrs.jsf.managedbean;

import at.srfg.kmt.ehealth.phrs.Constants;
import at.srfg.kmt.ehealth.phrs.PhrsConstants;
import at.srfg.kmt.ehealth.phrs.jsf.utils.WebUtil;
import at.srfg.kmt.ehealth.phrs.model.baseform.MonitorPhrItem;
import at.srfg.kmt.ehealth.phrs.model.baseform.PhrFederatedUser;
import at.srfg.kmt.ehealth.phrs.model.baseform.ProfileContactInfo;
import at.srfg.kmt.ehealth.phrs.persistence.client.PhrsStoreClient;
import at.srfg.kmt.ehealth.phrs.presentation.builder.ReportTool;
import at.srfg.kmt.ehealth.phrs.presentation.builder.ReportToolTransformer;
import at.srfg.kmt.ehealth.phrs.presentation.services.InteropProcessor;
import at.srfg.kmt.ehealth.phrs.presentation.services.ModelLabelValue;
import at.srfg.kmt.ehealth.phrs.presentation.services.UserService;
import at.srfg.kmt.ehealth.phrs.presentation.services.UserSessionService;
import at.srfg.kmt.ehealth.phrs.security.services.AuthorizationService;
import at.srfg.kmt.ehealth.phrs.support.test.CoreTestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// View scope complains about Audit client not serializable
@ManagedBean(name = "monmgtBean")
@RequestScoped
public class MonitorInfoMgtBean implements Serializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorInfoMgtBean.class);
    public final static String ROLEGROUP_MEDICAL = "ROLEGROUP_MEDICAL";
    protected UserService userService;
    protected InteropProcessor interopProcessor;
    protected AuthorizationService authorizationService;

    protected MonitorPhrItem selected = null;
    protected String selectedLocalResourceType;
    private String selectedOwnerUri;
    private String selectedTest;
    public ProfileContactInfo contactInfo;

    //results list
    protected List modelMain;
    protected List<ModelLabelValue> modelFormUserList;
    protected List<ModelLabelValue> modelFormLocalResources;
    private boolean testMode = false;
    private String showFormType = "";

    //the current user logged in wants to requrie
    private String requestorOwnerUri;
    private String ownerUri;
    private boolean hasMedicalRole = false;

    private String selectedOwnerGreetName;

    // for dialogs or forms to control view or edit mode of fields
    private String modify = AuthorizationService.MODIFY_YES;


    public MonitorInfoMgtBean() {
        userService = PhrsStoreClient.getInstance().getPhrsRepositoryClient().getUserService();
        reportTool = new ReportTool();
        requestorOwnerUri = userService.getOwnerUri();

        ownerUri = userService.getOwnerUri();

        hasMedicalRole = UserSessionService.sessionUserHasMedicalRole();

        if (hasMedicalRole) {
            showFormType = ROLEGROUP_MEDICAL;
        } else {
            showFormType = "";
            selectedOwnerGreetName = userService.getUserGreetName(ownerUri);

        }

        // consent.mode.roletest
//isAllHealthinfoAccessibleByRole  isHealthInfoAccessibleByRole

        CoreTestData.createTestUsersForMonitoring();

        modelMain = new ArrayList();
        //requestorOwnerUri=UserSessionService.getSessionAttributePhrId();
        initTools();

        initFormModel();

    }

    public MonitorInfoMgtBean(String requestorOwnerUri) {
        userService = PhrsStoreClient.getInstance().getPhrsRepositoryClient().getUserService();
        this.requestorOwnerUri = requestorOwnerUri;
        initTools();

        initFormModel();

    }

    private void initTools() {

        interopProcessor = PhrsStoreClient.getInstance().getInteropProcessor();
        authorizationService = new AuthorizationService();

    }

    public ModelLabelValue createUserLabelValue(PhrFederatedUser ph) {

        ModelLabelValue lv = new ModelLabelValue();

        lv.setId(ph.getOwnerUri());
        String greetname = userService.getUserGreetName(ph.getOwnerUri());
        greetname = greetname == null ? ph.getProtocolId() : greetname;
        lv.setLabel(greetname == null ? ph.getIdentifier() : greetname);

        return lv;
    }

    //UI action Need params: selected...
    public void findResourcesByUserAndType() {

        LOGGER.debug("findResourcesByUserAndType selectedOwnerUri=" + selectedOwnerUri + " selectedLocalResourceType=" + selectedLocalResourceType);
        initModelResults();
    }


    public void initModelResults() {
        ownerUri = userService.getOwnerUri();

        if (UserSessionService.sessionUserHasMedicalRole()) {
            showFormType = ROLEGROUP_MEDICAL;
        } else {
            selectedOwnerUri = ownerUri;
            showFormType = "";
        }

        modelMain = new ArrayList();


        testMode = selectedTest != null && ("true".equalsIgnoreCase(selectedTest));

        LOGGER.debug("initModelResults selectedOwnerUri" + selectedOwnerUri + " ownerUri=" + ownerUri + " selectedLocalResourceType=" + selectedLocalResourceType);

        if (selectedOwnerUri != null && selectedLocalResourceType != null) {

            boolean granted = false;

            if (ownerUri != null && selectedOwnerUri.equals(ownerUri)) {
                LOGGER.debug("selectedOwnerUri= ownerUri");
                granted = true;
                showFormType = "";


            } else {
                granted = grantPermissionForLocalResourceType(selectedOwnerUri, ownerUri, selectedLocalResourceType, true);
                if (! granted) {
                    LOGGER.debug("Granted false, try test patient 191. Initially choose ownerUri="+ownerUri);
                    //for testing, try this patient 191
                    granted = grantPermissionForLocalResourceType("191", ownerUri, selectedLocalResourceType, false);
                }
                selectedOwnerGreetName = userService.getUserGreetName(selectedOwnerUri);
            }
            LOGGER.debug("selectedOwnerUri= " + selectedOwnerUri + " granted=" + granted + " to access ownerUri " + ownerUri + " resourceType=" + selectedLocalResourceType + " selectedTest =" + selectedTest + "  testMode=" + testMode);
            if(granted){
                loadModelMainByUserAndResourceType(selectedOwnerUri, selectedLocalResourceType);
            }
            if (ownerUri != null && !ownerUri.equals(selectedOwnerUri)) {
                showGrantOutcomeMessage(granted);
            }

        } else {
            LOGGER.debug("Cant make query missing parameter selectedOwnerUri= " + selectedOwnerUri + " resourceType=" + selectedLocalResourceType + " selectedTest =" + selectedTest + "  testMode=" + testMode);

        }


    }

    public boolean isPrintable(){
        if(modelMain != null && ! modelMain.isEmpty())  return true;
        else return false;
    }

    private void loadModelMainByUserAndResourceType(String targetOwnerUri, String localResourceType) {
        if (targetOwnerUri != null && localResourceType != null) {
            int count = -1;

            if ("BW".equals(localResourceType)) {
                modelMain = userService.getResourcesVitalBodyWeight(targetOwnerUri);
                count = modelMain == null ? -1 : modelMain.size();
                LOGGER.debug("getResourcesVitalBodyWeight count=" + count);

            } else if ("BP".equals(localResourceType)) {
                modelMain = userService.getResourcesVitalBloodPressure(targetOwnerUri);
                count = modelMain == null ? -1 : modelMain.size();
                LOGGER.debug("getResourcesVitalBloodPressure count=" + count);

            } else if ("MED".equals(localResourceType)) {
                modelMain = userService.getResourcesMedication(targetOwnerUri);
                count = modelMain == null ? -1 : modelMain.size();
                LOGGER.debug("getResourcesMedication count=" + count);

            } else if ("PROBLEM".equals(localResourceType)) {
                modelMain = userService.getResourcesProblem(targetOwnerUri);
                count = modelMain == null ? -1 : modelMain.size();
                LOGGER.debug("getResourcesProblem count=" + count);

            } else if ("ADL".equals(localResourceType)) {
                modelMain = userService.getResourcesADL(targetOwnerUri);
                count = modelMain == null ? -1 : modelMain.size();
                LOGGER.debug("getResourcesADL count=" + count);
            } else if ("PDQ".equals(localResourceType)) {
                //no list model
                modelMain = null;
                contactInfo= userService.getProfileContactInfo(selectedOwnerUri);
                LOGGER.debug("contact info" );
            }
        }
        if (modelMain == null) modelMain = new ArrayList();

    }


    private String transformUIResourceType(String localResourceType) {
        if (localResourceType == null) return null;

        // BP  BW   MED   ADL   PROBLEM
        String result = null;
        if ("BP".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_BASIC_HEALTH;
        } else if ("BW".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_BASIC_HEALTH;
        } else if ("PDQ".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_BASIC_HEALTH;
        } else if ("MED".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_MEDICATION;
        } else if ("ADL".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_CONDITION;
        } else if ("PROB".equals(localResourceType)) {
            result = PhrsConstants.AUTHORIZE_RESOURCE_CODE_CONDITION;
        }
        return result;
    }

    public boolean grantPermissionForLocalResourceType(String targetOwnerUri, String requestorOwnerUri, String localResourceType, boolean phrId) {
        boolean granted = false;

        if (targetOwnerUri != null && requestorOwnerUri != null && targetOwnerUri.equals(requestorOwnerUri)) {

            granted = true;

        } else {
            String subjectRole = UserSessionService.getSessionAttributeRole();
            String resourceCode = transformUIResourceType(localResourceType);

            if (resourceCode != null && subjectRole != null && targetOwnerUri != null) {
                LOGGER.debug("PREPARE grantAccessByPhrIdAndRole by requestorUri="+requestorOwnerUri + " role=" + subjectRole + " on " + resourceCode + " targetUser=" + targetOwnerUri);
                if (phrId) {
                    granted = authorizationService.grantAccessByPhrIdAndRole(targetOwnerUri, resourceCode, "READ", subjectRole);
                } else {
                    granted = authorizationService.grantAccessByProtocolId(targetOwnerUri, resourceCode, "READ");
                }

            } else {
                LOGGER.debug("FAIL grantAccessByPhrIdAndRole NULL parameter found:  requestorUri="+requestorOwnerUri + " role=" + subjectRole + " on " + resourceCode + " targetUser=" + targetOwnerUri);

            }
        }

        return granted;
    }

    private void showGrantOutcomeMessage(boolean granted) {
        LOGGER.debug("showGrantOutcomeMessage "+granted);
        if (granted) {
            WebUtil.addFacesMessageSeverityInfo("Status", "Access granted, you are allowed access this material");
        } else {
            WebUtil.addFacesMessageSeverityWarn("Status", "Access denied ");
        }
    }


    private void initFormModel() {

        LOGGER.debug("START initFormModel ");

        try {


            modelFormUserList = new ArrayList<ModelLabelValue>();

            // if(if (UserSessionService.isSessionUser(requestorOwnerUri)) {

            //create pull down menu of users, but not for non-medical now...
            if (requestorOwnerUri != null) {

                if (!UserSessionService.sessionUserHasMedicalRole()) {
                    PhrFederatedUser ph = userService.getPhrUser(requestorOwnerUri);
                    LOGGER.debug("create model for pull down menu for only single user, this is not display on form");
                    if (ph != null) {
                        ModelLabelValue lv = createUserLabelValue(ph);
                        modelFormUserList.add(lv);
                    } else {
                        LOGGER.error("requestorOwnerUri non medical, is null, no user owner in session");
                    }
                  //getPhrUsersAll
                } else {
                    List<PhrFederatedUser> phrUsers = userService.getPhrUsersAll();//getResources(null, (Object)PhrFederatedUser.class);
                    if (phrUsers != null) {
                        for (PhrFederatedUser ph : phrUsers) {
                            //skip any reports for medical user
                            if (ph.getOwnerUri() != null) {
                                if (UserSessionService.sessionUserHasMedicalRole()) {
                                    if (ph.getOwnerUri().equals(requestorOwnerUri)) {
                                        //LOGGER.debug("skip report for requestorOwnerUri = "+requestorOwnerUri);
                                        continue;
                                    }
                                }

                                ModelLabelValue lv = createUserLabelValue(ph);
                                modelFormUserList.add(lv);
                            } else {
                                LOGGER.debug("PhrUser ownerUri NULL resourceUri "+ph.getResourceUri());

                            }
                        }
                    } else {
                        LOGGER.debug("create pull down menu for medical user, query results phrUsers are null  ");
                    }
                }
            } else {
                LOGGER.error("requestorOwnerUri is null, no user owner in session");
            }

            //create UI selection list
            modelFormLocalResources = new ArrayList<ModelLabelValue>();
            // BP  BW   MED   ADL   PROBLEM
            //hide contact information
            //modelFormLocalResources.add(new ModelLabelValue("PDQ", "Contact Information"));
            //

            modelFormLocalResources.add(new ModelLabelValue("BP", reportTool.getLabel("obsBloodPressureA01.label","Blood Pressure")));
            modelFormLocalResources.add(new ModelLabelValue("BW", reportTool.getLabel("obsBodyWeightBMW01.label","Body Weight")));
            modelFormLocalResources.add(new ModelLabelValue("MED", reportTool.getLabel("medicationSummary.label","Medications")));
            modelFormLocalResources.add(new ModelLabelValue("ADL", reportTool.getLabel("activityOfDailyLivingItem.activityCode.label","Activities of Daily Living")));
            modelFormLocalResources.add(new ModelLabelValue("PROBLEM", reportTool.getLabel("obsIssue.label","Problems")));
            //default
            if(selectedLocalResourceType ==null){
                selectedLocalResourceType="BP";//"PDQ";
            }
        } catch (Exception e) {
            LOGGER.error("initFormModel");
        }
        // this.internalModelList= buildView(list,this.permit, UserSessionService.getSessionAttributePhrId());


        LOGGER.debug("END initFormModel modelFormUserList size="+modelFormUserList.size());

    }

    private  ReportTool reportTool;
    public String getSelectedResourceType() {
        return selectedLocalResourceType;
    }

    public void setSelectedResourceType(String selectedLocalResourceType) {
        this.selectedLocalResourceType = selectedLocalResourceType;
    }

    public MonitorPhrItem getSelected() {
        return selected;
    }

    public void setSelected(MonitorPhrItem selected) {
        this.selected = selected;
    }


//    public String getStatusMessagePid() {
//        return statusMessage;
//    }
//
//    public void setStatusMessage(String statusMessage) {
//        this.statusMessage = statusMessage;
//    }
//public void addStatusMessagePID(String msg) {
//    statusMessage = statusMessage == null ? "" : statusMessage;
//}

    public String getModify() {
        return modify;
    }


    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }


    public void commandTest() {

        LOGGER.debug("commandTest()");
        WebUtil.addFacesMessageSeverityInfo("commandTest", "commandTest()");

    }

    public List getModelMain() {
        return modelMain;
    }

    public void setModelMain(List model) {
        modelMain = model;
    }

    public List<ModelLabelValue> getModelFormUserList() {
        return modelFormUserList;
    }

    public void setModelFormUserList(List model) {
        modelFormUserList = model;
    }

    public List<ModelLabelValue> getModelFormLocalResources() {
        return modelFormLocalResources;
    }

    public void setModelFormLocalResources(List<ModelLabelValue> modelFormLocalResources) {
        this.modelFormLocalResources = modelFormLocalResources;
    }

    public String getSelectedOwnerUri() {
        return selectedOwnerUri;
    }

    public void setSelectedOwnerUri(String selectedOwnerUri) {
        this.selectedOwnerUri = selectedOwnerUri;
    }

    public String getSelectedLocalResourceType() {
        return selectedLocalResourceType;
    }

    public void setSelectedLocalResourceType(String selectedLocalResourceType) {
        this.selectedLocalResourceType = selectedLocalResourceType;
    }

    public String getSelectedTest() {
        return selectedTest;
    }

    public void setSelectedTest(String selectedTest) {
        this.selectedTest = selectedTest;
    }


    public String getShowFormType() {
        return showFormType;
    }

    public void setShowFormType(String showFormType) {
        this.showFormType = showFormType;
    }

    public String getRequestorOwnerUri() {
        return requestorOwnerUri;
    }

    public void setRequestorOwnerUri(String requestorOwnerUri) {
        this.requestorOwnerUri = requestorOwnerUri;
    }

    public String getOwnerUri() {
        return ownerUri;
    }


    public boolean getHasMedicalRole() {
        return hasMedicalRole;
    }

    public boolean isHasMedicalRole() {
        return hasMedicalRole;
    }

    public String getSelectedOwnerGreetName() {
        return selectedOwnerGreetName;
    }

    public ProfileContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ProfileContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
