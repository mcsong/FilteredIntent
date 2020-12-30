package net.sjava.filteredintent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by mcsong@gmail.com on 6/30/2016.
 */
public class FilteredIntentFactory {

	private static PackageManager getPackageManager(Context context) {
		return context.getPackageManager();
	}

	/**
	 * Search and return filtered intent from all of activities
	 *
	 * @param context
	 * @param intent
	 * @param filter
	 * @return
	 */
	@Nullable
	public static Intent filter(Context context, Intent intent, String filter) {
		if (ObjectUtil.isAnyEmpty(context, intent)) {
			return null;
		}

		List<ResolveInfo> resInfos = getPackageManager(context).queryIntentActivities(intent, 0);
		if (ObjectUtil.isEmpty(resInfos)) {
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

	@Nullable
	public static ArrayList<Intent> filtersWithout(Context context,
	                                               Intent intent, String... withoutFilters) {
		if (ObjectUtil.isAnyEmpty(context, intent, withoutFilters)) {
			return null;
		}

		List<ResolveInfo> resInfos = getPackageManager(context).queryIntentActivities(intent, 0);
		if (ObjectUtil.isEmpty(resInfos)) {
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
				// type and data
				i.setType(intent.getType()).setData(intent.getData());
				i.setAction(intent.getAction());
				i.setFlags(intent.getFlags());
				i.setClipData(intent.getClipData());
				i.putExtras(intent.getExtras());
				intents.add(i);
			}
		}

		return intents;
	}

}
