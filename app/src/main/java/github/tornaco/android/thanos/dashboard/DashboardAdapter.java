package github.tornaco.android.thanos.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.android.thanos.databinding.ItemFeatureDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.TileHolder> {
    private final List<Tile> tiles = new ArrayList<>();

    public void replaceData(List<Tile> tiles) {
        this.tiles.clear();
        this.tiles.addAll(tiles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TileHolder(ItemFeatureDashboardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TileHolder holder, int position) {
        Tile tile = tiles.get(position);
        holder.binding.setTile(tile);
        holder.binding.setIsLastOne(position == getItemCount() - 1);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    static final class TileHolder extends RecyclerView.ViewHolder {
        private ItemFeatureDashboardBinding binding;

        TileHolder(@NonNull ItemFeatureDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
