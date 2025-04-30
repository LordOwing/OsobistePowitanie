package com.example.osobistepowitanie;



import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Button guzik;
    private EditText edit;
    private  String tekst;
    private  Bitmap bitmap;
    private static final String CHANNEL_ID = "My_channel_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        guzik = findViewById(R.id.guzik);
        edit = findViewById(R.id.imie);
        guzik.setOnClickListener(v->{
            tekst = edit.getText().toString();
            if(tekst.length()>0){
                showAlertDialog();
            }else {
                Toast.makeText(MainActivity.this, "Proszę wpisać swoje imię!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potwierdzenie");
        builder.setMessage("Cześć " + tekst + "! Czy chcesz otrzymac powiadomienie powitalne?");
        builder.setPositiveButton("Tak, proszę", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                sendNotification();
                Toast.makeText(MainActivity.this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Nie, dziękuję", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Rozumiem nie wysyłam powiadomienia", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            CharSequence name = "Kanał Powiadomień";
            String description = "Opis Kanału Powiadomień";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notifiactionManager = getSystemService(NotificationManager.class);
            notifiactionManager.createNotificationChannel(channel);
        }
    }

    void sendNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }


        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.cos2)
                .setContentTitle("Witaj")
                .setContentText("Miło cię widzieć " + tekst + "!" )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}