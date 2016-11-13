package bg.softuni.softuniada.studyrise.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.QuestionAdapter;
import bg.softuni.softuniada.studyrise.Question;
import bg.softuni.softuniada.studyrise.R;

public class DataParser extends AsyncTask<Void, Void, Boolean> {

    Context c;
    String text;
    private RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    String type;
    ProgressBar progressBar;

    ArrayList<Question> questions = new ArrayList<>();

    public DataParser(Context c, String text, RecyclerView lv, String type, ProgressBar progressBar) {
        this.c = c;
        this.text = text;
        this.recyclerView = lv;
        this.type = type;
        this.progressBar = progressBar;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (type.equals("questions")) {
            if (result) {
                questionAdapter = new QuestionAdapter(c, R.layout.main_list_questions, questions, progressBar);

                LinearLayoutManager llm = new LinearLayoutManager(c);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(questionAdapter);
            }
        }
    }

    private Boolean parseData() {

        if (type.equals("questions")) {
            try {
                JSONArray ja = new JSONArray(text);
                JSONObject jo = null;

                questions.clear();
                Question question;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    String questionDb = jo.getString("question");
                    String correctAnswer = jo.getString("correctAnswer");
                    String answer1 = jo.getString("answer1");
                    String answer2 = jo.getString("answer2");
                    String answer3 = jo.getString("answer3");

                    question = new Question();
                    question.setQuestion(questionDb);
                    question.setCorrectAnswer(correctAnswer);
                    question.setAnswer1(answer1);
                    question.setAnswer2(answer2);
                    question.setAnswer3(answer3);
                    questions.add(question);
                }
                return true;
            } catch (
                    JSONException e
                    )

            {
                e.printStackTrace();
            }
        }
        return false;
    }
}
