package it.polito.tdp.food.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;


public class Model {
	FoodDao dao ;
	private List<String> vertici;
	
	Graph<String, DefaultWeightedEdge> grafo;
	
	//ricorsione
	List<PorzioneEPeso> risultato;
	private double pesoMax;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public void creaGrafo(int calorie) {
		vertici = new ArrayList<>(dao.getAllVertex(calorie));
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, vertici);
		
		List<Correlazione> archi = new ArrayList<>(dao.creaArchi(vertici));
		
		for(Correlazione c: archi) {
			if(grafo.containsVertex(c.getTipo1()) && grafo.containsVertex(c.getTipo2()))
			Graphs.addEdgeWithVertices(grafo, c.getTipo1(), c.getTipo2(), c.getPeso());
		}
		
		

		/*for(String s1: grafo.vertexSet()) {
			for(String s2: grafo.vertexSet()) {
				if(s1.compareTo(s2) <0) {  // solo per non prendere s1-s2 e poi s2-s1
					Correlazione arco  = dao.getArchi(s1, s2);
					
					if(arco != null) {
						Graphs.addEdgeWithVertices(grafo, arco.getTipo1(), arco.getTipo2(), arco.getPeso());
					}
				}
			}
		}*/
	}
	
	public int NumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int NumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getVerticiGrafo(){
		return this.vertici;
	}
	
	public boolean esisteGrafo() {
		if(this.grafo == null)
			return false;
		return true;
	}

	public List<PorzioneEPeso> porzioniCorrelate(String p) {
		List<PorzioneEPeso> list = new ArrayList<>();
		
		for(DefaultWeightedEdge edge: grafo.edgesOf(p)) {
			PorzioneEPeso porzione = new PorzioneEPeso(grafo.getEdgeTarget(edge), grafo.getEdgeWeight(edge));
			list.add(porzione);
		}
		
		return list;
	}
	
	public List<PorzioneEPeso> cercaCammino(String partenza, int N){
		pesoMax =0.0; 
		this.risultato = new ArrayList<>();

		List<PorzioneEPeso> parziale = new ArrayList<>();
		
		parziale.add(new PorzioneEPeso(partenza, 0));
		
		  cerca(parziale, N);
		
		return risultato;
	}

	public void cerca(List<PorzioneEPeso> parziale, int N) {
		
		//caso terminale
		if(parziale.size() == N) {
			double pesoAttuale = calcolaPeso(parziale);
			
			if(pesoAttuale > this.pesoMax) {
				pesoMax = pesoAttuale;
				this.risultato = new ArrayList<>(parziale);
			}
			return;     //
		}
		
		//altrimenti non sono ancora arrivata ad una soluzione ammissibile
		String ultimo = parziale.get(parziale.size()-1).getPorzione();
		
		for(DefaultWeightedEdge edge: grafo.edgesOf(ultimo)) {
			
			PorzioneEPeso vicino = new PorzioneEPeso(grafo.getEdgeTarget(edge), grafo.getEdgeWeight(edge));
			
			if(!parziale.contains(vicino)) {
			    parziale.add(vicino);
			    
			    cerca(parziale, N);
			    parziale.remove(parziale.size()-1);
			}
		}
		
	}

	public double calcolaPeso(List<PorzioneEPeso> lista) {
		double result =0.0;
		
		for(PorzioneEPeso p: lista) {
			result += p.getPeso();
		}
		
		return result;
	}
	
	public double getPesoTotale() {
		return this.pesoMax;
	}
	
}
