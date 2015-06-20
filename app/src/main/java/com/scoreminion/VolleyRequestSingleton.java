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
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class to serialize network requests through Volley using a cache for image requests.
 */
public class VolleyRequestSingleton {
  private static VolleyRequestSingleton instance;
  private static Context context;
  private static final int CACHE_SIZE = 20;
  private volatile RequestQueue requestQueue;
  private ImageLoader imageLoader;


  /**
   * Initializes singleton image loader and cache.
   *
   * @param context
   */
  private VolleyRequestSingleton(Context context) {
    VolleyRequestSingleton.context = context;
    requestQueue = getRequestQueue();

    imageLoader = new ImageLoader(requestQueue,
        new ImageLoader.ImageCache() {
          private final LruCache<String, Bitmap> cache = new LruCache<>(CACHE_SIZE);

          @Override
          public Bitmap getBitmap(String url) {
            return cache.get(url);
          }

          @Override
          public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
          }
        });
  }

  /**
   * Returns the singleton instance of the request queue.
   *
   * @param context
   * @return the singleton instance.
   */
  public static synchronized VolleyRequestSingleton getInstance(Context context) {
    if (instance == null) {
      instance = new VolleyRequestSingleton(context);
    }
    return instance;
  }

  /**
   * Returns the underlying Volley request queue
   * @return the underlying Volley request queue
   */
  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    return requestQueue;
  }

  /**
   * Adds the given request to the underlying request queue.
   *
   * @param req the network request to enqueue
   * @param <T> the result of adding the request to the underlying queue
   */
  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }
}
