package com.shulventures.solarservicesbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // PERSONAL DETAILS

    @Column(length = 10)
    private String title;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    private LocalDate dob;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String relationship;


    /*
     * Frontend uses:
     *
     * emergencyContact
     *
     * Senior schema uses:
     *
     * alt_phone
     */

    @Column(name = "alt_phone", length = 20)
    private String emergencyContact;

    @Column(name = "mother_name", length = 100)
    private String motherName;

    @Column(name = "marital_status", length = 20)
    private String maritalStatus;



    // ADDRESS DETAILS
    @Column(name = "current_address", columnDefinition = "TEXT")
    private String currentAddress;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;



    // EDUCATION DETAILS
    @Column(length = 100)
    private String tenth;

    @Column(length = 100)
    private String twelfth;

    @Column(length = 100)
    private String graduation;

    @Column(name = "post_graduation", length = 100)
    private String postGraduation;



    // EXPERIENCE DETAILS
    @Column(name = "experience_type", length = 20)
    private String experienceType;

    @Column(name = "previous_experience", length = 100)
    private String previousExperience;


    /*
     * Frontend allows values such as:
     *
     * 0
     * 0.5
     * 1.5
     * 2.0
     *
     * Therefore BigDecimal is more appropriate than Integer.
     */

    @Column(name = "work_experience_years", precision = 4, scale = 1)
    private BigDecimal workExperienceYears;

    @Column(name = "previous_company_name", length = 100)
    private String previousCompanyName;

    @Column(name = "previous_designation", length = 100)
    private String previousDesignation;

    @Column(name = "previous_salary", precision = 15, scale = 2)
    private BigDecimal previousSalary;



    // EMPLOYEE DETAILS
    @Column(name = "employee_type", length = 20)
    private String employeeType;

    @Column(length = 50)
    private String department;


    /*
     * Java cannot use "package" as a variable name.
     *
     * Therefore:
     *
     * Java field     -> packageAmount
     * Database column -> package
     * JSON property    -> package
     */

    @Column(name = "package", precision = 15, scale = 2)
    private BigDecimal packageAmount;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(length = 100)
    private String designation;



    // BANK DETAILS
    @Column(name = "account_no", length = 50)
    private String accountNo;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;



    // KYC DETAILS
    @Column(name = "aadhar_no", length = 20)
    private String aadharNo;

    @Column(name = "pan_no", length = 20)
    private String panNo;


    // PROFILE PHOTO
    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;


    // PROVIDENT FUND & ESIC
    @Column(name = "uan_no", length = 50)
    private String uanNo;

    @Column(name = "pf_no", length = 50)
    private String pfNo;

    @Column(name = "esic_no", length = 50)
    private String esicNo;



    // TIMESTAMPS
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // ============================================================
    // SALARY RELATIONSHIP
    // ============================================================

    /*
     * One Employee can have many Salary records.
     *
     * Example:
     *
     * Employee 1
     *      |
     *      ├── Salary August 2026
     *      ├── Salary September 2026
     *      ├── Salary October 2026
     *      └── ...
     *
     * Salary owns the relationship through employee_id.
     */

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    @JsonIgnore
    private List<Salary> salaries = new ArrayList<>();



    // PRE PERSIST
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }


    // PRE UPDATE
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getTenth() {
        return tenth;
    }

    public void setTenth(String tenth) {
        this.tenth = tenth;
    }

    public String getTwelfth() {
        return twelfth;
    }

    public void setTwelfth(String twelfth) {
        this.twelfth = twelfth;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getPostGraduation() {
        return postGraduation;
    }

    public void setPostGraduation(String postGraduation) {
        this.postGraduation = postGraduation;
    }

    public String getExperienceType() {
        return experienceType;
    }

    public void setExperienceType(String experienceType) {
        this.experienceType = experienceType;
    }

    public String getPreviousExperience() {
        return previousExperience;
    }

    public void setPreviousExperience(String previousExperience) {
        this.previousExperience = previousExperience;
    }

    public BigDecimal getWorkExperienceYears() {
        return workExperienceYears;
    }

    public void setWorkExperienceYears(BigDecimal workExperienceYears) {
        this.workExperienceYears = workExperienceYears;
    }

    public String getPreviousCompanyName() {
        return previousCompanyName;
    }

    public void setPreviousCompanyName(String previousCompanyName) {
        this.previousCompanyName = previousCompanyName;
    }

    public String getPreviousDesignation() {
        return previousDesignation;
    }

    public void setPreviousDesignation(String previousDesignation) {
        this.previousDesignation = previousDesignation;
    }

    public BigDecimal getPreviousSalary() {
        return previousSalary;
    }

    public void setPreviousSalary(BigDecimal previousSalary) {
        this.previousSalary = previousSalary;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(BigDecimal packageAmount) {
        this.packageAmount = packageAmount;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUanNo() {
        return uanNo;
    }

    public void setUanNo(String uanNo) {
        this.uanNo = uanNo;
    }

    public String getPfNo() {
        return pfNo;
    }

    public void setPfNo(String pfNo) {
        this.pfNo = pfNo;
    }

    public String getEsicNo() {
        return esicNo;
    }

    public void setEsicNo(String esicNo) {
        this.esicNo = esicNo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
    }
}
