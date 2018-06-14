package org.mozilla.gecko.controlcenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.mozilla.gecko.R;
import org.mozilla.gecko.util.GeckoBundle;

/**
 * Copyright © Cliqz 2018
 */
public class SiteTrackersFragment extends ControlCenterFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private SiteTrackersListAdapter mTrackerListAdapter;

    public SiteTrackersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ghostery_site_trackers_fragment, container, false);
        ExpandableListView mTrackersList = (ExpandableListView) view.findViewById(R.id.trackers_list);
        View mOverflowMenu = view.findViewById(R.id.overflow_menu);
        mOverflowMenu.setOnClickListener(this);
        mTrackerListAdapter = new SiteTrackersListAdapter(getContext());
        mTrackersList.setAdapter(mTrackerListAdapter);
        return view;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.cc_title_site_trackers);
    }

    @Override
    public void updateUI(GeckoBundle data) {
        mTrackerListAdapter.setData(data);
    }

    @Override
    public void refreshUI() {
        mTrackerListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_all:
                //TODO add actions
                return true;
            case R.id.unblock_all:
                //TODO add actions
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.overflow_menu:
                showOverflowMenu(v);
                break;
        }

    }

    public void showOverflowMenu(View v) {
        final PopupMenu popup = new PopupMenu(getContext(), v);
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.ghostery_site_trackers_overflow_menu, popup.getMenu());
        popup.show();
    }
}