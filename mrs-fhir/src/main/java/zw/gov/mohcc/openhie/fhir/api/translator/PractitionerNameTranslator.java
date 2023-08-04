package zw.gov.mohcc.openhie.fhir.api.translator;

import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.HumanName;
import static zw.gov.mohcc.openhie.fhir.api.translator.FhirTranslatorUtils.getSiteScopedId;


public class PractitionerNameTranslator {

    private PractitionerNameTranslator(){
        
    }
    
    public static final String  SITE_ID="ZW00064";
    
    public static HumanName toFhirResource(@Nonnull zw.gov.mohcc.mrs.domain.User user) {

        HumanName humanName = new HumanName();
        humanName.setId(getSiteScopedId(user.getId(), SITE_ID));
        if (user.getFirstName() != null) {
            humanName.addGiven(user.getFirstName());
        }

        if (user.getLastName() != null) {
            humanName.setFamily(user.getLastName());
        }


        return humanName;
    }
    
}
