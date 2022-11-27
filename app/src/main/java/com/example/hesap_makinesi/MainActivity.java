package com.example.hesap_makinesi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity {
    EditText a;
    boolean esittireBasildi=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EditText e tıklandığında klavyenin açılmaması için
        a=findViewById(R.id.cevap);
        a.setShowSoftInputOnFocus(false);
        //Tıklandığında dokunun yazısını kaldırma
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("dokunun".equals(a.getText().toString()));
                a.setText(" ");
            }
        });
    }

    public void Buton(View view) {
        if (esittireBasildi){
            a.setText("");
            esittireBasildi=false;
        }
        switch (view.getId()){
            case R.id.silme      :a.setText("");   break;
            case R.id.parantezler:parantezEkle();  break;
            case R.id.karesi     :girdi("^");   break;
            case R.id.bolme      :girdi("÷");   break;
            case R.id.seven      :girdi("7");   break;
            case R.id.eight      :girdi("8");   break;
            case R.id.nine       :girdi("9");   break;
            case R.id.carpma     :girdi("x");   break;
            case R.id.four       :girdi("4");   break;
            case R.id.five       :girdi("5");   break;
            case R.id.six        :girdi("6");   break;
            case R.id.cikarma    :girdi("-");   break;
            case R.id.one        :girdi("1");   break;
            case R.id.two        :girdi("2");   break;
            case R.id.three      :girdi("3");   break;
            case R.id.toplama    :girdi("+");   break;
            case R.id.ucsifir    :ucsifirekle();  break;
            case R.id.zero       :girdi("0");   break;
            case R.id.nokta      :girdi(".");   break;
            case R.id.esittir    :Hesapla();       break;
            case R.id.silici     :silmeIslemi();   break;
        }
    }

    private void silmeIslemi() {
        int cursorPos =a.getSelectionStart();
        if (cursorPos>0){
            String eskiGirdi = a.getText().toString();
            String solGirdi=eskiGirdi.substring(0,cursorPos-1);
            String sagGirdi=eskiGirdi.substring(cursorPos);
            String yeniGirdi=solGirdi+sagGirdi;
            a.setText(yeniGirdi);
            a.setSelection(cursorPos-1);
        }
    }

    private void Hesapla() {
        String eskiGirdi= a.getText().toString();
        String eskiGirdi2=eskiGirdi.replaceAll("÷","/");
        eskiGirdi2=eskiGirdi.replaceAll("x","*");
        Expression isaret=new Expression(eskiGirdi2);
        String result=String.valueOf(isaret.calculate()).toString();
        if (!result.equals("NaN")){
            a.setText(result);
            a.setSelection(result.length());
        }else{
            showToast("Hatalı İşlem");
        }
        esittireBasildi=true;
    }

    private void ucsifirekle() {
        int cursorPos =a.getSelectionStart();
        if(getString(R.string.dokunun).equals(a.getText().toString())){
            a.setText(000);
        }else {
            String eskiGirdi = a.getText().toString();
            String solGirdi=eskiGirdi.substring(0,cursorPos);
            String sagGirdi=eskiGirdi.substring(cursorPos);
            String yeniGirdi=solGirdi+"000"+sagGirdi;
            a.setText(yeniGirdi);
        }
        a.setSelection(cursorPos+3);
    }

    private void girdi(String karakterGır) {
        //İşlemin tıklandığı noktadan sola ittirmesi için
        int cursorPos =a.getSelectionStart();
        if(getString(R.string.dokunun).equals(a.getText().toString())){
            a.setText(karakterGır);
        }else {
            String eskiGirdi = a.getText().toString();
            String solGirdi=eskiGirdi.substring(0,cursorPos);
            String sagGirdi=eskiGirdi.substring(cursorPos);
            String yeniGirdi=solGirdi+karakterGır+sagGirdi;
            a.setText(yeniGirdi);
        }
        a.setSelection(cursorPos+1);
    }
    //Eşittire basıldıktan sonra sıfırlama işlemi


    //Açılan parantez sayısı kadar parantez kapatma işlemi
    private void parantezEkle() {
        String eskiGirdi= a.getText().toString();
        int cursorPos=a.getSelectionStart();
        int parantezEkle=0;
        for (int i=0; i<eskiGirdi.length();i++){
            if (eskiGirdi.substring(i,i+1).equalsIgnoreCase("(")) parantezEkle++;
            if (eskiGirdi.substring(i,i+1).equalsIgnoreCase(")")) parantezEkle--;
        }
        String sonKarakteriAl=eskiGirdi.substring(eskiGirdi.length()-1);
        if (parantezEkle==0||sonKarakteriAl.equals("(")) girdi("(");
        else if (parantezEkle>0 && !sonKarakteriAl.equals(")")) girdi(")");
    }
    //Ekrana uyarı vermek için toast
    private  void showToast(String text){
        LayoutInflater inflater=getLayoutInflater();
        View loyout=inflater.inflate(R.layout.toast,(ViewGroup) findViewById((R.id.toast)));
        Toast toast=new Toast(getApplicationContext());
        TextView toastText=loyout.findViewById(R.id.toast_text);
        toastText.setText(text);

        toast.setGravity(Gravity.CENTER,0,-200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(loyout);
        toast.show();

    }
}