package db_lab.data;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class Attrezzatura {
    public final int codice;
    public final String marca;
    public final String modello;
    public final double chilometraggio;
    public final String username;
    public final int codiceTipoAttrezzatura;
    public final String tipoAttrezzatura;

    public Attrezzatura(int codice, String marca, String modello,
                        double chilometraggio, String username,
                        int codiceTipoAttrezzatura, String tipoAttrezzatura) {
        this.codice = codice;
        this.marca = marca;
        this.modello = modello;
        this.chilometraggio = chilometraggio;
        this.username = username;
        this.codiceTipoAttrezzatura = codiceTipoAttrezzatura;
        this.tipoAttrezzatura = tipoAttrezzatura;
    }

    @Override
    public String toString() {
        return codice + " - " + tipoAttrezzatura + " - " + marca + " " + modello + " (" + username + ")";
    }

    public static final class DAO {
        private DAO() { }

        public static List<Attrezzatura> list(Connection connection) {
            var result = new ArrayList<Attrezzatura>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_ATTREZZATURE);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Attrezzatura(
                            rs.getInt("codiceAttrezzatura"),
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getDouble("chilometraggio"),
                            rs.getString("username"),
                            rs.getInt("codiceTipoAttrezzatura"),
                            rs.getString("tipoAttrezzatura")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int codice,
                                  String marca, String modello,
                                  String username, int codiceTipoAttrezzatura) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.INSERT_ATTREZZATURA,
                    codice, marca, modello, username, codiceTipoAttrezzatura)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static boolean canBeUsedForActivity(Connection connection,
                                                   String username,
                                                   LocalDateTime dataOraInizio,
                                                   int codiceAttrezzatura) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.CHECK_UTILIZZO_VALIDO,
                    username,
                    Timestamp.valueOf(dataOraInizio),
                    codiceAttrezzatura);
                 var rs = statement.executeQuery()) {
                return rs.next();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void insertUse(Connection connection,
                                     String username,
                                     LocalDateTime dataOraInizio,
                                     int codiceAttrezzatura) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.INSERT_UTILIZZO,
                    username,
                    Timestamp.valueOf(dataOraInizio),
                    codiceAttrezzatura)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void updateMileage(Connection connection,
                                         String username,
                                         LocalDateTime dataOraInizio,
                                         int codiceAttrezzatura) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.UPDATE_CHILOMETRAGGIO_ATTREZZATURA,
                    username,
                    Timestamp.valueOf(dataOraInizio),
                    codiceAttrezzatura)) {
                int updated = statement.executeUpdate();
                if (updated != 1) {
                    throw new DAOException(new IllegalStateException(
                            "Attrezzatura inesistente: " + codiceAttrezzatura));
                }
            } catch (DAOException e) {
                throw e;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
