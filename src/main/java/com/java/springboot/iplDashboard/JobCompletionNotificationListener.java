package com.java.springboot.iplDashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

public class JobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	  private final JdbcTemplate jdbcTemplate;

	  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
	    this.jdbcTemplate = jdbcTemplate;
	  }

	  @Override
	  public void afterJob(JobExecution jobExecution) {
	    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
	      log.info("!!! JOB FINISHED! Time to verify the results");

	      jdbcTemplate
	          .query("SELECT team1, team2,date FROM match", (rs, row)-> "Team1"+rs.getString(1) + "Team2" + rs.getString(2) + "Date "+rs.getString(3))
	          .forEach(str -> log.info(str));
	    }
	  }
}
