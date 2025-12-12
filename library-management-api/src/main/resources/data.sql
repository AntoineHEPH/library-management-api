-- Nettoyage des tables (ordre pour respecter les clés étrangères)
DELETE FROM book_categories;
DELETE FROM loans;
DELETE FROM books;
DELETE FROM categories;
DELETE FROM authors;
DELETE FROM members;

-- Auteurs
INSERT INTO authors (id, first_name, last_name, nationality, birth_year) VALUES
  (1, 'Jules', 'Verne', 'France', 1828),
  (2, 'George', 'Orwell', 'UK', 1903),
  (3, 'Mary', 'Shelley', 'UK', 1797);

-- Catégories
INSERT INTO categories (id, name, description) VALUES
  (1, 'Science-Fiction', 'Voyages extraordinaires et anticipations scientifiques'),
  (2, 'Dystopie', 'Sociétés imaginaires, totalitarismes et surveillance'),
  (3, 'Horreur', 'Frissons, monstres et gothique romantique');

-- Membres
INSERT INTO members (id, email, first_name, last_name, membership_date, active) VALUES
  (1, 'alice@example.com', 'Alice', 'Durand', '2023-01-10', true),
  (2, 'bob@example.com', 'Bob', 'Martin', '2023-02-15', true),
  (3, 'carol@example.com', 'Carol', 'Leroy', '2023-03-20', true);

-- Livres
INSERT INTO books (id, isbn, title, publication_year, available_copies, total_copies, author_id) VALUES
  (1, '978-2-07-040051-9', 'Vingt mille lieues sous les mers', 1870, 3, 3, 1),
  (2, '978-2-07-036053-0', 'Le Tour du monde en 80 jours', 1872, 2, 2, 1),
  (3, '978-0-452-28423-4', '1984', 1949, 4, 4, 2),
  (4, '978-0-452-28424-1', 'Animal Farm', 1945, 3, 3, 2),
  (5, '978-0-553-21375-2', 'Frankenstein', 1818, 2, 2, 3);

-- Association Livres-Catégories
INSERT INTO book_categories (book_id, category_id) VALUES
  (1, 1),
  (2, 1),
  (3, 2),
  (4, 2),
  (5, 3);
