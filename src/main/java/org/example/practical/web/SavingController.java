package org.example.practical.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.practical.entities.Saving;
import org.example.practical.repositories.SavingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@SessionAttributes({"a", "e"})
@Controller
@AllArgsConstructor
public class SavingController {

    private final SavingRepository savingRepository;
    static int num = 0;

    @GetMapping(path = "/index")
    public String savings(Model model, @RequestParam(name = "exists", required = false) String exists) {
        List<Saving> savings = savingRepository.findAll();
        model.addAttribute("listSavings", savings);
        if (exists != null) {
            model.addAttribute("exists", true);
        }
        return "savings";
    }

    @GetMapping("/formSavings")
    public String formSavings(Model model) {
        model.addAttribute("saving", new Saving());
        return "formSavings";
    }

    @GetMapping("/editSavings")
    public String editSavings(Model model, String id, HttpSession session) {
        Saving saving = savingRepository.findById(id).orElse(null);
        if (saving == null) throw new RuntimeException("Saving record does not exist");
        model.addAttribute("saving", saving);
        return "editSavings";
    }

    @GetMapping("/delete")
    public String delete(String id) {
        savingRepository.deleteById(id);
        return "redirect:/index";
    }

    @PostMapping(path = "/add")
    public String add(Saving saving, BindingResult bindingResult, ModelMap mm, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "formSavings";
        } else {
            if (savingRepository.existsById(saving.getCustno())) {
                redirectAttributes.addAttribute("exists", true);
                return "redirect:/index";
            } else {
                savingRepository.save(saving);
                mm.put("a", 1);
                mm.put("e", 0);
                return "redirect:/index";
            }
        }
    }

    @PostMapping(path = "/edit")
    public String edit(Saving saving, BindingResult bindingResult, ModelMap mm) {
        if (bindingResult.hasErrors()) {
            return "editSavings";
        } else {
            savingRepository.save(saving);
            mm.put("e", 2);
            mm.put("a", 0);
            return "redirect:/index";
        }
    }

    @GetMapping("/viewSaving")
    public String viewSaving(Model model, String id) {
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

    public static class YearlyCalculation {
        private int year;
        private double startingAmount;
        private double interest;
        private double endingBalance;

        public YearlyCalculation(int year, double startingAmount, double interest, double endingBalance) {
            this.year = year;
            this.startingAmount = startingAmount;
            this.interest = interest;
            this.endingBalance = endingBalance;
        }

        // Getters and setters
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public double getStartingAmount() {
            return startingAmount;
        }

        public void setStartingAmount(double startingAmount) {
            this.startingAmount = startingAmount;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public double getEndingBalance() {
            return endingBalance;
        }

        public void setEndingBalance(double endingBalance) {
            this.endingBalance = endingBalance;
        }
    }

}
