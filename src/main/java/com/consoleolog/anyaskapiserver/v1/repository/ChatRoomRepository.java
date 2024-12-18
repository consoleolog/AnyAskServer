package com.consoleolog.anyaskapiserver.v1.repository;

import com.consoleolog.anyaskapiserver.v1.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomUserId(String roomUserId);

    Optional<ChatRoom> findByRoomNameAndRoomUserId(String roomName, String roomUserId);

    List<ChatRoom> findAllByRoomUserId(String roomUserId);

}
