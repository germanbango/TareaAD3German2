package com.german.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.german.tarea3AD2024base.config.StageManager;
import com.german.tarea3AD2024base.modelo.Parada;
import com.german.tarea3AD2024base.modelo.Rol;
import com.german.tarea3AD2024base.modelo.User;
import com.german.tarea3AD2024base.services.ParadaServicio;
import com.german.tarea3AD2024base.services.UserService;
import com.german.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class UserController implements Initializable {

    @FXML private Button btnLogout;
    @FXML private Label userId;
    @FXML private TextField txtParadaNombre;
    @FXML private TextField txtRegionLetra;
    @FXML private TextField txtResponsable;
    @FXML private TextField txtResponableUsuario;
    @FXML private PasswordField txtResponsableContrasena;
    @FXML private Button reset;
    @FXML private Button saveUser;
    
    @FXML private TableView<Parada> userTable;
    @FXML private TableColumn<Parada, Long> colParadId;
    @FXML private TableColumn<Parada, String> colParadaNombre;
    @FXML private TableColumn<Parada, Character> colParadaRegion;
    @FXML private TableColumn<Parada, String> colResponable;
    @FXML private TableColumn<Parada, Long> colResponsableId;
    @FXML private TableColumn<Parada, Boolean> colEdit;

    @Lazy @Autowired 
    private StageManager stageManager;
    
    @Autowired 
    private UserService userService;
    
    @Autowired 
    private ParadaServicio paradaServicio;

    @FXML
    private void logout() throws Exception {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    private void reset() {
        limpiarCampos();
    }

    @FXML
    private void saveUser() {
        if(validarCampos()) {
            try {
                User responsable = crearUsuarioResponsable();
                userService.save(responsable); 
                Parada nuevaParada = crearParada(responsable);
                paradaServicio.guardar(nuevaParada);

                mostrarAlerta(AlertType.INFORMATION, "Éxito", "Parada registrada correctamente");
                limpiarCampos();
                cargarParadas();
            } catch (Exception e) {
                mostrarAlerta(AlertType.ERROR, "Error", "Error al registrar parada: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private User crearUsuarioResponsable() {
        User responsable = new User();
        responsable.setEmail(txtResponableUsuario.getText().trim());
        responsable.setPassword(txtResponsableContrasena.getText().trim());
        responsable.setRol(Rol.RESPONSABLE);
        return responsable;
    }

    private Parada crearParada(User responsable) {
        Parada parada = new Parada();
        parada.setNombre(txtParadaNombre.getText().trim());
        parada.setRegion(Character.toUpperCase(txtRegionLetra.getText().trim().charAt(0)));
        parada.setResponsable(txtResponsable.getText().trim());
        parada.setUsuario(responsable);
        return parada;
    }

    private boolean validarCampos() {
        String nombreParada = txtParadaNombre.getText().trim();
        String regionInput = txtRegionLetra.getText().trim();
        String usuario = txtResponableUsuario.getText().trim();
        String contrasena = txtResponsableContrasena.getText().trim();
        String nombreResponsable = txtResponsable.getText().trim();

        if(nombreParada.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Ingrese el nombre de la parada");
            return false;
        }

        if(regionInput.length() != 1 || !Character.isLetter(regionInput.charAt(0))) {
            mostrarAlerta(AlertType.WARNING, "Validación", "La región debe ser una sola letra (A-Z)");
            return false;
        }
        char region = Character.toUpperCase(regionInput.charAt(0));

        if(paradaServicio.existeParadaConNombreYRegion(nombreParada, region)) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Ya existe una parada con este nombre en la región " + region);
            return false;
        }

        if(usuario.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Ingrese el usuario del responsable");
            return false;
        }
        if(usuario.contains(" ")) {
            mostrarAlerta(AlertType.WARNING, "Validación", "El usuario no puede contener espacios");
            return false;
        }
        if(userService.existeUsuarioConEmail(usuario)) {
            mostrarAlerta(AlertType.WARNING, "Validación", "El usuario ya está registrado");
            return false;
        }

        if(contrasena.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Ingrese la contraseña");
            return false;
        }
        if(contrasena.contains(" ")) {
            mostrarAlerta(AlertType.WARNING, "Validación", "La contraseña no puede contener espacios");
            return false;
        }

        if(nombreResponsable.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Ingrese el nombre del responsable");
            return false;
        }

        return true;
    }

    private void cargarParadas() {
        List<Parada> paradas = paradaServicio.encontrarTodos();
        userTable.getItems().setAll(paradas);
    }

    private void limpiarCampos() {
        txtParadaNombre.clear();
        txtRegionLetra.clear();
        txtResponsable.clear();
        txtResponableUsuario.clear();
        txtResponsableContrasena.clear();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnasTabla();
        cargarParadas();
    }

    private void configurarColumnasTabla() {
        colParadId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colParadaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colParadaRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
        colResponable.setCellValueFactory(new PropertyValueFactory<>("responsable"));

        colResponsableId.setCellValueFactory(cellData -> {
            Parada parada = cellData.getValue();
            if (parada != null && parada.getUsuario() != null) {
                return new SimpleObjectProperty<>(parada.getUsuario().getId());
            }
            return new SimpleObjectProperty<>(null);
        });
    }
}
