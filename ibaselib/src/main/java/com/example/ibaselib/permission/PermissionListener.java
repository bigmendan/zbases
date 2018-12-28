package com.example.ibaselib.permission;

import java.util.List;

/**
 * Created by EDZ on 2018/7/3.
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);

    void onShouldShowRationale(List<String> deniedPermission);
}
