package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.HumanName;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import javax.annotation.Nonnull;

public class PersonNameTranslator {

    private PersonNameTranslator(){
        
    }

    public static HumanName toFhirResource(@Nonnull Person person) {

        HumanName humanName = new HumanName();
        humanName.setId(person.getPersonId());
        if (person.getFirstname() != null) {
            humanName.addGiven(person.getFirstname());
        }

        if (person.getLastname() != null) {
            humanName.setFamily(person.getLastname());
        }


        return humanName;
    }
    
    
}
