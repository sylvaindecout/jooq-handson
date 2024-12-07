package fr.sdecout.handson.rest

import fr.sdecout.handson.rest.shared.AddressField
import fr.sdecout.handson.rest.shared.AuthorField
import fr.sdecout.handson.rest.shared.BookField
import fr.sdecout.handson.rest.shared.LibraryField

val DOSTOEVSKY = AuthorField(
    id = "286ee903-b96e-4a80-bb9c-863d11c3fa48",
    firstName = "Fyodor",
    lastName = "Dostoevsky",
)

val CLEMENTINE_BERJAUD = AuthorField(
    id = "7fca93e6-7aa1-4a06-a63e-e09d5e438d3c",
    firstName = "Clémentine",
    lastName = "Berjaud",
)

val AF_TAICLET = AuthorField(
    id = "92563dcc-89bb-4548-92ae-e07e8d30ca3d",
    firstName = "Anne-France",
    lastName = "Taiclet",
)

val THIBAUD_BONCOURT = AuthorField(
    id = "97e74b2e-0347-4249-8784-32bd87423e54",
    firstName = "Thibaud",
    lastName = "Boncourt",
)

val JULIEN_FRETEL = AuthorField(
    id = "87d01761-5b99-49a9-af70-3cd828d1eeef",
    firstName = "Julien",
    lastName = "Fretel",
)

val DANIEL_GAXIE = AuthorField(
    id = "c0cea35b-7b97-4425-9284-7c9c6fccb0e5",
    firstName = "Daniel",
    lastName = "Gaxie",
)

val CRIME_AND_PUNISHMENT = BookField(
    isbn = "978-0-67-973450-5",
    title = "Crime and Punishment",
    authors = mutableListOf(DOSTOEVSKY),
)

val THE_IDIOT = BookField(
    isbn = "978-0-37-570224-2",
    title = "The idiot",
    authors = mutableListOf(DOSTOEVSKY),
)

val LES_SENS_DU_VOTE = BookField(
    isbn = "978-2-75-354759-9",
    title = "Les sens du vote",
    authors = mutableListOf(CLEMENTINE_BERJAUD, AF_TAICLET, THIBAUD_BONCOURT, JULIEN_FRETEL, DANIEL_GAXIE),
)

val BNF = LibraryField(
    id = "54b8aff7-616c-433a-ab0e-02b1c3f6df2c",
    name = "BNF",
    address = AddressField(
        line1 = "Quai François-Mauriac",
        line2 = "Cedex 13",
        postalCode = "75706",
        city = "Paris",
    ),
)

val VILLON = LibraryField(
    id = "9ad7b7f8-a624-40b7-b7dd-5134135c065f",
    name = "Bibliothèque François Villon",
    address = AddressField(
        line1 = "81, boulevard de la Villette",
        postalCode = "75010",
        city = "Paris",
    ),
)

val CABANIS = LibraryField(
    id = "94e664d8-7525-4930-bf22-fe8f353c9c73",
    name = "Médiathèque José Cabanis",
    address = AddressField(
        line1 = "1 Allée Jacques Chaban-Delmas",
        postalCode = "31500",
        city = "Toulouse",
    ),
)
