package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.Address;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import javax.annotation.Nonnull;

public class PersonAddressTranslator {


    private PersonAddressTranslator(){
        
    }


    public static Address toFhirResource(@Nonnull Person person) {

        if (person.getAddress() == null) {
            return null;
        }

        Address fhirAddress = new Address();
        fhirAddress.setId(person.getPersonId());
        fhirAddress.setCity(person.getAddress().getCity());
        fhirAddress.setState(null);
        fhirAddress.setCountry(null);
        fhirAddress.setDistrict(null);
        fhirAddress.setPostalCode(null);

      
        fhirAddress.setUse(Address.AddressUse.OLD);

        BaseAddressTranslator.addAddressExtensions(fhirAddress, person.getAddress());

        return fhirAddress;
    }
}
