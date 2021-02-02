package com.importer.configuration;

import com.importer.component.custom.InputContentMatcher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Bean
	Step importStep() {

		ArrayList<String> result = new ArrayList<>();

		return stepBuilderFactory.get("importStep")//
			    .<String, String>chunk(100000) //
				.reader(itemReader(null)) //
				.writer(i -> determineFileContent(i)).build();

	}

	public Collection determineFileContent(List list) {

		Map<Long, List<String>> result = InputContentMatcher.findMatches(list);

		List<List<String>> anagrams = result.entrySet().stream().filter(entrySet -> entrySet.
				getValue().size()>1).map(x -> x.getValue()).collect(Collectors.toList());

		for(List<String> outputLine:anagrams) {

			outputLine.stream().sorted(Comparator.naturalOrder()).forEach(x -> System.out.print(x+" "));
			System.out.print("\n");

		}

		return list;
	}

	@Bean
	Job sampleJob() {
		Job job = jobBuilderFactory.get("fileImportJob") //
				.incrementer(new RunIdIncrementer()) //
				.start(importStep()) //
				.build();
		return job;
	}

	@Bean
	@StepScope
	FlatFileItemReader<String> itemReader(@Value("#{jobParameters[file_path]}") String filePath) {
		FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
		final FileSystemResource fileResource = new FileSystemResource(filePath);
		reader.setResource(fileResource);
		reader.setLineMapper(new PassThroughLineMapper());
		return reader;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

		//taskExecutor.setConcurrencyLimit(4);
		return taskExecutor;
	}

}
