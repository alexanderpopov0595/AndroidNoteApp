package com.fancysoft.androidnoteapp.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents app exception
 */
@Getter
@RequiredArgsConstructor
public class AppException extends RuntimeException {

    private final String message;
}
