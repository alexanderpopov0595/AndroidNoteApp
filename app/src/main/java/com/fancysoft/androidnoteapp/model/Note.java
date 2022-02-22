package com.fancysoft.androidnoteapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Note {

    private long id;
    private final long lastUpdate;
    private final String content;
}
