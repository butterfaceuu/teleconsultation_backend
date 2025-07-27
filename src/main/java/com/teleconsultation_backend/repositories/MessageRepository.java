package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderPatient.id = :patientId AND m.receiverPractitioner.id = :practitionerId) OR " +
           "(m.senderPractitioner.id = :practitionerId AND m.receiverPatient.id = :patientId) " +
           "ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenPatientAndPractitioner(@Param("patientId") Long patientId, @Param("practitionerId") Long practitionerId);
    
    @Query("SELECT DISTINCT p FROM Message m JOIN m.receiverPractitioner p WHERE m.senderPatient.id = :patientId " +
           "UNION " +
           "SELECT DISTINCT p FROM Message m JOIN m.senderPractitioner p WHERE m.receiverPatient.id = :patientId")
    List<Object> findPatientConversations(@Param("patientId") Long patientId);
    
    @Query("SELECT DISTINCT p FROM Message m JOIN m.receiverPatient p WHERE m.senderPractitioner.id = :practitionerId " +
           "UNION " +
           "SELECT DISTINCT p FROM Message m JOIN m.senderPatient p WHERE m.receiverPractitioner.id = :practitionerId")
    List<Object> findPractitionerConversations(@Param("practitionerId") Long practitionerId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverPatient.id = :patientId AND m.isRead = false")
    int countUnreadMessagesForPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverPractitioner.id = :practitionerId AND m.isRead = false")
    int countUnreadMessagesForPractitioner(@Param("practitionerId") Long practitionerId);
}
