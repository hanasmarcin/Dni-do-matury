package hanas.dnidomatury.model.exam;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

final public class ExamTimer {

    private long difSeconds;
    private long difMinutes;
    private long difHours;
    private long difDays;
    private CountDownTimer timer;

    public CountDownTimer getTimer() {
        return timer;
    }

    public long getMillisDiff(Calendar startDate, Calendar endDate){
        return endDate.getTimeInMillis() - startDate.getTimeInMillis();
    }


    public void startExamTimer(final Context context, final Exam exam, final TextView daysTimer, final TextView hmsTimer){

        final long millisDiff = getMillisDiff(Calendar.getInstance(), exam.getDate());
        timer = new CountDownTimer(millisDiff, 1000) {

            //nadpisywanie funkcji mowiacej, co ma robic program gdy odliczy sie countDownInterval
            public void onTick(long timeUntilFinished) {

                //zamiana czasu w ms na s/m/h/d i wyliczanie odpowiednich wartosci
                difSeconds = TimeUnit.MILLISECONDS.toSeconds(timeUntilFinished)%60;
                difMinutes = TimeUnit.MILLISECONDS.toMinutes(timeUntilFinished)%60;
                difHours = TimeUnit.MILLISECONDS.toHours(timeUntilFinished)%24;
                difDays = TimeUnit.MILLISECONDS.toDays(timeUntilFinished);
                if(!daysTimer.getText().equals(""+difDays)) daysTimer.setText("" + difDays);
                hmsTimer.setText(difHours + "h " + difMinutes + "min " + difSeconds + "s");

                //zmiana wartosci pol teksowych
            }

            //nadpisywanie funkcji mowiacej, co ma robic program gdy odliczanie sie skonczy
            public void onFinish() {
                //Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show();
            }

        };
        timer.start();
    }
}
