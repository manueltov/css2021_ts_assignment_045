package business.instalacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import business.eventtype.EventType;
import business.reservainstalacao.ReservaDeInstalacao;

public class Instalacao {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(nullable = false)
	private EventType eventType;

	@OneToMany(cascade = CascadeType.ALL,mappedBy="instalacao")
	private List<ReservaDeInstalacao> reservas;
	
	Instalacao(){}
	
	public Instalacao(String nome,EventType eventType) {
		this.eventType = eventType;
		this.nome = nome;
		reservas = new ArrayList<>();
	}
	
	public List<ReservaDeInstalacao> getReservas() {
		return reservas;
	}
	
	public String getNome() {
		return nome;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setReservas(List<ReservaDeInstalacao> reservas) {
		this.reservas = reservas;
	}
	
	public boolean availableOn(Date inicio,Date fim) {
		for (ReservaDeInstalacao reservaDeInstalacao : reservas) {
			if(reservaDeInstalacao.isOverlapedBy(inicio,fim)) {
				return false;
			}
		}
		return true;
	}
	
}
