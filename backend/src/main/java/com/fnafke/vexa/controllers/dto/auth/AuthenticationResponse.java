package com.fnafke.vexa.controllers.dto.auth;

import java.util.UUID;

public record AuthenticationResponse(
                String token,
                UUID id,
                String username,
                String message) {

}
