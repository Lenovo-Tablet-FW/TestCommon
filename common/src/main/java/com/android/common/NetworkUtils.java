/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import org.newstand.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class NetworkUtils {
    /**
     * Check if we have a network connection.
     */
    public static boolean hasWifiConnection(final Context context) {

        boolean state = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo info = connectivityManager.getNetworkInfo(network);
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    state = info.isConnectedOrConnecting();
                }
            }
        } else {
        /* Wi-Fi connection */
            NetworkInfo wifiNetwork = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null) {
                state = wifiNetwork.isConnectedOrConnecting();
            }
        /* Other networks */
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                state = activeNetwork.isConnectedOrConnecting();
            }
        }
        return state;
    }

    public static boolean hasMobileConnection(final Context context) {

        boolean state = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo info = connectivityManager.getNetworkInfo(network);
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    state = info.isConnectedOrConnecting();
                }
            }
        } else {
        /* Mobile data connection */
            NetworkInfo mobileNetwork = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null) {
                state = mobileNetwork.isConnectedOrConnecting();
            }
        }
        return state;
    }

    public static boolean ping(String addr) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("ping -c 2 -w 20 " + addr);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            Logger.i("Ping res: %s", buffer.toString());
            return status == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
