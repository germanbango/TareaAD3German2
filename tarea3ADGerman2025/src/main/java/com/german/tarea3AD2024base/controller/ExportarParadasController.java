package com.german.tarea3AD2024base.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.german.tarea3AD2024base.config.StageManager;
import com.german.tarea3AD2024base.modelo.Estancia;
import com.german.tarea3AD2024base.modelo.Parada;
import com.german.tarea3AD2024base.services.EstanciaServicio;
import com.german.tarea3AD2024base.services.ParadaServicio;
import com.german.tarea3AD2024base.utiles.Sesion;
import com.german.tarea3AD2024base.view.FxmlView;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Controller
public class ExportarParadasController {

	@FXML
	private DatePicker txtFechaInicial;
	@FXML
	private DatePicker txtFechaFinal;
	@FXML
	private TableView<Estancia> estanciasTabla;
	@FXML
	private TableColumn<Estancia,String> nombrePeregrinoCol;
	@FXML
	private TableColumn<Estancia,String> fechaEstanciaCol;
	@FXML
	private TableColumn<Estancia, String> vipCol;
	
	@Autowired
	private ParadaServicio paradaServicio;
	@Autowired
	private EstanciaServicio estanciaServicio;
	@Lazy
	@Autowired
	private StageManager stageManager;
	
	private Parada parada = new Parada() ;
	private List<Estancia> estancias = new ArrayList<Estancia>() ;
	
	@FXML
	public void Exportar() {
		if(validar()){
			exportarParadas();
		}
	}
	
	private void exportarParadas() {
		parada=paradaServicio.encontrarParadaPorUsuario(Sesion.getId());
		estancias = estanciaServicio.estanciasPorParada(parada.getId());
		boolean fechaIncorrecta = true;
		LocalDate fechaInicial = txtFechaInicial.getValue();
		LocalDateTime desde = fechaInicial.atStartOfDay();
		LocalDate fechaFinal = txtFechaFinal.getValue();
		LocalDateTime hasta = fechaFinal.atTime(LocalTime.MAX);
		List<Estancia> estanciasFechadas = new ArrayList<Estancia>();
		
		do {
			for(Estancia estancia: estancias){
				if(estancia.getFecha().isAfter(desde) && estancia.getFecha().isBefore(hasta)) {
					estanciasFechadas.add(estancia);
					fechaIncorrecta = !fechaIncorrecta;
				}
			}
			if(fechaIncorrecta){
				mostrarAlerta(AlertType.ERROR, "ERROR:", "No hay estancias en ese margen de tiempo");
			}else {
				mostrarAlerta(AlertType.CONFIRMATION, "Paradas exportadas", "Se han cargado las estancias");
				configurarTabla(estanciasFechadas);
			}
		}while(fechaIncorrecta);

		
	}
	
	private void configurarTabla(List<Estancia> estanciasFechadas) {
		 nombrePeregrinoCol.setCellValueFactory(estancia ->
	        new SimpleStringProperty(estancia.getValue().getPeregrino().getNombre())
	    );

	    fechaEstanciaCol.setCellValueFactory(estancia ->
	        new SimpleStringProperty(estancia.getValue().getFecha().toString())
	    );

	    vipCol.setCellValueFactory(estancia ->
	        new SimpleStringProperty(estancia.getValue().isVip() ? "Sí" : "No")
	    );

	    estanciasTabla.getItems().setAll(estanciasFechadas);
	}
	
	private boolean validar() {
		LocalDate fechaInicio = txtFechaInicial.getValue();
		LocalDate fechaFinal = txtFechaFinal.getValue();
		
		if(fechaInicio == null || fechaFinal == null){
			mostrarAlerta(AlertType.ERROR, "ERROR:", "Debe introducir las dos fechas");
			return false;
		}
		
		return true;
	}
	
	@FXML
	public void salir() {
		stageManager.switchScene(FxmlView.MENU_RESPONSABLE);
	}
	
	private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

}
