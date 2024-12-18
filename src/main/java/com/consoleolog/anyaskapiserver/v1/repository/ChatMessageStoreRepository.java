package com.consoleolog.anyaskapiserver.v1.repository;

import com.consoleolog.anyaskapiserver.v1.model.entity.ChatMessageStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageStoreRepository extends JpaRepository<ChatMessageStore, Long> {

    Optional<ChatMessageStore> findByRoomId(Long roomId);

}
