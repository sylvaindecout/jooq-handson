package fr.sdecout.handson.rest.library;

import java.util.Optional;

public interface LibraryAccess {
    Optional<LibraryResponse> findLibrary(LibraryId id);
}
