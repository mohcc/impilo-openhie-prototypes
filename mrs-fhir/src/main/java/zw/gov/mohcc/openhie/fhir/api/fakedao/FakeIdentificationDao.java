package zw.gov.mohcc.openhie.fhir.api.fakedao;

import zw.gov.mohcc.mrs.commons.data.Identifiable;
import zw.gov.mohcc.mrs.commons.data.reception.Identification;
import zw.gov.mohcc.mrs.commons.data.reception.Person;

import java.util.*;

import static zw.gov.mohcc.openhie.fhir.api.impl.FhirPatientIdentifierSystemService.*;

public class FakeIdentificationDao {

    public static List<Identification> getIdentifications(Person person) {
        return Arrays.asList(new Identification(
                        UUID.randomUUID().toString(),
                        "NUM-" + new Random().nextInt(),
                        new Identifiable(PASSPORT, "Passport"),
                        person

                ),
                new Identification(
                        UUID.randomUUID().toString(),
                        "NUM-" + new Random().nextInt(),
                        new Identifiable(BIRTH_CERTIFICATE, "Birth Certificate"),
                        person

                ),
                new Identification(
                        UUID.randomUUID().toString(),
                        "NUM-" + new Random().nextInt(),
                        new Identifiable(NATIONAL_ID, "National ID"),
                        person

                ),
                new Identification(
                        UUID.randomUUID().toString(),
                        "NUM-" + new Random().nextInt(),
                        new Identifiable(DRIVERS_LICENCE, "Driver's Licence"),
                        person

                ));
    }
}
