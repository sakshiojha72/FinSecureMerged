package com.ds.app;

import com.ds.app.entity.*;
import com.ds.app.enums.*;
import com.ds.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

<<<<<<< HEAD
//@Configuration
=======
@Configuration
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
public class FinanceDataLoader {

	@Bean
	CommandLineRunner loadData(
			iAppUserRepository userRepo,
			EmployeeRepository employeeRepo,
			PasswordEncoder passwordEncoder,
			FinanceBankRepository financeBankRepo,
			FinanceInvestmentRepository financeInvestmentRepo,
			EmployeeBankAccountRepository employeeBankRepo,
			EmployeeCardRepository employeeCardRepo,
			EmployeeInvestmentRepository employeeInvestmentRepo,
			SalaryJobRepository salaryJobRepo,
			SalaryRecordRepository salaryRecordRepo) {

		return args -> {
			
			// ── ADMIN ─────────────────────────────────────────────────────────
			if (!userRepo.existsByUsername("admin")) {
				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(UserRole.ADMIN);
				userRepo.save(admin);
				System.out.println(" Admin created — admin / admin123");
			}

			// ── FINANCE ───────────────────────────────────────────────────────
			if (!employeeRepo.existsByUsername("finance")) {
				Employee finance = new Employee();
				finance.setUsername("finance");
				finance.setFirstName("Yatin_finance");
				finance.setLastName("Sharma_finance");
				finance.setPassword(passwordEncoder.encode("finance123"));
				finance.setRole(UserRole.FINANCE);
				finance.setEmail("sharmayatin0882@gmail.com");
				finance.setCurrentSalary(75000.0);
				finance.setEmployeeCode("FIN001");
				finance.setStatus(Status.ACTIVE);
				employeeRepo.save(finance);
				System.out.println(" Finance created — finance / finance123");
			}

			// ── HR ────────────────────────────────────────────────────────────
			if (!employeeRepo.existsByUsername("hr")) {
				Employee hr = new Employee();
				hr.setUsername("hr");
				hr.setPassword(passwordEncoder.encode("hr123"));
				hr.setRole(UserRole.HR);
				hr.setFirstName("Priya");
				hr.setLastName("Sharma");
				hr.setEmail("sharmayatin0882@gmail.com");
				hr.setCurrentSalary(75000.0);
				hr.setEmployeeCode("HR001");
				hr.setStatus(Status.ACTIVE);
				employeeRepo.save(hr);
				System.out.println(" HR created — hr / hr123");
			}

			// ── EMPLOYEE 1 — RAHUL ────────────────────────────────────────────
			Employee rahul;
			if (!employeeRepo.existsByUsername("rahul")) {
				Employee emp1 = new Employee();
				emp1.setUsername("rahul");
				emp1.setPassword(passwordEncoder.encode("rahul123"));
				emp1.setRole(UserRole.EMPLOYEE);
				emp1.setFirstName("Rahul");
				emp1.setEmail("sharmayatin0882@gmail.com");
				emp1.setLastName("Verma");
				emp1.setCurrentSalary(60000.0);
				emp1.setEmployeeCode("EMP001");
				emp1.setStatus(Status.ACTIVE);
				rahul = employeeRepo.save(emp1);
				System.out.println(" Employee 1 created — rahul / rahul123");
			} else {
				rahul = employeeRepo.findByUsername("rahul").orElseThrow(); // ✅ FIXED
			}

			// ── EMPLOYEE 2 — SNEHA ────────────────────────────────────────────
			Employee sneha;
			if (!employeeRepo.existsByUsername("sneha")) {
				Employee emp2 = new Employee();
				emp2.setUsername("sneha");
				emp2.setPassword(passwordEncoder.encode("sneha123"));
				emp2.setRole(UserRole.EMPLOYEE);
				emp2.setFirstName("Sneha");
				emp2.setLastName("Patel");
				emp2.setCurrentSalary(55000.0);
				emp2.setEmail("sharmayatin0882@gmail.com");
				emp2.setEmployeeCode("EMP002");
				emp2.setStatus(Status.ACTIVE);
				sneha = employeeRepo.save(emp2);
				System.out.println(" Employee 2 created — sneha / sneha123");
			} else {
				sneha = employeeRepo.findByUsername("sneha").orElseThrow(); // ✅ FIXED
			}

			// ── EMPLOYEE 3 — INACTIVE ─────────────────────────────────────────
			if (!employeeRepo.existsByUsername("inactive_emp")) {
				Employee emp3 = new Employee();
				emp3.setUsername("inactive_emp");
				emp3.setPassword(passwordEncoder.encode("inactive123"));
				emp3.setRole(UserRole.EMPLOYEE);
				emp3.setFirstName("Inactive");
				emp3.setLastName("User");
				emp3.setCurrentSalary(50000.0);
				emp3.setEmail("sharmayatin0882@gmail.com");
				emp3.setEmployeeCode("EMP003");
				emp3.setStatus(Status.INACTIVE);
				employeeRepo.save(emp3);
				System.out.println(" Inactive employee created — inactive_emp / inactive123");
			}

			// ── EMPLOYEE 4 — ASHISH ───────────────────────────────────────────
			Employee ashish;
			if (!employeeRepo.existsByUsername("ASHISH")) {
				Employee emp4 = new Employee();
				emp4.setUsername("ASHISH");
				emp4.setPassword(passwordEncoder.encode("ASHISH123"));
				emp4.setRole(UserRole.EMPLOYEE);
				emp4.setFirstName("ASHISH");
				emp4.setLastName("BANSAL");
				emp4.setCurrentSalary(50000.0);
				emp4.setEmail("sharmayatin0882@gmail.com");
				emp4.setEmployeeCode("EMP004");
				emp4.setStatus(Status.ACTIVE);
				ashish = employeeRepo.save(emp4);
				System.out.println(" Employee created — ASHISH / ASHISH123");
			} else {
				ashish = employeeRepo.findByUsername("ASHISH").orElseThrow(); // ✅ FIXED
			}

			// ── EMPLOYEE 5 — SAKSHI ───────────────────────────────────────────
			if (!employeeRepo.existsByUsername("Sakshi")) {
				Employee emp5 = new Employee();
				emp5.setUsername("Sakshi");
				emp5.setPassword(passwordEncoder.encode("Sakshi123"));
				emp5.setRole(UserRole.EMPLOYEE);
				emp5.setFirstName("Sakshi");
				emp5.setLastName("Sakshi");
				emp5.setCurrentSalary(50000.0);
				emp5.setEmail("sharmayatin0882@gmail.com");
				emp5.setEmployeeCode("EMP005");
				emp5.setStatus(Status.ACTIVE);
				employeeRepo.save(emp5);
				System.out.println(" Employee created — Sakshi / Sakshi123");
			}

			// ── EMPLOYEE 6 — SHREYA ───────────────────────────────────────────
			if (!employeeRepo.existsByUsername("shreya")) {
				Employee emp6 = new Employee();
				emp6.setUsername("shreya");
				emp6.setPassword(passwordEncoder.encode("shreya123"));
				emp6.setRole(UserRole.EMPLOYEE);
				emp6.setFirstName("shreya");
				emp6.setLastName("singhal");
				emp6.setCurrentSalary(50000.0);
				emp6.setEmail("sharmayatin0882@gmail.com");
				emp6.setEmployeeCode("EMP006");
				emp6.setStatus(Status.ACTIVE);
				employeeRepo.save(emp6);
				System.out.println(" Employee created — shreya / shreya123");
			}

			// ── FINANCE USER (for addedBy references) ─────────────────────────
			Employee financeUser = employeeRepo.findByUsername("finance").orElseThrow(); // ✅ FIXED

			// ── FINANCE BANK ACCOUNTS ─────────────────────────────────────────
			FinanceBankAccount hdfc;
			if (!financeBankRepo.existsBybankCode("HDFC")) {
				hdfc = new FinanceBankAccount();
				hdfc.setBankName("HDFC Bank");
				hdfc.setBankCode("HDFC");
				hdfc.setStatus(BankStatus.BLACKLISTED);
				hdfc.setAddedBy(financeUser.getUserId());
				hdfc = financeBankRepo.save(hdfc);
				System.out.println(" FinanceBankAccount created — HDFC Bank");
			} else {
				hdfc = financeBankRepo.findBybankCode("HDFC").orElseThrow();
			}

			FinanceBankAccount icici;
			if (!financeBankRepo.existsBybankCode("ICICI")) {
				icici = new FinanceBankAccount();
				icici.setBankName("ICICI Bank");
				icici.setBankCode("ICICI");
				icici.setStatus(BankStatus.WHITELISTED);
				icici.setAddedBy(financeUser.getUserId());
				icici = financeBankRepo.save(icici);
				System.out.println(" FinanceBankAccount created — ICICI Bank");
			} else {
				icici = financeBankRepo.findBybankCode("ICICI").orElseThrow();
			}

			FinanceBankAccount sbi;
			if (!financeBankRepo.existsBybankCode("SBI")) {
				sbi = new FinanceBankAccount();
				sbi.setBankName("State Bank of India");
				sbi.setBankCode("SBI");
				sbi.setStatus(BankStatus.WHITELISTED);
				sbi.setAddedBy(financeUser.getUserId());
				sbi = financeBankRepo.save(sbi);
				System.out.println(" FinanceBankAccount created — State Bank of India");
			} else {
				sbi = financeBankRepo.findBybankCode("SBI").orElseThrow();
			}

			// ── FINANCE INVESTMENTS ───────────────────────────────────────────
			FinanceInvestment nifty50;
			if (!financeInvestmentRepo.existsByFundCode("AXIS-N50")) {
				nifty50 = FinanceInvestment.builder()
						.fundName("Axis Nifty 50 Index Fund")
						.fundCode("AXIS-N50")
						.category("Large Cap")
						.status(FundStatus.WHITELISTED)
						.addedBy(financeUser.getUserId())
						.build();
				nifty50 = financeInvestmentRepo.save(nifty50);
				System.out.println(" FinanceInvestment created — Axis Nifty 50 Index Fund");
			} else {
				nifty50 = financeInvestmentRepo.findByFundCode("AXIS-N50").orElseThrow();
			}

			FinanceInvestment mirae;
			if (!financeInvestmentRepo.existsByFundCode("MIR-LC")) {
				mirae = FinanceInvestment.builder()
						.fundName("Mirae Asset Large Cap Fund")
						.fundCode("MIR-LC")
						.category("Large Cap")
						.status(FundStatus.WHITELISTED)
						.addedBy(financeUser.getUserId())
						.build();
				mirae = financeInvestmentRepo.save(mirae);
				System.out.println(" FinanceInvestment created — Mirae Asset Large Cap Fund");
			} else {
				mirae = financeInvestmentRepo.findByFundCode("MIR-LC").orElseThrow();
			}

			FinanceInvestment ppfas;
			if (!financeInvestmentRepo.existsByFundCode("PPFAS-FLEX")) {
				ppfas = FinanceInvestment.builder()
						.fundName("Parag Parikh Flexi Cap Fund")
						.fundCode("PPFAS-FLEX")
						.category("Flexi Cap")
						.status(FundStatus.WHITELISTED)
						.addedBy(financeUser.getUserId())
						.build();
				ppfas = financeInvestmentRepo.save(ppfas);
				System.out.println(" FinanceInvestment created — Parag Parikh Flexi Cap Fund");
			} else {
				ppfas = financeInvestmentRepo.findByFundCode("PPFAS-FLEX").orElseThrow();
			}

			// ── EMPLOYEE BANK ACCOUNTS ────────────────────────────────────────
			if (!employeeBankRepo.existsByEmployee_UserId(rahul.getUserId())) {
				EmployeeBankAccount rahulBank = EmployeeBankAccount.builder()
						.employee(rahul)
						.bank(hdfc)
						.accountNumber("99000001234")
						.ifscCode("HDFC0001234")
						.accountHolderName("Rahul Verma")
						.validationStatus(BankValidationStatus.APPROVED)
						.reviewNote("Pre-seeded; verified.")
						.modifiedToday(0)
						.build();
				employeeBankRepo.save(rahulBank);
				System.out.println(" EmployeeBankAccount created — Rahul → HDFC");
			}

			if (!employeeBankRepo.existsByEmployee_UserId(sneha.getUserId())) {
				EmployeeBankAccount snehaBank = EmployeeBankAccount.builder()
						.employee(sneha)
						.bank(icici)
						.accountNumber("123400005678")
						.ifscCode("ICIC0005678")
						.accountHolderName("Sneha Patel")
						.validationStatus(BankValidationStatus.PENDING)
						.modifiedToday(0)
						.build();
				employeeBankRepo.save(snehaBank);
				System.out.println(" EmployeeBankAccount created — Sneha → ICICI (PENDING)");
			}
			

			if (!employeeBankRepo.existsByEmployee_UserId(ashish.getUserId())) {
				EmployeeBankAccount ashishBank = EmployeeBankAccount.builder()
						.employee(ashish)
						.bank(sbi)
						.accountNumber("678818123091")
						.ifscCode("123220009999")
						.accountHolderName("ASHISH BANSAL")
						.validationStatus(BankValidationStatus.APPROVED)
						.reviewNote("Pre-seeded; verified.")
						.modifiedToday(0)
						.build();
				employeeBankRepo.save(ashishBank);
				System.out.println(" EmployeeBankAccount created — ASHISH → SBI");
			}

			// ── EMPLOYEE CARDS ────────────────────────────────────────────────
			if (!employeeCardRepo.existsByEmployee_UserIdAndCardTypeAndCardStatus(
					rahul.getUserId(), CardType.DEBIT, CardStatus.ACTIVE)) {
				EmployeeCard rahulDebit = EmployeeCard.builder()
						.employee(rahul)
						.cardNumber("982818123409")
						.cardType(CardType.DEBIT)
						.expiryDate(YearMonth.of(2027, 6))
						.cardStatus(CardStatus.ACTIVE)
						.issuedAt(LocalDate.of(2023, 6, 1))
						.build();
				employeeCardRepo.save(rahulDebit);
				System.out.println(" EmployeeCard created — Rahul DEBIT");
			}

			if (!employeeCardRepo.existsByEmployee_UserIdAndCardTypeAndCardStatus(
					rahul.getUserId(), CardType.CREDIT, CardStatus.ACTIVE)) {
				EmployeeCard rahulCredit = EmployeeCard.builder()
						.employee(rahul)
						.cardNumber("82818125678")
						.cardType(CardType.CREDIT)
						.expiryDate(YearMonth.of(2026, 12))
						.cardStatus(CardStatus.ACTIVE)
						.issuedAt(LocalDate.of(2022, 12, 15))
						.build();
				employeeCardRepo.save(rahulCredit);
				System.out.println(" EmployeeCard created — Rahul CREDIT");
			}

			if (!employeeCardRepo.existsByEmployee_UserIdAndCardTypeAndCardStatus(
					sneha.getUserId(), CardType.DEBIT, CardStatus.ACTIVE)) {
				EmployeeCard snehaDebit = EmployeeCard.builder()
						.employee(sneha)
						.cardNumber("90818129999")
						.cardType(CardType.DEBIT)
						.expiryDate(YearMonth.of(2028, 3))
						.cardStatus(CardStatus.ACTIVE)
						.issuedAt(LocalDate.of(2024, 3, 10))
						.build();
				employeeCardRepo.save(snehaDebit);
				System.out.println(" EmployeeCard created — Sneha DEBIT");
			}

			// ── EMPLOYEE INVESTMENTS ──────────────────────────────────────────
			if (!employeeInvestmentRepo.existsByEmployee_UserIdAndMutualFund_MutualFundId(
					rahul.getUserId(), nifty50.getMutualFundId())) {
				EmployeeInvestment rahulMF = EmployeeInvestment.builder()
						.employee(rahul)
						.investmentType(InvestmentType.MUTUAL_FUND)
						.mutualFund(nifty50)
						.declaredAmount(10000.0)
						.complianceStatus(ComplianceStatus.COMPLIANT)
						.reviewNote("Pre-seeded; auto-approved.")
						.reviewedBy(financeUser.getUserId())
						.build();
				employeeInvestmentRepo.save(rahulMF);
				System.out.println(" EmployeeInvestment created — Rahul → Axis Nifty 50 (MUTUAL_FUND)");
			}

			if (!employeeInvestmentRepo.existsByEmployee_UserIdAndSecurityName(
					rahul.getUserId(), "Reliance Industries Ltd")) {
				EmployeeInvestment rahulEquity = EmployeeInvestment.builder()
						.employee(rahul)
						.investmentType(InvestmentType.DIRECT_EQUITY)
						.securityName("Reliance Industries Ltd")
						.declaredAmount(25000.0)
						.complianceStatus(ComplianceStatus.PENDING_REVIEW)
						.build();
				employeeInvestmentRepo.save(rahulEquity);
				System.out.println(" EmployeeInvestment created — Rahul → Reliance (DIRECT_EQUITY, PENDING)");
				
			}

			if (!employeeInvestmentRepo.existsByEmployee_UserIdAndMutualFund_MutualFundId(
					sneha.getUserId(), mirae.getMutualFundId())) {
				EmployeeInvestment snehaMF = EmployeeInvestment.builder()
						.employee(sneha)
						.investmentType(InvestmentType.MUTUAL_FUND)
						.mutualFund(mirae)
						.declaredAmount(5000.0)
						.complianceStatus(ComplianceStatus.PENDING_REVIEW)
						.build();
				employeeInvestmentRepo.save(snehaMF);
				System.out.println(" EmployeeInvestment created — Sneha → Mirae Large Cap (MUTUAL_FUND, PENDING)");
			}

			if (!employeeInvestmentRepo.existsByEmployee_UserIdAndMutualFund_MutualFundId(
					ashish.getUserId(), ppfas.getMutualFundId())) {
				EmployeeInvestment ashishMF = EmployeeInvestment.builder()
						.employee(ashish)
						.investmentType(InvestmentType.MUTUAL_FUND)
						.mutualFund(ppfas)
						.declaredAmount(15000.0)
						.complianceStatus(ComplianceStatus.COMPLIANT)
						.reviewNote("Pre-seeded; auto-approved.")
						.reviewedBy(financeUser.getUserId())
						.build();
				employeeInvestmentRepo.save(ashishMF);
				System.out.println(" EmployeeInvestment created — ASHISH → Parag Parikh Flexi Cap (MUTUAL_FUND)");
			}

			YearMonth feb2025 = YearMonth.of(2025, 2);
			if (!salaryJobRepo.existsByTargetMonth(feb2025)) {

				SalaryJob job1 = new SalaryJob();
				job1.setJobName("Salary Processing — Feb 2025");
				job1.setTargetMonth(feb2025);
				job1.setScheduledDateTime(LocalDateTime.of(2025, 2, 28, 10, 0));
//				job1.setJobStatus(JobStatus.COMPLETED);
//				job1.setTotalEmployees(2);   // only Rahul & Ashish have APPROVED bank accounts
//				job1.setSuccessCount(2);
//				job1.setFailureCount(0);
				job1.setCreatedBy(financeUser.getUserId());
				job1 = salaryJobRepo.save(job1);
				System.out.println("✅ SalaryJob created — Feb 2025 (COMPLETED)");

				// Re-fetch bank accounts (may have just been created above)
				EmployeeBankAccount rahulBank = employeeBankRepo.findByEmployee_UserId(rahul.getUserId()).orElseThrow();
				EmployeeBankAccount ashishBank = employeeBankRepo.findByEmployee_UserId(ashish.getUserId()).orElseThrow();


			}

	
		};
	}
}