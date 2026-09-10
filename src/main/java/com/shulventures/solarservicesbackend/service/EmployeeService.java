package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Employee;
import com.shulventures.solarservicesbackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    // CREATE EMPLOYEE

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }


    // GET ALL EMPLOYEES

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }



    // GET EMPLOYEE BY ID

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }



    // UPDATE EMPLOYEE

    public Employee updateEmployee(Long id, Employee employeeData) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + id)
                );


        // PERSONAL DETAILS

        existingEmployee.setTitle(employeeData.getTitle());
        existingEmployee.setName(employeeData.getName());
        existingEmployee.setPhone(employeeData.getPhone());
        existingEmployee.setDob(employeeData.getDob());
        existingEmployee.setGender(employeeData.getGender());
        existingEmployee.setEmail(employeeData.getEmail());
        existingEmployee.setRelationship(employeeData.getRelationship());
        existingEmployee.setEmergencyContact(employeeData.getEmergencyContact());
        existingEmployee.setMotherName(employeeData.getMotherName());
        existingEmployee.setMaritalStatus(employeeData.getMaritalStatus());


        // ADDRESS DETAILS
        existingEmployee.setCurrentAddress(
                employeeData.getCurrentAddress()
        );

        existingEmployee.setPermanentAddress(
                employeeData.getPermanentAddress()
        );



        // EDUCATION DETAILS
        existingEmployee.setTenth(employeeData.getTenth());
        existingEmployee.setTwelfth(employeeData.getTwelfth());
        existingEmployee.setGraduation(employeeData.getGraduation());
        existingEmployee.setPostGraduation(
                employeeData.getPostGraduation()
        );


        // EXPERIENCE DETAILS
        existingEmployee.setExperienceType(
                employeeData.getExperienceType()
        );

        existingEmployee.setPreviousExperience(
                employeeData.getPreviousExperience()
        );

        existingEmployee.setWorkExperienceYears(
                employeeData.getWorkExperienceYears()
        );

        existingEmployee.setPreviousCompanyName(
                employeeData.getPreviousCompanyName()
        );

        existingEmployee.setPreviousDesignation(
                employeeData.getPreviousDesignation()
        );

        existingEmployee.setPreviousSalary(
                employeeData.getPreviousSalary()
        );


        // EMPLOYEE DETAILS

        existingEmployee.setEmployeeType(
                employeeData.getEmployeeType()
        );

        existingEmployee.setDepartment(
                employeeData.getDepartment()
        );

        existingEmployee.setPackageAmount(
                employeeData.getPackageAmount()
        );

        existingEmployee.setJoiningDate(
                employeeData.getJoiningDate()
        );

        existingEmployee.setDesignation(
                employeeData.getDesignation()
        );

        // BANK DETAILS

        existingEmployee.setAccountNo(
                employeeData.getAccountNo()
        );

        existingEmployee.setBankName(
                employeeData.getBankName()
        );

        existingEmployee.setBranchName(
                employeeData.getBranchName()
        );

        existingEmployee.setIfscCode(
                employeeData.getIfscCode()
        );

        // KYC DETAILS
        existingEmployee.setAadharNo(
                employeeData.getAadharNo()
        );

        existingEmployee.setPanNo(
                employeeData.getPanNo()
        );

        // PROFILE PHOTO
        existingEmployee.setProfilePhoto(
                employeeData.getProfilePhoto()
        );


        // PROVIDENT FUND & ESIC
        existingEmployee.setUanNo(
                employeeData.getUanNo()
        );

        existingEmployee.setPfNo(
                employeeData.getPfNo()
        );

        existingEmployee.setEsicNo(
                employeeData.getEsicNo()
        );

        // SAVE UPDATED EMPLOYEE
        return employeeRepository.save(existingEmployee);
    }


    // ============================================================
    // DELETE EMPLOYEE
    // ============================================================
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException(
                    "Employee not found with id: " + id
            );
        }
        employeeRepository.deleteById(id);
    }
}
