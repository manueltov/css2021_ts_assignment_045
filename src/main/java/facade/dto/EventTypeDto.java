package facade.dto;

public class EventTypeDto {

	private String name;
	private int max_watch;
	private String lugares;
	
	public EventTypeDto(String name,int max_watch,String lugares) {
		this.name = name;
		this.max_watch = max_watch;
		this.lugares = lugares;
	}
	
	@Override
	public String toString() {
		return "Tipo de evento:"+name+"\nLotacao Maxima:"+max_watch+"\nTipo de lugares:"+lugares;
	}
}
