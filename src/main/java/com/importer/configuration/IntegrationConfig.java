package com.importer.configuration;

import java.io.File;

import com.importer.component.job.ImporterMessage;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileReadingMessageSource.WatchEventType;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

@Configuration
public class IntegrationConfig {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Value("${import.dir}")
	private String importDir;

	protected DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow importFlow() {

		return IntegrationFlows //
				.from(fileReadingMessageSource(), c -> c.poller(Pollers.fixedDelay(5000)))//
				.channel(inputChannel()) //
				.transform(fileRecordToJobReq()) //
				.handle(jobLaunchingMessageHandler()) //
				.handle(jobExecution -> {
					System.out.println(jobExecution.getPayload());
				}) //
				.get();
	}

	@Bean
	public MessageSource<File> fileReadingMessageSource() {
		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setDirectory(new File(importDir));
		source.setFilter(new SimplePatternFileListFilter("*.txt"));
		source.setUseWatchService(true);
		source.setWatchEvents(WatchEventType.CREATE);
		return source;
	}

	@Bean
	ImporterMessage fileRecordToJobReq() {
		ImporterMessage transformer = new ImporterMessage();
		transformer.setJob(job);
		transformer.setFileParameterName("file_path");
		return transformer;
	}

	@Bean
	JobLaunchingMessageHandler jobLaunchingMessageHandler() {
		JobLaunchingMessageHandler handler = new JobLaunchingMessageHandler(jobLauncher);
		return handler;
	}

}