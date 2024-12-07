package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.rest.shared.AddressField;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEntity {

    @Column(name = "ADDRESS_LINE_1", nullable = false)
    public String line1;

    @Column(name = "ADDRESS_LINE_2")
    public String line2;

    @Column(nullable = false)
    public String postalCode;

    @Column(nullable = false)
    public String city;

    public static AddressEntity from(AddressField address) {
        var instance = new AddressEntity();
        instance.line1 = address.line1();
        instance.line2 = address.line2();
        instance.postalCode = address.postalCode();
        instance.city = address.city();
        return instance;
    }

    public AddressField toAddressField() {
        return new AddressField(line1, line2, postalCode, city);
    }

}
