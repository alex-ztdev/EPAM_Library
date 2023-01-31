USE master

DROP DATABASE IF EXISTS Library

CREATE DATABASE Library
GO

use Library

CREATE TABLE Book_Genres(
                            id bigint identity(1, 1),
                            title nvarchar(50) NOT NULL

                                CONSTRAINT PK_Book_Genres PRIMARY KEY(id)
)

CREATE TABLE Publishers(
                           id bigint identity(1, 1),
                           title nvarchar(250)

                               CONSTRAINT PK_Publishers PRIMARY KEY (id)
);

CREATE TABLE Authors(
                        id bigint IDENTITY(1, 1),
                        first_name nvarchar(50) NOT NULL,
                        second_name nvarchar(50) NOT NULL,

                        CONSTRAINT PK_Authors PRIMARY KEY(id),
                        CONSTRAINT UQ_Authors_names UNIQUE (first_name, second_name)
);


CREATE TABLE Books(
                      id bigint identity(1,1),
                      title nvarchar(350) NOT NULL,
                      publisher_id bigint NOT NULL,
                      genre_id bigint NOT NULL,
                      author_id bigint NOT NULL,
                      page_number int NOT NULL,
                      publication_date date NOT NULL,

                      CONSTRAINT PK_Books_id PRIMARY KEY(id),
                      CONSTRAINT FK_Books_publisher_id FOREIGN KEY (publisher_id) REFERENCES Publishers(id),
                      CONSTRAINT FK_Books_genre_id FOREIGN KEY(genre_id) REFERENCES Book_Genres(id),
                      CONSTRAINT FK_Books_author_id FOREIGN KEY(author_id) REFERENCES Authors(id),
                      CONSTRAINT UQ_Books_Info_title_publ_genre UNIQUE(title,publisher_id, genre_id, author_id, page_number,publication_date )
);


CREATE TABLE Storage(
                        id bigint identity(1,1),
                        book_id bigint NOT NULL,
                        quantity int NOT NULL,
                        isRemoved BIT DEFAULT 0 NOT NULL,

                        CONSTRAINT PK_Storage_id PRIMARY KEY(id),
                        CONSTRAINT FK_Storage_book_id FOREIGN KEY (book_id) REFERENCES Books(id),
                        CONSTRAINT UQ_Storage_book_id UNIQUE(book_id)
)

CREATE TABLE Roles(
                      id bigint IDENTITY(1, 1),
                      title nvarchar(100) NOT NULL,

                      CONSTRAINT PK_Roles PRIMARY KEY(id),
                      CONSTRAINT UQ_Roles_title UNIQUE(title)
);

CREATE TABLE User_Statuses(
                              id bigint IDENTITY(1, 1),
                              title nvarchar(30) NOT NULL,

                              CONSTRAINT PK_User_statuses PRIMARY KEY (id),
                              CONSTRAINT UQ_User_statuses_title UNIQUE (title)
);

CREATE TABLE Users(
                      id bigint IDENTITY(10000, 1),
                      login nvarchar(100) NOT NULL,
                      password nvarchar(130) NOT NULL,
                      role_id bigint NOT NULL,
                      status_id bigint NOT NULL,
                      email nvarchar(250) NOT NULL,
                      phone_number nvarchar(25),
                      first_name nvarchar(50) NOT NULL,
                      second_name nvarchar(50) NOT NULL,

                      CONSTRAINT PK_Users PRIMARY KEY (id),
                      CONSTRAINT FK_Users_Role FOREIGN KEY (role_id) REFERENCES Roles(id),
                      CONSTRAINT FK_Users_Status FOREIGN KEY (status_id) REFERENCES User_statuses(id),
                      CONSTRAINT UQ_Users_login UNIQUE (login),
                      CONSTRAINT UQ_Users_Email UNIQUE (email),
);

CREATE UNIQUE INDEX UQ_Phone_number_ind
    ON Users(phone_number)
    WHERE phone_number IS NOT NULL
go

CREATE TABLE Orders (
                        id bigint identity(100000, 1),
                        user_id bigint NOT NULL,
                        book_id bigint NOT NULL,
                        order_start_date datetime NOT NULL,
                        order_end_date datetime NOT NULL,
                        actual_return_date datetime,

                        CONSTRAINT PK_Orders_id PRIMARY KEY (id),
                        CONSTRAINT UQ_Orders_users_books_id UNIQUE(user_id, book_id),
                        CONSTRAINT FK_users_in_orders_id FOREIGN KEY (user_id) REFERENCES Users(id),
                        CONSTRAINT FK_books_in_orders_id FOREIGN KEY (book_id) REFERENCES Books(id)
);
