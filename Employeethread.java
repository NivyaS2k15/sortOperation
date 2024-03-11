package java_test_1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Employee implements Comparable<Employee> {

    private String name;
    private double salary;
    private int age;

    // Getter and setter methods for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for 'salary'
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Getter and setter methods for 'age'
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Implementing Comparable interface for sorting based on salary
    @Override
    public int compareTo(Employee otherEmployee) {
        return Double.compare(this.salary, otherEmployee.getSalary());
    }
}

class SortingTask implements Runnable {
    private final Employee[] employees;
    private final Comparator<Employee> comparator;

    public SortingTask(Employee[] employees, Comparator<Employee> comparator) {
        this.employees = employees;
        this.comparator = comparator;
    }

    @Override
    public void run() {
        Arrays.sort(employees, comparator);
    }
}

public class Employeethread {
    public static void main(String[] args) {

        Employee[] employees = new Employee[5]; // Adjust the size as needed

        // Adding employees with random values to the array
        for (int i = 0; i < employees.length; i++) {
            Employee employee = new Employee();
            employee.setName(generateRandomName());
            employee.setSalary(generateRandomSalary());
            employee.setAge(generateRandomAge());
            employees[i] = employee;
        }

        // Create an ExecutorService with a fixed number of threads (2 in this case)
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Submit sorting tasks for salary and age
        executorService.submit(new SortingTask(employees.clone(), Comparator.comparingDouble(Employee::getSalary)));
        executorService.submit(new SortingTask(employees.clone(), Comparator.comparingInt(Employee::getAge)));

        // Shutdown the executor to prevent new tasks from being accepted
        executorService.shutdown();

        try {
            // Wait for all tasks to complete or until timeout (adjust as needed)
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Displaying the information of the employees after sorting by salary
        System.out.println("Sorted by Salary:");
        displayEmployeeInfo(employees);

        // Now, sort based on age
        Arrays.sort(employees, Comparator.comparingInt(Employee::getAge));

        // Displaying the information of the employees after sorting by age
        System.out.println("Sorted by Age:");
        displayEmployeeInfo(employees);
    }

    // Helper method to display employee information
    private static void displayEmployeeInfo(Employee[] employees) {
        for (Employee employee : employees) {
            System.out.println("Name: " + employee.getName());
            System.out.printf("Salary: %.0f%n", employee.getSalary());
            System.out.println("Age: " + employee.getAge());
            System.out.println("-------------");
        }
    }

    // Helper method to generate a random name
    private static String generateRandomName() {
        String[] names = {"Ammu", "karthik", "Charlie", "David", "Eva", "Frank", "Anu"};
        return names[new Random().nextInt(names.length)];
    }

    // Helper method to generate a random salary
    private static double generateRandomSalary() {
        double minSalary = 10000.0;
        double maxSalary = 99999.0;
        return minSalary + (maxSalary - minSalary) * new Random().nextDouble();
    }

    // Helper method to generate a random age
    private static int generateRandomAge() {
        int minAge = 22;
        int maxAge = 60;
        return new Random().nextInt(maxAge - minAge + 1) + minAge;
    }
}
