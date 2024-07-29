package com.carbonara.gardenadvisor.ui.addplants.adapter.swipe;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.carbonara.gardenadvisor.ui.addplants.adapter.AddPlantAdapter;
import com.carbonara.gardenadvisor.ui.addplants.adapter.viewholder.AddPlantViewHolder;
import java.util.HashMap;
import java.util.Map;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

  private AddPlantAdapter mAdapter;
  private float oldX;
  private Map<Integer, Integer> openItems = new HashMap<>();

  public SwipeToDeleteCallback(AddPlantAdapter adapter) {
    super(0, ItemTouchHelper.LEFT);
    mAdapter = adapter;
    mAdapter.setDelCallback(this::removeItem);
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
    loge("Move");
    return false;
  }

  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    int position = viewHolder.getAdapterPosition();
    if (viewHolder instanceof AddPlantViewHolder ){
      Integer res = openItems.putIfAbsent(viewHolder.getAdapterPosition(),direction);
      if(res == null || res != direction){
        ((AddPlantViewHolder) viewHolder).showDelete();
      }
    }
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
      @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
      int actionState, boolean isCurrentlyActive) {

  }

  public boolean removeItem(Integer key){
    return openItems.remove(key)!=null;
  }
}