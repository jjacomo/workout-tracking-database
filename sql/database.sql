CREATE DATABASE IF NOT EXISTS workout_tracking_db;
USE workout_tracking_db;

-- OP1 - UTENTI
CREATE TABLE UTENTI (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Tipi di attività e OP2 - ATTIVITA
CREATE TABLE TIPI_ATTIVITA (
    codiceTipoAttivita INT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    numeroAttivita INT NOT NULL DEFAULT 0 CHECK (numeroAttivita >= 0),
    distanzaTotale DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (distanzaTotale >= 0),
    durataTotale INT NOT NULL DEFAULT 0 CHECK (durataTotale >= 0)
);

CREATE TABLE ATTIVITA (
    username VARCHAR(50) NOT NULL,
    dataOraInizio DATETIME NOT NULL,
    durata INT NOT NULL CHECK (durata > 0),
    distanza DECIMAL(10,2) NOT NULL CHECK (distanza >= 0),
    dislivello INT NOT NULL CHECK (dislivello >= 0),
    codiceTipoAttivita INT NOT NULL,
    PRIMARY KEY (username, dataOraInizio),
    FOREIGN KEY (username) REFERENCES UTENTI(username),
    FOREIGN KEY (codiceTipoAttivita) REFERENCES TIPI_ATTIVITA(codiceTipoAttivita)
);

-- OP3, OP4, OP5 - ATTREZZATURE
CREATE TABLE TIPI_ATTREZZATURA (
    codiceTipoAttrezzatura INT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE ATTREZZATURE (
    codiceAttrezzatura INT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modello VARCHAR(80) NOT NULL,
    chilometraggio DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (chilometraggio >= 0),
    username VARCHAR(50) NOT NULL,
    codiceTipoAttrezzatura INT NOT NULL,
    FOREIGN KEY (username) REFERENCES UTENTI(username),
    FOREIGN KEY (codiceTipoAttrezzatura) REFERENCES TIPI_ATTREZZATURA(codiceTipoAttrezzatura)
);

CREATE TABLE CONSENTE (
    codiceTipoAttivita INT NOT NULL,
    codiceTipoAttrezzatura INT NOT NULL,
    PRIMARY KEY (codiceTipoAttivita, codiceTipoAttrezzatura),
    FOREIGN KEY (codiceTipoAttivita) REFERENCES TIPI_ATTIVITA(codiceTipoAttivita),
    FOREIGN KEY (codiceTipoAttrezzatura) REFERENCES TIPI_ATTREZZATURA(codiceTipoAttrezzatura)
);

CREATE TABLE UTILIZZI (
    username VARCHAR(50) NOT NULL,
    dataOraInizio DATETIME NOT NULL,
    codiceAttrezzatura INT NOT NULL,
    PRIMARY KEY (username, dataOraInizio, codiceAttrezzatura),
    FOREIGN KEY (username, dataOraInizio)
        REFERENCES ATTIVITA(username, dataOraInizio),
    FOREIGN KEY (codiceAttrezzatura)
        REFERENCES ATTREZZATURE(codiceAttrezzatura)
);

-- OP6, OP7 - SEGMENTI E ATTRAVERSAMENTI
CREATE TABLE SEGMENTI (
    IDSegmento INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    distanza DECIMAL(10,2) NOT NULL CHECK (distanza > 0),
    dislivello INT NOT NULL CHECK (dislivello >= 0),
    numeroAttraversamenti INT NOT NULL DEFAULT 0 CHECK (numeroAttraversamenti >= 0)
);

CREATE TABLE ATTRAVERSAMENTI (
    username VARCHAR(50) NOT NULL,
    dataOraInizio DATETIME NOT NULL,
    ordine INT NOT NULL CHECK (ordine > 0),
    tempoImpiegato INT NOT NULL CHECK (tempoImpiegato > 0),
    IDSegmento INT NOT NULL,
    PRIMARY KEY (username, dataOraInizio, ordine),
    FOREIGN KEY (username, dataOraInizio)
        REFERENCES ATTIVITA(username, dataOraInizio),
    FOREIGN KEY (IDSegmento)
        REFERENCES SEGMENTI(IDSegmento)
);

-- OP8, OP9, OP10 - CLUB E ISCRIZIONI
CREATE TABLE CLUB (
    IDClub INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descrizione VARCHAR(255) NOT NULL
);

CREATE TABLE ISCRIZIONI (
    username VARCHAR(50) NOT NULL,
    IDClub INT NOT NULL,
    dataIscrizione DATETIME NOT NULL,
    dataDisiscrizione DATETIME NULL,
    PRIMARY KEY (username, IDClub, dataIscrizione),
    FOREIGN KEY (username) REFERENCES UTENTI(username),
    FOREIGN KEY (IDClub) REFERENCES CLUB(IDClub),
    CHECK (dataDisiscrizione IS NULL OR dataDisiscrizione >= dataIscrizione)
);

CREATE TABLE POSIZIONI_AMMINISTRATORE (
    IDPosizione INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    IDClub INT NOT NULL,
    dataIscrizione DATETIME NOT NULL,
    dataInizio DATETIME NOT NULL,
    dataFine DATETIME NULL,
    UNIQUE (username, IDClub, dataIscrizione, dataInizio),
    FOREIGN KEY (username, IDClub, dataIscrizione)
        REFERENCES ISCRIZIONI(username, IDClub, dataIscrizione),
    CHECK (dataFine IS NULL OR dataFine >= dataInizio)
);

-- OP11, OP12 - GARE
CREATE TABLE GARE (
    codiceGara INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    dataCreazione DATETIME NOT NULL,
    dataInizio DATETIME NOT NULL,
    dataFine DATETIME NOT NULL,
    IDPosizione INT NOT NULL,
    codiceTipoAttivita INT NOT NULL,
    FOREIGN KEY (IDPosizione) REFERENCES POSIZIONI_AMMINISTRATORE(IDPosizione),
    FOREIGN KEY (codiceTipoAttivita) REFERENCES TIPI_ATTIVITA(codiceTipoAttivita),
    CHECK (dataFine >= dataInizio)
);

CREATE TABLE TRATTI_GARA (
    codiceGara INT NOT NULL,
    ordine INT NOT NULL CHECK (ordine > 0),
    IDSegmento INT NOT NULL,
    PRIMARY KEY (codiceGara, ordine),
    FOREIGN KEY (codiceGara) REFERENCES GARE(codiceGara),
    FOREIGN KEY (IDSegmento) REFERENCES SEGMENTI(IDSegmento)
);

CREATE TABLE PARTECIPAZIONI (
    username VARCHAR(50) NOT NULL,
    codiceGara INT NOT NULL,
    dataIscrizione DATETIME NOT NULL,
    tempoImpiegato INT NULL CHECK (tempoImpiegato IS NULL OR tempoImpiegato > 0),
    PRIMARY KEY (username, codiceGara),
    FOREIGN KEY (username) REFERENCES UTENTI(username),
    FOREIGN KEY (codiceGara) REFERENCES GARE(codiceGara)
);
