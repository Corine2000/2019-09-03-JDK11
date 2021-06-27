package it.polito.tdp.food.model;

public class PorzioneEPeso {
	private String porzione ;
	private double peso;
	public PorzioneEPeso(String porzione, double peso) {
		super();
		this.porzione = porzione;
		this.peso = peso;
	}
	public String getPorzione() {
		return porzione;
	}
	public void setPorzione(String porzione) {
		this.porzione = porzione;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return this.porzione + " peso| " +this.peso;
	}
	
	

}
