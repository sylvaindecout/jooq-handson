package fr.sdecout.handson.rest;

import fr.sdecout.handson.rest.shared.AddressField;
import fr.sdecout.handson.rest.shared.AuthorField;
import fr.sdecout.handson.rest.shared.BookField;
import fr.sdecout.handson.rest.shared.LibraryField;

import java.util.List;

public final class TestData {

    private TestData() {}

    public static final AuthorField DOSTOEVSKY = new AuthorField(
            "286ee903-b96e-4a80-bb9c-863d11c3fa48",
            "Fyodor",
            "Dostoevsky"
    );

    public static final AuthorField CLEMENTINE_BERJAUD = new AuthorField(
            "7fca93e6-7aa1-4a06-a63e-e09d5e438d3c",
            "Clémentine",
            "Berjaud"
    );

    public static final AuthorField AF_TAICLET = new AuthorField(
            "92563dcc-89bb-4548-92ae-e07e8d30ca3d",
            "Anne-France",
            "Taiclet"
    );

    public static final AuthorField THIBAUD_BONCOURT = new AuthorField(
            "97e74b2e-0347-4249-8784-32bd87423e54",
            "Thibaud",
            "Boncourt"
    );

    public static final AuthorField JULIEN_FRETEL = new AuthorField(
            "87d01761-5b99-49a9-af70-3cd828d1eeef",
            "Julien",
            "Fretel"
    );

    public static final AuthorField DANIEL_GAXIE = new AuthorField(
            "c0cea35b-7b97-4425-9284-7c9c6fccb0e5",
            "Daniel",
            "Gaxie"
    );

    public static final BookField CRIME_AND_PUNISHMENT = new BookField(
            "978-0-67-973450-5",
            "Crime and Punishment",
            List.of(DOSTOEVSKY)
    );

    public static final BookField THE_IDIOT = new BookField(
            "978-0-37-570224-2",
            "The idiot",
            List.of(DOSTOEVSKY)
    );

    public static final BookField LES_SENS_DU_VOTE = new BookField(
            "978-2-75-354759-9",
            "Les sens du vote",
            List.of(CLEMENTINE_BERJAUD, AF_TAICLET, THIBAUD_BONCOURT, JULIEN_FRETEL, DANIEL_GAXIE)
    );

    public static final LibraryField BNF = new LibraryField(
            "54b8aff7-616c-433a-ab0e-02b1c3f6df2c",
            "BNF",
            new AddressField(
                    "Quai François-Mauriac",
                    "Cedex 13",
                    "75706",
                    "Paris"
            )
    );

    public static final LibraryField VILLON = new LibraryField(
            "9ad7b7f8-a624-40b7-b7dd-5134135c065f",
            "Bibliothèque François Villon",
            new AddressField(
                    "81, boulevard de la Villette",
                    null,
                    "75010",
                    "Paris"
            )
    );

    public static final LibraryField CABANIS = new LibraryField(
            "94e664d8-7525-4930-bf22-fe8f353c9c73",
            "Médiathèque José Cabanis",
            new AddressField(
                    "1 Allée Jacques Chaban-Delmas",
                    null,
                    "31500",
                    "Toulouse"
            )
    );

}
