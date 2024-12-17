package com.consoleolog.anyaskapiserver.v1.repository;

import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomUserId(String roomUserId);

}
