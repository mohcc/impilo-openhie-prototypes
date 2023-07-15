package zw.gov.mohcc.openhie.fhir.api.translator;

import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.Period;
import zw.gov.mohcc.mrs.history.data.PersonInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.openhie.fhir.api.util.DateTimeUtils;

public class EncounterPeriodTranslator {

    public static Period toFhirResource(@Nonnull zw.gov.mohcc.mrs.consultation.data.Patient patient) {
        Period result = new Period();
        result.setStart(DateTimeUtils.convertToDate(patient.getTime()));
        return result;
    }

    public static Period toFhirResource(@Nonnull PersonInvestigation personInvestigation) {
        Period result = new Period();
        result.setStart(DateTimeUtils.convertToDate(personInvestigation.getDate()));
        return result;
    }

    public static Period toFhirResource(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {
        LaboratoryInvestigation laboratoryInvestigation = laboratoryRequestOrder.getLaboratoryInvestigation();
        PersonInvestigation personInvestigation = laboratoryInvestigation.getPersonInvestigation();
        return toFhirResource(personInvestigation);
    }

}
