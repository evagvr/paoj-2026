package com.pao.laboratory03.exercise2;

/**
 * TODO: Adaugă equals(Object o) și hashCode() — doi studenți sunt egali dacă au același id.
 * Model: vezi equalshashcode/Book.java
 */
import java.util.Objects;
public class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student s = (Student) o;
        return this.id == s.id;
    }
    // TODO: equals(Object o) — compară după id
    //   if (this == o) return true;
    //   if (o == null || getClass() != o.getClass()) return false;
    //   Student s = (Student) o;
    //   return this.id == s.id;
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
    // TODO: hashCode() — return Objects.hash(id);
    //   (import java.util.Objects)
}
