package zw.gov.mohcc.openhie.fhir.api.translator;

import java.util.Collections;
import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ServiceRequest;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.openhie.fhir.FhirConstants;
import zw.gov.mohcc.openhie.fhir.api.util.LabOrderUtils;
import zw.gov.mohcc.openhie.fhir.api.util.ReferenceUtils;

public class ServiceRequestTranslator {

    public static ServiceRequest toFhirResource(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {
        
        String serviceRequestId=LabOrderUtils.getServiceRequestId(laboratoryRequestOrder);
        
       
        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setId(serviceRequestId);

        serviceRequest.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);

        serviceRequest.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "10351-5", "Sample and Test")));

        serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);

        serviceRequest.setSubject(ReferenceUtils.getPatientReference(laboratoryRequestOrder));

        serviceRequest.setEncounter(ReferenceUtils.getEncounterReference(laboratoryRequestOrder));

        //This shld be the practictioner (logged in)
        //serviceRequest.setRequester(providerReferenceTranslator.toFhirResource(order.getOrderer()));

        //I can set the laboratory here:: TODO:: Confirm with DIGI
        serviceRequest.setLocationReference(Collections.singletonList(ReferenceUtils.getLabLocationReference(laboratoryRequestOrder)));

        Identifier labRequestNumIdentifier=serviceRequest.addIdentifier();
        labRequestNumIdentifier.setValue(laboratoryRequestOrder.getLaboratoryRequestNumber()).setId(serviceRequestId);
        labRequestNumIdentifier.setSystem(FhirConstants.IMPILO_SYSTEM);
        labRequestNumIdentifier.setType(new CodeableConcept(new Coding().setCode("LAB_REQUEST_NUM"))
                    .setText("Laboratory Request Number"));
        
        return serviceRequest;
    }
}
