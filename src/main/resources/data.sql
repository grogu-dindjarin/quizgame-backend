INSERT INTO badges (name, description, badge_condition, icon_path) VALUES
('Eerste quiz', 'Voltooi je eerste quiz', 'FIRST_QUIZ', null),
('Perfectionist', 'Behaal een score van 100%', 'PERFECT_SCORE', null),
('Doorzetter', 'Voltooi 10 quizzen', 'TEN_QUIZZES', null),
('Expert', 'Voltooi een quiz op expertniveau', 'EXPERT_QUIZ', null),
('Snelle vingers', 'Voltooi een quiz zonder fouten op beginnersniveau', 'BEGINNER_PERFECT', null);

INSERT INTO categories (name) VALUES ('Aardrijkskunde'), ('Geschiedenis'), ('Wetenschap');

INSERT INTO subcategories (name, difficulty, category_id) VALUES
('Hoofdsteden', 'BEGINNER', 1),
('Klimaatzones', 'INTERMEDIATE', 1),
('Gebergte & rivieren', 'EXPERT', 1),
('Tweede Wereldoorlog', 'BEGINNER', 2),
('Nederlandse geschiedenis', 'INTERMEDIATE', 2),
('Oude beschavingen', 'EXPERT', 2),
('Periodiek systeem', 'BEGINNER', 3),
('Sterrenkunde', 'INTERMEDIATE', 3),
('Kwantumfysica', 'EXPERT', 3);

INSERT INTO questions (text, image_path, subcategory_id) VALUES
('Wat is de hoofdstad van Nederland?', null, 1),
('Wat is de hoofdstad van Duitsland?', null, 1),
('Wat is de hoofdstad van België?', null, 1),
('Wat is de hoofdstad van Spanje?', null, 1),
('Wat is de hoofdstad van Portugal?', null, 1);

INSERT INTO answers (text, correct, question_id) VALUES
('Amsterdam', true, 1), ('Den Haag', false, 1), ('Rotterdam', false, 1), ('Utrecht', false, 1),
('Frankfurt', false, 2), ('München', false, 2), ('Berlijn', true, 2), ('Hamburg', false, 2),
('Antwerpen', false, 3), ('Brussel', true, 3), ('Gent', false, 3), ('Luik', false, 3),
('Barcelona', false, 4), ('Valencia', false, 4), ('Madrid', true, 4), ('Sevilla', false, 4),
('Porto', false, 5), ('Lissabon', true, 5), ('Braga', false, 5), ('Faro', false, 5);

INSERT INTO questions (text, image_path, subcategory_id) VALUES
('Welk klimaattype kenmerkt zich door hete, droge zomers en milde, natte winters?', null, 2),
('Wat is het kenmerk van een equatoriaal klimaat?', null, 2),
('In welk klimaatgebied valt de meeste neerslag per jaar?', null, 2),
('Welk continent heeft het grootste woestijngebied ter wereld?', null, 2);

INSERT INTO answers (text, correct, question_id) VALUES
('Poolklimaat', false, 6), ('Mediterraan klimaat', true, 6), ('Tropisch klimaat', false, 6), ('Steppenklimaat', false, 6),
('Koude droge zomers', false, 7), ('Het hele jaar neerslag en hoge temperaturen', true, 7), ('Weinig neerslag', false, 7), ('Koude winters', false, 7),
('Woestijn', false, 8), ('Poolgebied', false, 8), ('Regenwoud', true, 8), ('Savanne', false, 8),
('Antarctica', false, 9), ('Azië', false, 9), ('Afrika', true, 9), ('Australië', false, 9);

INSERT INTO questions (text, image_path, subcategory_id) VALUES
('In welk jaar brak de Tweede Wereldoorlog uit?', null, 4),
('Welk land viel Nederland binnen in mei 1940?', null, 4),
('Op welke datum capituleerde Duitsland in de Tweede Wereldoorlog?', null, 4),
('Wie was de leider van nazi-Duitsland?', null, 4),
('Wat was de codenaam voor de geallieerde landing in Normandië?', null, 4);

INSERT INTO answers (text, correct, question_id) VALUES
('1937', false, 10), ('1939', true, 10), ('1941', false, 10), ('1945', false, 10),
('Engeland', false, 11), ('Rusland', false, 11), ('Duitsland', true, 11), ('Italië', false, 11),
('1 september 1939', false, 12), ('6 juni 1944', false, 12), ('8 mei 1945', true, 12), ('2 september 1945', false, 12),
('Mussolini', false, 13), ('Stalin', false, 13), ('Franco', false, 13), ('Hitler', true, 13),
('Operatie Dynamo', false, 14), ('Operatie Barbarossa', false, 14), ('Operatie Overlord', true, 14), ('Operatie Market Garden', false, 14);

INSERT INTO questions (text, image_path, subcategory_id) VALUES
('Wat is het scheikundig symbool voor goud?', null, 7),
('Wat is het atoomnummer van waterstof?', null, 7),
('Welk element heeft het symbool O?', null, 7),
('Hoeveel protonen heeft koolstof?', null, 7),
('Wat is het lichtste element in het periodiek systeem?', null, 7);

INSERT INTO answers (text, correct, question_id) VALUES
('Go', false, 15), ('Ge', false, 15), ('Gd', false, 15), ('Au', true, 15),
('2', false, 16), ('1', true, 16), ('6', false, 16), ('8', false, 16),
('Osmium', false, 17), ('Oganesson', false, 17), ('Zuurstof', true, 17), ('Ozon', false, 17),
('4', false, 18), ('12', false, 18), ('8', false, 18), ('6', true, 18),
('Helium', false, 19), ('Lithium', false, 19), ('Waterstof', true, 19), ('Beryllium', false, 19);

INSERT INTO users (email, password, role) VALUES
('admin@quizzle.nl', '$2b$10$su68XqHufIItLSiMZRRO7.2BTg31hpNT5aEcOcFiXRsy35gKCEjxe', 'ADMIN'),
('speler@quizzle.nl', '$2b$10$su68XqHufIItLSiMZRRO7.2BTg31hpNT5aEcOcFiXRsy35gKCEjxe', 'USER');

INSERT INTO user_profiles (user_id, display_name, avatar_path, preferred_difficulty) VALUES
(1, 'Admin', null, 'EXPERT'),
(2, 'Speler', null, 'BEGINNER');
