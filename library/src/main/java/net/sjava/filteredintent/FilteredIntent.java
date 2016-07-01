package net.sjava.filteredintent;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
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

    public void addCategory(String category) {
        if(TextUtils.isEmpty(category))
            return;

        mIntent.addCategory(category);
    }

    public void addFlag(int flag) {
        mIntent.addFlags(flag);
    }

    public Intent getIntent() {
        return mIntent;
    }

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
}
