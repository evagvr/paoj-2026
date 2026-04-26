package com.pao.project.bankingApp.service;

import com.pao.project.bankingApp.exception.InsuranceNotFoundException;
import com.pao.project.bankingApp.model.Insurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsuranceService {
    private HashMap<String, Insurance> insurancesById;
    private HashMap<String, List<Insurance>> insurancesByCustomer;
    private static InsuranceService instance = null;

    private InsuranceService() {
        this.insurancesById = new HashMap<>();
        this.insurancesByCustomer = new HashMap<>();
    }

    public static InsuranceService getInstance() {
        if (instance == null) {
            instance = new InsuranceService();
        }
        return instance;
    }

    public void addInsurance(Insurance insurance) {
        insurancesById.put(insurance.getInsuranceId(), insurance);
        insurancesByCustomer
                .computeIfAbsent(insurance.getCustomerId(), k -> new ArrayList<>())
                .add(insurance);
    }

    public Insurance getInsuranceById(String insuranceId) throws InsuranceNotFoundException {
        Insurance insurance = insurancesById.get(insuranceId);
        if (insurance == null) throw new InsuranceNotFoundException(insuranceId);
        return insurance;
    }

    public List<Insurance> getInsurancesByCustomer(String customerId) {
        return insurancesByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    public void cancelInsurance(String insuranceId) throws InsuranceNotFoundException {
        Insurance insurance = getInsuranceById(insuranceId);
        insurance.setActive(false);
    }

    public List<Insurance> getAllInsurances() {
        return new ArrayList<>(insurancesById.values());
    }

    public List<Insurance> getActiveInsurances() {
        List<Insurance> active = new ArrayList<>();
        for (Insurance insurance : insurancesById.values()) {
            if (insurance.isActive() && !insurance.isExpired()) {
                active.add(insurance);
            }
        }
        return active;
    }

    public List<Insurance> getExpiredInsurances() {
        List<Insurance> expired = new ArrayList<>();
        for (Insurance insurance : insurancesById.values()) {
            if (insurance.isExpired()) {
                expired.add(insurance);
            }
        }
        return expired;
    }
    public HashMap<String, String> getInsuranceCoverageAnalysis(String customerId) {
        List<Insurance> insurances = getInsurancesByCustomer(customerId);
        HashMap<String, String> analysis = new HashMap<>();
        for (Insurance insurance : insurances) {
            String details = "Type: " + insurance.getInsuranceType() +
                    " | Coverage: " + insurance.getCoverageAmount() +
                    " | Premium paid: " + String.format("%.2f", insurance.getTotalPremiumPaid()) +
                    " | Days remaining: " + insurance.getRemainingCoverageDays() +
                    " | Expired: " + insurance.isExpired();
            analysis.put(insurance.getPolicyNumber(), details);
        }
        return analysis;
    }
    public void removeInsurance(String insuranceId) {
        Insurance insurance = getInsuranceById(insuranceId);
        insurancesByCustomer.get(insurance.getCustomerId()).remove(insurance);
        insurancesById.remove(insuranceId);
    }
}