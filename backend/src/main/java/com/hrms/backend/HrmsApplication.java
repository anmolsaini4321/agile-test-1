package com.hrms.backend;

import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.Role;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.services.superbaseImageStorageService.SuperbaseImageStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class HrmsApplication implements CommandLineRunner {

	public static void main(String[] args)  {
		SpringApplication.run(HrmsApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SuperbaseImageStorageServiceImpl superbaseImageStorageService;

	@Autowired
	private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {
		String[] alterCommands = {
				"ALTER TABLE users ALTER COLUMN gender TYPE VARCHAR(50)",
				"ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(50)",
				"ALTER TABLE tasks ALTER COLUMN priority TYPE VARCHAR(50)",
				"ALTER TABLE tasks ALTER COLUMN status TYPE VARCHAR(50)",
				"ALTER TABLE leave_requests ALTER COLUMN type TYPE VARCHAR(50)",
				"ALTER TABLE leave_requests ALTER COLUMN status TYPE VARCHAR(50)",
				"ALTER TABLE meetings ALTER COLUMN status TYPE VARCHAR(50)",
				"ALTER TABLE meeting_participants ALTER COLUMN status TYPE VARCHAR(50)",
				"ALTER TABLE chats ALTER COLUMN last_message_type TYPE VARCHAR(50)",
				"ALTER TABLE chats ALTER COLUMN last_message_status TYPE VARCHAR(50)",
				"ALTER TABLE chat_messages ALTER COLUMN message_type TYPE VARCHAR(50)",
				"ALTER TABLE chat_messages ALTER COLUMN message_status TYPE VARCHAR(50)"
		};
		for (String cmd : alterCommands) {
			try {
				jdbcTemplate.execute(cmd);
			} catch (Exception e) {
				// Ignore if table/column doesn't exist or already altered
			}
		}

		try {
			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS company_employees (" +
					"company_id VARCHAR(36) NOT NULL REFERENCES companies(id) ON DELETE CASCADE, " +
					"employee_id VARCHAR(255) NOT NULL, " +
					"PRIMARY KEY (company_id, employee_id))");
			
			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS company_waitlist (" +
					"company_id VARCHAR(36) NOT NULL REFERENCES companies(id) ON DELETE CASCADE, " +
					"employee_id VARCHAR(255) NOT NULL, " +
					"PRIMARY KEY (company_id, employee_id))");
			
			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS task_employees (" +
					"task_id VARCHAR(36) NOT NULL REFERENCES tasks(id) ON DELETE CASCADE, " +
					"employee_id VARCHAR(255) NOT NULL, " +
					"PRIMARY KEY (task_id, employee_id))");

			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS meeting_responses (" +
					"meeting_id VARCHAR(36) NOT NULL REFERENCES meetings(id) ON DELETE CASCADE, " +
					"participant VARCHAR(255), " +
					"status VARCHAR(50))");
		} catch (Exception e) {
			// Ignore table creation issues
		}

		boolean adminUser = userRepository.findByEmail("vikashwork321@gmail.com").isPresent();
		if(!adminUser){
			User user = new User();
			user.setName("Vikash");
			user.setEmail("vikashwork321@gmail.com"); //username is email here
			user.setGender("M");
			user.setPassword(passwordEncoder.encode("Vikash"));
			user.setRole(Role.ROLE_ADMIN);
			user.setCreatedAt(java.time.LocalDateTime.now());
			userRepository.save(user);
		}
	}
}
