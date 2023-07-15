package zw.gov.mohcc.openhie.fhir.api.impl;

import zw.gov.mohcc.mrs.commons.data.Identifiable;

import javax.annotation.Nonnull;

public class FhirPatientIdentifierSystemService {

    public static final String PASSPORT="2e68030e-adee-11e7-b30f-3372a2d8551e";
    public static final String BIRTH_CERTIFICATE="2e680554-adee-11e7-b30f-3372a2d8551e";

    public static final String NATIONAL_ID="2e68062d-adee-11e7-b30f-3372a2d8551e";

    public static final String DRIVERS_LICENCE="2e6806da-adee-11e7-b30f-3372a2d8551e";

    public static String getUrlByPatientIdentifierType(@Nonnull Identifiable patientIdentifierType) {
        String id=patientIdentifierType.getId();
        if(id.equals(PASSPORT)){
            return "moha:gov:zw:passport";
        }else if(id.equals(BIRTH_CERTIFICATE)){
            return "moha:gov:zw:bc";
        }else if(id.equals(NATIONAL_ID)){
            return "moha:gov:zw:birth:nid";
        }else if(id.equals(DRIVERS_LICENCE)){
            return "transcom:gov:zw:cvr";
        }else{
            throw new IllegalArgumentException("Invalid identifier type");
        }
    }



}
