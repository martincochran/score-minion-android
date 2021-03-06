/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-07-16 18:28:29 UTC)
 * on 2015-08-02 at 18:26:23 UTC 
 * Modify at your own risk.
 */

package com.appspot.omega_bearing_780.scores.model;

/**
 * Information to represent a game.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the scores. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ScoresMessagesGame extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("age_bracket")
  private java.lang.String ageBracket;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String division;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("game_status")
  private java.lang.String gameStatus;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("id_str")
  private java.lang.String idStr;

  /**
   * Source of latest model update to game.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("last_update_source")
  private ScoresMessagesGameSource lastUpdateSource;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String league;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.util.List<java.lang.Long> scores;

  /**
   * Message to identify a team. At least one field must be present.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<ScoresMessagesTeam> teams;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("tournament_id_str")
  private java.lang.String tournamentIdStr;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("tournament_name")
  private java.lang.String tournamentName;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAgeBracket() {
    return ageBracket;
  }

  /**
   * @param ageBracket ageBracket or {@code null} for none
   */
  public ScoresMessagesGame setAgeBracket(java.lang.String ageBracket) {
    this.ageBracket = ageBracket;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDivision() {
    return division;
  }

  /**
   * @param division division or {@code null} for none
   */
  public ScoresMessagesGame setDivision(java.lang.String division) {
    this.division = division;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getGameStatus() {
    return gameStatus;
  }

  /**
   * @param gameStatus gameStatus or {@code null} for none
   */
  public ScoresMessagesGame setGameStatus(java.lang.String gameStatus) {
    this.gameStatus = gameStatus;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getIdStr() {
    return idStr;
  }

  /**
   * @param idStr idStr or {@code null} for none
   */
  public ScoresMessagesGame setIdStr(java.lang.String idStr) {
    this.idStr = idStr;
    return this;
  }

  /**
   * Source of latest model update to game.
   * @return value or {@code null} for none
   */
  public ScoresMessagesGameSource getLastUpdateSource() {
    return lastUpdateSource;
  }

  /**
   * Source of latest model update to game.
   * @param lastUpdateSource lastUpdateSource or {@code null} for none
   */
  public ScoresMessagesGame setLastUpdateSource(ScoresMessagesGameSource lastUpdateSource) {
    this.lastUpdateSource = lastUpdateSource;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLeague() {
    return league;
  }

  /**
   * @param league league or {@code null} for none
   */
  public ScoresMessagesGame setLeague(java.lang.String league) {
    this.league = league;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * @param name name or {@code null} for none
   */
  public ScoresMessagesGame setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Long> getScores() {
    return scores;
  }

  /**
   * @param scores scores or {@code null} for none
   */
  public ScoresMessagesGame setScores(java.util.List<java.lang.Long> scores) {
    this.scores = scores;
    return this;
  }

  /**
   * Message to identify a team. At least one field must be present.
   * @return value or {@code null} for none
   */
  public java.util.List<ScoresMessagesTeam> getTeams() {
    return teams;
  }

  /**
   * Message to identify a team. At least one field must be present.
   * @param teams teams or {@code null} for none
   */
  public ScoresMessagesGame setTeams(java.util.List<ScoresMessagesTeam> teams) {
    this.teams = teams;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTournamentIdStr() {
    return tournamentIdStr;
  }

  /**
   * @param tournamentIdStr tournamentIdStr or {@code null} for none
   */
  public ScoresMessagesGame setTournamentIdStr(java.lang.String tournamentIdStr) {
    this.tournamentIdStr = tournamentIdStr;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTournamentName() {
    return tournamentName;
  }

  /**
   * @param tournamentName tournamentName or {@code null} for none
   */
  public ScoresMessagesGame setTournamentName(java.lang.String tournamentName) {
    this.tournamentName = tournamentName;
    return this;
  }

  @Override
  public ScoresMessagesGame set(String fieldName, Object value) {
    return (ScoresMessagesGame) super.set(fieldName, value);
  }

  @Override
  public ScoresMessagesGame clone() {
    return (ScoresMessagesGame) super.clone();
  }

}
