package net.sjava.filteredintent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcsong@gmail.com on 6/30/2016.
 */
public class FilteredIntentFactory {

  /**
   * Search and return filtered intent from all of activities
   *
   * @param context
   * @param intent
   * @param filter
   * @return
   */
  public static Intent filter(Context context, Intent intent, String filter) {
    if (context == null || intent == null) {
      return null;
    }

    List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);
    if (resInfos == null || resInfos.isEmpty()) {
      return null;
    }

    Intent resultIntent = (Intent) intent.clone();

    ActivityInfo activityInfo;
    for (ResolveInfo info : resInfos) {
      activityInfo = info.activityInfo;
      if (activityInfo.packageName.toLowerCase().contains(filter)
          || activityInfo.name.toLowerCase().contains(filter)) {
        resultIntent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        return resultIntent;
      }
    }

    return null;
  }

  public static ArrayList<Intent> filtersWithout(Context context,
                                                 Intent intent, String... withoutFilters) {
    if (context == null || intent == null || withoutFilters == null) {
      return null;
    }

    List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);
    if (resInfos == null || resInfos.isEmpty()) {
      return null;
    }

    ArrayList<Intent> intents = new ArrayList<>();

    ActivityInfo activityInfo;
    for (ResolveInfo info : resInfos) {
      activityInfo = info.activityInfo;

      String appName = activityInfo.name.toLowerCase();
      String packageName = activityInfo.packageName.toLowerCase();
      boolean isRemoved = false;

      for (String withoutFilter : withoutFilters) {
        if (appName.contains(withoutFilter) || packageName.contains(withoutFilter)) {
          isRemoved = true;
        }
      }

      if (!isRemoved) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        intents.add(i);
      }
    }

    return intents;
  }

}
