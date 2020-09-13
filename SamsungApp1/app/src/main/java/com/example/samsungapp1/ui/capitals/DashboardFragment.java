package com.example.samsungapp1.ui.capitals;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

import com.example.samsungapp1.R;
import com.example.samsungapp1.ui.profile.User;

public class DashboardFragment extends Fragment {

    /*
    Класс для работы с фрагментом для угадыния столицы государства.
    С помощью классов MyDataBase и GetRandomFromCountries фрагмент получает из базы данных со всеми
    госудрствами и их столицами случайно выбранное государство. При открытии данной вкладки
    начинается анимация с быстрой сменой названий стран. Эта анимация реализована в отдельном потоке,
    что позволяет приложению не виснуть во время ее выполнения. После завершения анимации, пользователю
    нужно написать в поле для ввода столицу предложенного государства. Если он угадывает, ему прибавляется
    3 балла и предлагается новое государство.
     */

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView forCountry = root.findViewById(R.id.country);
        final EditText answer = root.findViewById(R.id.countryAnswer);
        final TextView goodOrBad = root.findViewById(R.id.forGoodRes);
        final Button submit = root.findViewById(R.id.button);
        final TextView correct = root.findViewById(R.id.forRes);
        final MyDataBase helper = new MyDataBase(this.getContext(), MyDataBase.TABLE_NAME, null, 1);
        final Button next = root.findViewById(R.id.next);
        final GetRandomFromCountries getRandomFromCountries = new GetRandomFromCountries(helper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MyLog", "Thread started");
                    for (int i = 1; i < 50; i++) {
                        Log.d("MyLog", "New iteration" + i);
                        String[] data = getRandomFromCountries.getCountryAndCapital();
                        String country = data[0], capital = data[1];
                        char[] temp1 = country.toCharArray(), temp2 = capital.toCharArray();
                        final String copyCountry = new String(temp1), copyCapital = new String(temp2);
                        forCountry.setText(copyCountry);
                        correct.setText(copyCapital);
                        try {
                            Thread.sleep(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        String[] data = getRandomFromCountries.getCountryAndCapital();
        String country = data[0], capital = data[1];
        char[] temp1 = country.toCharArray(), temp2 = capital.toCharArray();
        final String copyCountry = new String(temp1), copyCapital = new String(temp2);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                forCountry.setText(copyCountry);
                correct.setText(copyCapital);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = forCountry.getText().toString(), capital = correct.getText().toString();
                String ans = answer.getText().toString();
                if (ans.equals(capital)) {
                    goodOrBad.setText(R.string.correct_answer);
                    GetRandomFromCountries getRandomFromCountries = new GetRandomFromCountries(helper);
                    String[] data = getRandomFromCountries.getCountryAndCapital();
                    forCountry.setText(data[0]);
                    correct.setText(data[1]);
                    answer.setText(R.string.empty);
                    User.setScore(User.getScore() + 3);
                    if (User.getScore() >= 100 && User.created())
                        Toast.makeText(getContext(), R.string.congrats, Toast.LENGTH_LONG).show();
                } else {
                    goodOrBad.setText(R.string.wrong_answer);
                    answer.setText(R.string.empty);
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                GetRandomFromCountries getRandomFromCountries = new GetRandomFromCountries(helper);
                String[] data = getRandomFromCountries.getCountryAndCapital();
                forCountry.setText(data[0]);
                correct.setText(data[1]);
                answer.setText(R.string.empty);
                next.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        return root;
    }
}