package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import zw.gov.mohcc.mrs.commons.data.reception.Identification;
import zw.gov.mohcc.mrs.commons.data.reception.Person;
import zw.gov.mohcc.openhie.fhir.api.fakedao.FakeIdentificationDao;
import zw.gov.mohcc.openhie.fhir.api.fakedao.FakePhoneDao;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PatientTranslator {


    public static Patient toFhirResource(zw.gov.mohcc.mrs.commons.data.reception.Person impiloPerson) {

        Patient patient = new Patient();
        patient.setId(impiloPerson.getPersonId());
        patient.setActive(true);

        List<Identification> identifications = FakeIdentificationDao.getIdentifications(impiloPerson);

        for (Identification identifier : identifications) {
            patient.addIdentifier(IdentifierTranslator.toFhirResource(identifier));
        }


        patient.addName(PersonNameTranslator.toFhirResource(impiloPerson));

        if (impiloPerson.getSex() != null) {
            patient.setGender(GenderTranslator.toFhirResource(impiloPerson.getSex()));
        }

        patient.setBirthDateElement(BirthDateTranslator.toFhirResource(impiloPerson));

        boolean dead = false;
        Date deathDate = null;


        if (dead) {
            if (deathDate != null) {
                patient.setDeceased(new DateTimeType(deathDate));
            } else {
                patient.setDeceased(new BooleanType(true));
            }
        } else {
            patient.setDeceased(new BooleanType(false));
        }


        patient.addAddress(PersonAddressTranslator.toFhirResource(impiloPerson));
        patient.setTelecom(getPatientContactDetails(impiloPerson));
        patient.getMeta().setLastUpdated(FhirTranslatorUtils.getLastUpdated(impiloPerson));

        return patient;
    }

    public static List<ContactPoint> getPatientContactDetails(@Nonnull Person person) {
        return FakePhoneDao.getPhones(person)
                .stream().map(TelecomTranslator::toFhirResource).collect(Collectors.toList());
    }


}
