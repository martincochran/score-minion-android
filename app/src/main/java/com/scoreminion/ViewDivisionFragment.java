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

import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic for displaying the view of a given division.
 */
public class ViewDivisionFragment extends ListFragment {

  private static final String TAG = ViewDivisionFragment.class.toString();

  /**
   * The fragment argument representing the section number for this
   * fragment that will be stored in the Bundle.
   */
  static final String ARG_SECTION_NUMBER = "section_number";

  private int mCurCheckPosition;
  private List<ScoresMessagesGame> games;

  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static ViewDivisionFragment newInstance(int sectionNumber) {
    ViewDivisionFragment fragment = new ViewDivisionFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    fragment.games = new ArrayList<>();
    fragment.mCurCheckPosition = 0;
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_view_games, container, false);
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
    games.clear();

    // Fire off the query to fetch the games data.
    new ScoresApiFetcherAsyncTask(this).execute();

    setListAdapter(new GameAdapter(getActivity().getApplicationContext(), games));

    if (savedInstanceState != null) {
      // Restore last state for checked position.
      mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
    }
  }

  /**
   * Accessor method for underlying list of ScoresMessagesGame objects
   *
   * @return the games list
   */
  List<ScoresMessagesGame> getGames() {
    return games;
  }
}