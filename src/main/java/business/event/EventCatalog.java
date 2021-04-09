package business.event;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.empresa.Empresa;
import business.eventtype.EventType;

public class EventCatalog {
	
	private EntityManager em;

	public EventCatalog(EntityManager em) {
		this.em = em;
	}

	public boolean nameIsAvailable(String name) {
		try {
			TypedQuery<String> query = em.createNamedQuery(Event.GET_ALL_NAMES, String.class);
			return !query.getResultList().contains(name);
		} catch (Exception e) {
			return false; 
		}	
	}

	public void addNewEvent(String nome, EventType eventType, List<EventTimeFrame> timeFrames, Empresa empresa) {
		em.persist(new Event(nome,eventType,timeFrames,empresa));		
	}
	


}
