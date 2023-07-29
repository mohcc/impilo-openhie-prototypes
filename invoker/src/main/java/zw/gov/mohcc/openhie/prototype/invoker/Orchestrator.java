package zw.gov.mohcc.openhie.prototype.invoker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Task;
import zw.gov.mohcc.mrs.commons.data.Identifiable;
import zw.gov.mohcc.mrs.facility.domain.Facility;
import zw.gov.mohcc.mrs.history.data.PersonInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryInvestigation;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.mrs.provider.domain.Laboratory;
import zw.gov.mohcc.mrs.terminology.enumeration.Gender;
import zw.gov.mohcc.mrs.terminology.enumeration.InvestigationOrderStatus;
import zw.gov.mohcc.mrs.terminology.enumeration.LabLocation;
import zw.gov.mohcc.openhie.fhir.api.translator.EncounterTranslator;
import zw.gov.mohcc.openhie.fhir.api.translator.LocationTranslator;
import zw.gov.mohcc.openhie.fhir.api.translator.PatientTranslator;
import zw.gov.mohcc.openhie.fhir.api.translator.ServiceRequestTranslator;
import zw.gov.mohcc.openhie.fhir.api.translator.TaskTranslator;
import static zw.gov.mohcc.openhie.prototype.invoker.Finder.client;

/**
 *
 * @author charles
 */
public class Orchestrator {
    
    
    public static final String LOCAL_HAPI_FHIR_URL = "http://localhost:8090/fhir/";

    public static final String HAPI_FHIR_URL = "http://197.221.242.150:10343/lims-fhir/";
    public static final String OPENHIM_FHIR_URL = "http://localhost:5001/fhir/";

    private static final List<Facility> FACILITIES = getImpiloFacilities();
    private static final List<Laboratory> LABORATORIES = getImpiloLaboraties();

    public static void main(String[] args) {
        
        zw.gov.mohcc.mrs.commons.data.reception.Person impiloPerson = getImpiloPerson();

        Laboratory laboratory = getImpiloLaboratory();
        Facility facility = getImpiloFacility();

        PersonInvestigation personInvestigation = getPersonInvestigation(impiloPerson.getPersonId());
        LaboratoryInvestigation laboratoryInvestigation = getLaboratoryInvestigation(personInvestigation, facility);
        LaboratoryRequestOrder laboratoryRequestOrder = getLaboratoryRequestOrder(laboratoryInvestigation, laboratory, facility);

        List<Resource> resources = new ArrayList<>();

        Location fhirFacilityLocation = LocationTranslator.toFhirResource(facility);
        Location fhirLabLocation = LocationTranslator.toFhirResource(laboratory);
        Patient fhirPatient = PatientTranslator.toFhirResource(impiloPerson);

        Encounter encounter = EncounterTranslator.toFhirResource(laboratoryRequestOrder);
        ServiceRequest serviceRequest = ServiceRequestTranslator.toFhirResource(laboratoryRequestOrder);
        Task task = TaskTranslator.toFhirResource(laboratoryRequestOrder);

        resources.add(fhirFacilityLocation);
        resources.add(fhirLabLocation);
        resources.add(fhirPatient);

        resources.add(encounter);
        resources.add(serviceRequest);
        resources.add(task);

        Bundle transactionBundle = new Bundle();
        transactionBundle.setType(Bundle.BundleType.TRANSACTION);
        for (Resource resource : resources) {
            Bundle.BundleEntryComponent component = transactionBundle.addEntry();
            component.setResource(resource);
            component.getRequest().setUrl(getRequestUrl(resource))
                    .setMethod(Bundle.HTTPVerb.PUT);
        }
        client.transaction().withBundle(transactionBundle).execute();

    }

    public static IGenericClient getClient(String baseUrl) {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(baseUrl);
    }

    private static LaboratoryRequestOrder getLaboratoryRequestOrder(LaboratoryInvestigation laboratoryInvestigation, Laboratory laboratory, Facility facility) {
        LaboratoryRequestOrder laboratoryRequestOrder = new LaboratoryRequestOrder();
        laboratoryRequestOrder.setLaboratoryRequestOrderId(UUID.randomUUID().toString());
        laboratoryRequestOrder.setLaboratoryInvestigation(laboratoryInvestigation);
        laboratoryRequestOrder.setLaboratory(new Identifiable(laboratory.getLaboratoryId(), laboratory.getName()));
        laboratoryRequestOrder.setFacility(new Identifiable(facility.getFacilityId(), facility.getName()));
        laboratoryRequestOrder.setLaboratoryRequestNumber("06070A-23-L-00001");
        laboratoryRequestOrder.setDateSampleTaken(LocalDate.now());
        return laboratoryRequestOrder;
    }

    private static LaboratoryInvestigation getLaboratoryInvestigation(PersonInvestigation personInvestigation, Facility facility) {

        LaboratoryInvestigation laboratoryInvestigation = new LaboratoryInvestigation();
        laboratoryInvestigation.setLaboratoryInvestigationId(UUID.randomUUID().toString());
        laboratoryInvestigation.setPersonInvestigation(personInvestigation);
        laboratoryInvestigation.setStatus(InvestigationOrderStatus.SENT_TO_LABORATORY);
        laboratoryInvestigation.setFacility(new Identifiable(facility.getFacilityId(), facility.getName()));
        return laboratoryInvestigation;
    }

    private static PersonInvestigation getPersonInvestigation(String personId) {
        PersonInvestigation personInvestigation = new PersonInvestigation();
        personInvestigation.setPersonInvestigationId(UUID.randomUUID().toString());
        personInvestigation.setPersonId(personId);
        personInvestigation.setInvestigationId("36069624-adee-11e7-b30f-3372a2d8551e");
        personInvestigation.setSample(new Identifiable("01", "Blood"));
        personInvestigation.setTest(new Identifiable("35ccdf6f-adee-11e7-b30f-3372a2d8551e", "Viral Load"));
        personInvestigation.setDate(LocalDate.now());
        return personInvestigation;
    }

    private static zw.gov.mohcc.mrs.commons.data.reception.Person getImpiloPerson() {
        Faker faker = new Faker();
        zw.gov.mohcc.mrs.commons.data.reception.Person impiloPerson = new zw.gov.mohcc.mrs.commons.data.reception.Person();
        impiloPerson.setPersonId(UUID.randomUUID().toString());
        impiloPerson.setBirthdate(LocalDate.now());
        impiloPerson.setFirstname(faker.name().firstName());
        impiloPerson.setLastname(faker.name().lastName());
        impiloPerson.setSex(new Random().nextBoolean() ? Gender.MALE : Gender.FEMALE);
        impiloPerson.setAddress(getImpiloAddress());
        return impiloPerson;
    }

    private static zw.gov.mohcc.mrs.commons.data.reception.Address getImpiloAddress() {
        zw.gov.mohcc.mrs.commons.data.reception.Address impiloAddress = new zw.gov.mohcc.mrs.commons.data.reception.Address();
        impiloAddress.setStreet("12345");
        impiloAddress.setCity("Mutare");
        return impiloAddress;
    }

    private static Facility getImpiloFacility() {
        return FACILITIES.get((int) (Math.random() * FACILITIES.size()));
    }

    private static Laboratory getImpiloLaboratory() {
        return LABORATORIES.get((int) (Math.random() * LABORATORIES.size()));
    }

    private static List<Facility> getImpiloFacilities() {
        List<Facility> impiloFacilities = new ArrayList<>();
        impiloFacilities.add(createImpiloFacility("ZW000A01", "ZW000A", "Avondale Clinic", "ZW00", 31.0291, -17.7883, "LixFsP9YMT9"));
        impiloFacilities.add(createImpiloFacility("ZW000A02", "ZW000A", "Arcadia Primary Care Clinic", "ZW00", 31.0546, -17.7883, "OBXlKhRkRIb"));
        impiloFacilities.add(createImpiloFacility("ZW000A05", "ZW000A", "Belvedere FHS", "ZW00", 31.0243, -17.8468, "UiPBTlrwap5"));
        impiloFacilities.add(createImpiloFacility("ZW000A07", "ZW000A", "Borrowdale Primary Care Clinic", "ZW00", 0.00, 0.00, "WaYcDd2d9pk"));
        impiloFacilities.add(createImpiloFacility("ZW000A08", "ZW000A", "Braeside FHS", "ZW00", 31.06571, -17.8399, "t0hAcwHFHPq"));
        impiloFacilities.add(createImpiloFacility("ZW000A09", "ZW000A", "Budiriro Polyclinic", "ZW00", 30.9354, -17.8912, "GdL5a5OSZ15"));
        impiloFacilities.add(createImpiloFacility("ZW000A0C", "ZW000A", "Beatrice Road Infectious Diseases Hospital", "ZW00", 31.0282, -17.8601, "MjDD6XQksTN"));
        return impiloFacilities;
    }

    private static Facility createImpiloFacility(String facilityId, String districtId, String name, String provinceId, Double longitude, Double latitude, String dhisCode) {
        Facility impiloFacility = new Facility();
        impiloFacility.setFacilityId(facilityId);
        impiloFacility.setDistrictId(districtId);
        impiloFacility.setName(name);
        impiloFacility.setProvinceId(provinceId);
        impiloFacility.setLongitude(longitude);
        impiloFacility.setLatitude(latitude);
        impiloFacility.setDhisCode(dhisCode);
        return impiloFacility;
    }

    private static List<Laboratory> getImpiloLaboraties() {
        List<Laboratory> impiloLaboratories = new ArrayList<>();
        impiloLaboratories.add(createImpiloLaboratory("002", "NMRL", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("10", "TSHELANYEMBA", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("100", "MUTARE ARMY", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("101", "MARONDERA", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("102", "MAHUSEKWA", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("103", "CHIVHU", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("104", "SADZA", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("105", "MT ST MARYS", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("106", "EPWORTH", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("107", "MUTOKO", LabLocation.EXTERNAL));
        impiloLaboratories.add(createImpiloLaboratory("108", "LOUISA GUIDOTTI", LabLocation.EXTERNAL));
        return impiloLaboratories;
    }

    private static Laboratory createImpiloLaboratory(String laboratoryId, String name, LabLocation location) {
        Laboratory impiloLaboratory = new Laboratory();
        impiloLaboratory.setLaboratoryId(laboratoryId);
        impiloLaboratory.setName(name);
        impiloLaboratory.setLocation(location);
        return impiloLaboratory;
    }

    public static String getRequestUrl(Resource resource) {
        return resource.fhirType() + "/" + resource.getIdElement().getIdPart();
    }

}
