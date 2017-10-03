/*
 *          Copyright 2016-2026 Rogério Lecarião Leite
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  CEO 2016: Rogério Lecarião Leite; ROLL Software
 */
package com.rollsoftware.br.common.db.internal.derby;

import com.rollsoftware.br.common.db.internal.DatabaseManager;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public class DerbyDatabaseManager implements DatabaseManager {

    private DerbyDatabaseManager() {
        try {
            System.setProperty("derby.system.home", getDerbySystemHome());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Throwable ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Connection connect(String connect) throws SQLException {
        return connect(connect, Resource.getSystemProperties());
    }

    @Override
    public Connection connect(String connect, Properties properties)
            throws SQLException {
        try {
            //System.out.println("CONNECT: " + connect);
            return DriverManager.getConnection(connect, properties);
        } catch (SQLException ex) {
            if ("XJ015".equals(ex.getSQLState())
                    && ex.getErrorCode() == 5000) {
                //complete shutdown
                return null;
            } else if ("08006".equals(ex.getSQLState())
                    && ex.getErrorCode() == 45000) {
                //database shutdown
                return null;
            }
            throw new SQLException("URL: " + connect, ex);
        }
    }

    @Override
    public void disconnect(Connection conn, String shutdown)
            throws SQLException {
        if (conn != null) {
            conn.close();
        }
        connect(shutdown);
    }

    @Override
    public boolean pathToDatabaseExists() {
        return new File(getPathToDatabase()).isDirectory();
    }

    @Override
    public boolean rulesExists() {
        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());
            Properties properties = Resource.getSystemProperties();

            for (Map.Entry entry : properties.entrySet()) {
                List<String> values = getDatabaseProperty(
                        conn, (String) entry.getKey());
                if (values.isEmpty()
                        || values.contains(null)
                        || !values.contains((String) entry.getValue())) {
                    return false;
                }
            }

            return true;
        } catch (SQLException ex) {
            return false;
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public boolean schemaExists(String schema) {
        String sql
                = "select * from sys.sysschemas as sysschemas"
                + " where schemaname = '" + schema + "'";

        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());

            List<String> values = executeQuerySQL(conn, sql);

            if (!values.isEmpty()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
        return false;
    }

    @Override
    public boolean tableExists(String schema, String table) {
        String sql
                = "select * from sys.systables as systables "
                + " left join sys.sysschemas as sysschemas "
                + "  on sysschemas.schemaid=systables.schemaid "
                + " where systables.tablename = '" + table + "' "
                + "  and sysschemas.schemaname = '" + schema + "'";

        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());

            List<String> values = executeQuerySQL(conn, sql);

            if (!values.isEmpty()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
        return false;
    }

    @Override
    public void createDatabase() throws SQLException {
        new File(getDerbySystemHome()).mkdirs();
        Connection conn = null;
        try {
            conn = connect(getURLDatabaseCreate());
        } finally {
            disconnect(conn, getURLDatabaseShutdown());
        }
        createUsers();
    }

    @Override
    public void createUsers() throws SQLException {
        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());
            Properties properties = Resource.getDerbyUsers();

            for (Map.Entry entry : properties.entrySet()) {
                setDatabaseProperty(conn,
                        (String) entry.getKey(),
                        (String) entry.getValue());
            }
        } catch (SQLException ex) {
            throw new SQLException("URL: " + getURLDatabaseConnect(), ex);
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public void createRules() throws SQLException {
        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());
            Properties properties = Resource.getSystemProperties();

            for (Map.Entry entry : properties.entrySet()) {
                setDatabaseProperty(conn,
                        (String) entry.getKey(),
                        (String) entry.getValue());
            }
        } catch (SQLException ex) {
            throw new SQLException("URL: " + getURLDatabaseConnect(), ex);
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public void createSchema(String schema, String userOwner)
            throws SQLException {
        String sql
                = "create schema " + schema + " authorization " + userOwner;

        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());

            executeSQL(conn, sql);
        } catch (SQLException ex) {
            throw new SQLException("SQL: " + sql, ex);
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public void createTable(String schema, String table, String columns)
            throws SQLException {
        String sql
                = "create table " + schema + "." + table + " " + columns;

        Connection conn = null;
        try {
            conn = connect(getURLDatabaseConnect());

            executeSQL(conn, sql);
        } catch (SQLException ex) {
            throw new SQLException("SQL: " + sql, ex);
        } finally {
            try {
                disconnect(conn, getURLDatabaseShutdown());
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public void start() throws SQLException {

        if (!pathToDatabaseExists()) {
            createDatabase();
        }

        if (!rulesExists()) {
            createRules();
        }

        for (String[] schema : getSchemas()) {
            if (!schemaExists(schema[0])) {
                createSchema(schema[0], schema[1]);
            }

            for (String[] table
                    : getTables(schema[0])) {
                if (!tableExists(schema[0], table[0])) {
                    createTable(schema[0], table[0], table[1]);
                }
            }
        }
    }

    protected static String getURLDatabaseCreate() {
        return Resource.getProperty(
                "url.database.create.encrypt.encryptionKey");
//return Resource.getProperty("url.database.create.encrypt.bootPassword");
//return Resource.getProperty("url.database.create");
    }

    protected static String getUserDatabase() {
        return Resource.getProperty("var.database.user");
    }

    protected static String getURLDatabaseConnect() {
        return Resource.getProperty(
                "url.database.connect.encrypt.encryptionKey");
//return Resource.getProperty("url.database.connect.encrypt.bootPassword");
//return Resource.getProperty("url.database.connect");
    }

    protected static String getURLDatabaseShutdown() {
        return Resource.getProperty(
                "url.database.shutdown.encrypt.encryptionKey");
//return Resource.getProperty("url.database.shutdown.encrypt.bootPassword");
//return Resource.getProperty("url.database.shutdown");
    }

    protected static String getDerbySystemHome() {
        return Resource.getProperty("derby.system.home");
    }

    protected static String getPathToDatabase() {
        return new File(getDerbySystemHome()
                + File.separator + Resource.getProperty("var.database.name"))
                .toPath().toString();
    }

    protected List<String[]> getSchemas() {
        List<String[]> schemas = new ArrayList();
        schemas.add(new String[]{"LFMS_DB", getUserDatabase()});
        return schemas;
    }

    protected List<String[]> getTables(String schema) {
        List<String[]> tables = new ArrayList();
        tables.add(new String[]{"OBJECT_DATA", " ("
            + "  odid int not null generated always as identity "
            + "    (start with 1, increment by 1),"
            + "  odVersion int not null,"
            + "  odType varchar(64) not null,"
            + "  odUUIDPK varchar(36) not null,"
            + "  constraint od_object_data_PK primary key (odUUIDPK),"
            + "  constraint od_object_data_ID unique (odid),"
            + "  constraint od_object_data_unique unique (odType, odUUIDPK)"
            + ")"});
        tables.add(new String[]{"LOGIN_INFO", "("
            + "  liUUIDFK varchar(36) not null,"
            + "  liVersion int not null,"
            + "  liUser varchar(128) not null,"
            + "  liPass varchar(128) not null,"
            + "  liFirstName varchar(64) not null,"
            + "  liLastName varchar(256) not null,"
            + "  liSuccessCount int default 0,"
            + "  liErrorCount int default 0,"
            + "  liBlockedCount int default 0,"
            + "  liDateSoftban timestamp default null,"
            + "  liDatePermban timestamp default null,"
            + "  liDateCreated timestamp not null default current_timestamp,"
            + "  liDateAccessed timestamp not null default current_timestamp,"
            + "  liDateActivated timestamp default null,"
            + "  liDateExpired timestamp default null,"
            + "  liDateBlocked timestamp default null,"
            + "  constraint li_login_info_PK primary key (liUUIDFK),"
            + "  constraint li_login_info_unique unique (liUser),"
            + "  constraint li_object_data_FK"
            + "   foreign key (liUUIDFK)"
            + "   references lfms_db.object_data (odUUIDPK)"
            + "   on update restrict"
            + "   on delete cascade"
            + ")"});
        tables.add(new String[]{"TOKEN_INFO", "("
            + "  tiUUIDFK varchar(36) not null,"
            + "  tiVersion int not null,"
            + "  ti_loginInfo_UUIDFK varchar(36) not null,"
            + "  tiAccessToken varchar(128) not null,"
            + "  tiUserIP varchar(64) not null,"
            + "  tiSuccessCount int default 0,"
            + "  tiRefusedCount int default 0,"
            + "  tiErrorCount int default 0,"
            + "  tiDateCreated timestamp not null default current_timestamp,"
            + "  tiDateAccessed timestamp not null default current_timestamp,"
            + "  tiDateExpires timestamp not null default current_timestamp,"
            + "  constraint ti_token_info_PK primary key (tiAccessToken),"
            + "  constraint ti_object_data_FK"
            + "   foreign key (tiUUIDFK)"
            + "   references lfms_db.object_data (odUUIDPK)"
            + "   on update restrict"
            + "   on delete cascade,"
            + "  constraint ti_login_info_FK"
            + "   foreign key (ti_loginInfo_UUIDFK)"
            + "   references lfms_db.login_info (liUUIDFK)"
            + "   on update restrict"
            + "   on delete cascade"
            + ")"});
        return tables;
    }

    protected static List<String> executeQuerySQL(
            Connection conn, String sql)
            throws SQLException {
        Statement stmt = null;
        List<String> values = new ArrayList();
        ResultSet results = null;
        try {
            stmt = conn.createStatement();

            //System.out.println("SQL: " + sql);
            results = stmt.executeQuery(sql);

            while (results.next()) {
                values.add((String) results.getObject(1));
            }
        } catch (SQLException ex) {
            throw new SQLException("SQL: " + sql, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                throw new SQLException("#close()", ex);
            }
        }

        return values;
    }

    protected static void executeSQL(
            Connection conn, String sql)
            throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            //System.out.println("SQL: " + sql);
            stmt.execute(sql);
        } catch (SQLException ex) {
            throw new SQLException("SQL: " + sql, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                throw new SQLException("#close()", ex);
            }
        }
    }

    protected static List<String> getDatabaseProperty(
            Connection conn, String propertyKey) throws SQLException {
        List<String> values = new ArrayList();
        Statement stmt = null;
        ResultSet results = null;
        try {
            stmt = conn.createStatement();
            results = stmt.executeQuery(
                    "values SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                    + "'" + propertyKey + "')");

            while (results.next()) {
                values.add((String) results.getObject(1));
            }

            //System.out.println(propertyKey + "=" + values.get(0));
        } catch (SQLException ex) {
            throw new SQLException("values "
                    + "SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY("
                    + "'" + propertyKey + "')", ex);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                throw new SQLException("#.close", ex);
            }
        }
        return values;
    }

    protected static void setDatabaseProperty(
            Connection conn, String propertyKey, String propertyValue)
            throws SQLException {
        Statement stmt = null;
        ResultSet results = null;
        try {
            stmt = conn.createStatement();

            //System.out.println("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
            //        + "'" + propertyKey + "', '" + propertyValue + "')");
            stmt.execute(
                    "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
                    + "'" + propertyKey + "', '" + propertyValue + "')");
        } catch (SQLException ex) {
            throw new SQLException("CALL SYSCS_UTIL."
                    + "SYSCS_SET_DATABASE_PROPERTY("
                    + "'" + propertyKey + "', '" + propertyValue + "')", ex);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                throw new SQLException("#close()", ex);
            }
        }
    }

    public static DatabaseManager getInstance() {
        return Singleton.INSTANCE;
    }

    private static interface Singleton {

        public DerbyDatabaseManager INSTANCE
                = new DerbyDatabaseManager();
    }

}
