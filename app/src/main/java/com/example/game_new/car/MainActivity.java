package com.example.game_new.car;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        private final String add="00:21:13:00:3B:05";
        private final UUID port= UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        private OutputStream outputStream;
        private BluetoothSocket socket;
        private BluetoothDevice device;
        String command;
        ListView list;
        Button forw,rev,left,right,voice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(init())
        {
            boolean x=con();
            Log.d("","x="+x);
        }
        forw=(Button) findViewById(R.id.forw);
        rev=(Button)findViewById(R.id.rev);
        right=(Button) findViewById(R.id.right);
        left=(Button) findViewById(R.id.left);
        voice=(Button)findViewById(R.id.voice);
        list=(ListView)findViewById(R.id.list);
        voice.setOnClickListener(this);


        forw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    command="1";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    command="0";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                return false;
            }
        });
        rev.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    command="2";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    command="0";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                return false;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    command="3";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    command="0";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                return false;
            }
        });
       left.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if(event.getAction()==MotionEvent.ACTION_DOWN)
               {
                   command="4";
                   try{
                       outputStream.write(command.getBytes());
                   }catch (IOException e){}
               }
               else if(event.getAction()==MotionEvent.ACTION_UP)
               {
                   command="0";
                   try{
                       outputStream.write(command.getBytes());
                   }catch (IOException e){}
               }
               return false;
           }
       });

    }
    @Override
    public void onClick(View view)
    {
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(i,1);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        Log.d("","on result called");
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            ArrayList<String> lists=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(String test:lists)
            {
                if(test.toLowerCase().equals("forward"))
                {
                    command="1";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(test.toLowerCase().equals("stop"))
                {
                    command="0";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(test.toLowerCase().equals("right"))
                {
                    command="3";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(test.toLowerCase().equals("left"))
                {
                    command="4";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
                else if(test.toLowerCase().equals("revere"))
                {
                    command="2";
                    try{
                        outputStream.write(command.getBytes());
                    }catch (IOException e){}
                }
            }
            Log.d("list:",""+lists);
            list.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,lists));
        }
        MainActivity.super.onActivityResult(requestCode,resultCode,data);
    }
    public boolean init()
    {
        boolean f=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null)
        {
            Toast.makeText(this,"Bluetooth not supported",Toast.LENGTH_LONG).show();
        }
        if(!bluetoothAdapter.enable()){
            Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(i);
//           try{
//                Thread.sleep(5000);
//            }catch (InterruptedException e) {}
        }
        Log.d("init","bluetooth enabled");
        Set<BluetoothDevice> list=bluetoothAdapter.getBondedDevices();
        Log.d("size",""+list.size());
        if(list==null)
        {
        //    Toast.makeText(this,"Please pair devices",Toast.LENGTH_LONG).show();
        }
        else
        {
            for(BluetoothDevice devices:list){
                Log.d("device:"+devices.getName(),"address:"+devices.getAddress());
                if(devices.getAddress().equals(add))
                {
                    Log.d("","entered the if");
                    //Toast.makeText(this,"found device",Toast.LENGTH_LONG).show();
                    f=true;
                    device=devices;
                    break;
                }
            }
            if(f==false)
            {
            //    Toast.makeText(this,"device not found",Toast.LENGTH_LONG).show();
            }
        }
        Log.d("","f returned");
        return f;
    }
    public boolean con()
    {
        Log.d("","inside con");
        boolean c=true;
        try{
            Log.d("","get socket");
            socket=(BluetoothSocket) device.createInsecureRfcommSocketToServiceRecord(port);
            Log.d("","connecting to socket");
          //  Toast.makeText(this,"going to connect",Toast.LENGTH_LONG).show();
            socket.connect();
            Log.d("","connected to socket");
           // Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show();

        }catch (Exception e)
        {
            Log.d("","Error!!!!");
            c=false;
        }
        Log.d("","c="+c);
        if(c)
        {
            try{
                Log.d("","getting output stream");
                outputStream=socket.getOutputStream();
            }catch (IOException e)
            {}
        }
        Log.d("","returning c");
        return c;
    }
}
