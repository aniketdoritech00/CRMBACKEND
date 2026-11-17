package com.doritech.microservice.AuthenticationService.ServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.doritech.microservice.AuthenticationService.Entity.Employee;
import com.doritech.microservice.AuthenticationService.Entity.ParamEntity;
import com.doritech.microservice.AuthenticationService.Repository.EmployeeRepository;
import com.doritech.microservice.AuthenticationService.Repository.ParamRepository;
import com.doritech.microservice.AuthenticationService.Request.EmployeeDTO;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import com.doritech.microservice.AuthenticationService.Service.AuthService;
import com.doritech.microservice.AuthenticationService.Service.JwtService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthServiceImpl implements AuthService {
	private final Map<String, String> otpStore = new HashMap();
	@Autowired
	private EmployeeRepository employeeRepo;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ParamRepository paramRepository;
	private final Random random = new Random();

	public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
		try {
			System.out.println("Login attempt for email: " + request.getUsername());
			Optional<Employee> employeeOpt = this.employeeRepo.findByEmail(request.getUsername());
			if (employeeOpt.isEmpty()) {
				System.out.println("User not found with email: " + request.getUsername());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(ApiResponse.error("Invalid username or password"));
			} else {
				Employee employee = (Employee) employeeOpt.get();
				boolean passwordMatches = this.passwordEncoder.matches(request.getPassword(), employee.getPassword());
				System.out.println(" Password matches: " + passwordMatches);
				if (!passwordMatches) {
					System.out.println("Password mismatch for user: " + request.getUsername());
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(ApiResponse.error("Invalid username or password"));
				} else {
					ParamEntity entity = this.paramRepository.findByCode("Expire_Password");
					int expireDays = Integer.parseInt(entity.getValue1());
					System.out.println("⏳ Password expiry days from config: " + expireDays);
					if (employee.getLastResetPasswordDate() == null) {
						System.out.println("⚠️ No password reset date found for user: " + request.getUsername());
						return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
								.body(ApiResponse.error("Please reset your password to continue."));
					} else {
						LocalDate expiryDate = employee.getLastResetPasswordDate().plusDays((long) expireDays);
						PrintStream var11 = System.out;
						String var12 = request.getUsername();
						var11.println("\ud83d\udcc5 Password expiry date for user " + var12 + ": "
								+ String.valueOf(expiryDate));
						if (LocalDate.now().isAfter(expiryDate)) {
							System.out.println("❌ Password expired for user: " + request.getUsername());
							return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
									.body(ApiResponse.error("Password has expired. Please reset your password."));
						} else {
							String token = this.jwtService.generateTokenWithEmployeeData(employee.getEmail(),
									employee.getRole());
							System.out.println("Generated JWT token for user: " + request.getUsername());
							System.out.println("Token contains: email=" + employee.getEmail());
							LoginResponse response = new LoginResponse();
							response.setId(employee.getId() != null ? employee.getId().toString() : null);
							response.setUsername(employee.getEmail());
							response.setToken(token);
							response.setFullname(employee.getName());
							response.setRole(employee.getRole());
							response.setPhoneNumber(employee.getPhoneNumber());
							response.setPosition(employee.getPosition());
							response.setOfficeBranch(employee.getOfficeBranch());
							return ResponseEntity.ok(ApiResponse.success("Login successful", response));
						}
					}
				}
			}
		} catch (Exception e) {
			PrintStream var10000 = System.err;
			String var10001 = request.getUsername();
			var10000.println(" Login error for user " + var10001 + ": " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error("Something went wrong: " + e.getMessage()));
		}
	}

	public ResponseEntity<ApiResponse<Integer>> sendOTP(String email) throws Throwable {
		try {
			Optional<Employee> employeeOpt = employeeRepo.findByEmail(email);

			if (employeeOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.<Integer>error("Email ID is not present in the database!"));
			}

			int otp = random.nextInt(900000) + 100000;
			String otpString = String.format("%06d", otp);

			otpStore.put(email, otpString);

			String subject = "Your One-Time Password";
			String htmlContent;

			try {
				htmlContent = loadHtmlTemplate("templates/otp.html");
			} catch (IOException e) {
				e.printStackTrace();

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.<Integer>error("Could not load email template."));
			}

			String message = htmlContent.replace("{{OTP}}", otpString);
			sendHtmlEmail(email, subject, message);

			return ResponseEntity.ok(ApiResponse.<Integer>success("OTP sent successfully to " + email, null));

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<Integer>error("An unexpected error occurred: " + e.getMessage()));
		}
	}

	private String loadHtmlTemplate(String templatePath) throws Throwable {
		ClassPathResource resource = new ClassPathResource(templatePath);
		Throwable var3 = null;
		Object var4 = null;

		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			String var10000;
			try {
				var10000 = FileCopyUtils.copyToString(reader);
			} finally {
				if (reader != null) {
					reader.close();
				}

			}

			return var10000;
		} catch (Throwable var11) {
			if (var3 == null) {
				var3 = var11;
			} else if (var3 != var11) {
				var3.addSuppressed(var11);
			}

			throw var3;
		}
	}

	@Async
	public void sendHtmlEmail(String to, String subject, String htmlContent) {
		try {
			MimeMessage message = this.mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);
			this.mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email to " + to, e);
		}
	}

	public ResponseEntity<ApiResponse<Boolean>> verifyOTP(String email, int enteredOtp) {
		String storedOtp = (String) this.otpStore.get(email);
		if (storedOtp == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error("OTP not sent or session expired."));
		} else if (String.valueOf(enteredOtp).equals(storedOtp)) {
			this.otpStore.remove(email);
			return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", true));
		} else {
			return ResponseEntity.badRequest().body(ApiResponse.error("Invalid OTP. Please try again."));
		}
	}

	public ResponseEntity<ApiResponse<Boolean>> updatePasswordByEmail(String email, String newPassword) {
		try {
			Optional<Employee> employeeOpt = this.employeeRepo.findByEmail(email);
			if (employeeOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error("Email ID not found in the database!"));
			} else {
				Employee employee = (Employee) employeeOpt.get();
				String hashedPassword = this.passwordEncoder.encode(newPassword);
				employee.setPassword(hashedPassword);
				this.employeeRepo.save(employee);
				return ResponseEntity.ok(ApiResponse.success("Password updated successfully", true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error("Error updating password: " + e.getMessage()));
		}
	}

	public ResponseEntity<ApiResponse<String>> resetPassword(EmployeeDTO req) {
		Optional<Employee> empOpt = this.employeeRepo.findByEmail(req.getEmail());
		if (empOpt.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.error("User not found!"));
		} else {
			Employee employee = (Employee) empOpt.get();
			employee.setPassword(this.passwordEncoder.encode(req.getPassword()));
			employee.setLastResetPasswordDate(LocalDate.now());
			this.employeeRepo.save(employee);
			return ResponseEntity.ok(ApiResponse.success("Password reset successfully", "UPDATED"));
		}
	}
}