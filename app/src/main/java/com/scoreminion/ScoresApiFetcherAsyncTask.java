/*
 * # Copyright 2015 Martin Cochran
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License");
 * # you may not use this file except in compliance with the License.
 * # You may obtain a copy of the License at
 * #
 * #     http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS,
 * # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * # See the License for the specific language governing permissions and
 * # limitations under the License.
 *
 */

package com.scoreminion;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.appspot.omega_bearing_780.scores.Scores;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGame;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates logic related to querying the Scores API.
 */
class ScoresApiFetcherAsyncTask extends AsyncTask<Void, Void, List<ScoresMessagesGame>> {

  private static final String TAG = ScoresApiFetcherAsyncTask.class.toString();

  private Scores scoresApi;

  // TODO: use a lighter interface to pass information back to the fragment. This class needs
  // the fragment ID to make the query correctly, the list of games to populate, and a way to
  // notify the ListViewAdapater that the data has changed.
  private ViewDivisionFragment fragment;

  ScoresApiFetcherAsyncTask(ViewDivisionFragment fragment) {
    this.fragment = fragment;
  }

  /**
   * Fetches the data from the API with the correct parameters for this fragment.
   *
   * @param params
   * @return the list of ScoresMessagesGames that match the query this fragment is exposing
   */
  @Override
  protected List<ScoresMessagesGame> doInBackground(Void... params) {
    if(scoresApi == null) {
      Scores.Builder builder = new Scores.Builder(AndroidHttp.newCompatibleTransport(),
          new AndroidJsonFactory(), null)
          .setApplicationName("ScoreMinion_Android");
      scoresApi = builder.build();
    }

    int fragmentId = fragment.getArguments().getInt(ViewDivisionFragment.ARG_SECTION_NUMBER);

    Log.d(TAG, "Making query for fragment " + fragmentId);

    try {
      List<ScoresMessagesGame> games = new ArrayList<>();
      String division = getDivisionFromFragmentId(fragmentId);
      String ageBracket = getAgeBracketFromFragmentId(fragmentId);
      String league = getLeagueFromFragmentId(fragmentId);
      Scores.GetGames request = scoresApi.getGames()
          .setDivision(division)
          .setAgeBracket(ageBracket)
          .setLeague(league);
      Log.d(TAG, "Request: " + request);
      List<ScoresMessagesGame> apiGames = request.execute().getGames();
      if (apiGames == null) {
        return games;
      }
      return apiGames;
    } catch (IOException e) {
      Log.e(TAG, "Caught exception", e);
      List<ScoresMessagesGame> games = new ArrayList<>();
      return games;
    }
  }

  /**
   * Updates the underlying list and notifies the adaptor that the contents have changed.
   *
   * @param result the result of the API call to retrieve the games
   */
  @Override
  protected void onPostExecute(List<ScoresMessagesGame> result) {
    Log.d(TAG, "post execute: " + result);
    if (fragment.getGames() == null) {
      return;
    }
    fragment.getGames().clear();
    fragment.getGames().addAll(result);
    if (fragment.getListAdapter() == null) {
      return;
    }
    ArrayAdapter arrayAdapter = (ArrayAdapter) fragment.getListAdapter();
    if (arrayAdapter != null) {
      arrayAdapter.notifyDataSetChanged();
    }
  }

  /**
   * Returns the division (OPEN, WOMENS, MIXED, etc) displayed given the value of the stored
   * ARG_SECTION_NUMBER for that segment. This must be kept in sync with the enum values in
   * ScoresMessagesGame in the API-generated code.
   *
   * @param fragmentId the ID passed in as the ARG_SECTION_NUMBER parameter in the Bundle with
   *                   the fragment constructor
   * @return the division associated with this fragment ID
   */
  private String getDivisionFromFragmentId(int fragmentId) {
    switch (fragmentId) {
      case 2:
      case 7:
        return "WOMENS";
      case 3:
        return "MIXED";
      default:
        return "OPEN";
    }
  }

  /**
   * Returns the age bracket (COLLEGE, MASTERS, etc) displayed given the value of the stored
   * ARG_SECTION_NUMBER for that segment. This must be kept in sync with the enum values in
   * ScoresMessagesGame in the API-generated code.
   *
   * @param fragmentId the ID passed in as the ARG_SECTION_NUMBER parameter in the Bundle with
   *                   the fragment constructor
   * @return the age bracket associated with this fragment ID
   */
  private String getAgeBracketFromFragmentId(int fragmentId) {
    switch (fragmentId) {
      case 6:
      case 7:
        return "COLLEGE";
      default:
        return "NO_RESTRICTION";
    }
  }

  /**
   * Returns the league (USAU, AUDL, MLU, etc) displayed given the value of the stored
   * ARG_SECTION_NUMBER for that segment. This must be kept in sync with the enum values in
   * ScoresMessagesGame in the API-generated code.
   *
   * @param fragmentId the ID passed in as the ARG_SECTION_NUMBER parameter in the Bundle with
   *                   the fragment constructor
   * @return the league associated with this fragment ID
   */
  private String getLeagueFromFragmentId(int fragmentId) {
    switch (fragmentId) {
      case 4:
        return "AUDL";
      case 5:
        return "MLU";
      default:
        return "USAU";
    }
  }
}
