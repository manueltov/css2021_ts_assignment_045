package facade.services;


import java.sql.Date;
import java.util.ArrayList;

import business.eventtype.EventType;
import business.handlers.NewEventHandler;
import facade.dto.EventTypeDto;
import facade.exceptions.ApplicationException;

public class EventService {

	private NewEventHandler newEventhandler;
	public EventService(NewEventHandler newEventhandler) {
		this.newEventhandler = newEventhandler;
	}
	
	public void createEvent() throws ApplicationException {
		newEventhandler.createEvent();
	}
	
	public Iterable<EventTypeDto> tryCreateEvent() throws ApplicationException {
		ArrayList<EventTypeDto> aux = new ArrayList<>();
		Iterable<EventType> evts = newEventhandler.tryCreateEvent();
		for (EventType et : evts) {
			aux.add(new EventTypeDto(et.getTipo(), et.getMax_watch(), et.getTipoDeLugares().toString()));			
		}
		return aux;
	}
	
	public void setTipoDeEvento(String tipo) throws ApplicationException {
		newEventhandler.setTipo(tipo);
	}
	
	public void setNome(String nome) throws ApplicationException {
		newEventhandler.setNome(nome);
	}
	
	public void setEmpresa(int empresa) throws ApplicationException {
		newEventhandler.setEmpresa(empresa);
	}
	
	public void addDate(Date inicio,Date fim) throws ApplicationException {
		newEventhandler.addDate(inicio, fim);
	}
	
	
}
