package bg.softuni.softuniada.studyrise.Fragments;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.database.Downloader;

public class QuestionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String urlAddress = "http://quotes.comlu.com/questions.php";
    private String type = "questions";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static TextView progressText;
    public static TextView progressAll;
    private ProgressBar progress;
    private static int correct, all;

    public static void setAll(int al) {
        all = al;
        progressAll.setText(all + "");
    }

    public static void setCorrect(int cor) {
        correct = cor;
        progressText.setText(correct + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questions, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.background);

        progress = (ProgressBar) rootView.findViewById(R.id.progressbar);
        progress.setProgressDrawable(drawable);

        progressText = (TextView) rootView.findViewById(R.id.progressText);
        progressAll = (TextView) rootView.findViewById(R.id.progressAll);

        progressText.setText("0");

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        new Downloader(getContext(), urlAddress, recyclerView, type, progress).execute();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );

        return rootView;
    }

    @Override
    public void onRefresh() {
        new Downloader(getContext(), urlAddress, recyclerView, type, progress).execute();
        progressText.setText("0");
        swipeRefreshLayout.setRefreshing(false);
    }
}
