package pl.intelligent.finance.main.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
//@ComponentScan(basePackages = {
//		"pl.intelligent.finance.persistence.repository"
//})
public class IntelligentFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentFinanceApplication.class, args);
	}

}
