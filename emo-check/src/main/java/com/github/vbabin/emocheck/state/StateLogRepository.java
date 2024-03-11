package com.github.vbabin.emocheck.state;

import com.github.vbabin.emocheck.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StateLogRepository extends CrudRepository<StateLog, Long> {
    List<StateLog> findAllByUserAndDateTimeAfter(User user, LocalDateTime dateTime);
}
