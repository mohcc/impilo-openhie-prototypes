package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.ContactPoint;
import zw.gov.mohcc.mrs.commons.data.reception.Phone;

import javax.annotation.Nonnull;

public class TelecomTranslator {


    public static ContactPoint toFhirResource(@Nonnull Phone phone) {
        if (phone == null) {
            return null;
        }
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setId(phone.getPhoneId());
        contactPoint.setValue(phone.getNumber());
        return contactPoint;
    }

}
