package pl.intelligent.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class IntelligentFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentFinanceApplication.class, args);
	}

}
