package com.fancysoft.androidnoteapp.repository;

import com.fancysoft.androidnoteapp.model.Note;

import java.util.List;

/**
 * Provides operations to work with database
 */
public interface NoteRepository {

    /**
     * Saves note in database
     * @param note - note to save
     * @return id of saved note
     */
    long save(Note note);

    /**
     * Retrieves note by its id from database
     * @param id - id of requested note
     * @return retrieved note
     */
    Note get(long id);

    /**
     * Retrieves all notes from database
     * @return list with all notes
     */
    List<Note> getAll();

    /**
     * Retrieves all notes with matched content
     * @param filter - specific content which note should contain
     * @return matched notes
     */
    List<Note> filter(String filter);

    /**
     * Updates note in database
     * @param note - note to update
     * @return id of updated note
     */
    long update(Note note);

    /**
     * Deletes note by id
     * @param id - id of requested note to delete
     */
    void delete(long id);
}
