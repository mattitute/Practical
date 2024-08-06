package org.example.practical.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.practical.entities.Saving;
import org.example.practical.repositories.SavingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor

public class SavingController {

    @Autowired
    private SavingRepository savingRepository;

    static int num=0;

    @PostMapping(path="/save")
    public String save(Model model, Saving saving, BindingResult bindingResult, ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "savings";
        } else {
            // Check if custno already exists
            if (savingRepository.existsById(saving.getCustno())) {
                model.addAttribute("exists", true);
                return "redirect:index"; // Return to the form page if custno exists
            }

            savingRepository.save(saving);
            return "redirect:index";
        }
    }
    @GetMapping(path = "/index")

    //Make data from database appear
    public String savings(Model model) {
        List<Saving> savings = savingRepository.findAll();
        model.addAttribute("listSavings", savings);

        return "savings";
    }
    @GetMapping("/delete") public String delete(Long id){
        savingRepository.deleteById(id);
        return "redirect:/index";
    }

    @GetMapping("/editSavings")
    public String editSavings(Model model, Long id, HttpSession session){
        session.setAttribute("info", 0);
        Saving saving = savingRepository.findById(id).orElse(null); if(saving==null) throw new RuntimeException("Patient does not exist"); model.addAttribute("saving", saving);
        return "editSavings";
    }

    @GetMapping("/formSavings")
    public String formSavings(Model model) {
        model.addAttribute("saving", new Saving());
        return "formSavings";
    }
}
