
package com.german.tarea3AD2024base.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.german.tarea3AD2024base.modelo.Parada;
import com.german.tarea3AD2024base.repositorios.ParadaRepositorio;

@Service
public class ParadaServicio {

	@Autowired
	private ParadaRepositorio paradaRepositorio;

	public Parada guardar(Parada entity) {
		return paradaRepositorio.save(entity);
	}

	public Parada actualizar(Parada entity) {
		return paradaRepositorio.save(entity);
	}

	public void borrar(Parada entity) {
		paradaRepositorio.delete(entity);
	}

	public void borrarPorId(Long id) {
		paradaRepositorio.deleteById(id);
	}

	public void borrarPorLote(List<Parada> paradas) {
		paradaRepositorio.deleteAll(paradas);
	}

	public Parada encontrarPorId(Long id) {
		return paradaRepositorio.findById(id).get();
	}

	public List<Parada> encontrarTodos() {
		return paradaRepositorio.findAll();
	}

	public Parada paradaPorNombreyRegion(String nombre, char region) {
		return paradaRepositorio.findByNombreAndRegion(nombre, region);
	}
	public boolean existeParadaConNombreYRegion(String nombre, char region) {
		return paradaRepositorio.existsByNombreAndRegion(nombre, region);
	}
	
	public List<String> nombreyRegiones() {
		List<String> ret = new ArrayList<>();
		for (Parada parada : encontrarTodos()) {
			ret.add(parada.getNombre() + "  " + parada.getRegion());
		}
		return ret;
	}
	
	public Parada encontrarParadaPorUsuario(Long usuario_id) {
		return paradaRepositorio.findByUsuario_id(usuario_id);
	}
	
	public List<Parada> encontrarParadasPorPeregrino(Long idPeregrino){
		return paradaRepositorio.findParadasByPeregrinoId(idPeregrino);
	}
	
	public String encontrarNombreParadaPorId(Long idParada) {
		return paradaRepositorio.findNombreParadaById(idParada);
	}

}
