package business.seat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import business.reservainstalacao.ReservaDeInstalacao;

@Entity
public class Seat {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@Column(nullable = false)
	private ReservaDeInstalacao reserva;
	
	Seat(){}
	
	public Seat(ReservaDeInstalacao reserva) {
		this.reserva = reserva;
	}
}
