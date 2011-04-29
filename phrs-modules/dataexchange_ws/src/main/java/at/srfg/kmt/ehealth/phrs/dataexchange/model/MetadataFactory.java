/*
 * Project :iCardea
 * File : MetadataFactory.java 
 * Encoding : UTF-8
 * Date : Apr 21, 2011
 * User : Mihai Radulescu
 */
package at.srfg.kmt.ehealth.phrs.dataexchange.model;

/**
 * Used to create metadata that can be used to enrich the classes builded with 
 * the <code>ModelClassFactory</code>. </br>
 * This is a <i>util</i> class and it can not be instantiated. The only way to 
 * use it is trough its methods.
 * This class can not be extended. </br>
 * 
 * @version 0.1
 * @since 0.1
 * @author Mihai
 */
final class MetadataFactory {
    
    /**
     * Don't let anybody to instantiate this class.
     */
    private MetadataFactory() {
        // UNIMPLEMENTED
    }

    /**
     * Build a metatada able to express the Intravascular Diastolic (blood 
     * pressure) in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Intravascular Diastolic (blood 
     * pressure) in LOINC code system.
     */
    static DynamicPropertyMetadata buildDiastolicCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8462-4");

        return result;
    }

    /**
     * Build a metatada able to express the Intravascular Systolic (blood 
     * pressure) in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Intravascular Diastolic (blood 
     * pressure) in LOINC code system.
     */
    static DynamicPropertyMetadata buildSystolicCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8480-6");

        return result;
    }

    /**
     * Build a metatada able to express the Heart Beat
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Heart Beat in LOINC code system.
     */
    static DynamicPropertyMetadata buildHeartBeatCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8867-4");

        return result;
    }

    /**
     * Build a metatada able to express the Body Temperature
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Body Temperature
     * in LOINC code system.
     */
    static DynamicPropertyMetadata buildBodyTemperatureCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8310-5");

        return result;
    }

    /**
     * Build a metatada able to express the Body Height (measured) 
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Intravascular Diastolic (blood 
     * preasure) in LOINC code system.
     */
    static DynamicPropertyMetadata buildBodyHeightCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8302-2");

        return result;
    }

    /**
     * Build a metatada able to express the Body Height (in lying) 
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Body height (in lying) 
     * in LOINC code system.
     */
    static DynamicPropertyMetadata buildBodyHeightLynigCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8306-3");

        return result;
    }

    /**
     * Build a metatada able to express the Body Weight
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express Body Weight in LOINC code system.
     */
    static DynamicPropertyMetadata buildBodyWeightCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:3141-9");

        return result;
    }

    /**
     * Build a metatada able to express the Body Height (in lying)
     * in LOINC code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Body Height (in lying) in LOINC
     * code system.
     */
    static DynamicPropertyMetadata buildBodyHeightLyingCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("2.16.840.1.113883.6.1:8306-3");

        return result;
    }
    
    /**
     * Build a metatada able to express the Body Mass Index (BMI)
     * in UMTS code system. </br>
     * This metadata name is "code".
     * 
     * @return a metatada able to express the Body Height (in lying) in LOINC
     * code system.
     */
    static DynamicPropertyMetadata buildBodyMassIndexCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("code");
        result.setValue("UMLS:C1305855");

        return result;
    }
    
    

    /**
     * Builds an observation code annotation, properties annotated with this 
     * represents the code element from a observation (in the HL7 V.3 
     * assumption) following the "1.3.6.1.4.1.19376.1.5.3.1.4.5" profile. </br>
     * The name for this metadata is "isObservationCode" and its type is boolean.
     * 
     * @return an observation code annotation.
     */
    static DynamicPropertyMetadata buildIsObservationCode() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("isObservationCode");
        result.setValue(Boolean.TRUE);

        return result;
    }

    /**
     * Builds an observation value annotation, properties annotated with this 
     * represents the value element from a observation (in the HL7 V.3 
     * assumption) following the "1.3.6.1.4.1.19376.1.5.3.1.4.5" profile. </br>
     * The name for this metadata is "isObservationValue" and its type is boolean.
     * 
     * @return an observation code annotation.
     */
    static DynamicPropertyMetadata buildIsObservationValue() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("isObservationValue");
        result.setValue(Boolean.TRUE);

        return result;
    }

    /**
     * Builds an observation effective annotation, properties annotated with this 
     * represents the effective element from a observation (in the HL7 V.3 
     * assumption) following the "1.3.6.1.4.1.19376.1.5.3.1.4.5" profile. </br>
     * The name for this metadata is "isObservationEfectiveDate" and its type is boolean.
     * 
     * @return an observation code annotation.
     */
    static DynamicPropertyMetadata buildIsObservationEfectiveDate() {
        final DynamicPropertyMetadata result =
                new DynamicPropertyMetadata();
        // FIXME : use constants here
        result.setName("isObservationEfectiveDate");
        result.setValue(Boolean.TRUE);

        return result;
    }
}
