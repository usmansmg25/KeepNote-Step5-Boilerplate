package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService{

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	
	private Log log = LogFactory.getLog(getClass());
	
	NoteRepository noteRepository;
	
	public NoteServiceImpl(NoteRepository noteRepository)
	{
		this.noteRepository  = noteRepository;
	}
	
	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		
		List<Note> noteList = new ArrayList<>();
		noteList.add(note);
		NoteUser noteuser = new NoteUser();
		noteuser.setUserId(note.getNoteCreatedBy());
		noteuser.setNotes(noteList);
		log.info("note.getNoteCreatedBy():: "+note.getNoteCreatedBy());
		log.info("noteList:: "+noteList);
		NoteUser noteUser =  noteRepository.insert(noteuser);
		log.info("noteUser:: "+noteUser);
		if(noteUser!=null)
		{
			return true;
		}
			return false;
	}
	
	/* This method should be used to delete an existing note. */

	
	public boolean deleteNote(String userId, int noteId) {
		
		Optional<NoteUser> noteUser = noteRepository.findById(userId);
		if(noteUser.get()!=null)
		{
			noteRepository.delete(noteUser.get());
			return true;
		}
		return false;
	}
	
	/* This method should be used to delete all notes with specific userId. */

	
	public boolean deleteAllNotes(String userId) {
		
		Optional<NoteUser> noteUserOptional = noteRepository.findById(userId);
		NoteUser noteUser = noteUserOptional.get();
		
		if(noteUser.getNotes()!=null)
		{
			List<Note> filteredNotes = new ArrayList<>();
			List<Note>   notes = noteUser.getNotes();
			notes.forEach(note-> {
				if(!note.getNoteCreatedBy().equals(userId))
				{
					filteredNotes.add(note);
				}
			});
			noteUser.setNotes(filteredNotes);
			noteRepository.save(noteUser);
			return true;
		}
		return false;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		
		try
		{
		Optional<NoteUser> noteUserOptional = noteRepository.findById(userId);
		NoteUser noteUser = noteUserOptional.get();
		if (noteUser.getNotes() != null) {
			List<Note> notes = noteUser.getNotes();
			List<Note> updateNotesList = new ArrayList<>();
			for (Note noteIter : notes) {
				if (noteIter.getNoteId() == id) {
					updateNotesList.add(note);
				} else {
					updateNotesList.add(noteIter);
				}
			}
			noteUser.setNotes(updateNotesList);
			noteRepository.save(noteUser);
		}
		}
		catch(NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("NoteNotFoundExeption");
		}
		return note;
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		Note noteReturn = null;
		try
		{
		Optional<NoteUser> noteUserOptional = noteRepository.findById(userId);
		NoteUser noteUser = noteUserOptional.get();
		if (noteUser.getNotes() != null) {
			List<Note> notes = noteUser.getNotes();

			for (Note note : notes) {
				if (note.getNoteId() == noteId) {
					noteReturn = note;
				}
			}
		}
		}
		catch(NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("NoteNotFoundExeption");
		}
		return noteReturn;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		
		NoteUser noteUser = noteRepository.findById(userId).get();
		return noteUser.getNotes();
	}

}
