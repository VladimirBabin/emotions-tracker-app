package com.specificgroup.emotionstracker.authentication.repositories;

import com.specificgroup.emotionstracker.authentication.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByLogin(String login);
}
