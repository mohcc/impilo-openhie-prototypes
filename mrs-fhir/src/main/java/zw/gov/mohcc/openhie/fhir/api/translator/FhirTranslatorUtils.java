package zw.gov.mohcc.openhie.fhir.api.translator;

import java.util.Date;

public class FhirTranslatorUtils {

    public static Date getLastUpdated(Object impiloObject) {
        return null;
    }

    public static String getSiteScopedId(Object objectId, String siteId) {
        return objectId + "-" + siteId;
    }
}
