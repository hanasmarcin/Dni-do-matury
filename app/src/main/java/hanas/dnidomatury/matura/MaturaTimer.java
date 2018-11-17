package hanas.dnidomatury.matura;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import hanas.dnidomatury.matura.Matura;

final public class MaturaTimer {

    public long getMillisDiff(Calendar startDate, Calendar endDate){
        return endDate.getTimeInMillis() - startDate.getTimeInMillis();
    }

    public void startMaturaTimer(final Context context, final Matura matura, final TextView daysTimer, final TextView hmsTimer){

        final long millisDiff = getMillisDiff(Calendar.getInstance(), matura.getDate());
        new CountDownTimer(millisDiff, 1000) {

            //nadpisywanie funkcji mowiacej, co ma robic program gdy odliczy sie countDownInterval
            public void onTick(long timeUntilFinished) {

                //zamiana czasu w ms na s/m/h/d i wyliczanie odpowiednich wartosci
                long difSeconds = TimeUnit.MILLISECONDS.toSeconds(timeUntilFinished)%60;
                long difMinutes = TimeUnit.MILLISECONDS.toMinutes(timeUntilFinished)%60;
                long difHours = TimeUnit.MILLISECONDS.toHours(timeUntilFinished)%24;
                long difDays = TimeUnit.MILLISECONDS.toDays(timeUntilFinished);

                //zmiana wartosci pol teksowych
                daysTimer.setText("" + difDays);
                hmsTimer.setText(difHours + "h " + difMinutes + "min " + difSeconds + "s");
            }

            //nadpisywanie funkcji mowiacej, co ma robic program gdy odliczanie sie skonczy
            public void onFinish() {
                Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show();
            }

        }.start();
    }
}
