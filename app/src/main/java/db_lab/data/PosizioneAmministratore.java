package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PosizioneAmministratore {
    public final int idPosizione;
    public final String username;
    public final int idClub;
    public final String nomeClub;
    public final LocalDateTime dataIscrizione;
    public final LocalDateTime dataInizio;
    public final LocalDateTime dataFine;

    public PosizioneAmministratore(int idPosizione, String username, int idClub,
                                   String nomeClub, LocalDateTime dataIscrizione,
                                   LocalDateTime dataInizio, LocalDateTime dataFine) {
        this.idPosizione = idPosizione;
        this.username = username;
        this.idClub = idClub;
        this.nomeClub = nomeClub;
        this.dataIscrizione = dataIscrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public static final class DAO {
        private DAO() { }

        public static List<PosizioneAmministratore> list(Connection connection) {
            var result = new ArrayList<PosizioneAmministratore>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_POSIZIONI_AMMINISTRATORE);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    var end = rs.getTimestamp("dataFine");
                    result.add(new PosizioneAmministratore(
                            rs.getInt("IDPosizione"),
                            rs.getString("username"),
                            rs.getInt("IDClub"),
                            rs.getString("nomeClub"),
                            rs.getTimestamp("dataIscrizione").toLocalDateTime(),
                            rs.getTimestamp("dataInizio").toLocalDateTime(),
                            end == null ? null : end.toLocalDateTime()));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int idPosizione, String username,
                                  int idClub, LocalDateTime dataIscrizione,
                                  LocalDateTime dataInizio) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_POSIZIONE_AMMINISTRATORE,
                    idPosizione, username, idClub,
                    Timestamp.valueOf(dataIscrizione), Timestamp.valueOf(dataInizio))) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static Integer findActiveIdForUserAndClub(Connection connection,
                                                           String username, int idClub) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.FIND_POSIZIONE_AMMINISTRATORE_ATTIVA,
                    username, idClub);
                 var rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getInt("IDPosizione");
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void closeActiveForMembership(Connection connection, String username,
                                                    int idClub, LocalDateTime dataIscrizione,
                                                    LocalDateTime dataFine) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.CLOSE_POSIZIONI_AMMINISTRATORE,
                    Timestamp.valueOf(dataFine), username, idClub,
                    Timestamp.valueOf(dataIscrizione))) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
