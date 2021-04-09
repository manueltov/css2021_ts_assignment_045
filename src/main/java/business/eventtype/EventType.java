package business.eventtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import business.SeatType;

@Entity
public class EventType {
	
	public static final String FIND_BY_NAME = "EventType.findByName";
	public static final String EVENT_TYPE_NAME = "name";
	public static final String GET_ALL_NAMES = "Event.getAllNames";

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private int max_watch;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SeatType tipoDeLugares;
	
	EventType(){}
	
	public EventType(String tipo,int max_watch,SeatType tipoDeLugares) {
		this.name = tipo;
		this.max_watch = max_watch;
		this.tipoDeLugares = tipoDeLugares;
	}
	
	public int getMax_watch() {
		return max_watch;
	}
	
	public SeatType getTipoDeLugares() {
		return tipoDeLugares;
	}
	
	public String getTipo() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
}
