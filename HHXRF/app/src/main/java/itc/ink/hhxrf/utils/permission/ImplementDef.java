package itc.ink.hhxrf.utils.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;

/**
 * Created by yangwenjiang on 2018/9/13.
 */

public class ImplementDef implements DynamicPermission.OutService {
    private static final String PACKAGE_URL_SCHEME = "package:";

    @Override
    public boolean hasPermission(Activity mActivity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(mActivity.checkSelfPermission(permission)  != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void showMissingPermissionDialog(final Activity mActivity, String msgText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("权限提示");
        builder.setMessage(msgText);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                mActivity.setResult(PackageManager.PERMISSION_DENIED);
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mActivity.getPackageName()));
                mActivity.startActivity(intent);
            }
        });

        builder.show();
    }

    @Override
    public boolean requestPermissions(Activity mActivity, int mRequestCode, String[] mPermissions) {
        String[] deniedPermissions = getDeniedPermissions(mActivity, mPermissions);

        if(deniedPermissions != null && deniedPermissions.length > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(deniedPermissions, mRequestCode);
            }
            return false;
        }else{
            return true;
        }
    }

    /**
     * 返回缺失的权限
     * @param mActivity
     * @param mPermissions
     * @return 返回缺少的权限，null 意味着没有缺少权限
     */
    public static String[] getDeniedPermissions(Activity mActivity, String[] mPermissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            if(mPermissions!=null){
                for(String permission : mPermissions){
                    if(mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                        deniedPermissionList.add(permission);
                    }
                }
            }

            int size = deniedPermissionList.size();
            if(size > 0){
                return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            }
        }
        return null;
    }

}
