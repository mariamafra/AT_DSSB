package com.example.AT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.AT.model.Departamento;
import com.example.AT.service.DepartamentoService;
import com.example.AT.controller.DepartamentoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartamentoController.class)
@WithMockUser(username = "user", password = "password", roles = "ADMIN")
public class DepartamentoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void listarTodosDepartamentos() throws Exception {
        Departamento departamento1 = new Departamento(null, "Russo", "8", null);
        Departamento departamento2 = new Departamento(null, "Indiano", "2", null);

        given(departamentoService.getAll()).willReturn(Arrays.asList(departamento1,departamento2));

        mockMvc.perform(get("/departamento")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Russo"))
                .andExpect(jsonPath("$[1].nome").value("Indiano"));
    }

    @Test
    public void listarDepartamentoPorId() throws Exception {
        Departamento departamento = new Departamento(null, "Russo", "8", null);

        given(departamentoService.getById(anyLong())).willReturn(departamento);

        mockMvc.perform(get("/departamento/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Russo"));
    }

    @Test
    public void salvarDepartamento() throws Exception {
        Departamento departamento = new Departamento(null, "Russo", "8", null);
        given(departamentoService.save(any(Departamento.class))).willReturn(departamento);

        mockMvc.perform(post("/departamento")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Russo"));
    }

    @Test
    public void atualizarDepartamento() throws Exception {
        Departamento departamento = new Departamento(1L, "Russo Atualizado", "8", null);

        given(departamentoService.update(anyLong(),any(Departamento.class))).willReturn(departamento);

        mockMvc.perform(put("/departamento/1")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Russo Atualizado"));
    }

    @Test
    public void removerDepartamento() throws Exception {
        mockMvc.perform(delete("/departamento/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isAccepted());
    }
}
