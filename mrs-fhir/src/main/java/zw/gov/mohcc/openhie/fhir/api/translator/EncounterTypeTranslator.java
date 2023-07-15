package zw.gov.mohcc.openhie.fhir.api.translator;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeableConcept;
import zw.gov.mohcc.mrs.history.data.PersonInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.openhie.fhir.FhirConstants;

public class EncounterTypeTranslator {

    public static List<CodeableConcept> toFhirResource(@Nonnull zw.gov.mohcc.mrs.consultation.data.Patient impiloPatient) {

        CodeableConcept code = new CodeableConcept();
        code.addCoding().setSystem(FhirConstants.ENCOUNTER_TYPE_SYSTEM_URI).setCode("QUEUE")
                .setDisplay("Queue");
        return Collections.singletonList(code);
    }

    public static List<CodeableConcept> toFhirResource(@Nonnull PersonInvestigation personInvestigation) {

        CodeableConcept code = new CodeableConcept();
        code.addCoding().setSystem(FhirConstants.ENCOUNTER_TYPE_SYSTEM_URI).setCode("INVESTIGATION")
                .setDisplay("Investigation");
        return Collections.singletonList(code);
    }

    public static List<CodeableConcept> toFhirResource(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {
        LaboratoryInvestigation laboratoryInvestigation = laboratoryRequestOrder.getLaboratoryInvestigation();
        PersonInvestigation personInvestigation = laboratoryInvestigation.getPersonInvestigation();
        return toFhirResource(personInvestigation);
    }

}
