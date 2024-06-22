package com.example.AT.service;

import com.example.AT.model.Departamento;
import com.example.AT.model.Funcionario;
import com.example.AT.repository.DepartamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> getAll(){
        return departamentoRepository.findAll();
    }

    public Departamento getById(Long id) {
        checkExistence(id);
        return departamentoRepository.findById(id).get();
    }

    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    public Departamento update(Long id, Departamento newDepartamento) {
        return departamentoRepository.findById(id).map(departamento -> {
            departamento.setNome(newDepartamento.getNome());
            departamento.setLocalDep(newDepartamento.getLocalDep());
            departamento.setFuncionarios(newDepartamento.getFuncionarios());
            return departamentoRepository.save(departamento);
        }).orElseGet(() -> {
            newDepartamento.setId(id);
            return departamentoRepository.save(newDepartamento);
        });
    }

    public void delete(Long id) {
        checkExistence(id);
        departamentoRepository.deleteById(id);
    }

    public List<Funcionario> getFuncionariosFromDepById(Long id) {
        Departamento departamento = departamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado"));
        return departamento.getFuncionarios();
    }

    public void checkExistence(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento não encontrado");
        }
    }
}
