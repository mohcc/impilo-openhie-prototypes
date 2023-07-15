package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import zw.gov.mohcc.openhie.fhir.FhirConstants;

import javax.annotation.Nonnull;
import static zw.gov.mohcc.openhie.fhir.api.translator.FhirTranslatorUtils.getSiteScopedId;

public class PractitionerTranslator {
    
    
    public static final String  SITE_ID="ZW00064";


    public static Practitioner toFhirResource(@Nonnull zw.gov.mohcc.mrs.domain.User user) {

        Practitioner practitioner = new Practitioner();
        practitioner.setId(getSiteScopedId(user.getId(), SITE_ID));

        Identifier userIdentifier = new Identifier();
        userIdentifier.setSystem(FhirConstants.IMPILO_FHIR_EXT_USER_IDENTIFIER);
        userIdentifier.setValue(getSiteScopedId(user.getId(), SITE_ID));
        practitioner.addIdentifier(userIdentifier);
        practitioner.addName(PractitionerNameTranslator.toFhirResource(user));
        
        practitioner.getMeta().setLastUpdated(null);

        return practitioner;
    }
}
