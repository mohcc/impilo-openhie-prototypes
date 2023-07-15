package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.HumanName;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import javax.annotation.Nonnull;

public class PersonNameTranslator {

    public static HumanName toFhirResource(@Nonnull Person name) {

        HumanName humanName = new HumanName();
        humanName.setId(name.getPersonId());
        if (name.getFirstname() != null) {
            humanName.addGiven(name.getFirstname());
        }

        String middleName=null;

        if (middleName != null) {
            humanName.addGiven(middleName);
        }

        if (name.getLastname() != null) {
            humanName.setFamily(name.getLastname());
        }


        return humanName;
    }
}
