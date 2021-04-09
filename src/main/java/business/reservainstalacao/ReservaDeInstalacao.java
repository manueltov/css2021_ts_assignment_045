package business.reservainstalacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import business.instalacao.Instalacao;

@Entity
public class ReservaDeInstalacao {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@Column(nullable = false)
	private Instalacao instalacao;
	
	@Temporal(TemporalType.DATE) @Column(nullable = false)
	private Date inicio;
	
	@Temporal(TemporalType.DATE) @Column(nullable = false)
	private Date fim;
	
	ReservaDeInstalacao(){}

	public ReservaDeInstalacao(Instalacao i,Date inicio,Date fim) {
		this.instalacao = i;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	public Instalacao getInstalacao() {
		return instalacao;
	}
	
	public Date getInicio() {
		return inicio;
	}
	public int getId() {
		return id;
	}
	public Date getFim() {
		return fim;
	}

	public boolean isOverlapedBy(Date inicio,Date fim) {
		return !((inicio.before(this.inicio) && fim.before(this.inicio)) ||
				(inicio.after(this.fim) && fim.after(this.fim)));	
	}
	
}
