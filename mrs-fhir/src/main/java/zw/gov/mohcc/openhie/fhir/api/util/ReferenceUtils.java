package zw.gov.mohcc.openhie.fhir.api.util;

import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import zw.gov.mohcc.mrs.commons.data.reception.Person;
import zw.gov.mohcc.mrs.facility.domain.Facility;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.mrs.provider.domain.Laboratory;
import zw.gov.mohcc.openhie.fhir.FhirConstants;
import static zw.gov.mohcc.openhie.fhir.FhirConstants.IMPILO_SYSTEM;

public class ReferenceUtils {
    
    
    public static Reference getServiceRequestReference(LaboratoryRequestOrder laboratoryRequestOrder) {
        return getServiceRequestReference(LabOrderUtils.getServiceRequestId(laboratoryRequestOrder));
    }
    
   
    public static Reference getServiceRequestReference(String serviceRequestId) {
        return getReference(serviceRequestId, FhirConstants.SERVICE_REQUEST, true);
    }

    
    public static Reference getEncounterReference(LaboratoryRequestOrder laboratoryRequestOrder) {
        return getEncounterReference(LabOrderUtils.getEncounterId(laboratoryRequestOrder));
    }
    
   
    public static Reference getEncounterReference(String encounterId) {
        return getReference(encounterId, FhirConstants.ENCOUNTER, true);
    }
    
    public static Reference getPatientReference(Person person) {
        return getPatientReference(person.getPersonId());
    }
    
    public static Reference getPatientReference(LaboratoryRequestOrder laboratoryRequestOrder) {
        return getPatientReference(LabOrderUtils.getPatientId(laboratoryRequestOrder));
    }

    public static Reference getPatientReference(String personId) {
        return getReference(personId, FhirConstants.PATIENT, true);
    }
    
    public static Reference getFacilityLocationReference(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {
        return getLocationReference(LabOrderUtils.getFacilityId(laboratoryRequestOrder));
    }
    
    public static Reference getLabLocationReference(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {
        return getLocationReference(LabOrderUtils.getLaboratoryId(laboratoryRequestOrder));
    }

    public static Reference getLocationReference(@Nonnull Facility facility) {
        return getLocationReference(facility.getFacilityId());
    }
    
    public static Reference getLocationReference(@Nonnull Laboratory laboratory) {
        return getLocationReference(laboratory.getLaboratoryId());
    }

    public static Reference getLocationReference(String facilityId) {
        return getReference(facilityId, FhirConstants.LOCATION, true);
    }
    
    public static Reference getObservationReference(String observationId) {
        return getReference(observationId, FhirConstants.OBSERVATION, true);
    }
    
    public static Reference getDiagnosticReportReference(String diagnosticReportId) {
        return getReference(diagnosticReportId, FhirConstants.DIAGNOSTIC_REPORT, true);
    }

    public static Reference getReference(String id, String type, boolean hasReference) {
        Reference reference = new Reference();
        if (hasReference) {
            reference.setReference(type + "/" + id).setType(type);
        } else {
            reference.setType(type);
            Identifier identifier = new Identifier();
            identifier.setSystem(IMPILO_SYSTEM);
            identifier.setValue(id);
            identifier.setUse(Identifier.IdentifierUse.SECONDARY);
            reference.setIdentifier(identifier);
        }
        return reference;
    }

}
