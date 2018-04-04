package net.sjava.filteredintent;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_CHOSEN_COMPONENT;

/**
 * Filtered intent class
 *
 * Created by mcsong@gmail.com on 6/30/2016.
 */
public class FilteredIntent {

    public static FilteredIntent newInstance(Context context, Intent intent) {
        return new FilteredIntent(context, intent);
    }

    private Context mContext;
    private Intent mIntent;

    private FilteredIntent(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
    }

    /**
     * Add category to Intent
     *
     * @param category
     */
    public void addCategory(String category) {
        if(TextUtils.isEmpty(category))
            return;

        mIntent.addCategory(category);
    }

    /**
     * Add flag to Intent
     * @param flag
     */
    public void addFlag(int flag) {
        mIntent.addFlags(flag);
    }

    /**
     * Return intent instance
     * @return
     */
    public Intent getIntent() {
        return mIntent;
    }

    /**
     * Get all of filtered intent list
     *
     * @param filters
     * @return
     */
    public List<Intent> getFilteredIntents(String... filters) {
        List<Intent> filteredIntents = new ArrayList<>();

        if(filters == null || filters.length == 0) {
            return filteredIntents;
        }

        for(String filter : filters) {
            Intent filterIntent = FilteredIntentFactory.filter(mContext, mIntent, filter);
            if(filterIntent != null)
                filteredIntents.add(filterIntent);
        }

        return filteredIntents;
    }

    /**
     * Start intent
     *
     * @param title
     */
    public void startIntent(String title) {
        Intent chooser = Intent.createChooser(mIntent, title);
        mContext.startActivity(chooser);
    }

    /**
     * Start intent
     *
     * @param title
     * @param filters array of the app name or package name
     */
    public void startIntent(String title, String... filters) {
        if(filters == null || filters.length ==0) {
            startIntent(title);
            return;
        }

        List<Intent> filteredIntents = getFilteredIntents(filters);
        if(filteredIntents.size() == 0) {
            startIntent(title);
            return;
        }

        Intent tIntent =filteredIntents.remove(0);
        Intent chooser = Intent.createChooser(tIntent, title);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, filteredIntents.toArray(new Parcelable[filteredIntents.size()]));
        mContext.startActivity(chooser);
    }


	private final String BROADCAST_MESSAGE = "net.sjava.filteredintent.APP_CHOSEN_BROAD_CAST";

    @TargetApi(22)
    public void startIntent(AppChosenListener listener, String title, String... filters) {
        if(ObjectUtil.isNotNull(listener)) {
	        mListener = listener;
        }

    	if(filters == null || filters.length ==0) {
            startIntent(title);
            return;
        }

        List<Intent> filteredIntents = getFilteredIntents(filters);
        if(filteredIntents.size() == 0) {
            startIntent(title);
            return;
        }

	    registerReceiver();

        Intent tIntent = filteredIntents.remove(0);

	    Intent receiver = new Intent(BROADCAST_MESSAGE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent chooser = Intent.createChooser(tIntent, title, pendingIntent.getIntentSender());
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, filteredIntents.toArray(new Parcelable[filteredIntents.size()]));
        mContext.startActivity(chooser);
    }

    private AppChosenListener mListener = null;
	private BroadcastReceiver mReceiver = null;

	private void registerReceiver(){
		if(mReceiver != null) {
			mContext.unregisterReceiver(mReceiver);
		}

		final IntentFilter theFilter = new IntentFilter();
		theFilter.addAction(BROADCAST_MESSAGE);

		this.mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if(intent.getAction().equals(BROADCAST_MESSAGE)){
					unRegisterReceiver();

					Object obj = intent.getExtras().get(EXTRA_CHOSEN_COMPONENT);
					if(obj == null) {
						return;
					}

					try {
						String packageName = ((ComponentName) obj).getPackageName();
						String appName = getAppNameFromPkgName(context, packageName);

						if(mListener != null) {
							mListener.chosen(appName);
						}
					} catch (Exception e) {
						// ignore
					}
				}
			}
		};

		mContext.registerReceiver(this.mReceiver, theFilter);
	}

	private void unRegisterReceiver() {
		if(mReceiver != null){
			mContext.unregisterReceiver(mReceiver);
			mReceiver = null;
		}

	}

	private String getAppNameFromPkgName(Context context, String packageName) {
		if(ObjectUtil.isEmpty(packageName)) {
			return "";
		}

		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			String appName = (String) packageManager.getApplicationLabel(info);
			return appName;
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}
	}

}
