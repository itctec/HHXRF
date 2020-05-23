package itc.ink.hhxrf.utils.permission;

import android.app.Activity;

/**
 * Created by yangwenjiang on 2018/9/13.
 */

public class DynamicPermission {
    public static final int IMPLEMENT_DEF = 0X01;

    public OutService outService = null;

    public DynamicPermission() {
        setServiceInterfaceImplement(IMPLEMENT_DEF);
    }

    public DynamicPermission(int mImplementCode) {
        setServiceInterfaceImplement(mImplementCode);
    }

    public void setServiceInterfaceImplement(int mImplementCode) {
        switch (mImplementCode) {
            case IMPLEMENT_DEF:
                outService = new ImplementDef();
                break;
            default:
                outService = new ImplementDef();
        }
    }

    public interface OutService {
        /**
         * 检查是否有权限
         */
        boolean hasPermission(Activity mActivity, String permission);

        /**
         * 单个权限需求中调用，弹出权限提示对话框
         */
        void showMissingPermissionDialog(Activity mActivity, String msgText);

        /**
         * 动态申请权限
         */
        boolean requestPermissions(Activity mActivity, int mRequestCode, String[] mPermissions);

    }


}
