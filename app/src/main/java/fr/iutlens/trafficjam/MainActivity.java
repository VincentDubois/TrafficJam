package fr.iutlens.trafficjam;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity {


    private TrafficView trafficView;
    private int tempsRestant;
    private ProgressBar timer;
    private ProgressBar pas_content;

    // Gestion du timer

    static class RefreshHandler extends Handler {
        WeakReference<MainActivity> weak;

        RefreshHandler(MainActivity animator){
            weak = new WeakReference(animator);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak.get() == null) return;
            weak.get().update();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }

    }

    public  void gameover() {
        tempsRestant=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        trafficView.init();
                        tempsRestant = 600;
                        update();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog ob1ject and return it
        builder.create().show();
    }

    private RefreshHandler handler;

    private TextView nb_voit;

    private void update() {

        if (tempsRestant > 0 && trafficView.getNbVoitures()>0) {
            handler.sleep(40);
            trafficView.act();

            int tmptotal = trafficView.getTmpstotal();
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.pas_content);
            progressBar.setProgress(tmptotal);
            //}
            int nbVoitures = trafficView.getNbVoitures(); // on récupère le nombre de voitures dans TrafficView
            nb_voit = (TextView) findViewById(R.id.nb_voit); // on récupère le TextView
            nb_voit.setText(""+nbVoitures);

            tempsRestant--;
            if (tempsRestant==0){
                   gameover();
            } else {

                timer.setProgress(tempsRestant);
                tmptotal = trafficView.getTmpstotal();
                if (tmptotal < 100) {

        } else if (tempsRestant <= 0) {
                    progressBar = (ProgressBar) findViewById(R.id.pas_content);
                    progressBar.setProgress(tmptotal);
                } else {
                    progressBar = (ProgressBar) findViewById(R.id.pas_content);
                    progressBar.setProgress(100);
                    gameover();
                }
                nbVoitures = trafficView.getNbVoitures();
                nb_voit = (TextView) findViewById(R.id.nb_voit); // on récupère le TextView
                nb_voit.setText("" + nbVoitures);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.game_over)
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();

        } else if (trafficView.getNbVoitures() <= 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.win)
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();

            // TODO : Gérer la possibilité de retourner au menu et de rejouer

        }
      
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new RefreshHandler(this);

        trafficView = (TrafficView) findViewById(R.id.view);

        // on démarre l'animation
        timer = (ProgressBar) findViewById(R.id.timer);
        timer.setMax(1000);
        tempsRestant = 1000;
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
