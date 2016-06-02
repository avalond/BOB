package com.ywhyw.bob.compat;

import android.bluetooth.BluetoothAdapter;
import android.net.Uri;
import android.util.Pair;

/**
 * Created by kevin@BizToolShop Inc.
 */
public class A2dpSwitcherUtils {
    /**
     * Parses the Bluetooth device address and name from a URI.
     *
     * @param uri The URI to parse.
     * @return A pair of {@link String}s representing the device address and name.
     */
    public static Pair<String, String> parseUri(Uri uri) {
        final String version = uri.getQueryParameter(Contants.QUERY_VERSION);
        if (version == null) {
            return getAddressVersion0(uri);
        }

        try {
            final int versionCode = Integer.parseInt(version);
            if (versionCode == 1) {
                return getAddressVersion1(uri);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Invalid version.
        return null;
    }

    private static Pair<String, String> getAddressVersion0(Uri uri) {
        final String path = uri.getPath();
        if ((path == null) || (path.length() <= 1)) {
            // Missing address.
            return null;
        }

        final String address = path.substring(1);
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            // Invalid address.
            return null;
        }

        return new Pair<String, String>(address, null);
    }

    private static Pair<String, String> getAddressVersion1(Uri uri) {
        final String address = uri.getQueryParameter(Contants.QUERY_ADDRESS);
        if ((address == null) || !BluetoothAdapter.checkBluetoothAddress(address)) {
            // Invalid or missing address.
            return null;
        }

        final String name = uri.getQueryParameter(Contants.QUERY_NAME);

        return new Pair<String, String>(address, name);
    }
}
