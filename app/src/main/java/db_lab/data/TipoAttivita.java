package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class TipoAttivita {
    public final int codice;
    public final String nome;
    public final int numeroAttivita;
    public final double distanzaTotale;
    public final int durataTotale;

    public TipoAttivita(int codice, String nome, int numeroAttivita,
            double distanzaTotale, int durataTotale) {
        this.codice = codice;
        this.nome = nome;
        this.numeroAttivita = numeroAttivita;
        this.distanzaTotale = distanzaTotale;
        this.durataTotale = durataTotale;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static final class DAO {
        private DAO() {
        }

        public static List<TipoAttivita> list(Connection connection) {
            var result = new ArrayList<TipoAttivita>();
            try (
                    var statement = DAOUtils.prepare(connection, Queries.LIST_TIPI_ATTIVITA);
                    var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new TipoAttivita(
                            rs.getInt("codiceTipoAttivita"),
                            rs.getString("nome"),
                            rs.getInt("numeroAttivita"),
                            rs.getDouble("distanzaTotale"),
                            rs.getInt("durataTotale")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void updateStatistics(Connection connection, int codiceTipoAttivita,
                double distanza, int durata) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.UPDATE_STATISTICHE_TIPO_ATTIVITA,
                    distanza, durata, codiceTipoAttivita)) {
                int updated = statement.executeUpdate();
                if (updated != 1) {
                    throw new DAOException(new IllegalStateException(
                            "Tipo attività inesistente: " + codiceTipoAttivita));
                }
            } catch (DAOException e) {
                throw e;
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
