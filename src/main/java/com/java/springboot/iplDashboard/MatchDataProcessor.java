package com.java.springboot.iplDashboard;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
	
	private static final Logger LOG = LoggerFactory.getLogger(MatchDataProcessor.class);

	@Override
	public Match process(final MatchInput matchInput) throws Exception {
		Match match = new Match();
		match.setId(Long.parseLong(matchInput.getId()));
		match.setCity(matchInput.getCity());
		match.setDate(LocalDate.parse(matchInput.getDate()));
		match.setPlayerOfMatch(matchInput.getPlayer_of_Match());
		match.setVenue(matchInput.getVenue());

		String team1, team2;
		if(matchInput.getTossDecision().equals("bat")) {
			team1 = matchInput.getTossWinner();
			team2 = matchInput.getTeam1().equals(matchInput.getTossWinner()) ? matchInput.getTeam2() : matchInput.getTeam1();
		} else {
			team2 = matchInput.getTossWinner();
			team1 = matchInput.getTeam1().equals(matchInput.getTossWinner()) ? matchInput.getTeam2() : matchInput.getTeam1();
		}
		match.setTeam1(team1);
		match.setTeam2(team2);
		
		match.setTossWinner(matchInput.getTossWinner());
		match.setMatchWinner(matchInput.getWinningTeam());
		match.setResult(matchInput.getWonBy());
		match.setResultMargin(matchInput.getMargin());
		match.setUmpire1(matchInput.getUmpire1());
		match.setUmpire2(matchInput.getUmpire2());

		return match;
	}

}
