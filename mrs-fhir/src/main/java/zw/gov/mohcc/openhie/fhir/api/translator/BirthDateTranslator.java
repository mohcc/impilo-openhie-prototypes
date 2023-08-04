package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.DateType;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import javax.annotation.Nonnull;
import java.util.Date;
import zw.gov.mohcc.openhie.fhir.api.util.DateTimeUtils;

public class BirthDateTranslator {

    private BirthDateTranslator(){

    }

    public static DateType toFhirResource(@Nonnull Person person) {
        if (person.getBirthdate() == null) {
            return null;
        }

        Date personBirthDate = DateTimeUtils.convertToDate(person.getBirthdate());

        return new DateType(personBirthDate);
    }

}
