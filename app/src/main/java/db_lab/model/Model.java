package db_lab.model;

import db_lab.data.Attivita;
import db_lab.data.Attraversamento;
import db_lab.data.Attrezzatura;
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

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface Model {
    List<Utente> listUtenti();
    void addUtente(String username, String password, String nome, String cognome, String email);

    List<Attivita> listAttivita();
    List<TipoAttivita> listTipiAttivita();
    void addAttivita(String username, LocalDateTime dataOraInizio, int durata,
                     double distanza, int dislivello, int codiceTipoAttivita);

    List<Attrezzatura> listAttrezzature();
    List<TipoAttrezzatura> listTipiAttrezzatura();
    void addAttrezzatura(int codiceAttrezzatura, String marca, String modello,
                         String username, int codiceTipoAttrezzatura);
    void addTipoAttrezzatura(int codiceTipoAttrezzatura, String nome,
                             List<Integer> codiciTipiAttivitaCompatibili);
    void addUtilizzo(String username, LocalDateTime dataOraInizio, int codiceAttrezzatura);

    List<Segmento> listSegmenti();
    List<Attraversamento> listAttraversamenti();
    void addSegmento(int idSegmento, String nome, double distanza, int dislivello);
    void addAttraversamento(String username, LocalDateTime dataOraInizio, int ordine,
                            int tempoImpiegato, int idSegmento);

    List<Club> listClub();
    List<Iscrizione> listIscrizioni();
    List<PosizioneAmministratore> listPosizioniAmministratore();
    void addClub(int idClub, String nome, String descrizione, String creatore,
                 int idPosizione, LocalDateTime dataCreazione);
    void addIscrizione(String username, int idClub, LocalDateTime dataIscrizione);
    void leaveClub(String username, int idClub, LocalDateTime dataDisiscrizione);

    List<Gara> listGare();
    List<TrattoGara> listTrattiGara();
    List<Partecipazione> listPartecipazioni();
    void addGara(int codiceGara, String nome, LocalDateTime dataCreazione,
                 LocalDateTime dataInizio, LocalDateTime dataFine, String creatore,
                 int idClub, int codiceTipoAttivita, List<Integer> idSegmenti);
    void addPartecipazione(String username, int codiceGara, LocalDateTime dataIscrizione);
    void registraRisultato(String username, int codiceGara, int tempoImpiegato);

    Report op13ClassificaDistanzaUtenti(LocalDateTime inizio, LocalDateTime fine);
    Report op14MiglioriTempiSegmento(int idSegmento);
    Report op15AttrezzaturePerChilometraggio();
    Report op16MediePerTipoAttivita();
    Report op17SegmentiPiuPercorsi();
    Report op18ClassificaClub(LocalDateTime inizio, LocalDateTime fine);
    Report op19ClassificaGara(int codiceGara);

    static Model fromConnection(Connection connection) {
        return new DBModel(connection);
    }
}
