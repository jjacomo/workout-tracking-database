package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Gara {
    public final int codice;
    public final String nome;
    public final LocalDateTime dataCreazione;
    public final LocalDateTime dataInizio;
    public final LocalDateTime dataFine;
    public final int idPosizione;
    public final String creatore;
    public final int idClub;
    public final String nomeClub;
    public final int codiceTipoAttivita;
    public final String tipoAttivita;

    public Gara(int codice, String nome, LocalDateTime dataCreazione,
                LocalDateTime dataInizio, LocalDateTime dataFine,
                int idPosizione, String creatore, int idClub, String nomeClub,
                int codiceTipoAttivita, String tipoAttivita) {
        this.codice = codice;
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.idPosizione = idPosizione;
        this.creatore = creatore;
        this.idClub = idClub;
        this.nomeClub = nomeClub;
        this.codiceTipoAttivita = codiceTipoAttivita;
        this.tipoAttivita = tipoAttivita;
    }

    @Override
    public String toString() {
        return codice + " - " + nome + " (" + nomeClub + ")";
    }

    public static final class DAO {
        private DAO() { }

        public static List<Gara> list(Connection connection) {
            var result = new ArrayList<Gara>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_GARE);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Gara(
                            rs.getInt("codiceGara"),
                            rs.getString("nome"),
                            rs.getTimestamp("dataCreazione").toLocalDateTime(),
                            rs.getTimestamp("dataInizio").toLocalDateTime(),
                            rs.getTimestamp("dataFine").toLocalDateTime(),
                            rs.getInt("IDPosizione"),
                            rs.getString("creatore"),
                            rs.getInt("IDClub"),
                            rs.getString("nomeClub"),
                            rs.getInt("codiceTipoAttivita"),
                            rs.getString("tipoAttivita")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int codice, String nome,
                                  LocalDateTime dataCreazione, LocalDateTime dataInizio,
                                  LocalDateTime dataFine, int idPosizione,
                                  int codiceTipoAttivita) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_GARA,
                    codice, nome,
                    Timestamp.valueOf(dataCreazione),
                    Timestamp.valueOf(dataInizio),
                    Timestamp.valueOf(dataFine),
                    idPosizione, codiceTipoAttivita)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
