package ru.sergeich.home_work;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sergeich.models.Course;

public class Program {
    /**
     * Создайте базу данных (например, SchoolDB).
     * В этой базе данных создайте таблицу Courses с полями id (ключ), title, и duration.
     * Настройте Hibernate для работы с вашей базой данных.
     * Создайте Java-класс Course, соответствующий таблице Courses, с необходимыми аннотациями Hibernate.
     * Используя Hibernate, напишите код для вставки, чтения, обновления и удаления данных в таблице Courses.
     * Убедитесь, что каждая операция выполняется в отдельной транзакции.
     */
    public static void main(String[] args) {

        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();) {
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            Course course = Course.create();
            //Создание записи
            createDate(session, course);

            //Чтение записи
            readDate(session, course);

            //Обновление записи
            updateDate(session, course);
            readDate(session, course);

            //Удаление записи
            deleteDate(session, course);

            commitSession(session);
        }
    }

    public static void createDate(Session session, Course course) {
        session.save(course);
        System.out.println("Course was saved");
    }

    public static void readDate(Session session, Course course) {
        Course retrivedCourse = session.get(Course.class, course.getId());
        System.out.println("Чтение из БД выполнено");
        System.out.println("Course: " + retrivedCourse);
    }

    public static void updateDate(Session session, Course course) {
        Course retrivedCourse = session.get(Course.class, course.getId());
        retrivedCourse.updateTitle();
        retrivedCourse.updateDuration();
        session.update(retrivedCourse);
        System.out.println("Обновление выполнено");
    }

    public static void deleteDate(Session session, Course retrivedCourse) {
        session.delete(retrivedCourse);
        System.out.println("Удаление выполнено");
    }

    public static void commitSession(Session session) {
        session.getTransaction().commit();
    }
}
