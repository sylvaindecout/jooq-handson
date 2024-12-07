package fr.sdecout.handson.rest.library;

import fr.sdecout.handson.rest.shared.AddressField;

public interface LibraryCreation {
    LibraryId addLibrary(String name, AddressField address);
}
