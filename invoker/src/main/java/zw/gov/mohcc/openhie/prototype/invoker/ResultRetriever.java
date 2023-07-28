package zw.gov.mohcc.openhie.prototype.invoker;

import ca.uhn.fhir.rest.gclient.TokenClientParam;
import java.util.List;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Task;
import static zw.gov.mohcc.openhie.prototype.invoker.Finder.client;

public class ResultRetriever {

    public static void retrieveResult(Task task) {

        List<Task.TaskOutputComponent> output = task.getOutput();
        if (!output.isEmpty()) {

            Task.TaskOutputComponent outputRef = output.get(0);
            String diagnosticReportUuid = ((Reference) outputRef.getValue()).getReferenceElement()
                    .getIdPart();
            // Get Diagnostic Report and associated Observations (using include)
            Bundle diagnosticReportBundle = client.search().forResource(DiagnosticReport.class)
                    .where(new TokenClientParam("_id").exactly().code(diagnosticReportUuid))
                    .include(DiagnosticReport.INCLUDE_RESULT).include(DiagnosticReport.INCLUDE_SUBJECT)
                    .returnBundle(Bundle.class).execute();

            DiagnosticReport diagnosticReport = (DiagnosticReport) diagnosticReportBundle.getEntryFirstRep()
                    .getResource();
            diagnosticReport.getCode();

            for (Bundle.BundleEntryComponent entry : diagnosticReportBundle.getEntry()) {
                if (entry.hasResource()) {
                    if (ResourceType.Observation.equals(entry.getResource().getResourceType())) {
                        Observation observation = (Observation) entry.getResource();
                        System.out.println("Str Result=" + observation.getValue().toString());
                        System.out.println("Result=" + observation.getValueQuantity().getValue());

                    }
                }
            }
        }
    }

}
