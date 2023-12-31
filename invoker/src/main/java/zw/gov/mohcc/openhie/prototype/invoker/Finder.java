package zw.gov.mohcc.openhie.prototype.invoker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.TaskStatus;

public class Finder {

    public static final IGenericClient client = getClient(Orchestrator.LOCAL_HAPI_FHIR_URL);
    private static final FhirContext fhirContext = FhirContext.forR4();

    private static Log log = LogFactory.getLog(Finder.class);

    private Finder(){
        
    }

    public enum ACTION {
        RETRIEVE_UPDATE_TASKS,
        RETRIEVE_REQUESTED_TASKS,
        GET_SPECIMEN,
        GET_TASK
    }

    public static void main(String[] args) {

        ACTION action = ACTION.GET_TASK;

        switch (action) {
            case GET_TASK:
                getTask();
                break;
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
    
    public static void getTask(){
        String taskId="4d7a41e8-f602-46ee-bcce-f7ec807ecbcd";
        printFhirResource(getTaskById(taskId));
    }

    public static void getSpecimenById(String specimenId) {
        Specimen specimen = (Specimen) getResourceById(specimenId, Specimen.class);
        printFhirResource(specimen);
    }
    
    public static Task getTaskById(String taskId) {
        return (Task) getResourceById(taskId, Task.class);
        
    }

    public static IBaseResource getResourceById(String resourceId, Class<? extends IBaseResource> theClass) {
        Bundle specimenBundle = client.search().forResource(theClass)
                .where(new TokenClientParam("_id").exactly().code(resourceId))
                .returnBundle(Bundle.class).execute();
        return specimenBundle.getEntryFirstRep().getResource();
    }

    public static void printTasks(Bundle taskBundle) {
        for (Iterator<Bundle.BundleEntryComponent> resources = taskBundle.getEntry().iterator(); resources.hasNext();) {
            Resource resource = resources.next().getResource();
            if (resource instanceof Task) {
                Task fhirTask = (Task) resource;
                log.info("For.ResourceType=" + fhirTask.getFor().getReferenceElement().getResourceType());
                log.info("For.ResourceId="+ fhirTask.getFor().getReferenceElement().getIdPart());
                printFhirResource(fhirTask);
            }else if(resource instanceof Patient){
                log.info("Printing Patient");
                Patient fhirPatient=(Patient)resource;
                printFhirResource(fhirPatient);
            }

        }
    }

    public static void printFhirResource(IBaseResource resource) {
       log.info(fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource));
    }

    private static IGenericClient getClient(String baseUrl) {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(baseUrl);
    }

}
