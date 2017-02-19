package bg.softuni.softuniada.studyrise.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Model.LineItem;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.RecylerAdapter.BaseViewAdapter;
import bg.softuni.softuniada.studyrise.RecylerAdapter.BindingViewHolder;
import bg.softuni.softuniada.studyrise.RecylerAdapter.MultiTypeAdapter;
import bg.softuni.softuniada.studyrise.SwipeDragLayout;
import bg.softuni.softuniada.studyrise.databinding.ChildStatusItemBinding;

public class CheckListActivity extends AppCompatActivity {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_TITLE = 2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.acitivity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.todoFabButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        MultiTypeAdapter adapter = new MultiTypeAdapter(this);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_TITLE, R.layout.group_status_item);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_ITEM, R.layout.child_status_item);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = dip2px(mContext, 10);
            }
        });

        adapter.addAll(fakeData(), new MultiTypeAdapter.CustomMultiViewTyper() {

            @Override
            public int getViewType(Object item, int pos) {
                if (item instanceof LineItem) {
                    if (((LineItem) item).isTitle()) {
                        return VIEW_TYPE_TITLE;
                    } else {
                        return VIEW_TYPE_ITEM;
                    }
                }
                throw new RuntimeException("unExcepted item type");
            }
        });

        //设置点击事件
        adapter.setPresenter(new ItemPresenter());
        //设置额外操作
        adapter.setDecorator(new ItemDecoration());
    }

    /**
     * @return 测试数据集
     */
    List<LineItem> fakeData() {
        List<LineItem> items = new ArrayList<>();
        int d = 1;
        for (int i = 0; i < 15; i++) {
            LineItem lineItem;

            if (i == 0 || i == 4 || i == 9) {

                lineItem = new LineItem("Title " + d, true);
                d++;
            } else {
                lineItem = new LineItem("Item  " + i, false);
            }
            items.add(lineItem);
        }
        return items;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置菜单按钮的点击事件
     */
    public class ItemPresenter implements BaseViewAdapter.Presenter {

        /**
         * 参考 dataBinding的用法，下同
         *
         * @param item
         */
        public void onStarClick(LineItem item) {
            Toast.makeText(mContext, "star", Toast.LENGTH_SHORT).show();
        }

        public void onDeleteClick(LineItem item) {
            Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
        }

    }

    public class ItemDecoration implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, final int position, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                ChildStatusItemBinding binding = (ChildStatusItemBinding) holder.getBinding();
                binding.swipLayout.addListener(new SwipeDragLayout.SwipeListener() {
                    @Override
                    public void onUpdate(SwipeDragLayout layout, float offset) {
                        Log.d("offset", "onUpdate() called with offset = [" + offset + "]");
                    }

                    @Override
                    public void onOpened(SwipeDragLayout layout) {
                        Toast.makeText(mContext, "onOpened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed(SwipeDragLayout layout) {
                        Toast.makeText(mContext, "onClosed", Toast.LENGTH_SHORT).show();

                    }

                    /**
                     * 等同于setOnClickListener
                     * 见Method {@link SwipeDragLayout#onFinishInflate()}
                     * @param layout
                     */
                    @Override
                    public void onClick(SwipeDragLayout layout) {
                        Toast.makeText(mContext, fakeData().get(position).getContent(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
