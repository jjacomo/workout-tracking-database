package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Iscrizione {
    public final String username;
    public final int idClub;
    public final String nomeClub;
    public final LocalDateTime dataIscrizione;
    public final LocalDateTime dataDisiscrizione;

    public Iscrizione(String username, int idClub, String nomeClub,
                      LocalDateTime dataIscrizione, LocalDateTime dataDisiscrizione) {
        this.username = username;
        this.idClub = idClub;
        this.nomeClub = nomeClub;
        this.dataIscrizione = dataIscrizione;
        this.dataDisiscrizione = dataDisiscrizione;
    }

    public static final class DAO {
        private DAO() { }

        public static List<Iscrizione> list(Connection connection) {
            var result = new ArrayList<Iscrizione>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_ISCRIZIONI);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    var end = rs.getTimestamp("DateDistrict");
                    result.add(new Iscrizione(
                            rs.getString("username"),
                            rs.getInt("IDClub"),
                            rs.getString("NameClub"),
                            rs.getTimestamp("Date").toLocalDateTime(),
                            end == null ? null : end.toLocalDateTime()));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static boolean hasActive(Connection connection, String username, int idClub) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.CHECK_ISCRIZIONE_ATTIVA, username, idClub);
                 var rs = statement.executeQuery()) {
                return rs.next();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void insert(Connection connection, String username, int idClub,
                                  LocalDateTime dataIscrizione) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_ISCRIZIONE,
                    username, idClub, Timestamp.valueOf(dataIscrizione))) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static LocalDateTime findActiveStart(Connection connection, String username, int idClub) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.FIND_ISCRIZIONE_ATTIVA, username, idClub);
                 var rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getTimestamp("Date").toLocalDateTime();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void close(Connection connection, String username, int idClub,
                                 LocalDateTime dataIscrizione, LocalDateTime dataDisiscrizione) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.CLOSE_ISCRIZIONE,
                    Timestamp.valueOf(dataDisiscrizione), username, idClub,
                    Timestamp.valueOf(dataIscrizione))) {
                int updated = statement.executeUpdate();
                if (updated != 1) {
                    throw new DAOException(new IllegalStateException(
                            "The active registration is no longer available."));
                }
            } catch (DAOException e) {
                throw e;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
