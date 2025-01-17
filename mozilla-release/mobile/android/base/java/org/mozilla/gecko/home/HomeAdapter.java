/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.gecko.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import org.mozilla.gecko.R;
import org.mozilla.gecko.activitystream.ActivityStream;
import org.mozilla.gecko.activitystream.homepanel.ActivityStreamHomeFragment;
import org.mozilla.gecko.home.HomeConfig.PanelConfig;
import org.mozilla.gecko.home.HomeConfig.PanelType;
import org.mozilla.gecko.preferences.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final ArrayList<PanelInfo> mPanelInfos;
    private final Map<String, HomeFragment> mPanels;
    private final Map<String, Bundle> mRestoreBundles;

    private boolean mCanLoadHint;

    private OnAddPanelListener mAddPanelListener;

    private HomeFragment.PanelStateChangeListener mPanelStateChangeListener = null;

    public interface OnAddPanelListener {
        /*Cliqz Start*/
        void onAddPanel(@DrawableRes int iconId);
        /*Cliqz End*/
    }

    public void setPanelStateChangeListener(HomeFragment.PanelStateChangeListener listener) {
        mPanelStateChangeListener = listener;

        for (Fragment fragment : mPanels.values()) {
            ((HomeFragment) fragment).setPanelStateChangeListener(listener);
        }
    }

    public HomeAdapter(Context context, FragmentManager fm) {
        super(fm);

        mContext = context;
        mCanLoadHint = HomeFragment.DEFAULT_CAN_LOAD_HINT;

        mPanelInfos = new ArrayList<>();
        mPanels = new HashMap<>();
        mRestoreBundles = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mPanelInfos.size();
    }

    @Override
    public Fragment getItem(int position) {
        PanelInfo info = mPanelInfos.get(position);
        return Fragment.instantiate(mContext, info.getClassName(mContext), info.getArgs());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mPanelInfos.size() > 0) {
            PanelInfo info = mPanelInfos.get(position);
            return info.getTitle().toUpperCase();
        }

        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final HomeFragment fragment = (HomeFragment) super.instantiateItem(container, position);
        fragment.setPanelStateChangeListener(mPanelStateChangeListener);

        final String id = mPanelInfos.get(position).getId();
        mPanels.put(id, fragment);

        if (mRestoreBundles.containsKey(id)) {
            fragment.restoreData(mRestoreBundles.get(id));
            mRestoreBundles.remove(id);
        }

        return fragment;
    }

    public void setRestoreData(int position, Bundle data) {
        final String id = mPanelInfos.get(position).getId();
        final HomeFragment fragment = mPanels.get(id);

        // We have no guarantees as to whether our desired fragment is instantiated yet: therefore
        // we might need to either pass data to the fragment, or store it for later.
        if (fragment != null) {
            fragment.restoreData(data);
        } else {
            mRestoreBundles.put(id, data);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final String id = mPanelInfos.get(position).getId();

        super.destroyItem(container, position, object);
        mPanels.remove(id);
    }

    public void setOnAddPanelListener(OnAddPanelListener listener) {
        mAddPanelListener = listener;
    }

    public int getItemPosition(String panelId) {
        for (int i = 0; i < mPanelInfos.size(); i++) {
            final String id = mPanelInfos.get(i).getId();
            if (id.equals(panelId)) {
                return i;
            }
        }

        return -1;
    }

    public String getPanelIdAtPosition(int position) {
        // getPanelIdAtPosition() might be called before HomeAdapter
        // has got its initial list of PanelConfigs. Just bail.
        if (mPanelInfos.isEmpty()) {
            return null;
        }

        return mPanelInfos.get(position).getId();
    }

    private void addPanel(PanelInfo info) {
        mPanelInfos.add(info);

        if (mAddPanelListener != null) {
            /*Cliqz Start*/
            mAddPanelListener.onAddPanel(info.getIconId());
            /*Cliqz End*/
        }
    }

    public void update(List<PanelConfig> panelConfigs) {
        mPanels.clear();
        mPanelInfos.clear();

        if (panelConfigs != null) {
            for (PanelConfig panelConfig : panelConfigs) {
                final PanelInfo info = new PanelInfo(panelConfig);
                addPanel(info);
            }
        }

        notifyDataSetChanged();
    }

    public boolean getCanLoadHint() {
        return mCanLoadHint;
    }

    public void setCanLoadHint(boolean canLoadHint) {
        // We cache the last hint value so that we can use it when
        // creating new panels. See PanelInfo.getArgs().
        mCanLoadHint = canLoadHint;

        // Enable/disable loading on all existing panels
        for (Fragment panelFragment : mPanels.values()) {
            final HomeFragment panel = (HomeFragment) panelFragment;
            panel.setCanLoadHint(canLoadHint);
        }
    }

    /* Cliqz Start */
    void updateBgAlpha(float positionOffset) {
        final CombinedHistoryPanel historyPanel = (CombinedHistoryPanel) mPanels.get(HomeConfig.COMBINED_HISTORY_PANEL_ID);
        final ActivityStreamHomeFragment freshTab = (ActivityStreamHomeFragment) mPanels.get(HomeConfig.TOP_SITES_PANEL_ID);
        final BookmarksPanel bookmarksPanel = (BookmarksPanel) mPanels.get(HomeConfig.BOOKMARKS_PANEL_ID);
        final MyOffrzPanel myOffrzPanel = (MyOffrzPanel) mPanels.get(HomeConfig.MY_OFFRZ_PANEL_ID);

        final boolean isLightTheme = PreferenceManager.getInstance().isLightThemeEnabled();

        final int opacity = (int) (41.1 * positionOffset);
        String desiredHex = Integer.toString(opacity) + "000000";
        if (freshTab != null && !isLightTheme) {
            freshTab.getView().findViewById(R.id.root_view).setBackgroundColor(
                    Integer.parseInt(desiredHex, 16));
        }
        if (historyPanel != null) {
            historyPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : Integer.parseInt(desiredHex, 16));
        }
        final int backgroundOverlayColor = ContextCompat.getColor(mContext, R.color.home_panel_background_overlay);

        if (myOffrzPanel != null) {
            myOffrzPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : backgroundOverlayColor);
        }

        if (bookmarksPanel != null) {
            bookmarksPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : backgroundOverlayColor);
        }
    }

    void setLightTheme(boolean isLightTheme) {
        final ActivityStreamHomeFragment freshTab = (ActivityStreamHomeFragment) mPanels.get(HomeConfig.TOP_SITES_PANEL_ID);
        final CombinedHistoryPanel historyPanel = (CombinedHistoryPanel) mPanels.get(HomeConfig.COMBINED_HISTORY_PANEL_ID);
        final MyOffrzPanel myOffrzPanel = (MyOffrzPanel) mPanels.get(HomeConfig.MY_OFFRZ_PANEL_ID);
        final BookmarksPanel bookmarksPanel = (BookmarksPanel) mPanels.get(HomeConfig.BOOKMARKS_PANEL_ID);

        if (isLightTheme && freshTab != null) {
            freshTab.getView().findViewById(R.id.root_view).setBackgroundColor(Color.TRANSPARENT);
        }

        final int backgroundOverlayColor = ContextCompat.getColor(mContext, R.color.home_panel_background_overlay);
        if (historyPanel != null) {
            historyPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : backgroundOverlayColor);
            historyPanel.setLightTheme(isLightTheme);
        }
        if (myOffrzPanel != null) {
            myOffrzPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : backgroundOverlayColor);
            myOffrzPanel.setLightTheme(isLightTheme);
        }
        if (bookmarksPanel != null) {
            bookmarksPanel.getView().findViewById(R.id.root_view).setBackgroundColor(
                    isLightTheme ? Color.TRANSPARENT : backgroundOverlayColor);
            bookmarksPanel.setLightTheme(isLightTheme);
        }
    }
    /* Cliqz End */

    private final class PanelInfo {
        private final PanelConfig mPanelConfig;

        PanelInfo(PanelConfig panelConfig) {
            mPanelConfig = panelConfig;
        }

        public String getId() {
            return mPanelConfig.getId();
        }

        public String getTitle() {
            return mPanelConfig.getTitle();
        }

        public String getClassName(Context context) {
            final PanelType type = mPanelConfig.getType();

            // Override top_sites with ActivityStream panel when enabled
            // PanelType.toString() returns the panel id
            if ("top_sites".equals(type.toString()) &&
                ActivityStream.isEnabled(context) &&
                ActivityStream.isHomePanel()) {
                return ActivityStreamHomeFragment.class.getName();
            }
            return type.getPanelClass().getName();
        }

        public Bundle getArgs() {
            final Bundle args = new Bundle();

            args.putBoolean(HomePager.CAN_LOAD_ARG, mCanLoadHint);

            // Only DynamicPanels need the PanelConfig argument
            if (mPanelConfig.isDynamic()) {
                args.putParcelable(HomePager.PANEL_CONFIG_ARG, mPanelConfig);
            }

            return args;
        }

        /*Cliqz Start*/
        @DrawableRes
        int getIconId() {
            return mPanelConfig.getIconId();
        }
        /*Cliqz End*/
    }
}
