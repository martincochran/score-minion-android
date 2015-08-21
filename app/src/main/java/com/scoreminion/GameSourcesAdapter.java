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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGame;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesGameSource;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesTeam;
import com.appspot.omega_bearing_780.scores.model.ScoresMessagesTwitterAccount;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by martincochran on 8/16/15.
 */
public class GameSourcesAdapter extends ArrayAdapter<ScoresMessagesGameSource> {

  private static final String TAG = GameSourcesAdapter.class.toString();

  // Format string for parsing date strings like the following:
  // "Sun Jun 28 08:17:54 2015"
  private static final String DATE_FORMAT = "dd HH:mm:ss yyyy";

  // Offset to skip the day of week and month shorthand names.
  private static final int DATE_PARSE_POSITION_OFFSET = 8;
  // View lookup cache
  private static class ViewHolder {
    // This displays the twitter image for the source.
    ImageView profileImage;

    // Name of the account.
    TextView accountName;

    // Tweet metadata.
    TextView tweetText;
    TextView date;
  }

  public GameSourcesAdapter(Context context, List<ScoresMessagesGameSource> games) {
    super(context, R.layout.list_item_game_source, games);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ScoresMessagesGameSource gameSource = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    ViewHolder viewHolder; // view lookup cache stored in tag
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.list_item_game_source, parent, false);
      viewHolder.accountName = (TextView) convertView.findViewById(R.id.tvAccountNameGameSource);
      viewHolder.tweetText = (TextView) convertView.findViewById(R.id.tvTweetTextGameSource);
      viewHolder.date = (TextView) convertView.findViewById(R.id.tvDateGameSource);
      viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.ivProfileGameSource);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    if (gameSource != null && gameSource.getTwitterAccount() != null) {
      ScoresMessagesTwitterAccount twitterAccount = gameSource.getTwitterAccount();
      if (twitterAccount.getScreenName() != null) {
        viewHolder.accountName.setText(twitterAccount.getScreenName());
      }
      viewHolder.tweetText.setText(gameSource.getTweetText());
      viewHolder.date.setText(convertDateString(gameSource.getUpdateTimeUtcStr()));
      setImageForTeam(twitterAccount, viewHolder.profileImage);
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
   * Fetch the image and populate the view for a given team. If no image URL exists set an error
   * image.
   *
   * @param account Twitter account information
   * @param view view to populate with the team image
   */
  private void setImageForTeam(ScoresMessagesTwitterAccount account, final ImageView view) {
    String url = account.getProfileImageUrlHttps();
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
