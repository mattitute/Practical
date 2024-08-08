package org.example.practical.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.practical.YearlyCalculation;
import org.example.practical.entities.Saving;
import org.example.practical.repositories.SavingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SessionAttributes({"a", "e"})
@Controller
@AllArgsConstructor
public class SavingController {

    private final SavingRepository savingRepository;
    static int num = 0;

    @GetMapping(path = "/index")
    public String savings(Model model) {
        List<Saving> savings = savingRepository.findAll();
        model.addAttribute("listSavings", savings);
        return "savings";
    }

    @GetMapping("/formSavings")
    public String formSavings(Model model) {
        model.addAttribute("saving", new Saving());
        return "formSavings";
    }

    @GetMapping("/editSavings")
    public String editSavings(Model model, Long id, HttpSession session) {
        num = 2;
        session.setAttribute("info", 0);
        Saving saving = savingRepository.findById(id).orElse(null);
        if (saving == null) throw new RuntimeException("Saving record does not exist");
        model.addAttribute("saving", saving);
        return "editSavings";
    }

    @GetMapping("/delete")
    public String delete(Long id) {
        savingRepository.deleteById(id);
        return "redirect:/index";
    }

    @PostMapping(path = "/save")
    public String save(Model model, Saving saving, BindingResult bindingResult, ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "formSavings";
        } else {
            savingRepository.save(saving);
            if (num == 2) {
                mm.put("e", 2);
                mm.put("a", 0);
            } else {
                mm.put("a", 1);
                mm.put("e", 0);
            }
            return "redirect:/index";
        }
    }

    @GetMapping("/calculateInterest")
    public String calculateInterest(Model model) {
        List<Saving> savings = savingRepository.findAll();
        model.addAttribute("listSavings", savings);
        return "calculateInterest";
    }

    @GetMapping("/viewSaving")
    public String viewSaving(Model model, Long id) {
        Saving saving = savingRepository.findById(id).orElse(null);
        if (saving == null) {
            throw new RuntimeException("Saving record does not exist");
        }

        List<YearlyCalculation> yearlyCalculations = calculateYearlyInterest(saving);
        model.addAttribute("saving", saving);
        model.addAttribute("yearlyCalculations", yearlyCalculations);

        return "viewSaving";
    }

    private List<YearlyCalculation> calculateYearlyInterest(Saving saving) {
        List<YearlyCalculation> calculations = new ArrayList<>();
        double principal = saving.getCdep();
        double rate = "Saving-Regular".equalsIgnoreCase(saving.getSavtype()) ? 0.10 : 0.15;

        for (int year = 1; year <= saving.getNyears(); year++) {
            double interest = principal * rate;
            double endingBalance = principal + interest;
            calculations.add(new YearlyCalculation(year, principal, interest, endingBalance));
            principal = endingBalance; // Update principal for next year
        }

        return calculations;
    }



    private double calculateInterest(double principal) {
        double rate = 0.10;
        return principal * rate;
    }


}
