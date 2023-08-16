package io.deeplay.communication.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponseDTO {
    private Exception exception;
    private String message;
}
