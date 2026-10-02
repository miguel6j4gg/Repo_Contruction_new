package pe.edu.uls.cos.demospring.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.uls.cos.demospring.dominio.entity.Producto;

public interface RepoProducto extends JpaRepository<Producto, Integer> {

}