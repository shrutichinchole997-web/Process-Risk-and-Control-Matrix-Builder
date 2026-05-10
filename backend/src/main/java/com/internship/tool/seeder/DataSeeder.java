package com.internship.tool.seeder;

import com.internship.tool.entity.ProcessRiskControl;
import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.repository.ProcessRiskControlRepository;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProcessRiskControlRepository prcRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedProcessRiskControls();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@tool68.com")
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .email("user@tool68.com")
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
    }

    private void seedProcessRiskControls() {
        if (prcRepository.count() == 0) {
            List<ProcessRiskControl> records = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                records.add(ProcessRiskControl.builder()
                        .processName("Process " + i)
                        .riskDescription("Risk description for process " + i + ". Potential financial loss due to system failure.")
                        .controlDescription("Control description for process " + i + ". Daily backups and redundant servers.")
                        .priority(i % 3 == 0 ? "HIGH" : (i % 2 == 0 ? "MEDIUM" : "LOW"))
                        .status(i % 5 == 0 ? "COMPLETED" : "IN_PROGRESS")
                        .build());
            }
            prcRepository.saveAll(records);
        }
    }
}
