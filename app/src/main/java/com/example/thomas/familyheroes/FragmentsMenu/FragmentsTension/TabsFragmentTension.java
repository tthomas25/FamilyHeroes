package com.example.thomas.familyheroes.FragmentsMenu.FragmentsTension;

/**
 * Created by Thomas on 28/10/2014.
 */
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.example.thomas.familyheroes.R;
import com.example.thomas.familyheroes.Utilities.SimpleTabDefinition;
import com.example.thomas.familyheroes.Utilities.TabDefinition;

/**
 * A {@link Fragment} used to switch between tabs.
 */
public class TabsFragmentTension extends Fragment implements OnTabChangeListener {
    //
    // Constants
    //
    private final TabDefinition[] TAB_DEFINITIONS = new TabDefinition[] {
            new SimpleTabDefinition(R.id.tab1, R.layout.fragment_graph_tension, R.string.nothing, new GraphiqueTensionFragment()),
            new SimpleTabDefinition(R.id.tab2, R.layout.fragment_historique_tension, R.string.nothing, new HistoriqueTensionFragment()),
    };

    //
    // Fields
    //
    private View _viewRoot;
    private TabHost _tabHost;

    //
    // Exposed Members
    //
    @Override
    public void onTabChanged(String tabId) {
        for (TabDefinition tab : TAB_DEFINITIONS) {
            if (tabId != tab.getId()) {
                continue;
            }

            if(_tabHost.getCurrentTab()==0)
            {
                _tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.stats_select);
                _tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.clock);

            }
            else if(_tabHost.getCurrentTab()==1)
            {
                _tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.stats);
                _tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.clock_select);

            }

            updateTab(tabId, tab.getFragment(), tab.getTabContentViewId());
            return;
        }

        throw new IllegalArgumentException("The specified tab id '" + tabId + "' does not exist.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _viewRoot = inflater.inflate(R.layout.tabs_fragment_tension, null);

        _tabHost = (TabHost)_viewRoot.findViewById(android.R.id.tabhost);
        _tabHost.setup();

        /* First, get the Display from the WindowManager */
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // check display size to figure out what image resolution will be loaded
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        Point size = new Point();
        display.getRealSize(size);
        display.getSize(size);
        int width = size.x;


        for (TabDefinition tab : TAB_DEFINITIONS) {
            _tabHost.addTab(createTab(inflater, _tabHost, _viewRoot, tab));
        }

        _tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.stats_select);
        _tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.clock);

        _tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = width/2;
        _tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 180;

        _tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = width/2;
        _tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 180;

        _tabHost.getTabWidget().getChildAt(0).invalidate();

        return _viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        _tabHost.setOnTabChangedListener(this);

        if (TAB_DEFINITIONS.length > 0) {
            onTabChanged(TAB_DEFINITIONS[0].getId());
        }
    }

    //
    // Internal Members
    //
    /**
     * Creates a {@link TabSpec} based on the specified parameters.
     * @param inflater The {@link LayoutInflater} responsible for creating {@link View}s.
     * @param tabHost The {@link TabHost} used to create new {@link TabSpec}s.
     * @param root The root {@link View} for the {@link Fragment}.
     * @param tabDefinition The {@link TabDefinition} that defines what the tab will look and act like.
     * @return A new {@link TabSpec} instance.
     */
    private TabSpec createTab(LayoutInflater inflater, TabHost tabHost, View root, TabDefinition tabDefinition) {
        ViewGroup tabsView = (ViewGroup)root.findViewById(android.R.id.tabs);
        View tabView = tabDefinition.createTabView(inflater, tabsView);

        TabSpec tabSpec = tabHost.newTabSpec(tabDefinition.getId());
        tabSpec.setIndicator(tabView);
        tabSpec.setContent(tabDefinition.getTabContentViewId());
        return tabSpec;
    }

    /**
     * Called when switching between tabs.
     * @param tabId The unique identifier for the tab.
     * @param fragment The {@link Fragment} to swap in for the tab.
     * @param containerId The layout ID for the {@link View} that houses the tab's content.
     */
    private void updateTab(String tabId, Fragment fragment, int containerId) {
        final FragmentManager manager = getFragmentManager();
        if (manager.findFragmentByTag(tabId) == null) {
            manager.beginTransaction()
                    .replace(containerId, fragment, tabId)
                    .commit();
        }


    }


}