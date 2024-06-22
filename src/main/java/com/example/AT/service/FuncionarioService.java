package com.example.AT.service;

import com.example.AT.model.Departamento;
import com.example.AT.model.Funcionario;
import com.example.AT.repository.DepartamentoRepository;
import com.example.AT.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Funcionario> getAll(){
        return funcionarioRepository.findAll();
    }

    public Funcionario getById(Long id) {
        checkExistence(id);
        return funcionarioRepository.findById(id).get();
    }

    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario update(Long id, Funcionario newFuncionario) {
        return funcionarioRepository.findById(id).map(funcionario -> {
            funcionario.setNome(newFuncionario.getNome());
            funcionario.setEndereco(newFuncionario.getEndereco());
            funcionario.setTelefone(newFuncionario.getTelefone());
            funcionario.setEmail(newFuncionario.getEmail());
            funcionario.setNascimento(newFuncionario.getNascimento());
            funcionario.setDepartamento(newFuncionario.getDepartamento());
            return funcionarioRepository.save(funcionario);
        }).orElseGet(() -> {
            newFuncionario.setId(id);
            return funcionarioRepository.save(newFuncionario);
        });
    }

    public void delete(Long id) {
        checkExistence(id);
        funcionarioRepository.deleteById(id);
    }

    public Funcionario addFuncionarioOnDep(Long departamentoId, Funcionario funcionario) {
        return departamentoRepository.findById(departamentoId).map(departamento -> {
            funcionario.setDepartamento(departamento);
            return funcionarioRepository.save(funcionario);
        }).orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado"));
    }

    public void checkExistence(Long id) {
        if (funcionarioRepository.existsById(id) == false) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }
    }
}
