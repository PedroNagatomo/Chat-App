package com.chat.repository;

import com.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderByTimestampAsc(Long roomId);

    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId ORDER BY m.timestamp DESC")
    List<Message> findLastMessages(@Param("roomId") Long roomId,
                                   org.springframework.data.domain.Pageable pageable);
}