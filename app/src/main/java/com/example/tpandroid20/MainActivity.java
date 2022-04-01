package com.example.tpandroid20;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.jdbc.MySQLConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Connection conn;
    private TextView textView;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog() // Enregistre un message à logcat
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath() //l'application se bloque, fonctionne à //la fin de toutes les sanctions permises
                .build());

        MysqlConnexion();

        button = (Button) findViewById(R.id.valider);
        spinner = (Spinner) findViewById(R.id.spinner);
        RequeteAssos();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    EditText pseudo = (EditText)findViewById(R.id.editTextPseudo);
                    String pseudoinput = pseudo.getText().toString();

                    String requete = "SELECT * FROM utilisateur";
                    Statement rqt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet resultSet = rqt.executeQuery(requete);
                    Toast.makeText(MainActivity.this, "Recupérer saisie: " + pseudoinput , Toast.LENGTH_LONG).show();

                    while (resultSet.next()){
                        Toast.makeText(MainActivity.this, "Utilisateur: " + resultSet.getString(1) + " Mot de passe : " + resultSet.getString(2), Toast.LENGTH_LONG).show();

                    }
                }
                catch (SQLException se){
                    se.printStackTrace();
                }
            }
        });

    }

    private void MysqlConnexion(){
        String jdbcURL = "jdbc:mysql://10.4.253.19:3306/lésigny";
        String user = "monty";
        String passwd = "some_pass";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, user, passwd);
            Toast.makeText(MainActivity.this, "Connection à la base de donnée réussie !!!", Toast.LENGTH_LONG).show();

        } catch ( ClassNotFoundException e) {
            Toast.makeText(MainActivity.this, "Driver manquant." + e.getMessage().toString(), Toast.LENGTH_LONG).show();

        } catch ( java.sql.SQLException ex ) {
            Toast.makeText(MainActivity.this, "Connexion au serveur impossible." + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            Log.d("error", "SQLException: " + ex.getMessage());
            Log.d("error","SQLState: " + ex.getSQLState());
            Log.d("error","VendorError: " + ex.getErrorCode());
        }
    } // fin de MysqlConnection


    public void RequeteAssos() {
        try {
            String req = "SELECT spinner FROM spinner";
            Statement pstm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rst1 = pstm.executeQuery(req);
            List<String> list = new ArrayList<>();;
            while (rst1.next()) {
                list.add(rst1.getString(1));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}