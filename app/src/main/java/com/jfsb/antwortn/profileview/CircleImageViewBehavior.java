package com.jfsb.antwortn.profileview;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.ViewGroupUtils;

import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Layout Behavior to show/hide a CircleImageView anchored to a AppBarLayout
 */
public class CircleImageViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private Rect mTmpRect;

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull CircleImageView child, @NotNull View dependency) {
        // check that our dependency is the AppBarLayout
        return dependency instanceof AppBarLayout;
    }

    @Override public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, @NotNull CircleImageView child,
                                                    @NotNull View dependency) {
        if (dependency instanceof AppBarLayout) {
            return updateCircleImageViewVisibility(parent, (AppBarLayout) dependency, child);
        }
        return false;
    }

    @SuppressLint("RestrictedApi")
    private boolean updateCircleImageViewVisibility(CoordinatorLayout parent,
                                                    AppBarLayout appBarLayout, CircleImageView child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            // The anchor ID doesn't match the dependency, so we won't automatically
            // show/hide the FAB
            return false;
        }

        if (mTmpRect == null) {
            mTmpRect = new Rect();
        }

        // First, let's get the visible rect of the dependency
        final Rect rect = mTmpRect;
        ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);

        if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
            // If the anchor's bottom is below the seam, hide it
            child.setVisibility(View.INVISIBLE);
        } else {
            // Else, show it
            child.setVisibility(View.VISIBLE);
        }
        return true;
    }
}