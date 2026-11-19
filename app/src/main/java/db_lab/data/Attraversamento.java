package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Attraversamento {
    public final String username;
    public final LocalDateTime dataOraInizio;
    public final int ordine;
    public final int tempoImpiegato;
    public final int idSegmento;
    public final String nomeSegmento;

    public Attraversamento(String username, LocalDateTime dataOraInizio, int ordine,
                           int tempoImpiegato, int idSegmento, String nomeSegmento) {
        this.username = username;
        this.dataOraInizio = dataOraInizio;
        this.ordine = ordine;
        this.tempoImpiegato = tempoImpiegato;
        this.idSegmento = idSegmento;
        this.nomeSegmento = nomeSegmento;
    }

    public static final class DAO {
        private DAO() { }

        public static List<Attraversamento> list(Connection connection) {
            var result = new ArrayList<Attraversamento>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_ATTRAVERSAMENTI);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Attraversamento(
                            rs.getString("username"),
                            rs.getTimestamp("dataOraInizio").toLocalDateTime(),
                            rs.getInt("ordine"),
                            rs.getInt("tempoImpiegato"),
                            rs.getInt("IDSegmento"),
                            rs.getString("nomeSegmento")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, String username,
                                  LocalDateTime dataOraInizio, int ordine,
                                  int tempoImpiegato, int idSegmento) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_ATTRAVERSAMENTO,
                    username, Timestamp.valueOf(dataOraInizio), ordine,
                    tempoImpiegato, idSegmento)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
