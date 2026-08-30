package org.ironlog.app.service.definition;

import org.ironlog.app.dto.LoginRequestDTO;
import org.ironlog.app.dto.RegisterRequestDTO;

public interface AuthService {

    void register(RegisterRequestDTO dto);
    String login(LoginRequestDTO dto);
}
