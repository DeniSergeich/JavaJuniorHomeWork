package ru.sergeich.task1;

import ru.sergeich.models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Program {

    private final static Random random = new Random();

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "admin";

        try(Connection connection = DriverManager.getConnection(url, user, password);) {

            createDB(connection);
            System.out.println("DB was created");

            useDB(connection);
            System.out.println("DB was used");

            createTable(connection);
            System.out.println("Table was created");

            int count = random.nextInt(5, 11);

            for (int i = 0; i < count; i++) {
                insertData(connection, Course.create());
            }
            System.out.println("Insertion was done");

            Collection<Course> courses = readData(connection);
            for (var course : courses) {
                System.out.println(course);
            }
            System.out.println("Reading was done");

            for (var course : courses) {
                course.updateTitle();
                course.updateDuration();
                updateData(connection, course);
            }
            System.out.println("Update was done");

            for (var course : courses) {
                System.out.println(course);
            }
            System.out.println("Reading was done");

            for (var course : courses) {
                deleteData(connection, course.getId());
            }
            System.out.println("Deletion was done");

            System.out.println("Connection was closed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Вспомогательные методы


    public static void createDB(Connection connection) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS schoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL);) {
            statement.execute();
        }
    }


    public static void useDB(Connection connection) throws SQLException {
        String useDatabaseSQL = "USE schoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(useDatabaseSQL);) {
            statement.execute();
        }
    }


    public static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS courses (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration INT);";
        try(PreparedStatement statement = connection.prepareStatement(createTableSQL);) {
            statement.execute();
        }
    }

    /**
     * Добавление данных в таблицу
     * @param connection Соединение с БД
     * @param course     Курс
     * @throws SQLException Если произошла ошибка
     */
    private static void insertData(Connection connection, Course course) throws SQLException {
        String insertDataSQL = "INSERT INTO courses (title, duration) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(insertDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.executeUpdate();
        }
    }

    private static Collection<Course> readData(Connection connection) throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();
        String readDataSQL = "SELECT * FROM courses;";
        try(PreparedStatement statement = connection.prepareStatement(readDataSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int duration = resultSet.getInt("duration");
                courses.add(new Course(id, title, duration));
            }
            return courses;
        }
    }

    private static void updateData(Connection connection, Course course) throws SQLException {
        String updateDataSQL = "UPDATE courses SET title = ?, duration = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(updateDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        }
    }

    private static void deleteData(Connection connection, int id) throws SQLException {
        String deleteDataSQL = "DELETE FROM courses WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(deleteDataSQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }


    //endregion
}
