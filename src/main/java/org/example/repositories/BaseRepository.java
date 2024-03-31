package org.example.repositories;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.annotations.AutoExclude;
import org.example.annotations.IdentityNo;
import org.example.supers.Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseRepository extends Utils {

    Dotenv env = Dotenv.load();

    protected Connection conn;

    private final String URL = env.get("DB_URL");
    private final String USERNAME = env.get("DB_USERNAME");
    private final String PASSWORD = env.get("DB_PASSWORD");

    public BaseRepository() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to DB: " + e.getMessage());
        }
    }

    /**
     * Endast för testning...
    */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            String tableName = u.getTableName(entityClass);
            String sql = "SELECT * FROM " + tableName;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                T entity = createEntityFromRS(rs, entityClass);
                list.add(entity);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T,R> List<T> findAllByForeignKey(Class<T> entityClass, Class<R> foreignClass, Integer foreignEntityId) {
        List<T> list = new ArrayList<>();
        try {
            String tableName = u.getTableName(entityClass);
            String foreignKeyName = u.getPrimaryKey(foreignClass);
            String sql = "SELECT * FROM " + tableName + " WHERE " + foreignKeyName + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, foreignEntityId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                T entity = createEntityFromRS(rs, entityClass);
                list.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    public <T> T findById(Class<T> entityClass, Integer entityId) {
        try {
            String tableName = u.getTableName(entityClass);
            String primaryKey = u.getPrimaryKey(entityClass);

            String sql = "SELECT * FROM " + tableName + " WHERE " + primaryKey + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entityId);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return createEntityFromRS(rs, entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find entity by ID: " + e.getMessage());
        }
    }

    public <T> T findByEmail(Class<T> entityClass, String entityEmail) {
        try {
            String tableName = u.getTableName(entityClass);
            String sql = "SELECT * FROM " + tableName + " WHERE email = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entityEmail);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return createEntityFromRS(rs, entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find entity by email: " + e.getMessage());
        }
    }

    public <T> T findByIdentityNo(Class<T> entityClass, String entityNo) {
        try {
            String tableName = u.getTableName(entityClass);
            String identityFieldName = u.getIdentityFieldName(entityClass);
            String sql = "SELECT * FROM " + tableName + " WHERE " + identityFieldName + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entityNo);

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return createEntityFromRS(rs, entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find entity by identity number: " + e.getMessage());
        }
    }

    public <T> T create(Class<T> entityClass, T entity) {
        try {
            String tableName = u.getTableName(entityClass);
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(tableName).append(" (");

            List<Field> fields = new ArrayList<>();
            for (Field field : entityClass.getDeclaredFields()) {
                System.out.println(field);
                field.setAccessible(true);
                if (field.isAnnotationPresent(AutoExclude.class)) continue;
                if (field.isAnnotationPresent(IdentityNo.class)) {
                    var identityNo = getIdentityNo(entityClass);
                    System.out.println(identityNo);
                    field.set(entity, identityNo);
                }
                sql.append(u.camelToSnakeCase(field.getName())).append(", ");
                fields.add(field);
            }

            System.out.println(sql);

            sql.setLength(sql.length() - 2);
            sql.append(") VALUES (");
            sql.append("?, ".repeat(fields.size()));
            sql.setLength(sql.length() - 2);
            sql.append(")");


            PreparedStatement pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(entity);
                pstmt.setObject(index++, value);
            }


            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new Exception("Not created...");
            }

            int insertedID;

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertedID = generatedKeys.getInt(1);
                return findById(entityClass, insertedID);
            } else {
                throw new Exception("Created but not found... wtf, mate?");
            }

        } catch (Exception e) {
            p.printError(e.getMessage());
            return null;
        }
    }

    public <T> T update(Class<T> entityClass, T updatedEntity) {
        try {
            String tableName = u.getTableName(entityClass);
            String primaryKey = u.getPrimaryKey(entityClass);
            Field[] fields = entityClass.getDeclaredFields();
            int primaryKeyValue = 0;

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ").append(tableName).append(" SET ");
            for (Field field : fields) {
                var fieldName = u.getFieldName(field);
                field.setAccessible(true);
                sql.append(fieldName).append(" = ?, ");
                if (fieldName.equals(primaryKey)) {
                    primaryKeyValue = (int) field.get(updatedEntity);
                }
            }
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE ").append(primaryKey).append(" = ?");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(updatedEntity);
                pstmt.setObject(index++, value);
            }
            pstmt.setObject(index, primaryKeyValue);

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new Exception("Not updated...");
            }

            return updatedEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> boolean deleteById(Class<T> entityClass, Integer entityId) {
        try {
            String tableName = u.getTableName(entityClass);
            String primaryKey = u.getPrimaryKey(entityClass);

            String sql = "DELETE FROM " + tableName + " WHERE " + primaryKey + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entityId);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T,R> boolean deleteByIdAuthId(Class<T> entityClass, Integer entityId, Class<R> authClass, Integer authId) {
        try {
            String tableName = u.getTableName(entityClass);
            String primaryKey = u.getPrimaryKey(entityClass);
            String authKey = u.getPrimaryKey(authClass);

            String sql = "DELETE FROM " + tableName + " WHERE " + primaryKey + " = ? AND " + authKey + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entityId);
            pstmt.setInt(2, authId);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> void deleteReferences(Class<T> entityClass, Integer entityId) throws SQLException {
        try {
            conn.setAutoCommit(false);
            String[] referencedTables = u.getReferencedTables(entityClass);
            String key = u.getPrimaryKey(entityClass);
            for (String table : referencedTables) {
                String sql = "UPDATE TABLE " + table + " SET " + key + " = ? WHERE " + key + " = ?";
                System.out.println(sql);
                var pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, null);
                pstmt.setObject(2, entityId);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            conn.rollback();
            p.printError(e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    public <T, R> Integer getCountByAuthId(Class<T> entityClass, Class<R> authClass, Integer authId) {
        try {
            String tableName = u.getTableName(entityClass);
            String primaryKey = u.getPrimaryKey(entityClass);
            String authKey = u.getPrimaryKey(authClass);

            String sql = "SELECT COUNT(" + primaryKey + ") AS count FROM " + tableName + " WHERE " + authKey + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, authId);

            ResultSet rs = pstmt.executeQuery();

            int count = 0;

            if (rs.next()) {
                count = rs.getInt("count");
            }

            return count;
        } catch (Exception e) {
            System.out.println("No count found...");
            return 0;
        }
    }

    /* ----- ResultSet ----- */
    protected <T> T createEntityFromRS(ResultSet rs, Class<T> entityClass) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                String columnName = u.snakeToCamelCase(rs.getMetaData().getColumnName(i));
                Object columnValue = rs.getObject(i);

                Field field = entityClass.getDeclaredField(columnName);
                field.setAccessible(true);
                if (columnValue != null) {
                    field.set(entity, columnValue);
                }

            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* --x-- ResultSet --x-- */

    public <T> String getIdentityNo(Class<T> entityClass) {
        System.out.println("IDENTITY NUMBER GENERATOR");
        try {
            String identityNo;
            String tableName = u.getTableName(entityClass);
            String columnName = u.getIdentityFieldName(entityClass);
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";

            boolean exists = true;
            do {
                identityNo = u.generateIdentityNo(entityClass);

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, identityNo);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);
                    exists = count > 0;
                }
            } while (exists);

            return identityNo;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIdentityNoMySQLFunctions(String capitalizedClassName) {
        try {
            var stmt = conn.createStatement();
            String sql = "SELECT generate" + capitalizedClassName + "No()";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                Object identityNo = rs.getInt(1);
                return identityNo.toString();
            }

            throw new SQLException("Not generated...");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void dropAll() {
        try {
            disableForeignKeyChecks();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                drop(tableName);
            }
            enableForeignKeyChecks();
            System.out.println("Tables dropped...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drop(String tableName) {
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            System.out.println("Failed to drop table " + tableName + ": " + e.getMessage());
        }
    }

    private void disableForeignKeyChecks() throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
        }
    }

    private void enableForeignKeyChecks() throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

}
