package ceng.ceng351.cengvacdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class CENGVACDB implements ICENGVACDB{
    private static String user = "e2448926";
    private static String password = "CiTn*kdjW$62";
    private static String host = "144.122.71.121";
    private static String database = "db2448926";
    private static int port = 8080;

    private static Connection connection = null;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection =  DriverManager.getConnection(url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {
        int numberOfTablesCreated = 0;

        // User (userID:int, userName:varchar(30), age:int, address:varchar(150), password:varchar(30), status:varchar(15))
        String userTable = "CREATE TABLE User (" +
                               "userID INT NOT NULL, " +
                               "userName VARCHAR(30), " +
                               "age INT, " +
                               "address VARCHAR(150), " +
                               "password VARCHAR(30), " +
                               "status VARCHAR(15), " +
                               "PRIMARY KEY (userID));";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(userTable);
            numberOfTablesCreated++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Vaccine (code:int, vaccinename:varchar(30), type:varchar(30))
        String vaccineTable = "CREATE TABLE Vaccine (" +
                                  "code INT NOT NULL, " +
                                  "vaccinename VARCHAR(30), " +
                                  "type VARCHAR(30), " +
                                  "PRIMARY KEY (code));";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(vaccineTable);
            numberOfTablesCreated++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Vaccination (code:int, userID:int, dose:int, vacdate:date) References Vaccine (code), User (userID)
        String vaccinationTable = "CREATE TABLE Vaccination (" +
                                      "code INT NOT NULL, " +
                                      "userID INT NOT NULL, " +
                                      "dose INT NOT NULL, " +
                                      "vacdate DATE, " +
                                      "FOREIGN KEY(code) REFERENCES Vaccine(code), " +
                                      "FOREIGN KEY(userID) REFERENCES User(userID), " +
                                      "PRIMARY KEY(code, userID, dose));";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(vaccinationTable);
            numberOfTablesCreated++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // AllergicSideEffect (effectcode:int, effectname:varchar(50))
        String allergicSideEffectTable = "CREATE TABLE AllergicSideEffect (" +
                                             "effectcode INT NOT NULL, " +
                                             "effectname VARCHAR(50), " +
                                             "PRIMARY KEY(effectcode));";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(allergicSideEffectTable);
            numberOfTablesCreated++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Seen (effectcode:int, code:int, userID:int, date:date, degree:varchar(30)) References AllergicSideEffect (effectcode), Vaccination (code), User (userID)
        String seenTable = "CREATE TABLE Seen (" +
                               "effectcode INT, " +
                               "code INT, " +
                               "userID INT, " +
                               "date DATE, " +
                               "degree VARCHAR(30), " +
                               "FOREIGN KEY(effectcode) REFERENCES AllergicSideEffect(effectcode), " +
                               "FOREIGN KEY(code) REFERENCES Vaccine(code) ON DELETE CASCADE, " +
                               "FOREIGN KEY(userID) REFERENCES User(userID), " +
                               "PRIMARY KEY(effectcode, code, userID));";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(seenTable);
            numberOfTablesCreated++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfTablesCreated;
    }

    @Override
    public int dropTables() {
        int numberOfTablesDropped = 0;

        String seenTable = "DROP TABLE IF EXISTS Seen;";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(seenTable);
            numberOfTablesDropped++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String vaccinationTable = "DROP TABLE IF EXISTS Vaccination;";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(vaccinationTable);
            numberOfTablesDropped++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String allergicSideEffectTable = "DROP TABLE IF EXISTS AllergicSideEffect;";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(allergicSideEffectTable);
            numberOfTablesDropped++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String vaccineTable = "DROP TABLE IF EXISTS Vaccine;";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(vaccineTable);
            numberOfTablesDropped++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String userTable = "DROP TABLE IF EXISTS User;";

        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(userTable);
            numberOfTablesDropped++;

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfTablesDropped;
    }

    @Override
    public int insertUser(User[] users) {
        int numberOfInsertedUsers = 0;

        for (int i = 0; i < users.length; i++) {
            String query = "INSERT INTO User VALUES(" +
                                users[i].getUserID() + ",'" +
                                users[i].getUserName() + "', " +
                                users[i].getAge() + ", '" +
                                users[i].getAddress() + "', '" +
                                users[i].getPassword() + "', '" +
                                users[i].getStatus() + "');";

            try {
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(query);
                numberOfInsertedUsers++;

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfInsertedUsers;
    }

    @Override
    public int insertAllergicSideEffect(AllergicSideEffect[] sideEffects) {
        int numberOfInsertedSideEffects = 0;

        for (int i = 0; i < sideEffects.length; i++) {
            String query = "INSERT INTO AllergicSideEffect VALUES(" +
                                sideEffects[i].getEffectCode() + ", '" +
                                sideEffects[i].getEffectName() + "');";

            try {
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(query);
                numberOfInsertedSideEffects++;

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfInsertedSideEffects;
    }

    @Override
    public int insertVaccine(Vaccine[] vaccines) {
        int numberOfInsertedVaccines = 0;

        for (int i = 0; i < vaccines.length; i++) {
            String query = "INSERT INTO Vaccine VALUES(" +
                                vaccines[i].getCode() + ", '" +
                                vaccines[i].getVaccineName() + "', '" +
                                vaccines[i].getType() + "');";

            try {
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(query);
                numberOfInsertedVaccines++;

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfInsertedVaccines;
    }

    @Override
    public int insertVaccination(Vaccination[] vaccinations) {
        int numberOfInsertedVaccinations = 0;

        for (int i = 0; i < vaccinations.length; i++) {
            String query = "INSERT INTO Vaccination VALUES(" +
                                vaccinations[i].getCode() + ", " +
                                vaccinations[i].getUserID() + ", " +
                                vaccinations[i].getDose() + ", '" +
                                vaccinations[i].getVacdate() + "');";

            try {
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(query);
                numberOfInsertedVaccinations++;

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfInsertedVaccinations;
    }

    @Override
    public int insertSeen(Seen[] seens) {
        int numberOfInsertedSeens = 0;

        for (int i = 0; i < seens.length; i++) {
            String query = "INSERT INTO Seen VALUES(" +
                                seens[i].getEffectcode() + ", " +
                                seens[i].getCode() + ", " +
                                seens[i].getUserID() + ", '" +
                                seens[i].getDate() + "', '" +
                                seens[i].getDegree() + "');";

            try {
                Statement statement = this.connection.createStatement();

                statement.executeUpdate(query);
                numberOfInsertedSeens++;

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfInsertedSeens;
    }

    @Override
    public Vaccine[] getVaccinesNotAppliedAnyUser() {
        Vector<Vaccine> vaccines = new Vector<>();
        Vaccine[] result;
        ResultSet queryResult;

        String query = "SELECT * " +
                       "FROM Vaccine V " +
                       "WHERE V.code NOT IN (SELECT C.code " +
                                            "FROM Vaccination C) " +
                       "ORDER BY V.code;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int code = queryResult.getInt("code");
                String vaccinename = queryResult.getString("vaccinename");
                String type = queryResult.getString("type");
                vaccines.addElement(new Vaccine(code, vaccinename, type));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = vaccines.toArray(new Vaccine[vaccines.size()]);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersforTwoDosesByDate(String vacdate) {
        Vector<QueryResult.UserIDuserNameAddressResult> vaccinatedTwoTimes = new Vector<>();
        QueryResult.UserIDuserNameAddressResult[] result;
        ResultSet queryResult;

        String query = "SELECT U.userID, U.userName, U.address " +
                       "FROM User U " +
                       "WHERE 2 = (SELECT COUNT(*) " +
                                  "FROM Vaccination V " +
                                  "WHERE U.userID = V.userID AND V.vacdate > '" + vacdate + "') " +
                       "ORDER BY U.userID;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int userID = queryResult.getInt("userID");
                String userName = queryResult.getString("userName");
                String address = queryResult.getString("address");
                vaccinatedTwoTimes.addElement(new QueryResult.UserIDuserNameAddressResult(userID, userName, address));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = vaccinatedTwoTimes.toArray(new QueryResult.UserIDuserNameAddressResult[vaccinatedTwoTimes.size()]);

        return result;
    }

    @Override
    public Vaccine[] getTwoRecentVaccinesDoNotContainVac() {
        Vector<Vaccine> vaccines = new Vector<>();
        Vaccine[] result;
        ResultSet queryResult;

        String query = "SELECT T.code, T.vaccinename, T.type " +
                       "FROM (SELECT V.code, V.vaccinename, V.type, MAX(C.vacdate) AS recent " +
                             "FROM Vaccination C, Vaccine V " +
                             "WHERE V.code = C.code AND V.vaccinename NOT LIKE '%vac%' " +
                             "GROUP BY C.code) AS T " +
                       "ORDER BY T.recent DESC " +
                       "LIMIT 2;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int code = queryResult.getInt("code");
                String vaccinename = queryResult.getString("vaccinename");
                String type = queryResult.getString("type");
                vaccines.addElement(new Vaccine(code, vaccinename, type));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = vaccines.toArray(new Vaccine[vaccines.size()]);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersAtHasLeastTwoDoseAtMostOneSideEffect() {
        Vector<QueryResult.UserIDuserNameAddressResult> twoDoseOneSideEffect = new Vector<>();
        QueryResult.UserIDuserNameAddressResult[] result;
        ResultSet queryResult;

        String query = "SELECT U.userID, U.userName, U.address " +
                       "FROM User U " +
                       "WHERE 2 <= (SELECT COUNT(*) " +
                                   "FROM Vaccination V " +
                                   "WHERE U.userID = V.userID) " +
                              "AND 1 >= (SELECT COUNT(*) " +
                                        "FROM Seen S " +
                                        "WHERE U.userID = S.userID)" +
                       "ORDER BY U.userID;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int userID = queryResult.getInt("userID");
                String userName = queryResult.getString("userName");
                String address = queryResult.getString("address");
                twoDoseOneSideEffect.addElement(new QueryResult.UserIDuserNameAddressResult(userID, userName, address));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = twoDoseOneSideEffect.toArray(new QueryResult.UserIDuserNameAddressResult[twoDoseOneSideEffect.size()]);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersWithAllVaccinesCanCauseGivenSideEffect(String effectname) {
        Vector<QueryResult.UserIDuserNameAddressResult> canCauseSideEffect = new Vector<>();
        QueryResult.UserIDuserNameAddressResult[] result;
        ResultSet queryResult;

        String query = "SELECT U.userID, U.userName, U.address " +
                       "FROM User U " +
                       "WHERE NOT EXISTS (SELECT S.code " +
                                         "FROM Seen S, AllergicSideEffect A " +
                                         "WHERE S.effectcode = A.effectcode AND A.effectname = '" + effectname + "' " +
                                               "AND S.code NOT IN (SELECT V.code " +
                                                                  "FROM Vaccination V " +
                                                                  "WHERE V.userID = U.userID)) " +
                       "ORDER BY U.userID;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int userID = queryResult.getInt("userID");
                String userName = queryResult.getString("userName");
                String address = queryResult.getString("address");
                canCauseSideEffect.addElement(new QueryResult.UserIDuserNameAddressResult(userID, userName, address));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = canCauseSideEffect.toArray(new QueryResult.UserIDuserNameAddressResult[canCauseSideEffect.size()]);

        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersWithAtLeastTwoDifferentVaccineTypeByGivenInterval(String startdate, String enddate) {
        Vector<QueryResult.UserIDuserNameAddressResult> canCauseSideEffect = new Vector<>();
        QueryResult.UserIDuserNameAddressResult[] result;
        ResultSet queryResult;

        String query = "SELECT DISTINCT U.userID, U.userName, U.address " +
                       "FROM User U, Vaccination V1, Vaccination V2, Vaccine X1, Vaccine X2 " +
                       "WHERE U.userID = V1.userID AND U.userID = V2.userID " +
                             "AND X1.code = V1.code AND X2.code = V2.code AND X1.type <> X2.type " +
                             "AND V1.vacdate >= '" + startdate + "' AND V1.vacdate <= '" + enddate + "' " +
                             "AND V2.vacdate >= '" + startdate + "' AND V2.vacdate <= '" + enddate + "' " +
                       "ORDER BY U.userID;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int userID = queryResult.getInt("userID");
                String userName = queryResult.getString("userName");
                String address = queryResult.getString("address");
                canCauseSideEffect.addElement(new QueryResult.UserIDuserNameAddressResult(userID, userName, address));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = canCauseSideEffect.toArray(new QueryResult.UserIDuserNameAddressResult[canCauseSideEffect.size()]);

        return result;
    }

    @Override
    public AllergicSideEffect[] getSideEffectsOfUserWhoHaveTwoDosesInLessThanTwentyDays() {
        Vector<AllergicSideEffect> allergicSideEffects = new Vector<>();
        AllergicSideEffect[] result;
        ResultSet queryResult;

        String query = "SELECT A.effectcode, A.effectname " +
                       "FROM User U, AllergicSideEffect A, Seen S " +
                       "WHERE U.userID IN (SELECT V1.userID " +
                                          "FROM Vaccination V1, Vaccination V2 " +
                                          "WHERE U.userID = V1.userID AND U.userID = V2.userID " +
                                                "AND V1.vacdate <> V2.vacdate AND DATEDIFF(V1.vacdate, V2.vacdate) < 20 AND DATEDIFF(V1.vacdate, V2.vacdate) > -20) " +
                              "AND S.userID = U.userID AND S.effectcode = A.effectcode " +
                       "ORDER BY A.effectcode;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            while(queryResult.next()) {
                int effectcode = queryResult.getInt("effectcode");
                String effectname = queryResult.getString("effectname");
                allergicSideEffects.addElement(new AllergicSideEffect(effectcode, effectname));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = allergicSideEffects.toArray(new AllergicSideEffect[allergicSideEffects.size()]);

        return result;
    }

    @Override
    public double averageNumberofDosesofVaccinatedUserOverSixtyFiveYearsOld() {
        double average_doses = 0;
        ResultSet queryResult;

        String query = "SELECT AVG(T.doses) " +
                       "FROM (SELECT COUNT(V.dose) AS doses " +
                             "FROM Vaccination V, User U " +
                             "WHERE V.userID = U.userID AND U.age > 65 " +
                             "GROUP BY V.userID) AS T;";

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(query);

            if (queryResult.next()) {
                average_doses = queryResult.getDouble(1);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return average_doses;
    }

    @Override
    public int updateStatusToEligible(String givendate) {
        int rowsUpdated = 0;

        String query = "UPDATE User " +
                       "SET status = 'eligible' " +
                       "WHERE status <> 'eligible' AND 120 <= DATEDIFF('" + givendate + "', (SELECT MAX(V.vacdate) FROM Vaccination V WHERE User.userID = V.userID));";

        try {
            Statement statement = this.connection.createStatement();

            rowsUpdated = statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsUpdated;
    }

    @Override
    public Vaccine deleteVaccine(String vaccineName) {

        String query = "DELETE FROM Vaccine WHERE vaccinename = '" + vaccineName + "';";
        String deletedVaccineQuery = "SELECT * FROM Vaccine WHERE vaccinename = '" + vaccineName + "';";
        ResultSet queryResult;
        Vaccine deletedVaccine = null;

        try {
            Statement statement = this.connection.createStatement();

            queryResult = statement.executeQuery(deletedVaccineQuery);
            queryResult.next();
            int code = queryResult.getInt("code");
            String vaccinename = queryResult.getString("vaccinename");
            String type = queryResult.getString("type");
            deletedVaccine = new Vaccine(code, vaccinename, type);

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deletedVaccine;
    }
}
