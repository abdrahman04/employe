package ma.ensias.employe.configuration;

import ma.ensias.employe.service.EmployeService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeConfig {
    @Bean
    public EmployeService employeBean() {
        return new EmployeService();
    }

    @Bean
    public ModelMapper modelMapperBean() {
        return new ModelMapper();
    }
}
