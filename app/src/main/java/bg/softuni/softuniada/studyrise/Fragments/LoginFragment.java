package bg.softuni.softuniada.studyrise.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.register.BackgroundWorker;

public class LoginFragment extends Fragment implements View.OnClickListener{

    EditText usernameEt, passwordEt;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEt = (EditText) root.findViewById(R.id.input_username);
        passwordEt = (EditText) root.findViewById(R.id.input_password);

        login = (Button) root.findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {

        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String type = "login";

        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
        backgroundWorker.execute(type, username, password);
    }
}