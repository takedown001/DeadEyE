package mobisocial.arcade.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import mobisocial.arcade.GccConfig.urlref;
import mobisocial.arcade.R;
import mobisocial.arcade.RDetailActivity;
import mobisocial.arcade.data.Reseller;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ResellerAdapter  extends PagerAdapter {
    private List<Reseller> movelist;
    private LayoutInflater layoutInflater;
    private final Context ctx;
    //plan
    private static final String TAG_RNAME = urlref.TAG_RNAME;
    private static final String TAG_RDESC = urlref.TAG_RDESC;
    private static final String TAG_RIMG = urlref.TAG_RIMG;



    public ResellerAdapter(List<Reseller> movelist, Context ctx) {
        this.movelist = movelist;
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return movelist.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.rsale, container, false);
        final Reseller reseller = movelist.get(position);
        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        if (reseller.getImage().contains("png") || reseller.getImage().contains("jpg")) {

            String img = urlref.Image + reseller.getImage();
            Glide.with(ctx).load(img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }

        title.setText(movelist.get(position).getTitle());
        desc.setText(movelist.get(position).getDesc());

        view.setOnClickListener(v -> {


            Intent intent = new Intent(ctx, RDetailActivity.class);

            intent.putExtra(TAG_RNAME, movelist.get(position).getTitle());
            intent.putExtra(TAG_RDESC,movelist.get(position).getDesc());
            intent.putExtra(TAG_RIMG,movelist.get(position).getImage());
            ctx.startActivity(intent);
            // finish();
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
