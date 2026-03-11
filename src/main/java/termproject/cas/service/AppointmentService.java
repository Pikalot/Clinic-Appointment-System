package termproject.cas.service;

import org.springframework.stereotype.Service;
import termproject.cas.model.Appointment;
import termproject.cas.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;

    public AppointmentService(AppointmentRepository repo) {
        this.repo = repo;
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment addAppointment(Appointment appt) {
        return repo.save(appt);
    }
}
