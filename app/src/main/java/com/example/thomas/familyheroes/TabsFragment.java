package com.example.thomas.familyheroes;

/**
 * Created by Thomas on 21/10/2014.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

/**
 * A {@link Fragment} used to switch between tabs.
 */
public class TabsFragment extends Fragment implements OnTabChangeListener {
    //
    // Constants
    //
    private final TabDefinition[] TAB_DEFINITIONS = new TabDefinition[] {
            new SimpleTabDefinition(R.id.tab1, R.layout.fragment_graph_cardiaque, R.string.nothing, new GraphiqueCardiaqueFragment()),
            new SimpleTabDefinition(R.id.tab2, R.layout.fragment_historique_cardiaque, R.string.nothing, new HistoriqueCardiaqueFragment()),
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
        _viewRoot = inflater.inflate(R.layout.tabs_fragment, null);

        _tabHost = (TabHost)_viewRoot.findViewById(android.R.id.tabhost);
        _tabHost.setup();

        for (TabDefinition tab : TAB_DEFINITIONS) {
            _tabHost.addTab(createTab(inflater, _tabHost, _viewRoot, tab));
        }

        _tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.stats_select);
        _tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.clock);

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