package com.example.thomas.familyheroes.Utilities;

/**
 * Created by Thomas on 21/10/2014.
 */
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * A class that defines a simple tab.
 */
public class SimpleTabDefinition extends TabDefinition {
    //
    // Fields
    //
    private final int _tabTitleResourceId;
    private final int _tabLayoutId;
    private final Fragment _fragment;

    //
    // Constructors
    //
    /**
     * The constructor for {@link SimpleTabDefinition}.
     * @param tabContentViewId The layout ID of the contents to use when the tab is active.
     * @param tabLayoutId The ID of the layout to use when inflating the tab {@link View}.
     * @param tabTitleResourceId The string resource ID for the title of the tab.
     * @param fragment The {@link Fragment} used when the tab is active.
     */
    public SimpleTabDefinition(int tabContentViewId, int tabLayoutId, int tabTitleResourceId,Fragment fragment) {
        super(tabContentViewId);

        _tabLayoutId = tabLayoutId;
        _tabTitleResourceId = tabTitleResourceId;
        _fragment = fragment;


    }

    //
    // Exposed Members
    //
    @Override
    public Fragment getFragment() {
        return _fragment;
    }

    @Override
    public View createTabView(LayoutInflater inflater, ViewGroup tabsView) {
        // we need to inflate the view based on the layout id specified when
        // this instance was created.
        View indicator = inflater.inflate(
                _tabLayoutId,
                tabsView,
                false);



        // ensure the control we're inflating is layed out properly. this will
        // cause our tab titles to be placed evenly weighted across the top.
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        indicator.setLayoutParams(layoutParams);

        return indicator;
    }
}