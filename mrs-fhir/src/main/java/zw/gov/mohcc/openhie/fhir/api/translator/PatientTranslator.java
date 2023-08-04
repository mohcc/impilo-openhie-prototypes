package zw.gov.mohcc.openhie.fhir.api.translator;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import zw.gov.mohcc.mrs.commons.data.reception.Identification;
import zw.gov.mohcc.mrs.commons.data.reception.Person;
import zw.gov.mohcc.openhie.fhir.api.fakedao.FakeIdentificationDao;
import zw.gov.mohcc.openhie.fhir.api.fakedao.FakePhoneDao;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import zw.gov.mohcc.openhie.fhir.FhirConstants;

public class PatientTranslator {

    private PatientTranslator(){
    }


    public static Patient toFhirResource(zw.gov.mohcc.mrs.commons.data.reception.Person impiloPerson) {

        Patient patient = new Patient();
        patient.setId(impiloPerson.getPersonId());
        patient.setActive(true);

        List<Identification> identifications = FakeIdentificationDao.getIdentifications(impiloPerson);
        
       

        for (Identification identifier : identifications) {
            patient.addIdentifier(IdentifierTranslator.toFhirResource(identifier));
        }
        
        Identifier systemIdentifier= patient.addIdentifier();
        systemIdentifier.setValue(impiloPerson.getPersonId()).setId(impiloPerson.getPersonId());
        systemIdentifier.setSystem(FhirConstants.IMPILO_SYSTEM);
        systemIdentifier.setType(new CodeableConcept(new Coding().setCode("IMPILO_ID"))
                    .setText("Impilo internal ID"));


        patient.addName(PersonNameTranslator.toFhirResource(impiloPerson));

        if (impiloPerson.getSex() != null) {
            patient.setGender(GenderTranslator.toFhirResource(impiloPerson.getSex()));
        }

        patient.setBirthDateElement(BirthDateTranslator.toFhirResource(impiloPerson));

        
        patient.setDeceased(new BooleanType(false));
        


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
