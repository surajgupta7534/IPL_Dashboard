package com.java.springboot.iplDashboard;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private final String[] FIELED_NAMES = new String[] { "ID", "City	", "Date", "Season	", "MatchNumber", "Team1",
			"Team2", "Venue", "TossWinner", "TossDecision", "SuperOver", "WinningTeam", "WonBy	", "Margin	",
			"method	", "Player_of_Match", "Team1Players", "Team2Players", "Umpire1", "Umpire2" };

    @Bean(name = "ItemReader")
    FlatFileItemReader<MatchInput> reader() {
		return new FlatFileItemReaderBuilder<MatchInput>().name("MatchItemReader")
				.resource(new ClassPathResource("match-data.csv")).delimited().names(FIELED_NAMES)
				.targetType(MatchInput.class).build();
	}

    @Bean
    MatchDataProcessor processor() {
		return new MatchDataProcessor();
	}

    @Bean
    JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Match>().sql(
				"INSERT INTO match(id,city,date,player_of_match,venue,team1,team2,toss_winner,toss_decision,match_winner,result,result_margin,umpire1,umpire2) "
						+ " VALUES (:id, :city, :date,:playerOfMatch, :venue, :team1,:team2,"
						+ ":tossWinner,:tossDecision,:matchWinner, :result,:resultMargin," + ":umpire1, + :umpire2,)")
				.dataSource(dataSource).beanMapped().build();
	}


    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
    		FlatFileItemReader<Match> reader, MatchDataProcessor processor, JdbcBatchItemWriter<Match> writer) {
		return new StepBuilder("step1", jobRepository).<MatchInput, Match>chunk(3, transactionManager).reader(reader())
				.processor(processor).writer(writer).build();
	}

    @Bean
    Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
		return new JobBuilder("importUserJob", jobRepository).listener(listener).start(step1).build();
	}
}
