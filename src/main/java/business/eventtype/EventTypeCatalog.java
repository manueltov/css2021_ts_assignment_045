package business.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.exceptions.EventTypeNotFoundException;

public class EventTypeCatalog {

	private EntityManager em;
	
	public EventTypeCatalog(EntityManager em) {
		this.em = em;
	}

	public EventType getEventTypeByName(String eventType) throws EventTypeNotFoundException {
		try {
			TypedQuery<EventType> query = em.createNamedQuery(EventType.FIND_BY_NAME, EventType.class);
			query.setParameter(EventType.EVENT_TYPE_NAME, eventType);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new EventTypeNotFoundException("Space with name " + eventType + " does not exist", e);
		}
	}

	public Iterable<EventType> getEventTypes() {
		try {
			TypedQuery<EventType> query = em.createNamedQuery(EventType.GET_ALL_NAMES, EventType.class);
			return query.getResultList();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

}
