package com.fancysoft.androidnoteapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Note {

    private long id;
    private final long lastUpdate;
    private final String content;
}
