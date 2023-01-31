USE Library

INSERT INTO Book_Genres
VALUES
    ('Science fiction'),
    ('Satire'),
    ('Drama'),
    ('Action and Adventure'),
    ('Romance'),
    ('Mystery'),
    ('Horror'),
    ('Health'),
    ('Guide'),
    ('Diaries'),
    ('Comics'),
    ('Diaries'),
    ('Journals'),
    ('Biographies'),
    ('Fantasy'),
    ('History'),
    ('Science'),
    ('Art'),
    ('Other')



INSERT INTO Publishers
VALUES
    ('Harper Collins'),
    ('Simon & Schuster'),
    ('Macmillan'),
    ('Hachette'),
    ('Penguin Random House')

INSERT INTO Authors (first_name, second_name)
VALUES
    ('William', 'Howells'),
    ('Frederic', 'Brown'),
    ('Jack', 'London'),
    ('Albert', 'Blaisdell'),
    ('Ellis', 'Butler'),
    ('Arthur', 'Machen'),
    ('Titus', 'Lucretius'),
    ('Rabindranath', 'Tagore'),
    ('Isaac', 'Asimov'),
    ('Charles', 'Dickens'),
    ('Ralph', 'Emerson'),
    ('Dorothy', 'Canfield'),
    ('Givoanni', 'Boccaccio'),
    ('George', 'Orwell'),
    ('Publius', 'Ovid'),
    ('Robert', 'Stevenson'),
    ('Virginia', 'Woolf'),
    ('George', 'Eliot'),
    ('Amelia', 'Edwards'),
    ('Fyodor', 'Dostoevsky'),
    ('Emily', 'Dickinson'),
    ('Edna', 'Ferber'),
    ('Joseph', 'LeFanu'),
    ('John', 'DosPassos'),
    ('Ruth', 'Stuart'),
    ('Vladimir', 'Nabokov'),
    ('Johanna', 'Spyri'),
    ('Ernest', 'Dowson'),
    ('Mary', 'Foote'),
    ('Zane', 'Grey'),
    ('H. P.', 'Lovecraft'),
    ('Samuel', 'Pepys'),
    ('Kate', 'Sweetser'),
    ('William', 'Lampton'),
    ('Mother', 'Goose'),
    ('Eleanor', 'Abbott'),
    ('Kate', 'Quinn'),
    ('Dray', 'Stephanie'),
    ('Laura', 'Kamoie')




INSERT INTO Books (title, page_number,genre_id, publication_date, publisher_id, author_id)
VALUES
    ('A Daughter of the Snows', 199, 9, '2018-4-3', 5, 3),
    ('The Near East: 10,000 Years of History', 298, 13, '2018-10-8', 3, 9),
    ('The Cocoon: A Rest-Cure Comedy', 90, 12, '2015-2-9', 2, 25),
    ('The Freakshow Murders', 321, 3, '2020-9-18', 2, 2),
    ('Pharaohs,  Fellahs and Explorers', 367, 9, '2015-1-5', 3, 19),
    ('Hard Times', 293, 1, '2021-1-1', 2, 10),
    ('A Modern Instance', 222, 12, '2018-6-7', 4, 1),
    ('The Real Mother Goose', 88, 1, '2015-11-4', 1, 35),
    ('A Thousand Miles Up the Nile', 110, 18, '2018-4-7', 1, 19),
    ('Children of Blood and Bone', 137, 13, '2021-10-2', 1, 7),
    ('A pushcart at the curb', 95, 1, '2022-7-14', 1, 24),
    ('The Desert and the Sown', 126, 10, '2018-2-7', 3, 29),
    ('Three Soldiers', 223, 16, '2015-8-14', 4, 24),
    ('The End of Eternity', 168, 1, '2017-2-5', 5, 9),
    ('Annie Kilburn', 291, 12, '2022-9-19', 2, 1),
    ('A Touch of Sun and Other Stories', 141, 9, '2015-10-10', 3, 29),
    ('Show Boat', 151, 18, '2015-10-1', 2, 22),
    ('The Call of the Wild', 362, 18, '2018-3-18', 2, 3),
    ('My Mark Twain', 339, 9, '2018-8-7', 5, 1),
    ('Broken Ties', 134, 5, '2019-3-13', 2, 8),
    ('Short Stories From American History', 305, 8, '2018-6-14', 1, 4),
    ('Mrs Rosie and the Priest', 104, 14, '2015-1-20', 3, 13),
    ('So Big', 209, 3, '2020-10-1', 3, 22),
    ('Monsieur Maurice ', 92, 8, '2019-11-9', 2, 19),
    ('The Master of Ballantrae', 236, 13, '2016-11-3', 1, 16),
    ('The Unlived Life of Little Mary Ellen', 99, 6, '2018-11-1', 4, 25),
    ('Mouse - The Last Train', 184, 14, '2017-6-9', 4, 2),
    ('Edith Bonham', 122, 7, '2015-7-4', 3, 29),
    ('Maybe Mother Goose', 161, 14, '2020-3-7', 1, 35),
    ('The Noble Gases', 321, 5, '2020-4-2', 3, 9),
    ('Rainy Week', 98, 7, '2020-11-10', 4, 36),
    ('A Hazard of New Fortunes', 144, 4, '2017-3-8', 2, 1),
    ('A Plot for Murder', 108, 13, '2022-3-2', 5, 2),
    ('Nature', 357, 8, '2017-10-10', 3, 11),
    ('Hickory Dickory Dock', 326, 6, '2021-2-12', 4, 35),
    ('Big Fat Hen', 232, 12, '2018-9-11', 3, 35),
    ('The Lone Star Ranger', 328, 9, '2021-11-16', 4, 30),
    ('The Great Fire of London', 252, 9, '2021-6-6', 1, 32),
    ('White Nights', 265, 14, '2015-3-20', 3, 20),
    ('In a Glass Darkly', 173, 9, '2022-5-11', 1, 23),
    ('Fanny herself', 248, 10, '2020-9-10', 2, 22),
    ('Loaded', 273, 4, '2019-6-16', 2, 2),
    ('A Foregone Conclusion', 121, 15, '2022-11-3', 3, 1),
    ('The Hill of Dreams', 175, 6, '2021-4-1', 4, 6),
    ('Adventure', 217, 8, '2021-2-16', 5, 3),
    ('A Tagore Reader', 285, 4, '2018-3-20', 2, 8),
    ('A Tale of Two Cities', 331, 13, '2017-11-3', 2, 10),
    ('Dombey and Son', 344, 14, '2016-9-2', 3, 10),
    ('Famous Women', 273, 1, '2016-8-5', 3, 13),
    ('Rejection,  The Ruling Spirit', 358, 2, '2015-2-12', 3, 5),
    ('Little Dorrit', 314, 13, '2015-9-14', 3, 10),
    ('The Four-Fifteen Express', 201, 2, '2016-9-1', 2, 19),
    ('Fairy Prince and Other Stories', 306, 15, '2016-10-12', 3, 36),
    ('Ten Tales from the Decameron', 183, 5, '2018-3-10', 3, 13),
    ('The Double', 372, 17, '2019-1-3', 1, 20),
    ('A Flight Of Swans', 115, 8, '2017-9-17', 1, 8),
    ('The Phantom Coach', 243, 17, '2020-10-9', 4, 19),
    ('The Game', 209, 10, '2017-7-20', 3, 3),
    ('Rico and Wiseli', 309, 8, '2015-9-13', 4, 27),
    ('The Shorter Pepys', 276, 5, '2017-11-17', 3, 32),
    ('My Life Had Stood a Loaded Gun ', 278, 12, '2016-6-1', 1, 21),
    ('A Tagore Testament', 268, 15, '2021-11-15', 3, 8),
    ('The Cruise of the Dazzler', 140, 10, '2021-9-9', 5, 3),
    ('To the Lighthouse', 90, 2, '2021-3-16', 3, 17),
    ('The Terror', 293, 18, '2019-8-8', 3, 6),
    ('The Sick-a-Bed Lady', 369, 9, '2021-7-4', 1, 36),
    ('Christmas Every Day', 98, 13, '2017-7-16', 4, 1),
    ('The home-maker', 316, 14, '2018-2-17', 1, 12),
    ('The Ground-Swell', 193, 10, '2017-3-9', 4, 29),
    ('A Sleep and a Forgetting', 360, 2, '2020-7-5', 1, 1),
    ('Nicholas Nickleby', 219, 1, '2017-8-6', 1, 10),
    ('The Whole Family: A Novel by Twelve Authors', 149, 6, '2016-4-6', 3, 1),
    ('Middlemarch', 288, 11, '2018-10-9', 5, 18),
    ('Life of Dante', 156, 14, '2021-9-1', 1, 13),
    ('Short Stories From English History', 189, 2, '2019-6-19', 5, 4),
    ('Little Eve Edgarton', 306, 5, '2020-10-19', 3, 36),
    ('The Kingdom of the Sun', 189, 9, '2016-11-11', 3, 9),
    ('Indian Summer', 107, 14, '2019-3-18', 5, 1),
    ('Mrs Dalloway', 321, 5, '2020-11-4', 3, 17),
    ('Demons', 232, 15, '2017-7-2', 2, 20),
    ('The Last Trail', 116, 14, '2018-1-18', 2, 30),
    ('The Fredric Brown Megapack', 324, 16, '2020-2-17', 4, 2),
    ('The Decameron: Selected Tales', 337, 17, '2019-4-17', 1, 13),
    ('Th bent twig', 367, 3, '2015-7-20', 4, 12),
    ('Things near and far', 199, 6, '2018-2-5', 2, 6),
    ('The Story of Salome', 316, 11, '2021-10-11', 1, 19),
    ('The Destruction of Our Children', 104, 9, '2015-7-19', 5, 5),
    ('Collected Stories', 295, 4, '2019-1-11', 5, 8),
    ('The House of the Dead', 209, 2, '2017-5-1', 5, 20),
    ('Sonny: A Christmas Guest', 186, 6, '2020-6-7', 5, 25),
    ('Amores', 297, 10, '2018-9-20', 4, 15),
    ('Chaturanga', 218, 10, '2021-6-6', 5, 8),
    ('Little Eve Edgarton', 247, 14, '2018-6-6', 5, 36),
    ('Chitra', 122, 9, '2015-5-1', 1, 8),
    ('The Iron Heel', 81, 15, '2021-11-4', 1, 3),
    ('Self Reliance', 137, 18, '2022-4-13', 2, 11),
    ('The Great God Pan And The Hill Of Dreams', 158, 5, '2017-9-9', 1, 6),
    ('The White People and Other Weird Stories', 100, 15, '2015-2-13', 1, 6),
    ('The Diary of Samuel Pepys: A Selection', 271, 3, '2015-9-9', 4, 32)


INSERT INTO Storage (book_id, quantity)
VALUES
    (1, 3),
    (2, 3),
    (3, 5),
    (4, 1),
    (5, 2),
    (6, 4),
    (7, 5),
    (8, 2),
    (9, 1),
    (10, 5),
    (11, 1),
    (12, 2),
    (13, 2),
    (14, 4),
    (15, 2),
    (16, 3),
    (17, 1),
    (18, 3),
    (19, 1),
    (20, 2),
    (21, 4),
    (22, 2),
    (23, 1),
    (24, 5),
    (25, 2),
    (26, 2),
    (27, 1),
    (28, 3),
    (29, 2),
    (30, 4),
    (31, 2),
    (32, 4),
    (33, 4),
    (34, 3),
    (35, 4),
    (36, 1),
    (37, 4),
    (38, 4),
    (39, 3),
    (40, 4),
    (41, 2),
    (42, 3),
    (43, 1),
    (44, 2),
    (45, 4),
    (46, 2),
    (47, 4),
    (48, 5),
    (49, 5),
    (50, 5),
    (51, 5),
    (52, 4),
    (53, 1),
    (54, 4),
    (55, 2),
    (56, 2),
    (57, 3),
    (58, 5),
    (59, 1),
    (60, 2),
    (61, 1),
    (62, 5),
    (63, 2),
    (64, 2),
    (65, 2),
    (66, 3),
    (67, 3),
    (68, 4),
    (69, 4),
    (70, 2),
    (71, 3),
    (72, 3),
    (73, 5),
    (74, 3),
    (75, 4),
    (76, 2),
    (77, 5),
    (78, 2),
    (79, 2),
    (80, 4),
    (81, 5),
    (82, 2),
    (83, 2),
    (84, 4),
    (85, 1),
    (86, 1),
    (87, 5),
    (88, 4),
    (89, 3),
    (90, 5),
    (91, 5),
    (92, 4),
    (93, 5),
    (94, 5),
    (95, 2),
    (96, 4),
    (97, 2),
    (98, 4),
    (99, 1)




INSERT INTO Roles
(title)
VALUES
    ('USER'),
    ('LIBRARIAN'),
    ('ADMIN')

INSERT INTO User_Statuses
VALUES
    ('normal'),
    ('blocked')



INSERT INTO Users
VALUES
    ('user', '70e9b857aca8d91bc6407f76262723939ea25cdaf74644820afffd28cfdba12d84121fd225a1c7bdac0c7d9116e04a08bde682716e43d24ac31436b8eb8f575a',
     1, 1 , 'usermail@gmail.com','3802222222', 'alex', 'jhones')

INSERT INTO Users
VALUES
    ('banned_user', '70e9b857aca8d91bc6407f76262723939ea25cdaf74644820afffd28cfdba12d84121fd225a1c7bdac0c7d9116e04a08bde682716e43d24ac31436b8eb8f575a',
     1, 1 , 'banned@gmail.com','3804444444', 'top', 'gun')

--password: user123

INSERT INTO Users
VALUES
    ('librarian', 'e86f447469030010bd4c518389fbe3f1d78b71d5c4bea72bc36b2101af6a0c8d188cdc4fc70e6c7d1dd7aca579d6b82c43855ac8e929ea98d1ef2dff3e5573b2',
     2, 1 , 'librarian@gmail.com','3803333333', 'mike', 'tayson')

--password: lib123

INSERT INTO Users
VALUES
    ('admin', '7fcf4ba391c48784edde599889d6e3f1e47a27db36ecc050cc92f259bfac38afad2c68a1ae804d77075e8fb722503f3eca2b2c1006ee6f6c7b7628cb45fffd1d',
     3, 1 , 'admin@gmail.com','3801111111', 'max', 'payne')

--password: admin123



INSERT INTO Orders
VALUES
    (10000, 1, GETDATE(),DATEADD(MONTH,1 , GETDATE()), NULL),
    (10000, 2, GETDATE(),DATEADD(MONTH,1 , GETDATE()), NULL),
    (10000, 3, GETDATE(),DATEADD(MONTH,1 , GETDATE()), NULL),
    (10000, 42, GETDATE(),DATEADD(MONTH,1 , GETDATE()), NULL),
    (10000, 50, GETDATE(),DATEADD(MONTH,1 , GETDATE()), NULL)

use Library
use master