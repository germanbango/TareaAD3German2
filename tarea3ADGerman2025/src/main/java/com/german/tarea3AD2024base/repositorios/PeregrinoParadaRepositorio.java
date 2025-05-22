
package com.german.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.german.tarea3AD2024base.modelo.Carnet;
import com.german.tarea3AD2024base.modelo.PeregrinoParada;

@Repository
public interface PeregrinoParadaRepositorio extends JpaRepository<PeregrinoParada, Long>{

}
