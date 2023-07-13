package zw.gov.mohcc.openhie.prototype.invoker.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import zw.gov.mohcc.openhie.prototype.invoker.util.FhirConstants;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static zw.gov.mohcc.openhie.prototype.invoker.util.FhirConstants.IMPILO_SYSTEM;

public class FhirClientInvoker {

    private static final Log LOG = LogFactory.getLog(FhirClientInvoker.class);

    public static final String SERVER_BASE = "http://localhost:8090/fhir";

    public static void main(String[] args) {
        IGenericClient client = getClient();
        createTask(client);
        LOG.info("Task created successfully");
    }


    public static Task createTask(IGenericClient client) {

        String orderUUid = UUID.randomUUID().toString();
        String patientUuid = UUID.randomUUID().toString();
        String systemUserUuid = UUID.randomUUID().toString();
        String encounterUuid = UUID.randomUUID().toString();
        String facilityUuid = UUID.randomUUID().toString();


        List<Reference> basedOnRefs = Collections.singletonList(
                newReference(orderUUid, FhirConstants.SERVICE_REQUEST));

        Reference forReference = newReference(patientUuid, FhirConstants.PATIENT);

        Reference ownerRef = newReference(systemUserUuid, FhirConstants.PRACTITIONER);

        Reference encounterRef = newReference(encounterUuid, FhirConstants.ENCOUNTER);

        Reference locationRef = newReference(facilityUuid, FhirConstants.LOCATION);


        Task task = new Task();
        task.setId(orderUUid);
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setBasedOn(basedOnRefs);
        task.setFor(forReference);
        task.setOwner(ownerRef);
        task.setEncounter(encounterRef);
        task.setLocation(locationRef);

        client.create().resource(task).execute();

        return task;
    }




    private static IGenericClient getClient() {
        FhirContext ctx = FhirContext.forR4();
        return ctx.newRestfulGenericClient(SERVER_BASE);
    }

    private static Reference newReference(String uuid, String type) {
        return newReference(uuid, type, false);
    }

    private static Reference newReference(String uuid, String type, boolean hasReference) {
        Reference reference = new Reference();
        if (hasReference) {
            reference.setReference(type + "/" + uuid).setType(type);
        }else {
            reference.setType(type);
            Identifier identifier = new Identifier();
            identifier.setSystem(IMPILO_SYSTEM);
            identifier.setValue(uuid);
            identifier.setUse(Identifier.IdentifierUse.SECONDARY);
            reference.setIdentifier(identifier);
        }
        return reference;
    }
}
