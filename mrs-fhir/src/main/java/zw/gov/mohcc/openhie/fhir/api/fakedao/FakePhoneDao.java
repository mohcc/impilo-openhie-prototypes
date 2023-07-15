package zw.gov.mohcc.openhie.fhir.api.fakedao;

import zw.gov.mohcc.mrs.commons.data.reception.Identification;
import zw.gov.mohcc.mrs.commons.data.reception.Person;
import zw.gov.mohcc.mrs.commons.data.reception.Phone;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FakePhoneDao {

    public static List<Phone> getPhones(Person person){
        return Arrays.asList(new Phone(UUID.randomUUID().toString(), "077772345", person));
    }
}
