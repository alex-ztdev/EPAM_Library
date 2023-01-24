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



CREATE TABLE Books(
	id bigint identity(1,1),
	title nvarchar(350) NOT NULL,
	publisher_id bigint NOT NULL,
	genre_id bigint NOT NULL,
	page_number int NOT NULL,
	publication_date date NOT NULL,
	isAvailable BIT DEFAULT 1 NOT NULL,
	isRemoved BIT DEFAULT 0 NOT NULL,

	CONSTRAINT PK_Books_id PRIMARY KEY(id),
	CONSTRAINT FK_Books_publisher_id FOREIGN KEY (publisher_id) REFERENCES Publishers(id),
	CONSTRAINT FK_Books_genre_id FOREIGN KEY(genre_id) REFERENCES Book_Genres(id),
	CONSTRAINT UQ_Books_Info_title_publ_genre UNIQUE(title, genre_id, page_number)
	--CONSTRAINT UQ_Books_publisher_book_info UNIQUE(book_info_id, publisher_id)
);


CREATE TABLE Authors(
	id bigint IDENTITY(1, 1),
	first_name nvarchar(50) NOT NULL,
	second_name nvarchar(50) NOT NULL,
	birth_date date NOT NULL,

	CONSTRAINT PK_Authors PRIMARY KEY(id),
	CONSTRAINT UQ_Authors_names_birthdate UNIQUE (first_name, second_name, birth_date)
);

CREATE TABLE Authors_Books(
	author_id bigint NOT NULL FOREIGN KEY REFERENCES Authors(id),
	book_id bigint NOT NULL FOREIGN KEY REFERENCES Books(id),

	CONSTRAINT PK_authors_books_id PRIMARY KEY(author_id, book_id)
);

--CREATE TABLE Books_Info(
--	id bigint IDENTITY(1, 1),
--	title nvarchar(350) NOT NULL,
--	page_number int NOT NULL,
--	genre_id bigint,
--	description nvarchar(500),

--	CONSTRAINT PK_Books_Info PRIMARY KEY(id),
--);



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


--GO
--CREATE PROCEDURE dbo.addUser
--    @login NVARCHAR(100), 
--    @password VARCHAR(50), 
--    @role_id bigint, 
--    @status_id bigint = 1, 
--    @email NVARCHAR(250),
--    @phone_number NVARCHAR(25) = NULL,
--    @first_name NVARCHAR(50),
--    @second_name NVARCHAR(50),
--    @birth_date date
--AS
--BEGIN
--    SET NOCOUNT ON
--	declare @responseMessage NVARCHAR(250)
--    BEGIN TRY
--
--        INSERT INTO dbo.[Users] 
--		VALUES
--			(@login, master.dbo.fn_varbintohexstr(HASHBYTES('SHA2_512', @password)), @role_id, @status_id,
--			@email, @phone_number, @first_name, @second_name,@birth_date )
--    END TRY
--    BEGIN CATCH
--	SET @responseMessage= ERROR_MESSAGE()
--        RAISERROR(@responseMessage, 16, 1);
--    END CATCH
--END

--SELECT * FROM Authors


--SELECT book_info_id, COUNT(book_info_id) FROM Books
--INNER JOIN Books_Info ON Books.book_info_id = Books_Info.id
--INNER JOIN Publishers ON publisher_id = Publishers.id
--WHERE Books.isAvailable = 0
--GROUP BY book_info_id







--SELECT * FROM Orders
--INNER JOIN Books ON  Orders.book_id = Books.id
--INNER JOIN Books_Info ON Books.book_info_id = Books_Info.id
--INNER JOIN Book_Genres ON Books_Info.genre_id = Book_Genres.id
--INNER JOIN Publishers ON Books.publisher_id = Publishers.id
--INNER JOIN Authors_Books ON Books_Info.id = Authors_Books.book_info_id
--INNER JOIN Authors ON Authors_Books.author_id = Authors.id
--INNER JOIN Users ON user_id = Users.id
--WHERE USers.login = '123'


SELECT 
	Books.id,
	Books.title,
	Publishers.title AS publisher,
	Book_Genres.title as book_title,
	Books.page_number,
	Books.publication_date,
	Books.isAvailable
FROM Books 
INNER JOIN Publishers ON Books.publisher_id = Publishers.id
INNER JOIN Book_Genres ON Books.genre_id = Book_Genres.id


SELECT * FROM  Authors
INNER JOIN Authors_Books ON Authors_Books.author_id  = Authors.id
WHERE id = 12


SELECT * FROM Book_Genres WHERE title='drama'

select * from Publishers

SELECT * FROM Books


SELECT * FROM Authors
ORDER BY id

SELECT author_id FROM Authors_Books
WHERE book_id = 100


SELECT id, login, password, role_id, status_id, email, phone_number, first_name, second_name
FROM Users WHERE id=10000

SELECT* FROM Users

SELECT * FROM Orders

SELECT 
	id, 
	user_id,
	book_id,
	order_start_date,
	order_end_date,
	actual_return_date
FROM Orders


SELECT * FROM Users WHERE phone_number=''

UPDATE USERS SET status_id = 2 WHERE login = 'user'

