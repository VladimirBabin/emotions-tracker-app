package com.github.vbabin.emocheck.state;

import com.github.vbabin.emocheck.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateLog {
    private Long id;
    private User user;
    private State state;
    private LocalDateTime dateTime;
}
