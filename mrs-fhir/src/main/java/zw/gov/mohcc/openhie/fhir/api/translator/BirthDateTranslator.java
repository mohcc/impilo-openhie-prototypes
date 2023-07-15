package zw.gov.mohcc.openhie.fhir.api.translator;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.DateType;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import zw.gov.mohcc.openhie.fhir.api.util.DateTimeUtils;

public class BirthDateTranslator {

    public static DateType toFhirResource(@Nonnull Person person) {
        if (person.getBirthdate() == null) {
            return null;
        }

        Date personBirthDate = DateTimeUtils.convertToDate(person.getBirthdate());

        Boolean birthdateEstimated = null;

        if (birthdateEstimated != null && birthdateEstimated) {
            DateType dateType = new DateType();
            LocalDate now = LocalDate.now();
            // 5 years is the cut-off for WHO and CDC infant growth charts, so it seems like a convenient break between
            // "infant" and "child"
            if (Period.between(person.getBirthdate(), now).getYears() > 5) {
                dateType.setValue(personBirthDate, TemporalPrecisionEnum.YEAR);
            } else {
                dateType.setValue(personBirthDate, TemporalPrecisionEnum.MONTH);
            }

            return dateType;
        }

        return new DateType(personBirthDate);
    }

}
