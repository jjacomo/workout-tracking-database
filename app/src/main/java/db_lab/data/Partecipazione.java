package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Partecipazione {
    public final String username;
    public final int codiceGara;
    public final String nomeGara;
    public final LocalDateTime dataIscrizione;
    public final Integer tempoImpiegato;

    public Partecipazione(String username, int codiceGara, String nomeGara,
                          LocalDateTime dataIscrizione, Integer tempoImpiegato) {
        this.username = username;
        this.codiceGara = codiceGara;
        this.nomeGara = nomeGara;
        this.dataIscrizione = dataIscrizione;
        this.tempoImpiegato = tempoImpiegato;
    }

    @Override
    public String toString() {
        return username + " - " + codiceGara + " - " + nomeGara;
    }

    public static final class DAO {
        private DAO() { }

        public static List<Partecipazione> list(Connection connection) {
            var result = new ArrayList<Partecipazione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_PARTECIPAZIONI);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    Object time = rs.getObject("tempoImpiegato");
                    result.add(new Partecipazione(
                            rs.getString("username"),
                            rs.getInt("codiceGara"),
                            rs.getString("nomeGara"),
                            rs.getTimestamp("dataIscrizione").toLocalDateTime(),
                            time == null ? null : rs.getInt("tempoImpiegato")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static boolean canJoin(Connection connection, String username, int codiceGara) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.CHECK_PARTECIPAZIONE_VALIDA,
                    codiceGara, username);
                 var rs = statement.executeQuery()) {
                return rs.next();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void insert(Connection connection, String username,
                                  int codiceGara, LocalDateTime dataIscrizione) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_PARTECIPAZIONE,
                    username, codiceGara, Timestamp.valueOf(dataIscrizione))) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void updateResult(Connection connection, String username,
                                        int codiceGara, int tempoImpiegato) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.UPDATE_RISULTATO_GARA,
                    tempoImpiegato, username, codiceGara)) {
                int updated = statement.executeUpdate();
                if (updated == 0) {
                    throw new IllegalArgumentException(
                            "L'utente selezionato non risulta iscritto alla gara.");
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
