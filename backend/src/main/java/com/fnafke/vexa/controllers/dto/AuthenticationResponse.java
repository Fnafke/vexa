package com.fnafke.vexa.controllers.dto;

import java.util.UUID;

public record AuthenticationResponse(
        String token,
        UUID id,
        String username,
        String message) {

}
