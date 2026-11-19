package db_lab.data;

public final class Queries {
    private Queries() { }

    // OP1 - Registrare un nuovo utente
    public static final String INSERT_UTENTE = """
        INSERT INTO UTENTI (username, password, nome, cognome, email)
        VALUES (?, ?, ?, ?, ?)
        """;

    // OP2 - Registrare una nuova attività
    public static final String INSERT_ATTIVITA = """
        INSERT INTO ATTIVITA
            (username, dataOraInizio, durata, distanza, dislivello, codiceTipoAttivita)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    // Aggiornamento degli attributi ridondanti di TIPI_ATTIVITA previsto da OP2.
    public static final String UPDATE_STATISTICHE_TIPO_ATTIVITA = """
        UPDATE TIPI_ATTIVITA
        SET numeroAttivita = numeroAttivita + 1,
            distanzaTotale = distanzaTotale + ?,
            durataTotale = durataTotale + ?
        WHERE codiceTipoAttivita = ?
        """;


    // OP3 - Registrare una nuova attrezzatura
    public static final String INSERT_ATTREZZATURA = """
        INSERT INTO ATTREZZATURE
            (codiceAttrezzatura, marca, modello, chilometraggio, username, codiceTipoAttrezzatura)
        VALUES (?, ?, ?, 0, ?, ?)
        """;

    // OP4 - Inserire un nuovo tipo di attrezzatura
    public static final String INSERT_TIPO_ATTREZZATURA = """
        INSERT INTO TIPI_ATTREZZATURA (codiceTipoAttrezzatura, nome)
        VALUES (?, ?)
        """;

    public static final String INSERT_CONSENTE = """
        INSERT INTO CONSENTE (codiceTipoAttivita, codiceTipoAttrezzatura)
        VALUES (?, ?)
        """;

    // OP5 - Verifica dei vincoli prima di associare un'attrezzatura a un'attività.
    public static final String CHECK_UTILIZZO_VALIDO = """
        SELECT *
        FROM ATTIVITA A, ATTREZZATURE AT, CONSENTE C
        WHERE A.username = ?
          AND A.dataOraInizio = ?
          AND AT.codiceAttrezzatura = ?
          AND AT.username = A.username
          AND C.codiceTipoAttivita = A.codiceTipoAttivita
          AND C.codiceTipoAttrezzatura = AT.codiceTipoAttrezzatura
        """;

    public static final String INSERT_UTILIZZO = """
        INSERT INTO UTILIZZI (username, dataOraInizio, codiceAttrezzatura)
        VALUES (?, ?, ?)
        """;

    public static final String UPDATE_CHILOMETRAGGIO_ATTREZZATURA = """
        UPDATE ATTREZZATURE
        SET chilometraggio = chilometraggio + (
            SELECT distanza
            FROM ATTIVITA
            WHERE username = ?
              AND dataOraInizio = ?
        )
        WHERE codiceAttrezzatura = ?
        """;

    // OP6 - Inserire un nuovo segmento
    public static final String INSERT_SEGMENTO = """
        INSERT INTO SEGMENTI (IDSegmento, nome, distanza, dislivello, numeroAttraversamenti)
        VALUES (?, ?, ?, ?, 0)
        """;

    // OP7 - Registrare un attraversamento relativo a un'attività
    public static final String INSERT_ATTRAVERSAMENTO = """
        INSERT INTO ATTRAVERSAMENTI
            (username, dataOraInizio, ordine, tempoImpiegato, IDSegmento)
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String UPDATE_NUMERO_ATTRAVERSAMENTI = """
        UPDATE SEGMENTI
        SET numeroAttraversamenti = numeroAttraversamenti + 1
        WHERE IDSegmento = ?
        """;

    // Query di supporto alla GUI: elenco segmenti.
    public static final String LIST_SEGMENTI = """
        SELECT IDSegmento, nome, distanza, dislivello, numeroAttraversamenti
        FROM SEGMENTI
        ORDER BY IDSegmento
        """;

    // Query di supporto alla GUI: elenco attraversamenti con nome del segmento.
    public static final String LIST_ATTRAVERSAMENTI = """
        SELECT A.username, A.dataOraInizio, A.ordine, A.tempoImpiegato,
               A.IDSegmento, S.nome AS nomeSegmento
        FROM ATTRAVERSAMENTI A, SEGMENTI S
        WHERE A.IDSegmento = S.IDSegmento
        ORDER BY A.dataOraInizio DESC, A.username, A.ordine
        """;

    // Query di supporto alla GUI: elenco tipi di attrezzatura.
    public static final String LIST_TIPI_ATTREZZATURA = """
        SELECT codiceTipoAttrezzatura, nome
        FROM TIPI_ATTREZZATURA
        ORDER BY codiceTipoAttrezzatura
        """;

    // Query di supporto alla GUI: elenco attrezzature con nome della tipologia.
    public static final String LIST_ATTREZZATURE = """
        SELECT A.codiceAttrezzatura, A.marca, A.modello, A.chilometraggio,
               A.username, A.codiceTipoAttrezzatura, T.nome AS tipoAttrezzatura
        FROM ATTREZZATURE A, TIPI_ATTREZZATURA T
        WHERE A.codiceTipoAttrezzatura = T.codiceTipoAttrezzatura
        ORDER BY A.codiceAttrezzatura
        """;

    // Query di supporto alla GUI: elenco utenti.
    public static final String LIST_UTENTI = """
        SELECT username, nome, cognome, email
        FROM UTENTI
        ORDER BY username
        """;

    // Query di supporto alla GUI: elenco tipi di attività.
    public static final String LIST_TIPI_ATTIVITA = """
        SELECT codiceTipoAttivita, nome, numeroAttivita, distanzaTotale, durataTotale
        FROM TIPI_ATTIVITA
        ORDER BY codiceTipoAttivita
        """;

    // Query di supporto alla GUI: elenco attività con nome della tipologia.
    public static final String LIST_ATTIVITA = """
        SELECT A.username, A.dataOraInizio, A.durata, A.distanza, A.dislivello,
               A.codiceTipoAttivita, T.nome AS tipoAttivita
        FROM ATTIVITA A, TIPI_ATTIVITA T
        WHERE A.codiceTipoAttivita = T.codiceTipoAttivita
        ORDER BY A.dataOraInizio DESC
        """;


    // OP8 - Creare un nuovo club
    public static final String INSERT_CLUB = """
        INSERT INTO CLUB (IDClub, nome, descrizione)
        VALUES (?, ?, ?)
        """;

    public static final String INSERT_ISCRIZIONE = """
        INSERT INTO ISCRIZIONI (username, IDClub, dataIscrizione, dataDisiscrizione)
        VALUES (?, ?, ?, NULL)
        """;

    public static final String INSERT_POSIZIONE_AMMINISTRATORE = """
        INSERT INTO POSIZIONI_AMMINISTRATORE
            (IDPosizione, username, IDClub, dataIscrizione, dataInizio, dataFine)
        VALUES (?, ?, ?, ?, ?, NULL)
        """;

    // OP9 - Iscrivere un utente a un club
    public static final String CHECK_ISCRIZIONE_ATTIVA = """
        SELECT dataIscrizione
        FROM ISCRIZIONI
        WHERE username = ?
          AND IDClub = ?
          AND dataDisiscrizione IS NULL
        """;

    // OP10 - Registrare l'abbandono di un club
    public static final String FIND_ISCRIZIONE_ATTIVA = """
        SELECT dataIscrizione
        FROM ISCRIZIONI
        WHERE username = ?
          AND IDClub = ?
          AND dataDisiscrizione IS NULL
        """;

    public static final String CLOSE_POSIZIONI_AMMINISTRATORE = """
        UPDATE POSIZIONI_AMMINISTRATORE
        SET dataFine = ?
        WHERE username = ?
          AND IDClub = ?
          AND dataIscrizione = ?
          AND dataFine IS NULL
        """;

    public static final String CLOSE_ISCRIZIONE = """
        UPDATE ISCRIZIONI
        SET dataDisiscrizione = ?
        WHERE username = ?
          AND IDClub = ?
          AND dataIscrizione = ?
          AND dataDisiscrizione IS NULL
        """;

    // Query di supporto alla GUI: elenco club.
    public static final String LIST_CLUB = """
        SELECT IDClub, nome, descrizione
        FROM CLUB
        ORDER BY IDClub
        """;

    // Query di supporto alla GUI: storico delle iscrizioni.
    public static final String LIST_ISCRIZIONI = """
        SELECT I.username, I.IDClub, C.nome AS nomeClub,
               I.dataIscrizione, I.dataDisiscrizione
        FROM ISCRIZIONI I, CLUB C
        WHERE I.IDClub = C.IDClub
        ORDER BY I.dataIscrizione DESC, I.username, I.IDClub
        """;

    // Query di supporto alla GUI: storico delle posizioni di amministratore.
    public static final String LIST_POSIZIONI_AMMINISTRATORE = """
        SELECT P.IDPosizione, P.username, P.IDClub, C.nome AS nomeClub,
               P.dataIscrizione, P.dataInizio, P.dataFine
        FROM POSIZIONI_AMMINISTRATORE P, CLUB C
        WHERE P.IDClub = C.IDClub
        ORDER BY P.dataInizio DESC, P.IDPosizione
        """;
    // OP11 - Creare una nuova gara
    public static final String FIND_POSIZIONE_AMMINISTRATORE_ATTIVA = """
        SELECT PA.IDPosizione
        FROM POSIZIONI_AMMINISTRATORE PA, ISCRIZIONI I
        WHERE PA.username = I.username
          AND PA.IDClub = I.IDClub
          AND PA.dataIscrizione = I.dataIscrizione
          AND PA.username = ?
          AND PA.IDClub = ?
          AND PA.dataFine IS NULL
          AND I.dataDisiscrizione IS NULL
        """;

    public static final String INSERT_GARA = """
        INSERT INTO GARE
            (codiceGara, nome, dataCreazione, dataInizio, dataFine,
             IDPosizione, codiceTipoAttivita)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String INSERT_TRATTO_GARA = """
        INSERT INTO TRATTI_GARA (codiceGara, ordine, IDSegmento)
        VALUES (?, ?, ?)
        """;

    // OP12 - Iscrivere un utente a una gara
    public static final String CHECK_PARTECIPAZIONE_VALIDA = """
        SELECT *
        FROM GARE G, POSIZIONI_AMMINISTRATORE PA, ISCRIZIONI I
        WHERE G.IDPosizione = PA.IDPosizione
          AND PA.IDClub = I.IDClub
          AND G.codiceGara = ?
          AND I.username = ?
          AND I.dataDisiscrizione IS NULL
        """;

    public static final String INSERT_PARTECIPAZIONE = """
        INSERT INTO PARTECIPAZIONI
            (username, codiceGara, dataIscrizione, tempoImpiegato)
        VALUES (?, ?, ?, NULL)
        """;

    // Query di supporto alla GUI: elenco gare con club, creatore e tipo di attività.
    public static final String LIST_GARE = """
        SELECT G.codiceGara, G.nome, G.dataCreazione, G.dataInizio, G.dataFine,
               G.IDPosizione, PA.username AS creatore, PA.IDClub, C.nome AS nomeClub,
               G.codiceTipoAttivita, T.nome AS tipoAttivita
        FROM GARE G, POSIZIONI_AMMINISTRATORE PA, CLUB C, TIPI_ATTIVITA T
        WHERE G.IDPosizione = PA.IDPosizione
          AND PA.IDClub = C.IDClub
          AND G.codiceTipoAttivita = T.codiceTipoAttivita
        ORDER BY G.dataInizio DESC, G.codiceGara
        """;

    // Query di supporto alla GUI: percorso ordinato delle gare.
    public static final String LIST_TRATTI_GARA = """
        SELECT TG.codiceGara, G.nome AS nomeGara, TG.ordine,
               TG.IDSegmento, S.nome AS nomeSegmento
        FROM TRATTI_GARA TG, GARE G, SEGMENTI S
        WHERE TG.codiceGara = G.codiceGara
          AND TG.IDSegmento = S.IDSegmento
        ORDER BY TG.codiceGara, TG.ordine
        """;

    // Query di supporto alla GUI: partecipazioni alle gare.
    public static final String LIST_PARTECIPAZIONI = """
        SELECT P.username, P.codiceGara, G.nome AS nomeGara,
               P.dataIscrizione, P.tempoImpiegato
        FROM PARTECIPAZIONI P, GARE G
        WHERE P.codiceGara = G.codiceGara
        ORDER BY P.dataIscrizione DESC, P.codiceGara, P.username
        """;


    // Operazione applicativa di supporto: registrare il risultato di una gara.
    public static final String UPDATE_RISULTATO_GARA = """
        UPDATE PARTECIPAZIONI
        SET tempoImpiegato = ?
        WHERE username = ?
          AND codiceGara = ?
        """;

    // OP13 - Classifica degli utenti per distanza percorsa in un periodo.
    public static final String OP13_CLASSIFICA_DISTANZA_UTENTI = """
        SELECT U.username, U.nome, U.cognome, SUM(A.distanza) AS distanzaTotale
        FROM UTENTI U, ATTIVITA A
        WHERE U.username = A.username
          AND A.dataOraInizio >= ?
          AND A.dataOraInizio <= ?
        GROUP BY U.username, U.nome, U.cognome
        ORDER BY distanzaTotale DESC
        """;

    // OP14 - Migliori tempi ottenuti su un segmento.
    public static final String OP14_MIGLIORI_TEMPI_SEGMENTO = """
        SELECT U.username, U.nome, U.cognome, A.tempoImpiegato, A.dataOraInizio
        FROM ATTRAVERSAMENTI A, UTENTI U
        WHERE A.username = U.username
          AND A.IDSegmento = ?
        ORDER BY A.tempoImpiegato ASC
        """;

    // OP15 - Attrezzature ordinate per chilometraggio complessivo.
    public static final String OP15_ATTREZZATURE_CHILOMETRAGGIO = """
        SELECT codiceAttrezzatura, marca, modello, username, chilometraggio
        FROM ATTREZZATURE
        ORDER BY chilometraggio DESC
        """;

    // OP16 - Distanza e durata media per tipo di attività usando gli attributi ridondanti.
    public static final String OP16_MEDIE_TIPI_ATTIVITA = """
        SELECT codiceTipoAttivita, nome,
               distanzaTotale / numeroAttivita AS distanzaMedia,
               durataTotale / numeroAttivita AS durataMedia
        FROM TIPI_ATTIVITA
        """;

    // OP17 - Segmenti più frequentemente percorsi.
    public static final String OP17_SEGMENTI_PIU_PERCORSI = """
        SELECT IDSegmento, nome, numeroAttraversamenti
        FROM SEGMENTI
        ORDER BY numeroAttraversamenti DESC
        """;

    // OP18 - Club con maggiore distanza percorsa dai membri in un periodo.
    public static final String OP18_CLASSIFICA_CLUB = """
        SELECT C.IDClub, C.nome, SUM(A.distanza) AS distanzaTotale
        FROM CLUB C, ISCRIZIONI I, ATTIVITA A
        WHERE C.IDClub = I.IDClub
          AND I.username = A.username
          AND A.dataOraInizio >= ?
          AND A.dataOraInizio <= ?
          AND A.dataOraInizio >= I.dataIscrizione
          AND (I.dataDisiscrizione IS NULL OR A.dataOraInizio <= I.dataDisiscrizione)
        GROUP BY C.IDClub, C.nome
        ORDER BY distanzaTotale DESC
        """;

    // OP19 - Classifica dei partecipanti a una gara.
    public static final String OP19_CLASSIFICA_GARA = """
        SELECT U.username, U.nome, U.cognome, P.tempoImpiegato
        FROM PARTECIPAZIONI P, UTENTI U
        WHERE P.username = U.username
          AND P.codiceGara = ?
          AND P.tempoImpiegato IS NOT NULL
        ORDER BY P.tempoImpiegato ASC
        """;

}
