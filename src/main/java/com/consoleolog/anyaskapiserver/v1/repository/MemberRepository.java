package com.consoleolog.anyaskapiserver.v1.repository;

import com.consoleolog.anyaskapiserver.v1.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUserEmail(String userEmail);

    Optional<Member> findByUserId(String userId);


}
