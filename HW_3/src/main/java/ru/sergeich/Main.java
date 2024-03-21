package ru.sergeich;

import javax.imageio.IIOException;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

       Student student = new Student("Ivan", 20, 3.5);

        try(FileOutputStream fileOutputStream = new FileOutputStream("student.bin")){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(student);
            System.out.println("The Student object has been serialized successfully.");
        }

        try(FileInputStream fileInputStream = new FileInputStream("student.bin")){
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            student = (Student)objectInputStream.readObject();
            System.out.println("The Student object has been deserialized successfully.");
        }

        System.out.println("Имя: " + student.getName());
        System.out.println("Возраст: " + student.getAge());
        System.out.println("Средний бал: (0.0, так как transient)" + student.getGPA());


    }
}