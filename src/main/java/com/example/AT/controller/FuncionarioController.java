package com.example.AT.controller;

import com.example.AT.model.Funcionario;
import com.example.AT.service.FuncionarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<Funcionario>> getAll() {
        return ResponseEntity.ok(funcionarioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(funcionarioService.getById(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Funcionario> create(@RequestBody Funcionario funcionario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioService.save(funcionario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> update(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(funcionarioService.update(id, funcionario));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            funcionarioService.delete(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /* Relação Funcionario - Departamento*/
    @PostMapping("/departamento/{departamentoId}")
    public ResponseEntity<Funcionario> addFuncionarioNoDepartamento(@PathVariable Long departamentoId, @RequestBody Funcionario funcionario) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(funcionarioService.addFuncionarioOnDep(departamentoId, funcionario));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
