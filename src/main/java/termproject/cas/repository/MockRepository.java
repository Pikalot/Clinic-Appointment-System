package termproject.cas.repository;

import org.springframework.stereotype.Component;
import termproject.cas.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MockRepository {
    private final Map<Long, Patient> patientMap = new HashMap<>();
    private final Map<Long, Slot> slotMap = new HashMap<>();
    private final Map<Long, Appointment> apptMap = new HashMap<>();
    private boolean loaded = false;

    public void ensureLoaded() {
        if (!loaded) {
            loadAll();
            loaded = true;
        }
    }

    public Map<Long, Patient> getPatientMap() {
        return patientMap;
    }

    public Map<Long, Slot> getSlotMap() {
        return slotMap;
    }

    public Map<Long, Appointment> getApptMap() {
        return apptMap;
    }

    public boolean isLoaded() {
        return loaded;
    }

    private Patient loadMockPatient() throws IOException {
        try (
                InputStream patientFile = getClass().getResourceAsStream("/mockupDB_Patient.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(patientFile))
        ) {
            String line = reader.readLine();
            if (line == null) {
                throw new RuntimeException("mockupDB_Patient.csv is empty");
            }

            String[] data = line.split(",");
            Patient patient = new Patient();
            patient.setMrn(Long.parseLong(data[0].trim()));
            patient.setFirstName(data[1].trim());
            patient.setMiddleName(data[2].trim());
            patient.setLastName(data[3].trim());
            patient.setSex(data[4].trim());
            patient.setDob(LocalDate.parse(data[5].trim()));
            patient.setEmails(new ArrayList<>(List.of(data[6].trim())));
            return patient;
        }
    }

    private Provider loadMockProvider() throws IOException {
        try (
                InputStream providerFile = getClass().getResourceAsStream("/mockupDB_Provider.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(providerFile))
        ) {
            String line = reader.readLine();
            if (line == null) {
                throw new RuntimeException("mockupDB_Provider.csv is empty");
            }

            String[] data = line.split(",");
            Provider provider = new Provider();
            provider.setId(Long.parseLong(data[0].trim()));
            provider.setFirstName(data[1].trim());
            provider.setLastName(data[2].trim());
            provider.setUsername(data[3].trim());
            provider.setEmail(data[4].trim());
//            provider.setAdmin(Boolean.parseBoolean(data[5].trim()));
            provider.setLicenseNumber(Long.parseLong(data[6].trim()));
            provider.setType(data[7].trim());

            Clinic clinic = new Clinic();
            clinic.setName("SJSU Clinic");
            clinic.setStreet("1 Washington Sq");
            clinic.setCity("San Jose");
            clinic.setState("CA");
            clinic.setZip("95192");

//            provider.setClinic(clinic);
            return provider;
        }
    }

    private List<Slot> loadMockSlots(Provider provider) throws IOException {
        List<Slot> slots = new ArrayList<>();

        try (
                InputStream slotFile = getClass().getResourceAsStream("/mockupDB_AvailableSlot.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(slotFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                Slot slot = new Slot();
                slot.setId(Long.parseLong(data[0].trim()));
                slot.setStartTime(LocalDateTime.parse(data[1].trim()));
                slot.setEndTime(LocalDateTime.parse(data[2].trim()));
                slot.setVersion(Integer.parseInt(data[3].trim()));
                slot.setProvider(provider);
                slot.setStatus("Available");

                slots.add(slot);
            }
        }

        return slots;
    }

    private Appointment buildSeedAppointment(Patient patient, Slot slot) {
        Appointment appt = new Appointment();
        appt.setId(1L);
        appt.setPatient(patient);
        appt.setAvailableSlot(slot);
        appt.setStatus("SCHEDULED");
        appt.setVersion(slot.getVersion());
        return appt;
    }

    private void loadAll() {
        try {
            Patient patient = loadMockPatient();
            Provider provider = loadMockProvider();
            List<Slot> slots = loadMockSlots(provider);

            if (slots.isEmpty()) {
                throw new RuntimeException("No mock slots found");
            }

            patientMap.put(patient.getMrn(), patient);

            for (Slot slot : slots) {
                slotMap.put(slot.getId(), slot);
            }

            Appointment seedAppt = buildSeedAppointment(patient, slots.get(0));
            apptMap.put(seedAppt.getId(), seedAppt);

            // mark the first slot as already taken by the seeded appointment
            slots.get(0).setStatus("Taken");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load mock data", e);
        }
    }
}
