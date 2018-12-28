package com.example.ibaselib.permission;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by EDZ on 2018/7/3.
 * 申请权限 工具类  ，需要在清单文件中先填写相关权限
 * <p>
 * 使用方法：
 * new PermissionUtil(this).requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE},
 * new  PermissionListener(){
 *
 *              @Override public void onGranted() {
 *                  //所有权限都已经授权
 * <p>
 *                  }
 *              @Override public void onDenied(List<String> deniedPermission) {
 *              //Toast第一个被拒绝的权限
 * <p>
 *              }
 *              @Override public void onShouldShowRationale(List<String> deniedPermission) {
 *              //Toast第一个勾选不在提示的权限
 * <p>
 *              }
 * })
 */

public class PermissionUtil {
    private static final String TAG = PermissionUtil.class.getSimpleName();

    private PermissionFragment fragment;

    public PermissionUtil(@NonNull FragmentManager fm) {
        fragment = getRxPermissionsFragment(fm);
    }

    private PermissionFragment getRxPermissionsFragment(FragmentManager fm) {
        PermissionFragment fragment = (PermissionFragment) fm.findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new PermissionFragment();
            fm.beginTransaction()
                    .add(fragment, TAG)
                    .commit();
            fm.executePendingTransactions();
        }

        return fragment;
    }

    /**
     * 外部使用 申请权限
     *
     * @param permissions 申请授权的权限
     * @param listener    授权回调的监听
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        fragment.setListener(listener);
        fragment.requestPermissions(permissions);

    }

}
