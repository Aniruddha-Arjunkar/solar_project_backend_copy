package com.shulventures.solarservicesbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // EMPLOYEE RELATION

    /*
     * Many Salary records can belong to one Employee.
     *
     * Example:
     *
     * Employee ID 1
     *      |
     *      ├── Salary - January 2026
     *      ├── Salary - February 2026
     *      ├── Salary - March 2026
     *      └── Salary - April 2026
     *
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;


    // SALARY PERIOD

    /*
     * Store month in YYYY-MM format.
     *
     * Example:
     * 2026-01
     * 2026-02
     * 2026-03
     */

    @Column(nullable = false)
    private String month;


    // SALARY AMOUNT

    /*
     * Main / Net Salary entered by the user.
     *
     * Example:
     * 50000
     */

    @Column(nullable = false)
    private BigDecimal amount;


    // SALARY COMPONENTS

    /*
     * These components are calculated from the salary amount
     * according to the frontend salary calculation.
     */
    private BigDecimal basic;
    private BigDecimal hra;
    private BigDecimal conveyance;
    private BigDecimal foodAllowance;
    private BigDecimal performanceIncentive;


    // DEDUCTIONS / OTHER AMOUNTS

    private BigDecimal professionTax;
    private BigDecimal advance;
    private BigDecimal reimbursement;


    // PAYMENT DETAILS
    /*
     * Examples:
     *
     * SALARY
     * PER_DAY
     * ADVANCE
     */
    private String paymentType;
    private LocalDate advanceDate;


    // PER DAY SALARY

    private BigDecimal perDayAmount;
    private Integer presentDays;


    // REMARK
    @Column(columnDefinition = "TEXT")
    private String remark;

    public Salary() {
    }

    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBasic() {
        return basic;
    }

    public void setBasic(BigDecimal basic) {
        this.basic = basic;
    }

    public BigDecimal getHra() {
        return hra;
    }

    public void setHra(BigDecimal hra) {
        this.hra = hra;
    }

    public BigDecimal getConveyance() {
        return conveyance;
    }

    public void setConveyance(BigDecimal conveyance) {
        this.conveyance = conveyance;
    }

    public BigDecimal getFoodAllowance() {
        return foodAllowance;
    }

    public void setFoodAllowance(BigDecimal foodAllowance) {
        this.foodAllowance = foodAllowance;
    }

    public BigDecimal getPerformanceIncentive() {
        return performanceIncentive;
    }

    public void setPerformanceIncentive(BigDecimal performanceIncentive) {
        this.performanceIncentive = performanceIncentive;
    }

    public BigDecimal getProfessionTax() {
        return professionTax;
    }

    public void setProfessionTax(BigDecimal professionTax) {
        this.professionTax = professionTax;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(BigDecimal reimbursement) {
        this.reimbursement = reimbursement;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(LocalDate advanceDate) {
        this.advanceDate = advanceDate;
    }

    public BigDecimal getPerDayAmount() {
        return perDayAmount;
    }

    public void setPerDayAmount(BigDecimal perDayAmount) {
        this.perDayAmount = perDayAmount;
    }

    public Integer getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Integer presentDays) {
        this.presentDays = presentDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
