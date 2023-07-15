package zw.gov.mohcc.openhie.fhir.api.translator;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import zw.gov.mohcc.mrs.laboratory.data.LaboratoryRequestOrder;
import zw.gov.mohcc.openhie.fhir.api.util.LabOrderUtils;
import zw.gov.mohcc.openhie.fhir.api.util.ReferenceUtils;

public class TaskTranslator {
    
    public static Task toFhirResource(@Nonnull LaboratoryRequestOrder laboratoryRequestOrder) {

        List<Reference> basedOnRefs = Collections.singletonList(ReferenceUtils.getServiceRequestReference(laboratoryRequestOrder));

        Reference forReference = ReferenceUtils.getPatientReference(laboratoryRequestOrder);

        Reference encounterRef = ReferenceUtils.getEncounterReference(laboratoryRequestOrder);

        Reference locationRef = ReferenceUtils.getFacilityLocationReference(laboratoryRequestOrder);


        Task task = new Task();
        task.setId(LabOrderUtils.getTaskId(laboratoryRequestOrder));
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setBasedOn(basedOnRefs);
        task.setFor(forReference);
        task.setEncounter(encounterRef);
        task.setLocation(locationRef);
        
        return task;
    }
}
