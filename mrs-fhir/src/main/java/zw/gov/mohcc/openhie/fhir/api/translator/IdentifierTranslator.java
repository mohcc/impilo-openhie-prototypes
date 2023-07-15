package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import zw.gov.mohcc.mrs.commons.data.Identifiable;
import zw.gov.mohcc.openhie.fhir.api.impl.FhirPatientIdentifierSystemService;

public class IdentifierTranslator {

    public static Identifier toFhirResource(zw.gov.mohcc.mrs.commons.data.reception.Identification impiloIdentification) {
        Identifier patientIdentifier = new Identifier();

        patientIdentifier.setValue(impiloIdentification.getNumber()).setId(impiloIdentification.getIdentificationId());

        boolean preferred = false;

        if (preferred) {
            patientIdentifier.setUse(Identifier.IdentifierUse.OFFICIAL);
        } else {
            patientIdentifier.setUse(Identifier.IdentifierUse.USUAL);
        }


        if (impiloIdentification.getType() != null) {
            Identifiable identifierType = impiloIdentification.getType();
            patientIdentifier.setSystem(FhirPatientIdentifierSystemService.getUrlByPatientIdentifierType(identifierType));
            patientIdentifier.setType(new CodeableConcept(new Coding().setCode(identifierType.getId()))
                    .setText(identifierType.getName()));
        }


        return patientIdentifier;

    }
}
