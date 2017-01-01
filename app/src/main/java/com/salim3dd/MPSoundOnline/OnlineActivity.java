package com.salim3dd.MPSoundOnline;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class OnlineActivity extends AppCompatActivity {
    ListView listView;
    MediaPlayer sound = new MediaPlayer();

    ArrayList<listitemOnline> listitemOnlines = new ArrayList<>();

    private SeekBar seekBar;
    Button btn_play, btn_pause, btn_stop;
    TextView tvTitle, tvCurrentTime, tvTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        listView = (ListView) findViewById(R.id.listView2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);


        ///online
        String[] Titles = getResources().getStringArray(R.array.Titles);
        String[] Photo = getResources().getStringArray(R.array.Photo);
        String[] URLSound = getResources().getStringArray(R.array.URLSound);

        for (int i = 0; i < URLSound.length; i++) {
            listitemOnlines.add(new listitemOnline(Titles[i], Photo[i], URLSound[i]));
        }
        listAdapterOnline listAdapter = new listAdapterOnline(listitemOnlines);
        listView.setAdapter(listAdapter);

        /////////////////////////

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (!sound.isPlaying()) {
                    Thread updateSeekBar;
                    updateSeekBar = new Thread() {
                        @Override
                        public void run() {
                            int SoundDuration = sound.getDuration();
                            int currentPostion = 0;
                            seekBar.setMax(SoundDuration);
                            while (currentPostion < SoundDuration) {
                                try {
                                    sleep(50);
                                    currentPostion = sound.getCurrentPosition();
                                    seekBar.setProgress(currentPostion);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    sound.start();
                    updateSeekBar.start();
                //}
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.stop();

            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.pause();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) sound.seekTo(i);
                SoundTime();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sound.stop();
                sound.reset();
                view.setSelected(true);
                try {
                    sound.setDataSource(listitemOnlines.get(i).getSound());
                    sound.prepare();
                    //sound.start();
                    tvTitle.setText(listitemOnlines.get(i).getTitle());
                    SoundTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void SoundTime() {
        seekBar.setMax(sound.getDuration());
        int tim = (seekBar.getMax() / 1000);
        int m = tim / 60;
        int s = tim % 60;
        //////
        int tim0 = (seekBar.getProgress() / 1000);
        int m0 = tim0 / 60;
        int s0 = tim0 % 60;

        tvTotalTime.setText(s + " : " + m);
        tvCurrentTime.setText(s0 + " : " + m0);
    }

    class listAdapterOnline extends BaseAdapter {

        ArrayList<listitemOnline> listOnline = new ArrayList<>();

        public listAdapterOnline(ArrayList<listitemOnline> lis) {
            this.listOnline = lis;
        }

        @Override
        public int getCount() {
            return listOnline.size();
        }

        @Override
        public Object getItem(int position) {
            return listOnline.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            final View view = layoutInflater.inflate(R.layout.row_itme, null);
            final ImageView img = (ImageView) view.findViewById(R.id.imageView);
            TextView title = (TextView) view.findViewById(R.id.textView_title);

            title.setText(listOnline.get(i).getTitle());
            Picasso.with(OnlineActivity.this).load(listOnline.get(i).getImg()).into(img);

            return view;
        }
    }

}
