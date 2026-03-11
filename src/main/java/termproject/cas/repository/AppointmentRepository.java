package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AppointmentRepository {

    private final Map<Long, Appointment> apptMap = new HashMap<>();
    private long nextId = 1L;

    // Load a mock appointment data
    protected boolean loadMockData() {
        Patient patient;
        Provider provider;
        Slot availSlot;
        Appointment appt;

        // Map to patient model
        try (InputStream patientFile = getClass().getResourceAsStream("/mockupDB_Patient.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(patientFile))) {
            patient = new Patient();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                patient.setMrn(Long.parseLong(data[0].trim()));
                patient.setFirstName(data[1].trim());
                patient.setMiddleName(data[2].trim());
                patient.setLastName(data[3].trim());
                patient.setSex(data[4].trim());
                patient.setDob(LocalDate.parse(data[5].trim()));
            }
        }
        catch (Exception e) {
            System.out.println("Failed to load patient.csv: " + e.getMessage());
            return false;
        }

        // Map to provider model
        try (
                InputStream providerFile = getClass().getResourceAsStream("/mockupDB_Provider.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(providerFile))) {
            provider = new Provider();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                provider.setId(Long.parseLong(data[0].trim()));
                provider.setFirstName(data[1].trim());
                provider.setLastName(data[2].trim());
                provider.setUsername(data[3].trim());
                provider.setEmail(data[4].trim());
                provider.setAdmin(Boolean.parseBoolean(data[5].trim()));
                provider.setLicenseNumber(Long.parseLong(data[6].trim()));
                provider.setType(data[7].trim());
            }
        }
        catch (Exception e) {
            System.out.println("Failed to load provider.csv: " + e.getMessage());
            return false;
        }

        // Map Available Slot
        try (
                InputStream slotFile = getClass().getResourceAsStream("/mockupDB_AvailableSlot.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(slotFile))
                ) {
            availSlot = new Slot();
            availSlot.setProvider(provider);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lines = line.split(",");
                availSlot.setId(Long.parseLong(lines[0].trim()));
                availSlot.setStartTime(LocalDateTime.parse(lines[1].trim()));
                availSlot.setEndTime(LocalDateTime.parse(lines[2].trim()));
            }
        }
        catch (Exception e) {
            System.out.println("Failed to load AvailableSlot.csv: " + e.getMessage());
            return false;
        }

        // Assemble for appointment
        appt = new Appointment(2L, availSlot, patient);
        apptMap.put(appt.getId(), appt);
        return true;
    }


    public List<Appointment> findAll() {
        loadMockData();
        return new ArrayList<>(apptMap.values());
    }

    public Appointment save(Appointment appt) {
        if (appt.getId() == null) {
            appt.setId(nextId++);
        }
        apptMap.put(appt.getId(), appt);
        return appt;
    }
}
