package com.specificgroup.emotionstracker.authorization.repositories;

import com.specificgroup.emotionstracker.authorization.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByLogin(String login);
}
