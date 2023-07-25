package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.Enumerations;
import zw.gov.mohcc.mrs.terminology.enumeration.Gender;

import javax.annotation.Nonnull;

public class GenderTranslator {

    public static Enumerations.AdministrativeGender toFhirResource(@Nonnull Gender gender) {
        switch (gender) {
            case MALE:
                return Enumerations.AdministrativeGender.MALE;
            case FEMALE:
                return Enumerations.AdministrativeGender.FEMALE;
            case UNKNOWN:
                return Enumerations.AdministrativeGender.UNKNOWN;
            case OTHER:
            default:
                return Enumerations.AdministrativeGender.OTHER;
        }
    }
}
