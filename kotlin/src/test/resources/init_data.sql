-- Libraries

INSERT INTO library (id, name, address_line_1, address_line_2, postal_code, city)
VALUES ('54b8aff7-616c-433a-ab0e-02b1c3f6df2c', 'BNF', 'Quai François-Mauriac', 'Cedex 13', '75706', 'Paris');

INSERT INTO library (id, name, address_line_1, postal_code, city)
VALUES ('9ad7b7f8-a624-40b7-b7dd-5134135c065f', 'Bibliothèque François Villon', '81, boulevard de la Villette', '75010',
        'Paris');

INSERT INTO library (id, name, address_line_1, postal_code, city)
VALUES ('94e664d8-7525-4930-bf22-fe8f353c9c73', 'Médiathèque José Cabanis', '1 Allée Jacques Chaban-Delmas', '31500',
        'Toulouse');


-- Crime and Punishment

INSERT INTO author (id, first_name, last_name)
VALUES ('286ee903-b96e-4a80-bb9c-863d11c3fa48', 'Fyodor', 'Dostoevsky');

INSERT INTO book (isbn, title)
VALUES ('9780679734505', 'Crime and Punishment');

INSERT INTO book_author (book, author)
VALUES ('9780679734505', '286ee903-b96e-4a80-bb9c-863d11c3fa48');

INSERT INTO library_book (library, book)
VALUES ('54b8aff7-616c-433a-ab0e-02b1c3f6df2c', '9780679734505');

INSERT INTO library_book (library, book)
VALUES ('9ad7b7f8-a624-40b7-b7dd-5134135c065f', '9780679734505');

-- Les sens du vote

INSERT INTO author (id, first_name, last_name)
VALUES ('7fca93e6-7aa1-4a06-a63e-e09d5e438d3c', 'Clémentine', 'Berjaud'),
       ('92563dcc-89bb-4548-92ae-e07e8d30ca3d', 'Anne-France', 'Taiclet'),
       ('97e74b2e-0347-4249-8784-32bd87423e54', 'Thibaud', 'Boncourt'),
       ('87d01761-5b99-49a9-af70-3cd828d1eeef', 'Julien', 'Fretel'),
       ('c0cea35b-7b97-4425-9284-7c9c6fccb0e5', 'Daniel', 'Gaxie');

INSERT INTO book (isbn, title)
VALUES ('9782753547599', 'Les sens du vote');

INSERT INTO book_author (book, author)
VALUES ('9782753547599', '7fca93e6-7aa1-4a06-a63e-e09d5e438d3c'),
       ('9782753547599', '92563dcc-89bb-4548-92ae-e07e8d30ca3d'),
       ('9782753547599', '97e74b2e-0347-4249-8784-32bd87423e54'),
       ('9782753547599', '87d01761-5b99-49a9-af70-3cd828d1eeef'),
       ('9782753547599', 'c0cea35b-7b97-4425-9284-7c9c6fccb0e5');

