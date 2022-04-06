package com.example.trial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trial.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MutableLiveData<String> response =new MutableLiveData<String>();
    private String sentence;
    private String modifiedSentence;
    private Socket clientSocket;
    private BufferedReader inFromUser =new BufferedReader(new InputStreamReader(System.in));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        binding.connection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d("test","hello");
                            clientSocket= new Socket("192.168.0.42",12355);
                            Log.d("test","rana");
                        }
                        catch(IOException e){
                            e.printStackTrace();

                        }
                    }
                }).start();
                Log.d("test","reem");
                Toast.makeText(
                        getApplicationContext(),"Intiating Connection", Toast.LENGTH_LONG).show();

            }
        });
        binding.Send.setOnClickListener(new View.OnClickListener(){
            EditText ET = (EditText) findViewById(R.id.fromUser);
            @Override

            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            DataOutputStream outToServer =
                                    new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer =
                                    new BufferedReader(new
                                            InputStreamReader(clientSocket.getInputStream()));


                            sentence = ET.getText().toString();

                            outToServer.write((sentence + '\n').getBytes());

                            modifiedSentence = inFromServer.readLine();
                            response.postValue(modifiedSentence);
                        }
                        catch(IOException e){
                            e.printStackTrace();

                        }
                    }
                }).start();
                Toast.makeText(
                         getApplicationContext(),"Sending....", Toast.LENGTH_LONG).show();


            }
        });
        response.observe(this, new Observer() {
            @Override
            public void onChanged(Object o){
                Toast.makeText(
                        getApplicationContext(), response.getValue(), Toast.LENGTH_LONG).show();


            }
        });

    }
}