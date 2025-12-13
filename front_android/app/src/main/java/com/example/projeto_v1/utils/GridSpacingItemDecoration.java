package com.example.projeto_v1.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private int edgeSpacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, int edgeSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.edgeSpacing = edgeSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (includeEdge) {

            if (column == 0) {
                outRect.left = edgeSpacing;
                outRect.right = spacing / 2;
            }
            else if (column == 1) {
                outRect.left = spacing / 2;
                outRect.right = edgeSpacing;
            }

            outRect.bottom = spacing;

            if (position < spanCount) {
                outRect.top = spacing;
            }
        } else {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            if (position < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        }
    }
}