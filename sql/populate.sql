SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE ATTRAVERSAMENTI;
TRUNCATE TABLE UTILIZZI;
TRUNCATE TABLE PARTECIPAZIONI;
TRUNCATE TABLE TRATTI_GARA;
TRUNCATE TABLE GARE;
TRUNCATE TABLE POSIZIONI_AMMINISTRATORE;
TRUNCATE TABLE ISCRIZIONI;
TRUNCATE TABLE ATTREZZATURE;
TRUNCATE TABLE ATTIVITA;
TRUNCATE TABLE SEGMENTI;
TRUNCATE TABLE CLUB;
TRUNCATE TABLE CONSENTE;
TRUNCATE TABLE TIPI_ATTREZZATURA;
TRUNCATE TABLE TIPI_ATTIVITA;
TRUNCATE TABLE UTENTI;

SET FOREIGN_KEY_CHECKS = 1;

START TRANSACTION;

-- 1. TIPI DI ATTIVITA

INSERT INTO TIPI_ATTIVITA
    (codiceTipoAttivita, nome, numeroAttivita, distanzaTotale, durataTotale)
VALUES
    (1, 'Corsa',       6,  53.10, 16250),
    (2, 'Ciclismo',    6, 305.90, 36900),
    (3, 'Camminata',   3,  16.10, 11100),
    (4, 'Trekking',    3,  55.70, 43200);


-- 2. TIPI DI ATTREZZATURA

INSERT INTO TIPI_ATTREZZATURA
    (codiceTipoAttrezzatura, nome)
VALUES
    (10, 'Scarpe da corsa'),
    (20, 'Bicicletta'),
    (30, 'Scarpe da trekking');


-- 3. COMPATIBILITA TRA TIPI DI ATTIVITA E ATTREZZATURA

INSERT INTO CONSENTE
    (codiceTipoAttivita, codiceTipoAttrezzatura)
VALUES
    (1, 10), -- corsa -> scarpe da corsa
    (2, 20), -- ciclismo -> bicicletta
    (3, 10), -- camminata -> scarpe da corsa
    (3, 30), -- camminata -> scarpe da trekking
    (4, 30); -- trekking -> scarpe da trekking


-- 4. UTENTI

INSERT INTO UTENTI
    (username, password, nome, cognome, email)
VALUES
    ('luca87',   'luca123',   'Luca',   'Bianchi',   'luca.bianchi@example.com'),
    ('anna_run', 'anna123',   'Anna',   'Rossi',     'anna.rossi@example.com'),
    ('marco_b',  'marco123',  'Marco',  'Bernardi',  'marco.bernardi@example.com'),
    ('giulia90', 'giulia123', 'Giulia', 'Conti',     'giulia.conti@example.com'),
    ('paolo_c',  'paolo123',  'Paolo',  'Ferrari',   'paolo.ferrari@example.com'),
    ('sara_fit', 'sara123',   'Sara',   'Romano',    'sara.romano@example.com'),
    ('elena_t',  'elena123',  'Elena',  'Galli',     'elena.galli@example.com'),
    ('davide92', 'davide123', 'Davide', 'Moretti',   'davide.moretti@example.com');


-- 5. CLUB

INSERT INTO CLUB
    (IDClub, nome, descrizione)
VALUES
    (1, 'Bologna Runners',
        'Club dedicato agli appassionati di corsa su strada di Bologna.'),
    (2, 'Ciclisti di Romagna',
        'Gruppo per uscite in bicicletta e gare amatoriali in Romagna.'),
    (3, 'Trekking Appennino',
        'Club dedicato a trekking, sentieri e percorsi in Appennino.');


-- 6. ISCRIZIONI AI CLUB
-- E' presente anche una iscrizione conclusa per testare lo storico.

INSERT INTO ISCRIZIONI
    (username, IDClub, dataIscrizione, dataDisiscrizione)
VALUES
    ('luca87',   1, '2026-01-10 10:00:00', NULL),
    ('anna_run', 1, '2026-02-01 09:30:00', NULL),
    ('marco_b',  1, '2026-03-01 11:00:00', '2026-07-31 18:00:00'),
    ('sara_fit', 1, '2026-05-15 08:45:00', NULL),

    ('paolo_c',  2, '2026-01-05 12:00:00', NULL),
    ('marco_b',  2, '2026-02-10 14:00:00', NULL),
    ('giulia90', 2, '2026-03-20 16:30:00', NULL),
    ('davide92', 2, '2026-04-01 10:20:00', NULL),
    ('anna_run', 2, '2026-07-01 09:00:00', NULL),

    ('elena_t',  3, '2026-01-20 13:00:00', NULL),
    ('giulia90', 3, '2026-02-12 17:00:00', NULL),
    ('marco_b',  3, '2026-04-15 18:30:00', NULL);


-- 7. POSIZIONI DI AMMINISTRATORE

INSERT INTO POSIZIONI_AMMINISTRATORE
    (IDPosizione, username, IDClub, dataIscrizione, dataInizio, dataFine)
VALUES
    (1, 'luca87',   1, '2026-01-10 10:00:00', '2026-01-10 10:00:00', NULL),
    (2, 'paolo_c',  2, '2026-01-05 12:00:00', '2026-01-05 12:00:00', NULL),
    (3, 'elena_t',  3, '2026-01-20 13:00:00', '2026-01-20 13:00:00', NULL),
    (4, 'anna_run', 1, '2026-02-01 09:30:00', '2026-06-01 08:00:00', NULL),
    (5, 'marco_b',  1, '2026-03-01 11:00:00', '2026-04-01 09:00:00',
        '2026-07-15 20:00:00');


-- 8. SEGMENTI
-- numeroAttraversamenti e' coerente con gli attraversamenti
-- inseriti nella sezione 14.

INSERT INTO SEGMENTI
    (IDSegmento, nome, distanza, dislivello, numeroAttraversamenti)
VALUES
    (101, 'Sprint Parco Nord',     1.20,   8,  7),
    (102, 'Salita San Luca',       2.10, 190,  2),
    (103, 'Lungofiume',            3.40,  15, 12),
    (104, 'Colle Panoramico',      4.80, 310,  9),
    (105, 'Via Verde',             2.70,  25,  7),
    (106, 'Passo del Falco',       6.30, 520,  7);


-- 9. GARE

INSERT INTO GARE
    (codiceGara, nome, dataCreazione, dataInizio, dataFine,
     IDPosizione, codiceTipoAttivita)
VALUES
    (1001, '10K dei Portici',
        '2026-05-20 12:00:00', '2026-07-26 08:00:00',
        '2026-07-26 13:00:00', 1, 1),

    (1002, 'Gran Fondo Romagna',
        '2026-06-01 10:00:00', '2026-08-09 07:00:00',
        '2026-08-09 16:00:00', 2, 2),

    (1003, 'Trail dell''Appennino',
        '2026-06-15 15:00:00', '2026-08-16 07:30:00',
        '2026-08-16 18:00:00', 3, 4);


-- 10. PERCORSI DELLE GARE

INSERT INTO TRATTI_GARA
    (codiceGara, ordine, IDSegmento)
VALUES
    (1001, 1, 101),
    (1001, 2, 103),
    (1001, 3, 105),

    (1002, 1, 103),
    (1002, 2, 104),
    (1002, 3, 106),

    (1003, 1, 104),
    (1003, 2, 106);


-- 11. PARTECIPAZIONI ALLE GARE

INSERT INTO PARTECIPAZIONI
    (username, codiceGara, dataIscrizione, tempoImpiegato)
VALUES
    ('luca87',   1001, '2026-06-10 10:00:00', 2980),
    ('anna_run', 1001, '2026-06-12 11:30:00', 2875),
    ('marco_b',  1001, '2026-06-18 09:00:00', 2790),
    ('sara_fit', 1001, '2026-06-20 17:20:00', 3050),

    ('paolo_c',  1002, '2026-06-20 12:00:00', 6900),
    ('marco_b',  1002, '2026-06-22 15:00:00', 7150),
    ('giulia90', 1002, '2026-06-25 16:15:00', 7020),
    ('davide92', 1002, '2026-07-05 10:10:00', 7300),
    ('anna_run', 1002, '2026-07-10 08:30:00', 7450),

    ('elena_t',  1003, '2026-07-01 13:00:00', 9800),
    ('giulia90', 1003, '2026-07-03 14:00:00', 9400),
    ('marco_b',  1003, '2026-07-04 18:00:00', NULL);


-- 12. ATTIVITA

INSERT INTO ATTIVITA
    (username, dataOraInizio, durata, distanza, dislivello, codiceTipoAttivita)
VALUES
    -- Corsa
    ('luca87',   '2026-07-02 07:15:00',  2700,  8.50, 120, 1),
    ('anna_run', '2026-07-03 18:10:00',  3200, 10.20, 150, 1),
    ('marco_b',  '2026-07-10 06:50:00',  1850,  6.00,  80, 1),
    ('giulia90', '2026-08-01 07:30:00',  3600, 12.00, 210, 1),
    ('luca87',   '2026-08-12 19:00:00',  2100,  7.10,  95, 1),
    ('sara_fit', '2026-08-18 06:40:00',  2800,  9.30, 130, 1),

    -- Ciclismo
    ('marco_b',  '2026-07-05 09:00:00',  5400, 42.00,  650, 2),
    ('paolo_c',  '2026-07-12 08:00:00',  7200, 61.50,  980, 2),
    ('giulia90', '2026-07-20 17:00:00',  4500, 35.20,  430, 2),
    ('davide92', '2026-08-02 08:15:00',  6600, 55.00,  820, 2),
    ('anna_run', '2026-08-09 09:30:00',  5100, 39.80,  510, 2),
    ('paolo_c',  '2026-08-16 07:45:00',  8100, 72.40, 1100, 2),

    -- Camminata
    ('elena_t',  '2026-07-04 17:30:00',  3600,  5.20,  40, 3),
    ('sara_fit', '2026-07-15 19:00:00',  4200,  6.10,  55, 3),
    ('davide92', '2026-08-05 18:20:00',  3300,  4.80,  30, 3),

    -- Trekking
    ('elena_t',  '2026-07-19 07:00:00', 14400, 18.00,  980, 4),
    ('marco_b',  '2026-08-08 06:30:00', 16200, 21.50, 1250, 4),
    ('giulia90', '2026-08-20 07:10:00', 12600, 16.20,  840, 4);


-- 13. ATTREZZATURE

INSERT INTO ATTREZZATURE
    (codiceAttrezzatura, marca, modello, chilometraggio,
     username, codiceTipoAttrezzatura)
VALUES
    (1,  'Nike',        'Pegasus 41',       15.60, 'luca87',   10),
    (2,  'Asics',       'Novablast 5',      10.20, 'anna_run', 10),
    (3,  'Hoka',        'Clifton 10',         6.00, 'marco_b',  10),
    (4,  'Brooks',      'Ghost 17',           9.30, 'sara_fit', 10),
    (5,  'Saucony',     'Ride 18',           12.00, 'giulia90', 10),

    (6,  'Bianchi',     'Via Nirone 7',      42.00, 'marco_b',  20),
    (7,  'Canyon',      'Endurace CF',       133.90, 'paolo_c',  20),
    (8,  'Trek',        'Domane AL 5',        35.20, 'giulia90', 20),
    (9,  'Specialized', 'Allez Sport',        55.00, 'davide92', 20),
    (10, 'Giant',       'Contend AR',         39.80, 'anna_run', 20),

    (11, 'Salomon',     'X Ultra 5',          23.20, 'elena_t',  30),
    (12, 'Merrell',     'Moab Speed 2',       21.50, 'marco_b',  30),
    (13, 'Columbia',    'Peakfreak II',       16.20, 'giulia90', 30),
    (14, 'Skechers',    'Go Run Ride',         6.10, 'sara_fit', 10);


-- 14. UTILIZZI DELLE ATTREZZATURE

INSERT INTO UTILIZZI
    (username, dataOraInizio, codiceAttrezzatura)
VALUES
    ('luca87',   '2026-07-02 07:15:00', 1),
    ('luca87',   '2026-08-12 19:00:00', 1),
    ('anna_run', '2026-07-03 18:10:00', 2),
    ('marco_b',  '2026-07-10 06:50:00', 3),
    ('sara_fit', '2026-08-18 06:40:00', 4),
    ('giulia90', '2026-08-01 07:30:00', 5),

    ('marco_b',  '2026-07-05 09:00:00', 6),
    ('paolo_c',  '2026-07-12 08:00:00', 7),
    ('paolo_c',  '2026-08-16 07:45:00', 7),
    ('giulia90', '2026-07-20 17:00:00', 8),
    ('davide92', '2026-08-02 08:15:00', 9),
    ('anna_run', '2026-08-09 09:30:00', 10),

    ('elena_t',  '2026-07-04 17:30:00', 11),
    ('elena_t',  '2026-07-19 07:00:00', 11),
    ('marco_b',  '2026-08-08 06:30:00', 12),
    ('giulia90', '2026-08-20 07:10:00', 13),
    ('sara_fit', '2026-07-15 19:00:00', 14);


-- 15. ATTRAVERSAMENTI

INSERT INTO ATTRAVERSAMENTI
    (username, dataOraInizio, ordine, tempoImpiegato, IDSegmento)
VALUES
    -- Corsa: Luca 02/07
    ('luca87',   '2026-07-02 07:15:00', 1, 310, 101),
    ('luca87',   '2026-07-02 07:15:00', 2, 930, 103),

    -- Corsa: Anna 03/07
    ('anna_run', '2026-07-03 18:10:00', 1, 295, 101),
    ('anna_run', '2026-07-03 18:10:00', 2, 760, 102),
    ('anna_run', '2026-07-03 18:10:00', 3, 890, 103),

    -- Corsa: Marco 10/07
    ('marco_b',  '2026-07-10 06:50:00', 1, 280, 101),
    ('marco_b',  '2026-07-10 06:50:00', 2, 700, 105),

    -- Corsa: Giulia 01/08
    ('giulia90', '2026-08-01 07:30:00', 1, 270, 101),
    ('giulia90', '2026-08-01 07:30:00', 2, 710, 102),
    ('giulia90', '2026-08-01 07:30:00', 3, 850, 103),

    -- Corsa: Luca 12/08
    ('luca87',   '2026-08-12 19:00:00', 1, 300, 101),
    ('luca87',   '2026-08-12 19:00:00', 2, 680, 105),

    -- Corsa: Sara 18/08
    ('sara_fit', '2026-08-18 06:40:00', 1, 305, 101),
    ('sara_fit', '2026-08-18 06:40:00', 2, 900, 103),

    -- Ciclismo: Marco 05/07
    ('marco_b',  '2026-07-05 09:00:00', 1, 420, 103),
    ('marco_b',  '2026-07-05 09:00:00', 2, 980, 104),
    ('marco_b',  '2026-07-05 09:00:00', 3, 1260, 106),

    -- Ciclismo: Paolo 12/07
    ('paolo_c',  '2026-07-12 08:00:00', 1, 390, 103),
    ('paolo_c',  '2026-07-12 08:00:00', 2, 910, 104),
    ('paolo_c',  '2026-07-12 08:00:00', 3, 1190, 106),

    -- Ciclismo: Giulia 20/07
    ('giulia90', '2026-07-20 17:00:00', 1, 405, 103),
    ('giulia90', '2026-07-20 17:00:00', 2, 310, 105),
    ('giulia90', '2026-07-20 17:00:00', 3, 950, 104),

    -- Ciclismo: Davide 02/08
    ('davide92', '2026-08-02 08:15:00', 1, 395, 103),
    ('davide92', '2026-08-02 08:15:00', 2, 930, 104),
    ('davide92', '2026-08-02 08:15:00', 3, 1210, 106),

    -- Ciclismo: Anna 09/08
    ('anna_run', '2026-08-09 09:30:00', 1, 410, 103),
    ('anna_run', '2026-08-09 09:30:00', 2, 325, 105),
    ('anna_run', '2026-08-09 09:30:00', 3, 970, 104),

    -- Ciclismo: Paolo 16/08
    ('paolo_c',  '2026-08-16 07:45:00', 1, 380, 103),
    ('paolo_c',  '2026-08-16 07:45:00', 2, 890, 104),
    ('paolo_c',  '2026-08-16 07:45:00', 3, 1160, 106),

    -- Camminata
    ('elena_t',  '2026-07-04 17:30:00', 1, 1250, 105),
    ('elena_t',  '2026-07-04 17:30:00', 2, 2400, 103),

    ('sara_fit', '2026-07-15 19:00:00', 1, 1350, 105),
    ('sara_fit', '2026-07-15 19:00:00', 2, 2500, 103),

    ('davide92', '2026-08-05 18:20:00', 1, 1200, 105),
    ('davide92', '2026-08-05 18:20:00', 2, 720, 101),

    -- Trekking
    ('elena_t',  '2026-07-19 07:00:00', 1, 3600, 104),
    ('elena_t',  '2026-07-19 07:00:00', 2, 5200, 106),

    ('marco_b',  '2026-08-08 06:30:00', 1, 3400, 104),
    ('marco_b',  '2026-08-08 06:30:00', 2, 4900, 106),

    ('giulia90', '2026-08-20 07:10:00', 1, 3500, 104),
    ('giulia90', '2026-08-20 07:10:00', 2, 5000, 106);


COMMIT;
