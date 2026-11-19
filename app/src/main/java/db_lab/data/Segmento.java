package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class Segmento {
    public final int id;
    public final String nome;
    public final double distanza;
    public final int dislivello;
    public final int numeroAttraversamenti;

    public Segmento(int id, String nome, double distanza, int dislivello,
                    int numeroAttraversamenti) {
        this.id = id;
        this.nome = nome;
        this.distanza = distanza;
        this.dislivello = dislivello;
        this.numeroAttraversamenti = numeroAttraversamenti;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + distanza + " km)";
    }

    public static final class DAO {
        private DAO() { }

        public static List<Segmento> list(Connection connection) {
            var result = new ArrayList<Segmento>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_SEGMENTI);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new Segmento(
                            rs.getInt("IDSegmento"),
                            rs.getString("nome"),
                            rs.getDouble("distanza"),
                            rs.getInt("dislivello"),
                            rs.getInt("numeroAttraversamenti")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int id, String nome,
                                  double distanza, int dislivello) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_SEGMENTO,
                    id, nome, distanza, dislivello)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void incrementCrossings(Connection connection, int idSegmento) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.UPDATE_NUMERO_ATTRAVERSAMENTI, idSegmento)) {
                int updated = statement.executeUpdate();
                if (updated != 1) {
                    throw new DAOException(new IllegalStateException(
                            "Il segmento da aggiornare non esiste."));
                }
            } catch (DAOException e) {
                throw e;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
