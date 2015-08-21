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
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGameSource;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martincochran on 8/18/15.
 */
public class GameSourcesApiFetcherAsyncTask extends
    AsyncTask<Void, Void, List<ScoresMessagesGameSource>> {

  private static final String TAG = GameSourcesApiFetcherAsyncTask.class.toString();

  private Scores scoresApi;

  // TODO: use a lighter interface to pass information back to the fragment. This class needs
  // the fragment ID to make the query correctly, the list of games to populate, and a way to
  // notify the ListViewAdapater that the data has changed.
  private ViewGameSourcesFragment fragment;

  GameSourcesApiFetcherAsyncTask(ViewGameSourcesFragment fragment) {
    this.fragment = fragment;
  }

  /**
   * Fetches the data from the API with the correct parameters for this fragment.
   *
   * @param params
   * @return the list of ScoresMessagesGames that match the query this fragment is exposing
   */
  @Override
  protected List<ScoresMessagesGameSource> doInBackground(Void... params) {
    if(scoresApi == null) {
      Scores.Builder builder = new Scores.Builder(AndroidHttp.newCompatibleTransport(),
          new AndroidJsonFactory(), null)
          .setApplicationName("ScoreMinion_Android");
      scoresApi = builder.build();
    }

    String gameId = fragment.getActivity().getIntent().getStringExtra(ViewGameSourcesFragment.ARG_GAME_ID);
    //String gameId = fragment.getArguments().getString(ViewGameSourcesFragment.ARG_GAME_ID);

    Log.d(TAG, "Making query for game " + gameId);

    try {
      List<ScoresMessagesGameSource> sources = new ArrayList<>();
      Scores.GetGameInfo request = scoresApi.getGameInfo()
          .setGameIdStr(gameId);
      Log.d(TAG, "Request: " + request);
      List<ScoresMessagesGameSource> apiSources = request.execute().getTwitterSources();
      if (apiSources == null) {
        return sources;
      }
      return apiSources;
    } catch (IOException e) {
      Log.e(TAG, "Caught exception", e);
      List<ScoresMessagesGameSource> sources = new ArrayList<>();
      return sources;
    }
  }

  /**
   * Updates the underlying list and notifies the adaptor that the contents have changed.
   *
   * @param result the result of the API call to retrieve the games
   */
  @Override
  protected void onPostExecute(List<ScoresMessagesGameSource> result) {
    Log.d(TAG, "post execute: " + result);
    if (fragment.getGameSources() == null) {
      Log.d(TAG, "Game sources is null");
      return;
    }
    fragment.getGameSources().clear();
    fragment.getGameSources().addAll(result);
    if (fragment.getListAdapter() == null) {
      Log.d(TAG, "List adapter is null");
      return;
    }
    ArrayAdapter arrayAdapter = (ArrayAdapter) fragment.getListAdapter();
    if (arrayAdapter != null) {
      arrayAdapter.notifyDataSetChanged();
    }
  }
}
