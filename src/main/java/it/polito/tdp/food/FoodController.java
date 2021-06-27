/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.PorzioneEPeso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco cammino peso massimo...");
    	
    	if(!model.esisteGrafo()) {
    		txtResult.setText("Crea il grafo prima");
    		return ;
    	}
    	
        String p = this.boxPorzioni.getValue();
    	
    	if(p == null) {
    		txtResult.setText("scegli una porzione di partenza");
    		return;
    	}
    	
    	if(txtPassi.getText().isEmpty()) {
    		txtResult.setText("Non hai inserito un valore nella casella passi");
    		return ;
    	}
    	
    	int passi=0;
    	
    	try {
    		passi = Integer.parseInt(this.txtPassi.getText());
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserisci un valore numerico");
    		return;
    	}
    	
    	List<PorzioneEPeso> risultato = model.cercaCammino(p, passi);
    	
    	if(risultato.size() == 0) {
    		txtResult.setText("NESSUN RISULTATO");
    		return ;
    	}
    	
    	for(PorzioneEPeso pp: risultato) {
    		txtResult.appendText(pp.getPorzione()+"\n");
    	}
    	
    	txtResult.appendText("Il peso totale vale: "+model.getPesoTotale());
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco porzioni correlate...\n ");
    	
    	if(!model.esisteGrafo()) {
    		txtResult.appendText("crea il grafo prima");
    		return ;
    	}
    	
    	String p = this.boxPorzioni.getValue();
    	
    	if(p == null) {
    		txtResult.setText("scegli una porzione di partenza");
    		return;
    	}
    	
    	List<PorzioneEPeso> correlate = new ArrayList<>(model.porzioniCorrelate(p));
    	if(correlate.size() == 0) {
    		txtResult.setText("la porzione scelta non ha adiacenti");
    		return ;
    	}
    	  for(PorzioneEPeso pe: correlate) {
    		  txtResult.appendText(pe.toString()+"\n");
    	  }
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...");
    	
    	if(txtCalorie.getText().isEmpty()){
    		txtResult.setText("inserisce un valore nella casella calories");
    		return ;
    	}
    	int c = 0;
    	
    	try {
    		c = Integer.parseInt(this.txtCalorie.getText());
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("inserisci un intero");
    		return;
    	}
    	
    	model.creaGrafo(c);
    	txtResult.appendText("Grafo Creato: \n #Vertici:"+model.NumVertici()+"\n#Archi:"+model.NumArchi());
    	
    	boxPorzioni.getItems().addAll(model.getVerticiGrafo());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
