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

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGame;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesTeam;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * View adapter for each ScoresMessagesGame in a list.
 */
public class GameAdapter extends ArrayAdapter<ScoresMessagesGame> {

  // Format string for parsing date strings like the following:
  // "Sun Jun 28 08:17:54 2015"
  private static final String DATE_FORMAT = "dd HH:mm:ss yyyy";

  // Offset to skip the day of week and month shorthand names.
  private static final int DATE_PARSE_POSITION_OFFSET = 8;
  // View lookup cache
  private static class ViewHolder {
    // These display the twitter images.
    ImageView homeProfileImage;
    ImageView awayProfileImage;

    // Names of the teams.
    TextView homeName;
    TextView awayName;

    // Tweet metadata.
    TextView tweetText;
    TextView date;
  }

  public GameAdapter(Context context, List<ScoresMessagesGame> games) {
    super(context, R.layout.list_item_game, games);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ScoresMessagesGame game = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    ViewHolder viewHolder; // view lookup cache stored in tag
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.list_item_game, parent, false);
      viewHolder.homeName = (TextView) convertView.findViewById(R.id.tvHomeTeamName);
      viewHolder.awayName = (TextView) convertView.findViewById(R.id.tvAwayTeamName);
      viewHolder.tweetText = (TextView) convertView.findViewById(R.id.tvTweetText);
      viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
      viewHolder.homeProfileImage = (ImageView) convertView.findViewById(R.id.ivHomeProfile);
      viewHolder.awayProfileImage = (ImageView) convertView.findViewById(R.id.ivAwayProfile);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    if (game != null && game.getTeams() != null) {

      // Populate the views with the data from the API.
      if (game.getTeams().size() > 0) {
        if (game.getTeams().get(0).getTwitterAccount() != null) {
          viewHolder.homeName.setText(game.getTeams().get(0).getTwitterAccount().getScreenName());
        } else {
          viewHolder.homeName.setText(game.getTeams().get(0).getScoreReporterId());
        }
      }
      if (game.getTeams().size() > 1) {
        if (game.getTeams().get(1).getTwitterAccount() != null) {
          viewHolder.awayName.setText(game.getTeams().get(1).getTwitterAccount().getScreenName());
        } else {
          viewHolder.awayName.setText(game.getTeams().get(1).getScoreReporterId());
        }
      }
      setTeamImages(game.getTeams(), viewHolder);
    }
    if (game != null && game.getLastUpdateSource() != null) {
      viewHolder.tweetText.setText(game.getLastUpdateSource().getTweetText());
      viewHolder.date.setText(convertDateString(game.getLastUpdateSource().getUpdateTimeUtcStr()));
    }
    return convertView;
  }

  /**
   * Converts UTC date string to localized date string.
   *
   * @param utcDateStr the date string returned from the Scores API
   * @return the localized date string
   */
  private String convertDateString(String utcDateStr) {
    Calendar rightNow = Calendar.getInstance();
    TimeZone timeZone = rightNow.getTimeZone();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    Date date = dateFormat.parse(utcDateStr, new ParsePosition(DATE_PARSE_POSITION_OFFSET));
    Date adjustedDate = new Date(date.getTime() + timeZone.getOffset(rightNow.getTimeInMillis()));

    return dateFormat.format(adjustedDate);
  }

  /**
   * Populates the images for the teams if the API returned links to profile images.
   *
   * @param teams the list containing ScoresMessagesTeam corresponding to the two teams
   * @param viewHolder viewHolder for the views
   */
  private void setTeamImages(List<ScoresMessagesTeam> teams, ViewHolder viewHolder) {
    final ImageView homeImageView = viewHolder.homeProfileImage;
    final ImageView awayImageView = viewHolder.awayProfileImage;

    if (teams == null) {
      setErrorImage(homeImageView);
      setErrorImage(awayImageView);
    }
    if (teams.size() > 0) {
      setImageForTeam(teams.get(0), homeImageView);
    } else {
      setErrorImage(homeImageView);
    }
    if (teams.size() > 1) {
      setImageForTeam(teams.get(1), awayImageView);
    } else {
      setErrorImage(awayImageView);
    }
  }

  /**
   * Fetch the image and populate the view for a given team. If no image URL exists set an error
   * image.
   *
   * @param team team information
   * @param view view to populate with the team image
   */
  private void setImageForTeam(ScoresMessagesTeam team, final ImageView view) {
    if (team.getTwitterAccount() == null) {
      setErrorImage(view);
      return;
    }
    String url = team.getTwitterAccount().getProfileImageUrlHttps();
    ImageRequest request = new ImageRequest(url,
        new Response.Listener<Bitmap>() {
          @Override
          public void onResponse(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
          }
        }, 0, 0, null,
        new Response.ErrorListener() {
          public void onErrorResponse(VolleyError error) {
            setErrorImage(view);
          }
        });
    VolleyRequestSingleton.getInstance(this.getContext()).addToRequestQueue(request);
  }

  private void setErrorImage(ImageView view) {
    view.setImageResource(R.drawable.image_load_error);
  }
}