package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class TipoAttrezzatura {
    public final int codice;
    public final String nome;

    public TipoAttrezzatura(int codice, String nome) {
        this.codice = codice;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static final class DAO {
        private DAO() { }

        public static List<TipoAttrezzatura> list(Connection connection) {
            var result = new ArrayList<TipoAttrezzatura>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_TIPI_ATTREZZATURA);
                var rs = statement.executeQuery()
            ) {
                while (rs.next()) {
                    result.add(new TipoAttrezzatura(
                            rs.getInt("codiceTipoAttrezzatura"),
                            rs.getString("nome")));
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return result;
        }

        public static void insert(Connection connection, int codice, String nome) {
            try (var statement = DAOUtils.prepare(
                    connection, Queries.INSERT_TIPO_ATTREZZATURA, codice, nome)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void insertCompatibility(Connection connection,
                                               int codiceTipoAttivita,
                                               int codiceTipoAttrezzatura) {
            try (var statement = DAOUtils.prepare(
                    connection,
                    Queries.INSERT_CONSENTE,
                    codiceTipoAttivita,
                    codiceTipoAttrezzatura)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
