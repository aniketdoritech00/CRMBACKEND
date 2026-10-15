package com.doritech.microservice.AuthenticationService.ServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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
import com.doritech.microservice.AuthenticationService.Repository.EmployeeRepository;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import com.doritech.microservice.AuthenticationService.Service.AuthService;
import com.doritech.microservice.AuthenticationService.Service.JwtService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthServiceImpl implements AuthService {

	// In-memory OTP storage map
	private final Map<String, String> otpStore = new HashMap<>();

	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	private final Random random = new Random();

	@Override
	public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
		try {
			System.out.println("Login attempt for email: " + request.getUsername());

			Optional<Employee> employeeOpt = employeeRepo.findByEmail(request.getUsername());

			if (employeeOpt.isEmpty()) {
				System.out.println("User not found with email: " + request.getUsername());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(ApiResponse.error("Invalid username or password"));
			}

			Employee employee = employeeOpt.get();
			boolean passwordMatches = passwordEncoder.matches(request.getPassword(), employee.getPassword());
			System.out.println(" Password matches: " + passwordMatches);

			if (!passwordMatches) {
				System.out.println("Password mismatch for user: " + request.getUsername());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(ApiResponse.error("Invalid username or password"));
			}

			String token = jwtService.generateTokenWithEmployeeData(employee.getEmail(), employee.getRole());

			System.out.println("Generated JWT token for user: " + request.getUsername());
			System.out.println("Token contains: email=" + employee.getEmail());

			LoginResponse response = new LoginResponse(employee.getEmail(), token, employee.getName(),
					employee.getRole(), employee.getPhoneNumber(), employee.getPosition());

			return ResponseEntity.ok(ApiResponse.success("Login successful", response));

		} catch (Exception e) {
			System.err.println(" Login error for user " + request.getUsername() + ": " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error("Something went wrong: " + e.getMessage()));
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Integer>> sendOTP(String email) {
		try {
			Optional<Employee> employeeOpt = employeeRepo.findByEmail(email);
			if (employeeOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error("Email ID is not present in the database!"));
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
						.body(ApiResponse.error("Could not load email template."));
			}
			String message = htmlContent.replace("{{OTP}}", otpString);
			sendHtmlEmail(email, subject, message);

			return ResponseEntity.ok(ApiResponse.success("OTP sent successfully to " + email, null));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error("An unexpected error occurred: " + e.getMessage()));
		}
	}

	private String loadHtmlTemplate(String templatePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(templatePath);
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		}
	}

	@Async
	public void sendHtmlEmail(String to, String subject, String htmlContent) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email to " + to, e);
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean>> verifyOTP(String email, int enteredOtp) {
		String storedOtp = otpStore.get(email);
		if (storedOtp == null) {
			return ResponseEntity.badRequest().body(ApiResponse.error("OTP not sent or session expired."));
		}
		if (String.valueOf(enteredOtp).equals(storedOtp)) {
			otpStore.remove(email);
			return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", true));
		} else {
			return ResponseEntity.badRequest().body(ApiResponse.error("Invalid OTP. Please try again."));
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean>> updatePasswordByEmail(String email, String newPassword) {
		try {
			Optional<Employee> employeeOpt = employeeRepo.findByEmail(email);
			if (employeeOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error("Email ID not found in the database!"));
			}
			Employee employee = employeeOpt.get();
			String hashedPassword = passwordEncoder.encode(newPassword);
			employee.setPassword(hashedPassword);
			employeeRepo.save(employee);
			return ResponseEntity.ok(ApiResponse.success("Password updated successfully", true));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error("Error updating password: " + e.getMessage()));
		}
	}
}
