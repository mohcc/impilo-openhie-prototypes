package zw.gov.mohcc.openhie.prototype.invoker;

import ca.uhn.fhir.rest.gclient.TokenClientParam;
import java.util.List;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import static zw.gov.mohcc.openhie.prototype.invoker.Finder.client;

public class ResultRetriever {

    public static void main(String[] args) {

        String taskId = "";

        Task task = Finder.getTaskById(taskId);

        retrieveResult(task);
    }

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
                        if (observation.getValue() instanceof Quantity) {
                            System.out.println("Quantity Result=" + observation.getValueQuantity().getValue());
                        } else if (observation.getValue() instanceof StringType) {
                            System.out.println("String Result=" + observation.getValueStringType().getValue());
                        } else if (observation.getValue() instanceof IntegerType) {
                            System.out.println("Integer Result=" + observation.getValueIntegerType().getValue());
                        } else if (observation.getValue() instanceof BooleanType) {
                            System.out.println("Boolean Result=" + observation.getValueBooleanType().getValue());
                        } else if (observation.getValue() instanceof Period) {
                            System.out.println("Start=" + observation.getValuePeriod().getStart());
                            System.out.println("End=" + observation.getValuePeriod().getEnd());
                        } else if (observation.getValue() instanceof Ratio) {
                            System.out.println("Denominator=" + observation.getValueRatio().getNumerator().getValue());
                            System.out.println("Denominator=" + observation.getValueRatio().getDenominator().getValue());
                        }

                    }
                }
            }
        }
    }

}
