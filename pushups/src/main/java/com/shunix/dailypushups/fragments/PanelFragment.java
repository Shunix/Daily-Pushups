package com.shunix.dailypushups.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunix.dailypushups.BuildConfig;
import com.shunix.dailypushups.R;
import com.shunix.dailypushups.ui.RadialMenuWidget;

import java.util.List;

/**
 * @author Ray WANG <admin@shunix.com>
 * @version 1.0.0
 * @since Feb 9th, 2014
 */
public class PanelFragment extends Fragment {

    /**
     * Return the view for this element.
     */
    private LinearLayout getPanelView(Context context) {
        if (BuildConfig.DEBUG) {
            Log.d("PanelFragment", "Creating View");
        }
        LinearLayout layout = new LinearLayout(context);
        RadialMenuWidget radialMenu = new RadialMenuWidget(context);

        // Make the view suitable for current screen size.
        int xScreenSize = context.getResources().getDisplayMetrics().widthPixels;
        int yScreenSize = context.getResources().getDisplayMetrics().heightPixels;
        int xCenter = xScreenSize / 2;
        int yCenter = yScreenSize / 2;
        int xSource = xCenter;
        int ySource = yCenter;

        // Set params for the radial menu.
        radialMenu.setSourceLocation(xCenter, yCenter);
        radialMenu.setCenterLocation(xCenter, yCenter);
        radialMenu.setShowSourceLocation(true);

        // Set menu entries for the radial menu.
        radialMenu.setCenterCircle(new CenterPoint());
        radialMenu.addMenuEntry(new CenterPoint());

        // Add Radial Menu to the layout.
        layout.addView(radialMenu);

        if (BuildConfig.DEBUG) {
            Log.d("PanelFragment", "View has been created");
        }
        return layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getPanelView(getActivity());
    }
}

/**
 * Menu entry in the center of radial menu.
 *
 * @author Ray WANG <admin@shunix.com>
 * @version 1.0.0
 * @since Feb 9th, 2014
 */
class CenterPoint implements RadialMenuWidget.RadialMenuEntry {
    @Override
    public String getName() {
        return "Center";
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_anchor;
    }

    @Override
    public List<RadialMenuWidget.RadialMenuEntry> getChildren() {
        return null;
    }

    @Override
    public void menuActiviated() {
        if (BuildConfig.DEBUG) {
            Log.d("PanelFragment", "Center menu is clicker");
        }
    }
}