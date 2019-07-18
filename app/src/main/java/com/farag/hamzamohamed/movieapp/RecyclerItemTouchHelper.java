package com.farag.hamzamohamed.movieapp;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.farag.hamzamohamed.movieapp.Adapter.DataBaseMovieAdapter;
import com.farag.hamzamohamed.movieapp.Listeners.RecyclerItemTouchHelperListener;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs , RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null){
            View view = ((DataBaseMovieAdapter.MoviesDBaseViewHolder)viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(view);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        View view = ((DataBaseMovieAdapter.MoviesDBaseViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,view,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View view = ((DataBaseMovieAdapter.MoviesDBaseViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(view);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view = ((DataBaseMovieAdapter.MoviesDBaseViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,view,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
