package zw.gov.mohcc.openhie.fhir.api.translator;

import javax.annotation.Nonnull;
import org.hl7.fhir.r4.model.Location;
import zw.gov.mohcc.mrs.facility.domain.Facility;
import zw.gov.mohcc.mrs.provider.domain.Laboratory;

public class LocationTranslator {

    public static Location toFhirResource(@Nonnull Facility facility) {

        Location fhirLocation = new Location();
        Location.LocationPositionComponent position = new Location.LocationPositionComponent();
        fhirLocation.setId(facility.getFacilityId());
        fhirLocation.setName(facility.getName());
        fhirLocation.setDescription(null);

        if (facility.getLatitude() != null && facility.getLatitude() >= 0.0d) {
            position.setLatitude(facility.getLatitude());
        }

        if (facility.getLongitude() != null && facility.getLongitude() >= 0.0d) {
            position.setLongitude(facility.getLongitude());
        }

        fhirLocation.setPosition(position);

        /*if (openmrsLocation.getParentLocation() != null) {
			fhirLocation.setPartOf(createLocationReference(openmrsLocation.getParentLocation()));
		}*/
        fhirLocation.getMeta().setLastUpdated(null);

        return fhirLocation;
    }
    
    public static Location toFhirResource(@Nonnull Laboratory laboratory) {

        Location fhirLocation = new Location();
        fhirLocation.setId(laboratory.getLaboratoryId());
        fhirLocation.setName(laboratory.getName());
        fhirLocation.setDescription(null);
        fhirLocation.getMeta().setLastUpdated(null);
        return fhirLocation;
    }
}
