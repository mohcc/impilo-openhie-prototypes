package zw.gov.mohcc.openhie.fhir.api.util;

import zw.gov.mohcc.mrs.history.data.PersonInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;

public class LabOrderUtils {

    public static String getPatientId(LaboratoryRequestOrder laboratoryRequestOrder) {
        LaboratoryInvestigation laboratoryInvestigation = laboratoryRequestOrder.getLaboratoryInvestigation();
        PersonInvestigation personInvestigation = laboratoryInvestigation.getPersonInvestigation();
        return personInvestigation.getPersonId();
    }

    public static String getEncounterId(LaboratoryRequestOrder laboratoryRequestOrder) {
        LaboratoryInvestigation laboratoryInvestigation = laboratoryRequestOrder.getLaboratoryInvestigation();
        PersonInvestigation personInvestigation = laboratoryInvestigation.getPersonInvestigation();
        return personInvestigation.getPersonInvestigationId();
    }

    public static String getLaboratoryId(LaboratoryRequestOrder laboratoryRequestOrder) {
        return laboratoryRequestOrder.getLaboratory().getId();
    }

    public static String getFacilityId(LaboratoryRequestOrder laboratoryRequestOrder) {
        LaboratoryInvestigation laboratoryInvestigation = laboratoryRequestOrder.getLaboratoryInvestigation();
        return laboratoryInvestigation.getFacility().getId();
    }
    
    public static String getServiceRequestId(LaboratoryRequestOrder laboratoryRequestOrder) {
        return  laboratoryRequestOrder.getLaboratoryRequestOrderId();
    }
    
    public static String getTaskId(LaboratoryRequestOrder laboratoryRequestOrder) {
        return  laboratoryRequestOrder.getLaboratoryRequestOrderId();
    }
    
    

}
