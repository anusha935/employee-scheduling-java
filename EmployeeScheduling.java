import java.util.*;

public class EmployeeScheduler {
    static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    static final String[] SHIFTS = {"Morning", "Afternoon", "Evening"};
    static final int MAX_WORK_DAYS = 5;
    static final int MIN_EMPLOYEES_PER_SHIFT = 2;

    static class Employee {
        String name;
        Map<String, List<String>> preferences = new HashMap<>();
        Map<String, String> assignedShifts = new HashMap<>();
        int assignedDays = 0;

        Employee(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        // Predefined 6 employees and their ranked preferences
        employees.add(createEmployee("Alice", new String[][] {
                {"Morning"}, {"Afternoon"}, {"Morning"}, {"Morning"}, {"Evening"}, {"Afternoon"}, {"Afternoon"}
        }));

        employees.add(createEmployee("John", new String[][] {
                {"Morning"}, {"Morning"}, {"Evening"}, {"Afternoon"}, {"Afternoon"}, {"Afternoon"}, {"Morning"}
        }));

        employees.add(createEmployee("Helen", new String[][] {
                {"Afternoon"}, {"Morning"}, {"Afternoon"}, {"Evening"}, {"Morning"}, {"Evening"}, {"Morning"}
        }));

        employees.add(createEmployee("Paul", new String[][] {
                {"Afternoon"}, {"Evening"}, {"Afternoon"}, {"Morning"}, {"Morning"}, {"Evening"}, {"Afternoon"}
        }));

        employees.add(createEmployee("Steve", new String[][] {
                {"Evening"}, {"Evening"}, {"Morning"}, {"Evening"}, {"Afternoon"}, {"Morning"}, {"Evening"}
        }));

        employees.add(createEmployee("Clara", new String[][] {
                {"Evening"}, {"Afternoon"}, {"Evening"}, {"Afternoon"}, {"Evening"}, {"Morning"}, {"Evening"}
        }));

        // Initialize schedule
        Map<String, Map<String, List<String>>> schedule = new HashMap<>();
        for (String day : DAYS) {
            Map<String, List<String>> shifts = new HashMap<>();
            for (String shift : SHIFTS) {
                shifts.put(shift, new ArrayList<>());
            }
            schedule.put(day, shifts);
        }

        Random random = new Random();

        // Assign shifts
        for (String day : DAYS) {
            // Assign based on preferences first
            for (Employee e : employees) {
                if (e.assignedDays < MAX_WORK_DAYS && !e.assignedShifts.containsKey(day)) {
                    for (String preferredShift : e.preferences.get(day)) {
                        List<String> assigned = schedule.get(day).get(preferredShift);
                        if (assigned.size() < MIN_EMPLOYEES_PER_SHIFT) {
                            assigned.add(e.name);
                            e.assignedShifts.put(day, preferredShift);
                            e.assignedDays++;
                            break;
                        }
                    }
                }
            }

            // Fill remaining shifts if under-staffed
            for (String shift : SHIFTS) {
                List<String> assigned = schedule.get(day).get(shift);
                while (assigned.size() < MIN_EMPLOYEES_PER_SHIFT) {
                    List<Employee> available = new ArrayList<>();
                    for (Employee e : employees) {
                        if (!e.assignedShifts.containsKey(day) && e.assignedDays < MAX_WORK_DAYS) {
                            available.add(e);
                        }
                    }
                    if (available.isEmpty()) break;
                    Employee selected = available.get(random.nextInt(available.size()));
                    assigned.add(selected.name);
                    selected.assignedShifts.put(day, shift);
                    selected.assignedDays++;
                }
            }
        }

        // Print schedule
        System.out.println("\nFinal Weekly Schedule:\n");
        for (String day : DAYS) {
            System.out.println("=== " + day + " ===");
            for (String shift : SHIFTS) {
                List<String> assigned = schedule.get(day).get(shift);
                if (!assigned.isEmpty()) {
                    System.out.println("  " + shift + "  : " + String.join(", ", assigned));
                } else {
                    System.out.println("  " + shift + "  : (none)");
                }
            }
            System.out.println();
        }

        // Print per-employee assigned days
        System.out.println("Per-employee assigned days:");
        for (Employee e : employees) {
            System.out.print("  " + e.name + ": " + e.assignedDays + " days -> ");
            List<String> assignments = new ArrayList<>();
            for (String day : DAYS) {
                if (e.assignedShifts.containsKey(day)) {
                    assignments.add(day + ":" + e.assignedShifts.get(day).toLowerCase());
                }
            }
            System.out.println(String.join(", ", assignments));
        }
    }

    private static Employee createEmployee(String name, String[][] dayPreferences) {
        Employee e = new Employee(name);
        for (int i = 0; i < DAYS.length; i++) {
            e.preferences.put(DAYS[i], Arrays.asList(dayPreferences[i]));
        }
        return e;
    }
}
