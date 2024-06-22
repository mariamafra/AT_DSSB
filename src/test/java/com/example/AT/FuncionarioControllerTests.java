package com.example.AT;

import com.example.AT.controller.FuncionarioController;
import com.example.AT.model.Funcionario;
import com.example.AT.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FuncionarioController.class)
@WithMockUser(username = "user", password = "password", roles = "ADMIN")
public class FuncionarioControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FuncionarioService funcionarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarTodosFuncionario() throws Exception {
        Funcionario funcionario1 = new Funcionario(null, "Carlos", "Rua 10", "21854788569", "carlos@gmail.com", new Date(), null);
        Funcionario funcionario2 = new Funcionario(null, "Barbara", "Rua 87", "77846788569", "barbara@gmail.com", new Date(), null);

        given(funcionarioService.getAll()).willReturn(Arrays.asList(funcionario1, funcionario2));

        mockMvc.perform(get("/funcionario")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Carlos"))
                .andExpect(jsonPath("$[1].nome").value("Barbara"));
    }

    @Test
    public void listarFuncionarioPorId() throws Exception {
        Funcionario funcionario = new Funcionario(null, "Carlos", "Rua 10", "21854788569", "carlos@gmail.com", new Date(), null);

        given(funcionarioService.getById(anyLong())).willReturn(funcionario);

        mockMvc.perform(get("/funcionario/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    public void salvarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario(null, "Carlos", "Rua 10", "21854788569", "carlos@gmail.com", new Date(), null);

        given(funcionarioService.save(any(Funcionario.class))).willReturn(funcionario);

        mockMvc.perform(post("/funcionario")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    public void atualizarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario(1L, "Carlos Atualizado", "Rua 10", "21854788569", "carlos@gmail.com", new Date(), null);

        given(funcionarioService.update(anyLong(), any(Funcionario.class))).willReturn(funcionario);

        mockMvc.perform(put("/funcionario/1")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"));
    }

    @Test
    public void removerFuncionario() throws Exception {
        mockMvc.perform(delete("/funcionario/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isAccepted());
    }
}
