package com.school.service;


	import java.util.List;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

import com.school.entities.Events;
import com.school.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;


	@Service
	public class EventService {

	    @Autowired
	    private EventRepository competitionRepository;
	    
	   
	    public List<Events> getAllCompetitions() {
	        return competitionRepository.findAll();
	    }

	    public Events getCompetitionById(Long id) {
	        return competitionRepository.findById(id)
	                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id " + id));
	    }
	   
	    public Events createCompetition(Events events) {
	        return competitionRepository.save(events);
	    }

	    public Events updateCompetition(Long id, Events events) {
	        Events existingCompetition = getCompetitionById(id);
	        existingCompetition.setName(events.getName());
	        existingCompetition.setDescription(events.getDescription());
	        existingCompetition.setFee(events.getFee());
	        existingCompetition.setOptions(events.getOptions());
	        existingCompetition.setStart_date(events.getStart_date());
	        existingCompetition.setEnd_date(events.getEnd_date());
	        existingCompetition.setCategory(events.getCategory());
	        existingCompetition.setAttendee_limit(events.getAttendee_limit());
	        existingCompetition.setRegistration_deadline(events.getRegistration_deadline());
	        existingCompetition.setOrganizer(events.getOrganizer());
	        existingCompetition.setContact_person(events.getContact_person() );
	        existingCompetition.setContact_email(events.getContact_email());
	        return competitionRepository.save(existingCompetition);
	    }

	    public void deleteCompetition(Long id) {
	        Events existingCompetition = getCompetitionById(id);
	        competitionRepository.delete(existingCompetition);
	    }

		public Events saveCompetition(Events events) {
			return competitionRepository.save(events);
		}

		public Events findByOptions(String options) {
			Events existingCompetition=competitionRepository.findByOptions(options);
			
			return competitionRepository.save(existingCompetition);
		}

		
		
		
		
		
	}



