package com.german.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.german.tarea3AD2024base.modelo.Parada;

@Repository
public interface ParadaRepositorio extends JpaRepository<Parada, Long>{
    boolean existsByNombreAndRegion(String nombre, char region);
    Parada findByNombreAndRegion(String nombre, char region);
    Parada findByUsuario_id(Long usuario_id);
}