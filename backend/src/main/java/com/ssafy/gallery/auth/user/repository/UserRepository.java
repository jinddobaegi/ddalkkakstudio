package com.ssafy.gallery.auth.user.repository;

import com.ssafy.gallery.auth.oauth.dto.Domain;
import com.ssafy.gallery.auth.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDomain(Domain domain);
}