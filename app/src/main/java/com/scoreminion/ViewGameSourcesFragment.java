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

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGameSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martincochran on 8/18/15.
 */
public class ViewGameSourcesFragment extends ListFragment {

  private static final String TAG = ViewGameSourcesFragment.class.toString();

  /**
   * The fragment argument representing the section number for this
   * fragment that will be stored in the Bundle.
   */
  static final String ARG_GAME_ID = "game_id_str";

  private List<ScoresMessagesGameSource> gameSources;

  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static ViewGameSourcesFragment newInstance(String gameId) {
    ViewGameSourcesFragment fragment = new ViewGameSourcesFragment();
    Bundle args = new Bundle();
    args.putString(ARG_GAME_ID, gameId);
    fragment.setArguments(args);
    fragment.gameSources = new ArrayList<>();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_view_game_sources2, container, false);
    return rootView;
  }

  /**
   * Kicks off the query to the API and restores saved state.
   *
   * @param savedInstanceState saved state
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    gameSources = new ArrayList<>();
    gameSources.clear();

    // Fire off the query to fetch the games data.
    new GameSourcesApiFetcherAsyncTask(this).execute();

    setListAdapter(new GameSourcesAdapter(getActivity().getApplicationContext(), gameSources));
  }

  /**
   * Accessor method for underlying list of ScoresMessagesGame objects
   *
   * @return the games list
   */
  List<ScoresMessagesGameSource> getGameSources() {
    return gameSources;
  }
}
