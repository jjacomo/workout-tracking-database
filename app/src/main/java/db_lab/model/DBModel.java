package db_lab.model;

import db_lab.data.Attivita;
import db_lab.data.Attraversamento;
import db_lab.data.Attrezzatura;
import db_lab.data.DAOException;
import db_lab.data.TipoAttivita;
import db_lab.data.TipoAttrezzatura;
import db_lab.data.Segmento;
import db_lab.data.Club;
import db_lab.data.Iscrizione;
import db_lab.data.PosizioneAmministratore;
import db_lab.data.Utente;
import db_lab.data.Partecipazione;
import db_lab.data.TrattoGara;
import db_lab.data.Gara;
import db_lab.data.Report;
import db_lab.data.Queries;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class DBModel implements Model {
    private final Connection connection;

    public DBModel(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public List<Utente> listUtenti() {
        return Utente.DAO.list(connection);
    }

    @Override
    public void addUtente(String username, String password, String nome,
                          String cognome, String email) {
        Utente.DAO.insert(connection, username, password, nome, cognome, email);
    }

    @Override
    public List<Attivita> listAttivita() {
        return Attivita.DAO.list(connection);
    }

    @Override
    public List<TipoAttivita> listTipiAttivita() {
        return TipoAttivita.DAO.list(connection);
    }

    @Override
    public void addAttivita(String username, LocalDateTime dataOraInizio, int durata,
                            double distanza, int dislivello, int codiceTipoAttivita) {
        inTransaction(() -> {
            Attivita.DAO.insert(connection, username, dataOraInizio, durata,
                    distanza, dislivello, codiceTipoAttivita);
            TipoAttivita.DAO.updateStatistics(connection, codiceTipoAttivita, distanza, durata);
        });
    }

    @Override
    public List<Attrezzatura> listAttrezzature() {
        return Attrezzatura.DAO.list(connection);
    }

    @Override
    public List<TipoAttrezzatura> listTipiAttrezzatura() {
        return TipoAttrezzatura.DAO.list(connection);
    }

    @Override
    public void addAttrezzatura(int codiceAttrezzatura, String marca, String modello,
                                String username, int codiceTipoAttrezzatura) {
        Attrezzatura.DAO.insert(connection, codiceAttrezzatura, marca, modello,
                username, codiceTipoAttrezzatura);
    }

    @Override
    public void addTipoAttrezzatura(int codiceTipoAttrezzatura, String nome,
                                    List<Integer> codiciTipiAttivitaCompatibili) {
        inTransaction(() -> {
            TipoAttrezzatura.DAO.insert(connection, codiceTipoAttrezzatura, nome);
            for (int codiceTipoAttivita : codiciTipiAttivitaCompatibili) {
                TipoAttrezzatura.DAO.insertCompatibility(
                        connection, codiceTipoAttivita, codiceTipoAttrezzatura);
            }
        });
    }

    @Override
    public void addUtilizzo(String username, LocalDateTime dataOraInizio,
                            int codiceAttrezzatura) {
        inTransaction(() -> {
            boolean valid = Attrezzatura.DAO.canBeUsedForActivity(
                    connection, username, dataOraInizio, codiceAttrezzatura);
            if (!valid) {
                throw new DAOException(new IllegalArgumentException(
                        "The equipment does not belong to the activity user, or its type is not compatible."));
            }

            Attrezzatura.DAO.insertUse(connection, username, dataOraInizio, codiceAttrezzatura);
            Attrezzatura.DAO.updateMileage(connection, username, dataOraInizio, codiceAttrezzatura);
        });
    }

    @Override
    public List<Segmento> listSegmenti() {
        return Segmento.DAO.list(connection);
    }

    @Override
    public List<Attraversamento> listAttraversamenti() {
        return Attraversamento.DAO.list(connection);
    }

    @Override
    public void addSegmento(int idSegmento, String nome, double distanza, int dislivello) {
        Segmento.DAO.insert(connection, idSegmento, nome, distanza, dislivello);
    }

    @Override
    public void addAttraversamento(String username, LocalDateTime dataOraInizio, int ordine,
                                   int tempoImpiegato, int idSegmento) {
        inTransaction(() -> {
            Attraversamento.DAO.insert(connection, username, dataOraInizio, ordine,
                    tempoImpiegato, idSegmento);
            Segmento.DAO.incrementCrossings(connection, idSegmento);
        });
    }

    @Override
    public List<Club> listClub() {
        return Club.DAO.list(connection);
    }

    @Override
    public List<Iscrizione> listIscrizioni() {
        return Iscrizione.DAO.list(connection);
    }

    @Override
    public List<PosizioneAmministratore> listPosizioniAmministratore() {
        return PosizioneAmministratore.DAO.list(connection);
    }

    @Override
    public void addClub(int idClub, String nome, String descrizione, String creatore,
                        int idPosizione, LocalDateTime dataCreazione) {
        inTransaction(() -> {
            Club.DAO.insert(connection, idClub, nome, descrizione);
            Iscrizione.DAO.insert(connection, creatore, idClub, dataCreazione);
            PosizioneAmministratore.DAO.insert(
                    connection, idPosizione, creatore, idClub, dataCreazione, dataCreazione);
        });
    }

    @Override
    public void addIscrizione(String username, int idClub, LocalDateTime dataIscrizione) {
        inTransaction(() -> {
            if (Iscrizione.DAO.hasActive(connection, username, idClub)) {
                throw new DAOException(new IllegalArgumentException(
                        "The user already has an active registration for this club."));
            }
            Iscrizione.DAO.insert(connection, username, idClub, dataIscrizione);
        });
    }

    @Override
    public void leaveClub(String username, int idClub, LocalDateTime dataDisiscrizione) {
        inTransaction(() -> {
            var dataIscrizione = Iscrizione.DAO.findActiveStart(connection, username, idClub);
            if (dataIscrizione == null) {
                throw new DAOException(new IllegalArgumentException(
                        "L'utente non possiede un'iscrizione attiva a questo club."));
            }
            if (dataDisiscrizione.isBefore(dataIscrizione)) {
                throw new DAOException(new IllegalArgumentException(
                        "The departure date cannot precede the registration date."));
            }

            PosizioneAmministratore.DAO.closeActiveForMembership(
                    connection, username, idClub, dataIscrizione, dataDisiscrizione);
            Iscrizione.DAO.close(
                    connection, username, idClub, dataIscrizione, dataDisiscrizione);
        });
    }

    @Override
    public List<Gara> listGare() {
        return Gara.DAO.list(connection);
    }

    @Override
    public List<TrattoGara> listTrattiGara() {
        return TrattoGara.DAO.list(connection);
    }

    @Override
    public List<Partecipazione> listPartecipazioni() {
        return Partecipazione.DAO.list(connection);
    }

    @Override
    public void addGara(int codiceGara, String nome, LocalDateTime dataCreazione,
                        LocalDateTime dataInizio, LocalDateTime dataFine, String creatore,
                        int idClub, int codiceTipoAttivita, List<Integer> idSegmenti) {
        inTransaction(() -> {
            Integer idPosizione = PosizioneAmministratore.DAO.findActiveIdForUserAndClub(
                    connection, creatore, idClub);
            if (idPosizione == null) {
                throw new DAOException(new IllegalArgumentException(
                        "The user is not an active administrator of the selected club."));
            }
            if (idSegmenti.isEmpty()) {
                throw new DAOException(new IllegalArgumentException(
                        "Il percorso della gara deve contenere almeno un segmento."));
            }

            Gara.DAO.insert(connection, codiceGara, nome, dataCreazione, dataInizio,
                    dataFine, idPosizione, codiceTipoAttivita);
            for (int i = 0; i < idSegmenti.size(); i++) {
                TrattoGara.DAO.insert(connection, codiceGara, i + 1, idSegmenti.get(i));
            }
        });
    }

    @Override
    public void addPartecipazione(String username, int codiceGara,
                                  LocalDateTime dataIscrizione) {
        inTransaction(() -> {
            if (!Partecipazione.DAO.canJoin(connection, username, codiceGara)) {
                throw new DAOException(new IllegalArgumentException(
                        "The user is not currently a member of the club organizing the race."));
            }
            Partecipazione.DAO.insert(connection, username, codiceGara, dataIscrizione);
        });
    }

    @Override
    public void registraRisultato(String username, int codiceGara, int tempoImpiegato) {
        if (tempoImpiegato <= 0) {
            throw new DAOException(new IllegalArgumentException(
                    "Il tempo impiegato deve essere maggiore di zero."));
        }
        Partecipazione.DAO.updateResult(connection, username, codiceGara, tempoImpiegato);
    }

    @Override
    public Report op13ClassificaDistanzaUtenti(LocalDateTime inizio, LocalDateTime fine) {
        return Report.DAO.execute(connection, Queries.OP13_CLASSIFICA_DISTANZA_UTENTI,
                Timestamp.valueOf(inizio), Timestamp.valueOf(fine));
    }

    @Override
    public Report op14MiglioriTempiSegmento(int idSegmento) {
        return Report.DAO.execute(connection, Queries.OP14_MIGLIORI_TEMPI_SEGMENTO, idSegmento);
    }

    @Override
    public Report op15AttrezzaturePerChilometraggio() {
        return Report.DAO.execute(connection, Queries.OP15_ATTREZZATURE_CHILOMETRAGGIO);
    }

    @Override
    public Report op16MediePerTipoAttivita() {
        return Report.DAO.execute(connection, Queries.OP16_MEDIE_TIPI_ATTIVITA);
    }

    @Override
    public Report op17SegmentiPiuPercorsi() {
        return Report.DAO.execute(connection, Queries.OP17_SEGMENTI_PIU_PERCORSI);
    }

    @Override
    public Report op18ClassificaClub(LocalDateTime inizio, LocalDateTime fine) {
        return Report.DAO.execute(connection, Queries.OP18_CLASSIFICA_CLUB,
                Timestamp.valueOf(inizio), Timestamp.valueOf(fine));
    }

    @Override
    public Report op19ClassificaGara(int codiceGara) {
        return Report.DAO.execute(connection, Queries.OP19_CLASSIFICA_GARA, codiceGara);
    }

    private void inTransaction(Runnable action) {
        boolean previousAutoCommit;
        try {
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new DAOException(e);
        }

        try {
            action.run();
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception rollbackError) {
                e.addSuppressed(rollbackError);
            }
            if (e instanceof DAOException daoException) {
                throw daoException;
            }
            throw new DAOException(e);
        } finally {
            try {
                connection.setAutoCommit(previousAutoCommit);
            } catch (Exception ignored) { }
        }
    }
}
