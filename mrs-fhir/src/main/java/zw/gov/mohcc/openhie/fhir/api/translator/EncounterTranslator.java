package zw.gov.mohcc.openhie.fhir.api.translator;

import java.util.Collections;
import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.r4.model.Reference;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.openhie.fhir.FhirConstants;
import zw.gov.mohcc.openhie.fhir.api.util.LabOrderUtils;
import zw.gov.mohcc.openhie.fhir.api.util.ReferenceUtils;

public class EncounterTranslator {

    public static Encounter toFhirResource(@Nonnull zw.gov.mohcc.mrs.consultation.data.Patient impiloPatient) {

        Encounter encounter = new Encounter();
        encounter.setId(impiloPatient.getPatientId());
        encounter.setStatus(Encounter.EncounterStatus.UNKNOWN);
        encounter.setType(EncounterTypeTranslator.toFhirResource(impiloPatient));

        encounter.setSubject(ReferenceUtils.getPatientReference(impiloPatient.getPersonId()));

        if (impiloPatient.getFacility() != null) {
            Reference reference = ReferenceUtils.getLocationReference(impiloPatient.getFacility().getId());
            EncounterLocationComponent encounterLocationComponent = new EncounterLocationComponent(reference);
            encounter.setLocation(Collections.singletonList(encounterLocationComponent));
        }

        encounter.setPeriod(EncounterPeriodTranslator.toFhirResource(impiloPatient));

        encounter.getMeta().addTag(FhirConstants.IMPILO_FHIR_EXT_ENCOUNTER_TAG, "encounter", "Encounter");
        encounter.getMeta().setLastUpdated(null);
        encounter.setClass_(null);

        return encounter;
    }

    public static Encounter toFhirResource(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {

        Encounter encounter = new Encounter();
        encounter.setId(LabOrderUtils.getEncounterId(laboratoryRequestOrder));
        encounter.setStatus(Encounter.EncounterStatus.UNKNOWN);
        encounter.setType(EncounterTypeTranslator.toFhirResource(laboratoryRequestOrder));

        encounter.setSubject(ReferenceUtils.getPatientReference(laboratoryRequestOrder));

        Reference reference = ReferenceUtils.getFacilityLocationReference(laboratoryRequestOrder);
        EncounterLocationComponent encounterLocationComponent = new EncounterLocationComponent(reference);
        encounter.setLocation(Collections.singletonList(encounterLocationComponent));

        encounter.setPeriod(EncounterPeriodTranslator.toFhirResource(laboratoryRequestOrder));

        encounter.getMeta().addTag(FhirConstants.IMPILO_FHIR_EXT_ENCOUNTER_TAG, "encounter", "Encounter");
        encounter.getMeta().setLastUpdated(null);
        encounter.setClass_(null);

        return encounter;
    }
}
