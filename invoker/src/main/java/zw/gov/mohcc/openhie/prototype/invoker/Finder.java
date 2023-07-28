package zw.gov.mohcc.openhie.prototype.invoker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import java.util.Iterator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.TaskStatus;

public class Finder {

    public static final IGenericClient client = getClient(Orchestrator.HAPI_FHIR_URL);
    private static final FhirContext fhirContext = FhirContext.forR4();

    public static enum ACTION {
        RETRIEVE_UPDATE_TASKS,
        RETRIEVE_REQUESTED_TASKS,
        GET_SPECIMEN
    }

    public static void main(String[] args) {

        ACTION action = ACTION.RETRIEVE_REQUESTED_TASKS;

        switch (action) {
            case RETRIEVE_REQUESTED_TASKS:
                retrieveRequestedTasks();
                break;
            case RETRIEVE_UPDATE_TASKS:
                retrieveUpdatedTasks();
                break;
            case GET_SPECIMEN:
                getSpecimen();
                break;
        }
    }

    public static void retrieveRequestedTasks() {
        Bundle taskBundle = client.search().forResource(Task.class)
                .where(Task.STATUS.exactly().code(TaskStatus.REQUESTED.toCode()))
                /*.include(Task.INCLUDE_SUBJECT)*/
                .returnBundle(Bundle.class)
                .execute();

        printTasks(taskBundle);
    }

    public static void retrieveUpdatedTasks() {
        Bundle taskBundle = client.search().forResource(Task.class)
                .where(Task.IDENTIFIER.hasSystemWithAnyCode("urn:impilo:uid:ZW06070A"))
                .where(Task.STATUS.exactly().codes(
                        TaskStatus.REJECTED.toCode(),
                        TaskStatus.ACCEPTED.toCode(),
                        TaskStatus.COMPLETED.toCode(),
                        TaskStatus.RECEIVED.toCode(),
                        TaskStatus.REJECTED.toCode()))
                .sort(new SortSpec("_lastUpdated", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        printTasks(taskBundle);

    }

    public static void retrieveRequestedTaskBySystem() {
        Bundle taskBundle = client.search().forResource(Task.class)
                .where(Task.IDENTIFIER.hasSystemWithAnyCode("urn:impilo:uid"))
                .where(Task.STATUS.exactly().code(TaskStatus.REQUESTED.toCode()))
                .returnBundle(Bundle.class)
                .execute();

        printTasks(taskBundle);
    }

    public static void getSpecimen() {
        String specimenId = "eedaf77b-f417-4455-b10d-ef18683daf61";
        getSpecimenById(specimenId);
    }

    public static void getSpecimenById(String specimenId) {
        Specimen specimen = (Specimen) getResourceById(specimenId, Specimen.class);
        printFhirResource(specimen);
    }
    
    public static Task getTaskById(String taskId) {
        return (Task) getResourceById(taskId, Task.class);
        
    }

    public static IBaseResource getResourceById(String id, Class<? extends IBaseResource> theClass) {
        String specimenId = "eedaf77b-f417-4455-b10d-ef18683daf61";
        Bundle specimenBundle = client.search().forResource(theClass)
                .where(new TokenClientParam("_id").exactly().code(specimenId))
                .returnBundle(Bundle.class).execute();
        return specimenBundle.getEntryFirstRep().getResource();
    }

    public static void printTasks(Bundle taskBundle) {
        for (Iterator resources = taskBundle.getEntry().iterator(); resources.hasNext();) {
            Resource resource = ((Bundle.BundleEntryComponent) resources.next()).getResource();
            if (resource instanceof Task) {
                Task fhirTask = (Task) resource;
                System.out.println("For.ResourceType=" + fhirTask.getFor().getReferenceElement().getResourceType());
                System.out.println("For.ResourceId=" + fhirTask.getFor().getReferenceElement().getIdPart());
                printFhirResource(fhirTask);
            }else if(resource instanceof Patient){
                System.out.println("Printing Patient");
                Patient fhirPatient=(Patient)resource;
                printFhirResource(fhirPatient);
            }

        }
    }

    public static void printFhirResource(IBaseResource resource) {
        System.out.println(fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource));
    }

    private static IGenericClient getClient(String baseUrl) {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(baseUrl);
    }

}
