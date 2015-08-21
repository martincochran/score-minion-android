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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link GameListFragment} and the item details
 * (if present) is a {@link GameDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link GameListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class GameListActivity extends FragmentActivity
    implements GameListFragment.Callbacks {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game_list);

    if (findViewById(R.id.game_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-large and
      // res/values-sw600dp). If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;

      // In two-pane mode, list items should be given the
      // 'activated' state when touched.
      ((GameListFragment) getSupportFragmentManager()
          .findFragmentById(R.id.game_list))
          .setActivateOnItemClick(true);
    }

    // TODO: If exposing deep links into your app, handle intents here.
  }

  /**
   * Callback method from {@link GameListFragment.Callbacks}
   * indicating that the item with the given ID was selected.
   */
  @Override
  public void onItemSelected(String id) {
    if (mTwoPane) {
      // In two-pane mode, show the detail view in this activity by
      // adding or replacing the detail fragment using a
      // fragment transaction.
      Bundle arguments = new Bundle();
      arguments.putString(GameDetailFragment.ARG_ITEM_ID, id);
      GameDetailFragment fragment = new GameDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.game_detail_container, fragment)
          .commit();

    } else {
      // In single-pane mode, simply start the detail activity
      // for the selected item ID.
      Intent detailIntent = new Intent(this, GameDetailActivity.class);
      detailIntent.putExtra(GameDetailFragment.ARG_ITEM_ID, id);
      startActivity(detailIntent);
    }
  }
}
