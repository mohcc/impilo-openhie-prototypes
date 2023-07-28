package zw.gov.mohcc.openhie.prototype.invoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.aspectj.weaver.Dump;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import zw.gov.mohcc.openhie.fhir.api.util.ReferenceUtils;
import static zw.gov.mohcc.openhie.prototype.invoker.Finder.client;
import static zw.gov.mohcc.openhie.prototype.invoker.Orchestrator.getRequestUrl;

public class ResultIssuer {
    

    public static void main(String[] args) {
        
        String taskId="";

        Task task = Finder.getTaskById(taskId);

        issueResult(task);
    }

    public static void issueResult(Task task) {
        
        List<Resource> fhirResources=new ArrayList<>();
        
        task.setStatus(Task.TaskStatus.COMPLETED);
        Task.TaskOutputComponent output = new Task.TaskOutputComponent();

        DiagnosticReport diagnosticReport = getDiagnosticReport(task);
        String diagnosticReportId = diagnosticReport.getId();
        Reference diagnosticReportReference = ReferenceUtils.getDiagnosticReportReference(diagnosticReportId);
        output.setValue(diagnosticReportReference);
        task.setOutput(Collections.singletonList(output));

        Observation observation = getObservation(task);

        String observationId = observation.getId();
        Reference observationReference = ReferenceUtils.getObservationReference(observationId);
        diagnosticReport.setResult(Collections.singletonList(observationReference));
        
        fhirResources.add(observation);
        fhirResources.add(diagnosticReport);
        fhirResources.add(task);
        
       
        saveFhirResources(fhirResources);
        
        
    }

    public static DiagnosticReport getDiagnosticReport(Task task) {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReport.setCode(new CodeableConcept(new Coding("http://loinc.org", "22748-8", "")));
        return diagnosticReport;
    }

    //Analysis
    public static Observation getObservation(Task task) {
        Observation observation = new Observation();
        observation.setId(UUID.randomUUID().toString());
        observation.setSubject(task.getFor());
        //Add Test Analysis Code
        observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "22748-8", "")));
        observation.setValue(new Quantity().setValue(55).setUnit("UI/L"));
        return observation;
    }
    
    public static void saveFhirResources(List<Resource> resources){
        
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

}
