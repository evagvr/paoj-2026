package com.pao.laboratory04.exercise.model;
import com.pao.laboratory04.exceptions.InvalidAgeException;
import com.pao.laboratory04.exercise.exception.InvalidGradeException;
import java.util.HashMap;
import java.util.Map;
public class Student {
    private final String name;
    private final int age;
    private Map<Subject, Double> grades;
    public Student(String name, int age){
        this.grades = new HashMap<>();
        if (age < 18 || age > 60){
            throw new InvalidAgeException("Vârsta " + age + " nu este validă (18-60)");
        }
        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }
    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public Map<Subject, Double> getGrades(){
        return grades;
    }
    public void addGrade(Subject subject, double grade){
        if (grade < 1 || grade > 10){
            throw new InvalidGradeException("Nota " + grade + " nu este validă (1-10)");
        }
        grades.put(subject, grade);
    }
    public double getAverage(){
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (double grd : grades.values()){
            sum += grd;
        }
        double avg = sum / grades.size();
        return avg;
    }
    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, avg=%.2f}", name, age, getAverage());
    }
}
