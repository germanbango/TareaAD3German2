package com.german.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.german.tarea3AD2024base.config.StageManager;
import com.german.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

@Controller
public class MenuResponsableController implements Initializable {

	@FXML
	private MenuBar barraMenuResponsable;
	@FXML
	private Menu menuGestion;
	@FXML
	private MenuItem menuItemSellar;
	@FXML
	private MenuItem menuItemExportar;
	
	@Lazy
	@Autowired
	StageManager stageManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menuItemSellar.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		menuItemExportar.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));

	}
	
	@FXML
	private void abrirSellarAlojarse() {
		stageManager.showModal(FxmlView.SELLAR_ALOJARSE);
	}
	
	@FXML
	private void abirExportarParadas() {
		stageManager.showModal(FxmlView.EXPORTAR_ALOJARSE);
	}

}
