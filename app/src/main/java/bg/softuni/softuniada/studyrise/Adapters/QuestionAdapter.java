package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bg.softuni.softuniada.studyrise.Fragments.QuestionsFragment;
import bg.softuni.softuniada.studyrise.Question;
import bg.softuni.softuniada.studyrise.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private Context context;
    private int layoutId;
    private List<Question> data;
    private ProgressBar progressBar;
    private int corrects;

    public QuestionAdapter(Context context, int textViewResourceId, List<Question> objects, ProgressBar progressBar) {
        this.context = context;
        layoutId = textViewResourceId;
        data = objects;
        this.progressBar = progressBar;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_questions, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Question question = data.get(position);
        holder.question.setText(question.getQuestion());

        String[] str = {question.getCorrectAnswer(), question.getAnswer1(), question.getAnswer2(), question.getAnswer3()};
        List<String> arr = Arrays.asList(str);
        Collections.shuffle(arr);
        arr.toArray(str);

        holder.answer1.setText(str[0]);
        holder.answer2.setText(str[1]);
        holder.answer3.setText(str[2]);
        holder.answer4.setText(str[3]);

        progressBar.setProgress(0);
        progressBar.setMax(100);
        QuestionsFragment.setAll(getItemCount());

        holder.answer1.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if (!holder.clicked) {
                                                      holder.clicked = true;
                                                      if (holder.answer1.getText().equals(question.getCorrectAnswer())) {
                                                          holder.correct.setVisibility(View.VISIBLE);
                                                          holder.answer1.setTypeface(null, Typeface.BOLD);
                                                          holder.answer1.setTextSize(25);
                                                          corrects++;
                                                          updateProgressBar(corrects);
                                                      } else {
                                                          holder.wrong.setVisibility(View.VISIBLE);
                                                          if (holder.answer2.getText().equals(question.getCorrectAnswer())) {
                                                              holder.answer2.setTextSize(30);
                                                              holder.answer2.setTypeface(null, Typeface.BOLD);
                                                          } else if (holder.answer3.getText().equals(question.getCorrectAnswer())) {
                                                              holder.answer3.setTextSize(30);
                                                              holder.answer3.setTypeface(null, Typeface.BOLD);
                                                          } else if (holder.answer4.getText().equals(question.getCorrectAnswer())) {
                                                              holder.answer4.setTextSize(30);
                                                              holder.answer4.setTypeface(null, Typeface.BOLD);
                                                          }
                                                      }
                                                  }
                                              }
                                          }

        );

        holder.answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.clicked) {
                    holder.clicked = true;
                    if (holder.answer2.getText().equals(question.getCorrectAnswer())) {
                        holder.correct.setVisibility(View.VISIBLE);
                        holder.answer2.setTextSize(30);
                        holder.answer2.setTypeface(null, Typeface.BOLD);
                        corrects++;
                        updateProgressBar(corrects);
                    } else {
                        holder.wrong.setVisibility(View.VISIBLE);
                        if (holder.answer1.getText().equals(question.getCorrectAnswer())) {
                            holder.answer1.setTextSize(30);
                            holder.answer1.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer3.getText().equals(question.getCorrectAnswer())) {
                            holder.answer3.setTextSize(25);
                            holder.answer3.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer4.getText().equals(question.getCorrectAnswer())) {
                            holder.answer4.setTextSize(25);
                            holder.answer4.setTypeface(null, Typeface.BOLD);
                        }
                    }
                }
            }
        });


        holder.answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.clicked) {
                    holder.clicked = true;
                    if (holder.answer3.getText().equals(question.getCorrectAnswer())) {
                        holder.correct.setVisibility(View.VISIBLE);
                        holder.answer3.setTypeface(null, Typeface.BOLD);
                        holder.answer3.setTextSize(25);
                        corrects++;
                        updateProgressBar(corrects);
                    } else {
                        holder.wrong.setVisibility(View.VISIBLE);
                        if (holder.answer2.getText().equals(question.getCorrectAnswer())) {
                            holder.answer2.setTextSize(25);
                            holder.answer2.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer1.getText().equals(question.getCorrectAnswer())) {
                            holder.answer1.setTextSize(25);
                            holder.answer1.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer4.getText().equals(question.getCorrectAnswer())) {
                            holder.answer4.setTextSize(25);
                            holder.answer4.setTypeface(null, Typeface.BOLD);
                        }
                    }
                }
            }
        });


        holder.answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.clicked) {
                    holder.clicked = true;
                    if (holder.answer4.getText().equals(question.getCorrectAnswer())) {
                        holder.correct.setVisibility(View.VISIBLE);
                        holder.answer4.setTypeface(null, Typeface.BOLD);
                        holder.answer4.setTextSize(25);
                        corrects++;
                        updateProgressBar(corrects);
                    } else {
                        holder.wrong.setVisibility(View.VISIBLE);
                        if (holder.answer2.getText().equals(question.getCorrectAnswer())) {
                            holder.answer2.setTextSize(25);
                            holder.answer2.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer3.getText().equals(question.getCorrectAnswer())) {
                            holder.answer3.setTextSize(25);
                            holder.answer3.setTypeface(null, Typeface.BOLD);
                        } else if (holder.answer1.getText().equals(question.getCorrectAnswer())) {
                            holder.answer1.setTextSize(25);
                            holder.answer1.setTypeface(null, Typeface.BOLD);
                        }
                    }
                }
            }
        });
    }

    private void updateProgressBar(int corrects) {
        float percent = (corrects * 100.0f) / getItemCount();
        progressBar.setProgress((int) percent);
        QuestionsFragment.setCorrect(corrects);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question, answer1, answer2, answer3, answer4;
        public CardView cardView;
        public ImageView correct, wrong;
        public boolean clicked;

        public MyViewHolder(View view) {
            super(view);

            question = (TextView) view.findViewById(R.id.question);
            answer1 = (TextView) view.findViewById(R.id.answer1);
            answer2 = (TextView) view.findViewById(R.id.answer2);
            answer3 = (TextView) view.findViewById(R.id.answer3);
            answer4 = (TextView) view.findViewById(R.id.answer4);

            cardView = (CardView) view.findViewById(R.id.carr_view);

            correct = (ImageView) view.findViewById(R.id.correct);
            wrong = (ImageView) view.findViewById(R.id.wrong);

            clicked = false;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
