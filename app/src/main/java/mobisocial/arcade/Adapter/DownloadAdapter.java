package mobisocial.arcade.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.R;
import mobisocial.arcade.data.Download;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    List<Download> item;
    private final Context ctx;
    public DownloadAdapter(Context context, List<Download> item) {
        ctx = context;
        this.item = item;
    }


    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.downloadrow,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
        public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder holder, final int position) {
        final Download download = item.get(position);
        if (download.getImg().contains("png") || download.getImg().contains("jpg")) {

            String img = urlref.Image + download.getImg();
            Glide.with(ctx).load(img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
        }

          holder.title.setText(item.get(position).getTitle());
            holder.desc.setText(item.get(position).getDescription());
            holder.foldingCell.setOnClickListener(v -> holder.foldingCell.toggle(false));
            holder.download.setOnClickListener(v -> {
                Uri uri = Uri.parse(download.getDownurl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ctx.startActivity(intent);
            });
        holder.downloadinside.setOnClickListener(v -> {
            Uri uri = Uri.parse(download.getDownurl()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ctx.startActivity(intent);
        });

        }

    @Override
    public int getItemCount() {
      return item.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView desc;
        ImageView img;
        ImageView download;
        ImageView downloadinside;
        FoldingCell foldingCell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            img =itemView.findViewById(R.id.img);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            download = itemView.findViewById(R.id.download);
            downloadinside = itemView.findViewById(R.id.downloadinside);


        }
    }
}