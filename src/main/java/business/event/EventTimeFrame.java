package business.event;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Embeddable
public class EventTimeFrame {

	@Temporal(TemporalType.TIMESTAMP) @Column(nullable = false)
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable = false)
	private Date end;
	
	EventTimeFrame() {}
	
	public EventTimeFrame(Date inicio,Date fim) {
		this.start = inicio;
		this.end = fim;
	}
	
	public Date getStart() {
		return start;
	}
	
	
	public Date getEnd() {
		return end;
	}
	
	
}
