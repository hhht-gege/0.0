package competition.fragment.Search.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import competition.R;

public class SearchAdapter_gv extends BaseAdapter {

    private Context context;
    private List<String> list;

    public  SearchAdapter_gv(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    //根据情况可以设置是否需要。。
    public int getMaxPosition(){
        return 6;
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        SearchAdapter_gv.ViewHolder vh=null;
        if (v==null){
            vh=new SearchAdapter_gv.ViewHolder();
            v= LayoutInflater.from(context).inflate(R.layout.fragment_searchgv,parent,false);
            vh.img= (ImageView) v.findViewById(R.id.search_gv_img);
            v.setTag(vh);
        }else{
            vh= (SearchAdapter_gv.ViewHolder) v.getTag();
        }
        Glide.with(context).load(list.get(position)).into(vh.img);//设置

        return v;
    }

    public class ViewHolder{
        public ImageView img;
    }

}
