package org.jacobvv.androidsamples.permission.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.jacobvv.androidsamples.R;
import org.jacobvv.permission.annotation.PermissionRequest;

import java.util.List;

/**
 * @author jacob
 * @date 19-4-27
 */
public class PermissionSampleUtils {

    public static <T extends Context> AlertDialog getRationaleDialog(
            final T target, final PermissionRequest<T> request, List<String> permissions, String title) {
        StringBuilder msgBuilder = new StringBuilder(target.getString(R.string.permission_rationale_msg));
        for (String permission : permissions) {
            msgBuilder.append("\n").append(permission);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(target);
        builder.setMessage(msgBuilder.toString())
                .setPositiveButton(R.string.permission_rationale_proceed, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.proceed(target);
                    }
                })
                .setNegativeButton(R.string.permission_rationale_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.cancel(target);
                    }
                });
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        return builder.create();
    }
}
