package com.specificgroup.emotionstracker.authorization.repositories;

import com.specificgroup.emotionstracker.authorization.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
