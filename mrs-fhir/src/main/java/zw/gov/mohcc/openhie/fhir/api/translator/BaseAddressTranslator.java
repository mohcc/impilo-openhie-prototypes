package zw.gov.mohcc.openhie.fhir.api.translator;


import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;
import zw.gov.mohcc.openhie.fhir.FhirConstants;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BaseAddressTranslator {

    public static Optional<Extension> getImpiloAddressExtension(@Nonnull Address address) {
        return Optional.ofNullable(address.getExtensionByUrl(FhirConstants.IMPILO_FHIR_EXT_ADDRESS));
    }

    public static void addAddressExtension(@Nonnull Address address, @Nonnull String extensionProperty, @Nonnull String value) {
        if (value == null) {
            return;
        }

        getImpiloAddressExtension(address)
                .orElseGet(() -> address.addExtension().setUrl(FhirConstants.IMPILO_FHIR_EXT_ADDRESS))
                .addExtension(FhirConstants.IMPILO_FHIR_EXT_ADDRESS + "#" + extensionProperty, new StringType(value));
    }

    protected static void addAddressExtensions(Address address, zw.gov.mohcc.mrs.commons.data.reception.Address impiloAddress) {
        addAddressExtension(address, "street", impiloAddress.getStreet());
    }
}
