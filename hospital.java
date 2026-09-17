import java.util.ArrayList;
import java.util.Scanner;

class Patient {

    int id;
    String name;
    int age;
    String gender;
    String phone;
    String department;
    String reason;

    Patient(int id, String name, int age, String gender,
            String phone, String department, String reason) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.department = department;
        this.reason = reason;
    }

    void display() {
        System.out.println("\nPatient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone: " + phone);
        System.out.println("Department: " + department);
        System.out.println("Reason: " + reason);
    }
}

public class HospitalManagement {

    static ArrayList<Patient> patients = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 1;

    // ADD PATIENT
    static void addPatient() {

        System.out.println("\n--- PATIENT ADMISSION ---");

        System.out.print("Enter patient name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter department: ");
        String department = sc.nextLine();

        System.out.print("Enter reason for admission: ");
        String reason = sc.nextLine();

        Patient patient = new Patient(
                nextId,
                name,
                age,
                gender,
                phone,
                department,
                reason
        );

        patients.add(patient);

        System.out.println("\nPatient admitted successfully!");
        System.out.println("Patient ID: " + nextId);

        nextId++;
    }

    // VIEW PATIENTS
    static void viewPatients() {

        System.out.println("\n--- ADMITTED PATIENTS ---");

        if (patients.isEmpty()) {
            System.out.println("No patients admitted.");
            return;
        }

        for (Patient patient : patients) {
            patient.display();
        }
    }

    // SEARCH PATIENT
    static void searchPatient() {

        System.out.println("\n--- SEARCH PATIENT ---");

        System.out.print("Enter patient name: ");
        String search = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Patient patient : patients) {

            if (patient.name.toLowerCase().contains(search)) {
                patient.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Patient not found.");
        }
    }

    // EDIT PATIENT
    static void editPatient() {

        System.out.println("\n--- EDIT PATIENT ---");

        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Patient patient : patients) {

            if (patient.id == id) {

                System.out.print("Enter new name: ");
                patient.name = sc.nextLine();

                System.out.print("Enter new age: ");
                patient.age = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter new gender: ");
                patient.gender = sc.nextLine();

                System.out.print("Enter new phone: ");
                patient.phone = sc.nextLine();

                System.out.print("Enter new department: ");
                patient.department = sc.nextLine();

                System.out.print("Enter new reason: ");
                patient.reason = sc.nextLine();

                System.out.println("Patient details updated!");

                return;
            }
        }

        System.out.println("Patient ID not found.");
    }

    // DELETE PATIENT
    static void deletePatient() {

        System.out.println("\n--- DELETE PATIENT ---");

        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Patient patient : patients) {

            if (patient.id == id) {

                patients.remove(patient);

                System.out.println("Patient record deleted.");

                return;
            }
        }

        System.out.println("Patient ID not found.");
    }

    // MAIN MENU
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("   AMALA HOSPITAL MANAGEMENT");
            System.out.println("==============================");

            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Edit Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addPatient();
                    break;

                case 2:
                    viewPatients();
                    break;

                case 3:
                    searchPatient();
                    break;

                case 4:
                    editPatient();
                    break;

                case 5:
                    deletePatient();
                    break;

                case 6:
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
