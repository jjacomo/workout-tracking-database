package db_lab.data;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Attivita {
    public final String username;
    public final LocalDateTime dataOraInizio;
    public final int durata;
    public final double distanza;
    public final int dislivello;
    public final int codiceTipoAttivita;
    public final String tipoAttivita;

    public Attivita(String username, LocalDateTime dataOraInizio, int durata,
                    double distanza, int dislivello, int codiceTipoAttivita,
                    String tipoAttivita) {
        this.username = username;
        this.dataOraInizio = dataOraInizio;
        this.durata = durata;
        this.distanza = distanza;
        this.dislivello = dislivello;
        this.codiceTipoAttivita = codiceTipoAttivita;
        this.tipoAttivita = tipoAttivita;
    }

    @Override
    public String toString() {
        return username + " - " + dataOraInizio + " - " + tipoAttivita + " (" + distanza + " km)";
    }

    public static final class DAO {
        private DAO() { }

        public static List<Attivita> list(Connection connection) {
            var result = new ArrayList<Attivita>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_ATTIVITA);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Attivita(
                        rs.getString("username"),
                        rs.getTimestamp("dataOraInizio").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getDouble("distanza"),
                        rs.getInt("dislivello"),
                        rs.getInt("codiceTipoAttivita"),
                        rs.getString("tipoAttivita")
                    ));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, String username,
                                  LocalDateTime dataOraInizio, int durata,
                                  double distanza, int dislivello,
                                  int codiceTipoAttivita) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.INSERT_ATTIVITA,
                    username,
                    Timestamp.valueOf(dataOraInizio),
                    durata,
                    distanza,
                    dislivello,
                    codiceTipoAttivita)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
