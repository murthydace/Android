package com.local.growkart.util.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by atselkovsky on 26.06.17.
 */

public class FirstLastSpacingItemDecoration extends RecyclerView.ItemDecoration {

    public enum Mode {
        VERTICAL, HORIZONTAL, BOTH
    }

    private int verticalSpacing;
    private int horizontalSpacing;
    private Mode mode;


    public FirstLastSpacingItemDecoration(int verticalSpacing, int horizontalSpacing, Mode mode) {
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
        this.mode = mode;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isFirst = position == 0;
        boolean isLast = position == state.getItemCount() - 1;
        if (mode == Mode.HORIZONTAL || mode == Mode.BOTH) {
            outRect.left = isFirst ? horizontalSpacing : horizontalSpacing / 2;
            outRect.right = isLast ? horizontalSpacing : horizontalSpacing / 2;
        }

        if (mode == Mode.VERTICAL || mode == Mode.BOTH) {
            outRect.top = isFirst ? verticalSpacing : verticalSpacing / 2;
            outRect.bottom = isLast ? verticalSpacing : verticalSpacing / 2;
        }
    }
}
