package org.mozilla.gecko.controlcenter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.Utils;

import org.mozilla.gecko.EventDispatcher;
import org.mozilla.gecko.R;
import org.mozilla.gecko.Tabs;
import org.mozilla.gecko.util.GeckoBundle;

/**
 * Copyright © Cliqz 2018
 */
public class GlobalTrackersListAdapter extends BaseExpandableListAdapter {

    private GeckoBundle[] mListData;
    private GeckoBundle data;
    private Context mContext;

    GlobalTrackersListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        if (mListData == null) {
            return 0;
        }
        return mListData.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mListData == null) {
            return 0;
        }
        return mListData[groupPosition].getBundleArray("trackers").length;
    }

    @Override
    public GeckoBundle getGroup(int groupPosition) {
        if (mListData == null) {
            return null;
        }
        return mListData[groupPosition];
    }

    @Override
    public GeckoBundle getChild(int groupPosition, int childPosition) {
        if (mListData == null) {
            return null;
        }
        return mListData[groupPosition].getBundleArray("trackers")[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 100 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ghostery_list_item_category, null);
        }
        final View view = convertView.findViewById(R.id.tracker_options);
        view.setX(parent.getWidth());
        final GeckoBundle groupGeckoBundle = getGroup(groupPosition);
        final String categoryId = groupGeckoBundle.getString("id");
        //TODO add name to enum
        final String categoryName = groupGeckoBundle.getString("name");
        final Categories category = Categories.safeValueOf(categoryId);
        final int totalTrackers = groupGeckoBundle.getInt("num_total");
        final int blockedTrackers = groupGeckoBundle.getInt("num_blocked");
        final TextView categoryNameTextView = (TextView) convertView.findViewById(R.id.category_name);
        final TextView totalTrackersTextView = (TextView) convertView.findViewById(R.id.total_trackers);
        final TextView blockedTrackersTextView = (TextView) convertView.findViewById(R.id.blocked_trackers);
        final ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.category_icon);
        final ImageView stateCheckBox = (ImageView) convertView.findViewById(R.id.cb_block_all);
        categoryIcon.setImageDrawable(ContextCompat.getDrawable(mContext, category.categoryIcon));
        categoryNameTextView.setText(categoryName);
        totalTrackersTextView.setText(mContext.getResources().getQuantityString(R.plurals.cc_total_trackers, totalTrackers, totalTrackers));
        blockedTrackersTextView.setText(mContext.getString(R.string.cc_num_blocked, blockedTrackers));
        if (blockedTrackers == 0) {
            stateCheckBox.setImageResource(R.drawable.ic_cb_unchecked);
        } else if (blockedTrackers == totalTrackers) {
            stateCheckBox.setImageResource(R.drawable.ic_cb_checked_block);
        } else {
            stateCheckBox.setImageResource(R.drawable.ic_cb_checked_mixed);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ghostery_list_item_global_tracker, null);
        }
        final GeckoBundle childGeckoBundle = getChild(groupPosition, childPosition);
        final String trackerName = childGeckoBundle.getString("name");
        final String trackerId = childGeckoBundle.getString("id");
        final boolean isBlocked = childGeckoBundle.getBoolean("blocked");
        final TextView trackerNameTextView = (TextView) convertView.findViewById(R.id.tracker_name);
        final ImageView trackerCheckBox = (ImageView) convertView.findViewById(R.id.cb_block_tracker);
        final View blockButton = convertView.findViewById(R.id.block_button);
        blockButton.setX(parent.getWidth());
        final View infoButton = convertView.findViewById(R.id.info_icon);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tabs.getInstance().loadUrlInTab("https://whotracks.me/trackers/" + trackerName.toLowerCase().replace(" ", "_") + ".html");
            }
        });
        if (isBlocked) {
            trackerCheckBox.setImageResource(R.drawable.ic_cb_checked_block);
        } else {
            trackerCheckBox.setImageResource(R.drawable.ic_cb_unchecked);
        }
        trackerNameTextView.setText(trackerName);
        final ObjectAnimator animation = ObjectAnimator.ofFloat(blockButton, "translationX",
                parent.getWidth() - Utils.convertDpToPixel(80));
        animation.setDuration(400);
        trackerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.start();
            }
        });
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GeckoBundle selectedAppIds = data.getBundle("data").getBundle("blocking")
                        .getBundle("selected_app_ids");
                selectedAppIds.putInt(trackerId, 1);
                final GeckoBundle geckoBundle = new GeckoBundle();
                geckoBundle.putBundle("selected_app_ids", selectedAppIds);
                EventDispatcher.getInstance().dispatch("Privacy:SetInfo", geckoBundle);
                childGeckoBundle.putBoolean("blocked",!isBlocked);
                final GeckoBundle[] siteTrackerCategories = data.getBundle("data")
                        .getBundle("summary").getBundleArray("categories");
                for (GeckoBundle category : siteTrackerCategories) {
                    final GeckoBundle[] trackers = category.getBundleArray("trackers");
                    for (GeckoBundle tracker : trackers) {
                        if (tracker.getInt("id") == Integer.valueOf(trackerId)) {
                            tracker.putBoolean("blocked", true);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    void setData(GeckoBundle data) {
        this.data = data;
        mListData = data.getBundle("data").getBundle("settings")
                .getBundleArray("categories");
        notifyDataSetChanged();
    }

    public void blockAllTrackers() {
        final GeckoBundle selectedAppIds = data.getBundle("data").getBundle("blocking")
                .getBundle("selected_app_ids");
        final GeckoBundle[] globalCategories = data.getBundle("data").getBundle("settings")
                .getBundleArray("categories");
        for (GeckoBundle category : globalCategories) {
            final int totalTrackers = category.getInt("num_total");
            category.putInt("num_blocked", totalTrackers);
            final GeckoBundle[] trackers = category.getBundleArray("trackers");
            for (GeckoBundle tracker : trackers) {
                final String trackerId = tracker.getString("id");
                tracker.putBoolean("blocked", true);
                selectedAppIds.putInt(trackerId, 1);
            }
        }
        final GeckoBundle[] siteTrackerCategories = data.getBundle("data")
                .getBundle("summary").getBundleArray("categories");
        for (GeckoBundle category : siteTrackerCategories) {
            final int totalTrackers = category.getInt("num_total");
            category.putInt("num_blocked", totalTrackers);
            final GeckoBundle[] trackers = category.getBundleArray("trackers");
            for (GeckoBundle tracker : trackers) {
                tracker.putBoolean("blocked", true);
            }
        }
        final GeckoBundle geckoBundle = new GeckoBundle();
        geckoBundle.putBundle("selected_app_ids", selectedAppIds);
        EventDispatcher.getInstance().dispatch("Privacy:SetInfo", geckoBundle);
        notifyDataSetChanged();
    }

    public void unBlockAllTrackers() {
        final GeckoBundle selectedAppIds = data.getBundle("data").getBundle("blocking")
                .getBundle("selected_app_ids");
        final GeckoBundle[] globalCategories = data.getBundle("data").getBundle("settings")
                .getBundleArray("categories");
        for (GeckoBundle category : globalCategories) {
            category.putInt("num_blocked", 0);
            final GeckoBundle[] trackers = category.getBundleArray("trackers");
            for (GeckoBundle tracker : trackers) {
                final String trackerId = tracker.getString("id");
                tracker.putBoolean("blocked", false);
                selectedAppIds.remove(trackerId);
            }
        }
        final GeckoBundle[] siteTrackerCategories = data.getBundle("data")
                .getBundle("summary").getBundleArray("categories");
        for (GeckoBundle category : siteTrackerCategories) {
            category.putInt("num_blocked", 0);
            final GeckoBundle[] trackers = category.getBundleArray("trackers");
            for (GeckoBundle tracker : trackers) {
                tracker.putBoolean("blocked", false);
            }
        }
        final GeckoBundle geckoBundle = new GeckoBundle();
        geckoBundle.putBundle("selected_app_ids", selectedAppIds);
        EventDispatcher.getInstance().dispatch("Privacy:SetInfo", geckoBundle);
        notifyDataSetChanged();
    }
}