package com.fancysoft.androidnoteapp.exception.model;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents app exception
 */
@Generated
@Getter
@RequiredArgsConstructor
public class AppException extends RuntimeException {

    private final String message;
}
