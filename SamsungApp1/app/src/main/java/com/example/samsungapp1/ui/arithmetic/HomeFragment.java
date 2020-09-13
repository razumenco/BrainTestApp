package com.example.samsungapp1.ui.arithmetic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.example.samsungapp1.ArithmeticExpression;
import com.example.samsungapp1.DoubleStackAlgorithm;
import com.example.samsungapp1.R;
import com.example.samsungapp1.ui.profile.User;

import java.util.Locale;

public class HomeFragment extends Fragment {


    /*
    Фрагмент для работы с пользовательским интерфейсом решения арифметических примеров.
    Пользователь вводит свой результат решения предложенного арифметического примера и нажимает на кнопку проверки.
    Если ответ верный, пользователю предлагается новый пример для решения.
    Также присутствует кнопка для генерации другого примера на тот случай, если пользователь затрудняется дать ответ.
     */

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false); // установка разметки этого фрагмента

        final TextView textView = root.findViewById(R.id.textView);
        final EditText answer = root.findViewById(R.id.editText);
        final TextView fgr = root.findViewById(R.id.forGoodRes);
        final Button submit = root.findViewById(R.id.button);
        final Button next = root.findViewById(R.id.next);

        final String toCalculate = ArithmeticExpression.generate();
        char[] buff = toCalculate.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i < buff.length - 2;  i++) {
            stringBuilder.append(buff[i]);
        }
        final String expr = stringBuilder.toString() + " = ";
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(expr);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int correct = DoubleStackAlgorithm.calculate(String.format(Locale.US,"( %s )", textView.getText().toString()));
                int res = correct - 1;

                try {
                    res = Integer.parseInt(answer.getText().toString());
                } catch (Exception e) {
                    fgr.setText(R.string.wrong_answer);
                }

                if (res != correct)
                    fgr.setText(R.string.wrong_answer);
                else {
                    fgr.setText(R.string.correct_answer);
                    User.setScore(User.getScore() + 1);
                    if (User.getScore() >= 100 && User.created())
                        Toast.makeText(getContext(), R.string.congrats, Toast.LENGTH_LONG).show();
                    String newExpr = ArithmeticExpression.generate();
                    char[] buff = newExpr.toCharArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 2; i < buff.length - 2;  i++) {
                        stringBuilder.append(buff[i]);
                    }
                    final String expr = stringBuilder.toString() + " = ";
                    textView.setText(expr);
                    answer.setText("");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                String newExpr = ArithmeticExpression.generate();
                char[] buff2 = newExpr.toCharArray();
                StringBuilder stringBuilder1 = new StringBuilder();

                for (int i = 2; i < buff2.length - 2; i++) {
                    stringBuilder1.append(buff2[i]);
                }
                final String expr = stringBuilder1.toString() + " = ";
                textView.setText(expr);
                next.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        return root;
    }
}