package ru.croc.ctp.mts;

import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.croc.ctp.jxfw.core.config.XfwCoreConfig;
import ru.croc.ctp.jxfw.jpa.config.XfwJpaConfig;
import ru.croc.ctp.jxfw.security.config.XfwSecurityCoreConfig;
import ru.croc.ctp.jxfw.transfer.config.XfwTransferConfig;
import ru.croc.ctp.jxfw.wc.WebClientLoaderConfig;
import ru.croc.ctp.mts.domain.Project;
import ru.croc.ctp.mts.domain.repo.ProjectRepository;
import ru.croc.ctp.mts.domain.repo.TaskRepository;

/**
 * Class for running main application.
 * 
 * @author Team1
 *
 */
@Configuration
@EnableAutoConfiguration
@Import({ XfwCoreConfig.class, WebClientLoaderConfig.class, XfwJpaConfig.class, XfwSecurityCoreConfig.class,
		XfwTransferConfig.class })
@ComponentScan(basePackages = { "ru.croc.ctp.mts" })
public class Application {

	/**
	 * Main method of running Application.
	 * 
	 * @param args - default input arguments          
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ProjectRepository projectRepository;
	@SuppressWarnings("unused")
	private TaskRepository taskRepository;

	@Bean
	public InitializingBean addProject1() {
		return () -> {
			Project project = new Project();
			project.setId(UUID.randomUUID().toString());
			project.setDescription("description project1");
			project.setTitle("project1");
			project.setCodeProject("pr1");
			project.setTaskCounter(0);
			projectRepository.save(project);

		};
	}

	@Bean
	public InitializingBean addProject2() {
		return () -> {
			Project project = new Project();
			project.setId(UUID.randomUUID().toString());
			project.setDescription("description project2");
			project.setTitle("project2");
			project.setCodeProject("pr2");
			project.setTaskCounter(0);
			projectRepository.save(project);

		};
	}

}