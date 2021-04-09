package business.event;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import business.empresa.Empresa;
import business.eventtype.EventType;
import business.instalacao.Instalacao;



@Entity
@NamedQuery(name=Event.GET_ALL_NAMES, query="SELECT ev.name FROM Event ev")
public class Event {


	public static final String GET_ALL_NAMES = "Event.getAllNames";

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	 @ElementCollection
	   @CollectionTable(name="Dates", joinColumns=@JoinColumn(name="id",referencedColumnName = "id"))
	private List<EventTimeFrame> datas;
	
	@Column(nullable = false)
	private EventType eventType;
	
	@ManyToOne
	@Column(nullable = false)
	private Empresa empresa;
	
	@ManyToOne
	@Column(nullable = true)
	private Instalacao instalacao;
	Event(){}
	
	
	public Event(String nome, EventType eventType, List<EventTimeFrame> timeFrames, Empresa empresa) {
		this.name = nome;
		this.eventType = eventType;
		this.datas = new ArrayList<>();
		this.datas.addAll(timeFrames);
		this.empresa = empresa;
	}
	
	public Instalacao getInstalacao() {
		return this.instalacao;
	}
	
	public void setInstalacao(Instalacao instalacao) {
		this.instalacao = instalacao;
	}


	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public EventTimeFrame[] getDatas() {
		EventTimeFrame[] aux = new EventTimeFrame[datas.size()];
		return datas.toArray(aux);
	}
	
	
}
