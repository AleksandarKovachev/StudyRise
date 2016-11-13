package bg.softuni.softuniada.studyrise.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.register.BackgroundWorker;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    EditText usernameEt, passwordEt, password2Et, emailEt;
    Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        usernameEt = (EditText) root.findViewById(R.id.input_username_registration);
        passwordEt = (EditText) root.findViewById(R.id.input_password_registration1);
        password2Et = (EditText) root.findViewById(R.id.input_password_registration2);
        emailEt = (EditText) root.findViewById(R.id.input_email);

        register = (Button) root.findViewById(R.id.registration_btn);
        register.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {

        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String password2 = password2Et.getText().toString();
        String email = emailEt.getText().toString();
        String type = "register";

        if (!password.equals(password2)) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Password incorrect");
            alertDialog.setMessage("The passwords does not equals");
            alertDialog.show();
        } else if(username.isEmpty() || username.length() < 4) {
            usernameEt.setError("Въведете поне 4 символа");
        } else if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Въведете валиден имейл адрес");
        }else{
            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
            backgroundWorker.execute(type, username, password, email);
        }

    }
}