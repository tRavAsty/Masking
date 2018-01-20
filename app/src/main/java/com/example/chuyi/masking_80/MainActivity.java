package com.example.chuyi.masking_80;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    static String p_80 = "1800596156325862845126904659690695138772991217924221350214425537433328811991802644818223864660643801080123145233753960191081149929822278773498692314776363";
    static String q_80 = "916926325447118192184196590485219490524913823665263861943391771902959670857047572182578721737111849615119047644843705801834129152135868392941286040861139";

    static String p_112 = "5658926927563527279935540388102927019894493065615789543404658931832005693381601378557121585716920564934296676636915007189743635672768438088394305808567960129759218440293554987770851426040855019043134870209208913896724608568276408281764529603013069574611837516971347638805344851631130116087807321924024544739";
    static String q_112 = "9560054374823147469907744377236176001841364138669518477332840480102490667063370121811947035483736387256386702118521514457848402225392811504118154129813436881507865550921337574813793397469442228606082658396101265183133936543642274922784794961263801033071235885761822302022152733078365291318074240915517753183";

    static String p_128 = "79488395918181690559697870319709856738354557108582770383633822632728782496591229529153314881787240542790632199390285859771728494929432699938277329912073270159432834089901731702496087454468257939931125522713345017743432896931106418957446785525283885174686180825142709210202771543883790612695195508633693973351738319579091192563670878659833431241421752656492684170076303645009177530858526038854086116305004341958866759781485496128142488903501226300100109104374087";
    static String q_128 = "167667046444010020648893134323438051195662486092141292826242430839977764216848482455936801042568388144970495329993411490038696114955474504261118499430874742356233603055884194266255361055273537097247035677508377735659608946855183691497446702860264465760288390981972177067957808295115164427531951350918096078930820359021977376291405653279594678752859475194662249012578722388087633579302829093997673545607016163359673959110730297494495039059373564983096391603613159";


    com.example.chuyi.masking_80.masking[] masking = new com.example.chuyi.masking_80.masking[3];
    //static BigInteger N = p.multiply(q);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        masking[0] = new com.example.chuyi.masking_80.masking(p_80,q_80);
        masking[1] = new com.example.chuyi.masking_80.masking(p_112,q_112);
        masking[2] = new com.example.chuyi.masking_80.masking(p_128,q_128);
        //String[] results = new String[30];




//        final Button b = findViewById(R.id.b1_0);
//        b.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//            }
//
//        });
    }


    public void sendMessage(View view){
        String[] par = view.getTag().toString().split("_");
        int i = Integer.parseInt(par[0]);
        int j = Integer.parseInt(par[1]);
        String r = masking[i-1].masking((j+1)*100);
        String textID = "security" + i + "_" + (j);
        int resID = getResources().getIdentifier(textID, "id", getPackageName());
        TextView t = (TextView)findViewById(resID);
        t.setText(r);

    }
    public void sendAll(View view){
        TextView[] t_all = new TextView[30];

        for(int i=1; i<2; i++) {
            for(int j=0; j<10; j++) {
                String textID = "security" + i + "_" + (j);
                int resID = getResources().getIdentifier(textID, "id", getPackageName());
                t_all[(i-1)*10+(j)] = ((TextView) findViewById(resID));
                String r = masking[i-1].masking((j+1)*100);
                //String r = "rrr";
                t_all[(i-1)*10+(j)].setText(r);
                //buttons[i][j].setOnClickListener(this);
            }
        }

 /*
        String FILENAME = "hello_file";


        BufferedWriter out = null;
        try
        {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);


            //FileWriter fstream = new FileWriter("out.txt", true); //true tells to append data.
            //out = new BufferedWriter(fstream);
            //out.write("\nsue");
            for(int i=1; i<4; i++) {
                for(int j=0; j<10; j++) {
                    String r = masking[i-1].masking((j+1)*100);
                    //String r = "rrr";
                    //t_all[(i-1)*10+(j)].setText(r);
                    //buttons[i][j].setOnClickListener(this);
                    fos.write(r.getBytes());
                    //out.write(r+"\n");
                }
            }
            fos.close();
           // out.close();

        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());

        }
        */
    }

}
