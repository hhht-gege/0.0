package competition.fragment.Me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

import competition.Utils.WebSocket.CircleImageView;
import competition.fragment.Search.Adapter.SearchAdapter;
import competition.fragment.Search.Search;

public class Medynamic extends AppCompatActivity {
    private MaterialTextView title;
    private CircleImageView img;
    private MaterialTextView name;

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    public static List<Search> searchList;
    private SearchAdapter adapter;
}
