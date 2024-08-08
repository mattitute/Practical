package org.example.practical.web;

import org.example.practical.entities.Saving;
import org.example.practical.repositories.SavingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.servlet.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class SavingControllerTest {

    Saving saving;


    private MockMvc mockMvc;

    @Mock
    SavingRepository savingRepository;

    @Mock
    View mockView;

    @InjectMocks
    SavingController savingController;

    @BeforeEach
    void setUp() throws Exception {
        saving = new Saving();
        saving.setCustno(5L);
        saving.setCustname("Test");
        saving.setCdep(7000.0);
        saving.setNyears(2);
        saving.setSavtype("Saving-Deluxe");

        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(savingController).setSingleView(mockView).build();
    }
    @Test
    public  void findAll_ListView() throws Exception {
        List<Saving> list = new ArrayList<Saving>();
        list.add(saving);
        list.add(saving);

        when(savingRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listSavings", list))
                .andExpect(view().name("savings"))
                .andExpect(model().attribute("listSavings" , hasSize(2)));
        verify(savingRepository, times(1)).findAll();
        verifyNoMoreInteractions(savingRepository);
    }



    @Test
    void delete(){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(savingRepository).deleteById(idCaptor.capture());
       savingRepository.deleteById(1L);
        assertEquals(1L, idCaptor.getValue());
        verify(savingRepository, times(1)).deleteById(1L);

    }
    @Test
    void save() throws Exception {
        when(savingRepository.save(saving)).thenReturn(saving);
        savingRepository.save(saving);
        verify(savingRepository, times(1)).save(saving);
    }


    @Test
    void savings() {
    }



    @Test
    void formStudents() throws Exception {
        mockMvc.perform(get("/formSavings/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("saving", new Saving()))
                .andExpect(view().name("formSavings"));
    }

    @Test
    void editStudents() throws Exception {

        Saving s2 = new Saving();
        s2.setCustno(1L);
        s2.setCustname("John Mast");

        String sDate1 ="2012/11/11";
        Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
        saving.setCdep(50.0);
        saving.setNyears(10);
        saving.setSavtype("Saving-Deluxe");

        Long iid = 1L;

        when(savingRepository.findById(iid)).thenReturn(Optional.of(s2));
        mockMvc.perform(get("/editSavings/").param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("saving", s2))
                .andExpect(view().name("editSavings"));

        verify(savingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(savingRepository);

    }
}