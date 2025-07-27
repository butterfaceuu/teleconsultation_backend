package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.PractitionerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PractitionerScheduleRepository extends JpaRepository<PractitionerSchedule, Long> {
    List<PractitionerSchedule> findByPractitionerIdAndActiveTrue(Long practitionerId);
    List<PractitionerSchedule> findByPractitionerIdAndDayOfWeekAndActiveTrue(Long practitionerId, String dayOfWeek);
}
