package net.sjava.filteredintent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by mcsong@gmail.com on 6/30/2016.
 */
public class FilteredIntentFactory {

    public static Intent filter(Context context, Intent intent, String filter) {
        if(context == null || intent == null)
            return null;

        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        if(resInfos == null || resInfos.isEmpty())
            return null;

        Intent resultIntent = (Intent)intent.clone();

        ActivityInfo activityInfo;
        for (ResolveInfo info : resInfos) {
            activityInfo = info.activityInfo;
            if (activityInfo.packageName.toLowerCase().contains(filter) || activityInfo.name.toLowerCase().contains(filter) ) {
                //resultIntent.setPackage(activityInfo.packageName);
                resultIntent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                return resultIntent;
            }
        }

        return null;
    }
}
