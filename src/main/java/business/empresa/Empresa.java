package business.empresa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import business.eventtype.EventType;


public class Empresa {

	public static final String FIND_BY_ID = "Empresa.findById";
	public static final String ID = "id";


	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToMany
	@JoinTable(name = "EVENTS_ALLOWED")
	private List<EventType> tipos_de_eventos;
	
	public Empresa(List<EventType> authEvents) {
		tipos_de_eventos = new ArrayList<>();
		for (EventType eventType : authEvents) {
			tipos_de_eventos.add(eventType);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public List<EventType> getTiposDeEventos() {
		return tipos_de_eventos;
	}

	public boolean haveLicense(EventType eventType) {
		for (EventType ev : tipos_de_eventos) {
			if(ev.getTipo().contentEquals(eventType.getTipo())) {
				return true;
			}
		}
		return false;
	}
	
	
}
