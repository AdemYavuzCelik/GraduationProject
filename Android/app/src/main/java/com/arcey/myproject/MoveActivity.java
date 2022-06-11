package com.arcey.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MoveActivity extends AppCompatActivity {
    public int x, y, x1, y1;
    public String[] cords;
    public int angle = 0;
    public int temp = 0;
    public int i = 0;
    int distance;
    public int toplam ;
    StringBuilder data = new StringBuilder();
    public static String path;
    public int meter;
    EditText editMeter;
    ImageButton buttonClear,buttonRotaGonder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        editMeter=findViewById(R.id.editMeter);
        buttonRotaGonder = findViewById(R.id.butonRotaGonder);
        buttonClear = findViewById(R.id.buttonClear);
        editMeter.setText("1");
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaintView.moveCords.clear();
                PaintView.yCord.clear();
                PaintView.xCord.clear();
                PaintView.path.reset();
                path="";
                data.delete(0,data.length());
                PaintView.pathList.clear();
                PaintView.firstTime=true;
            }
        });

        buttonRotaGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < PaintView.moveCords.size() - 1; i++) {
                    sumMoveMeter(PaintView.moveCords.get(i), PaintView.moveCords.get(i + 1));
                }
                //System.out.println(toplam);
                meter = Integer.parseInt(editMeter.getText().toString());
                System.out.println(meter);
                for (int i = 0; i < PaintView.moveCords.size() - 1; i++) {
                    moveType(PaintView.moveCords.get(i), PaintView.moveCords.get(i + 1));
                }
                data.append("!");
                path = data.toString();
                ManualActivity.pathCiziliMi = true;
                Intent intent = new Intent(MoveActivity.this, ManualActivity.class);
                intent.putExtra("ROTA", path);
                System.out.println(path);
                startActivity(intent);
            }
        });
    }

    private void sumMoveMeter(String s, String s1) {
        cords = s.split("-", 2);
        x = Integer.parseInt(cords[0]);
        y = Integer.parseInt(cords[1]);
        cords = s1.split("-", 2);
        x1 = Integer.parseInt(cords[0]);
        y1 = Integer.parseInt(cords[1]);
        distance = (int) Math.hypot(Math.abs(x - x1), Math.abs(y - y1));
        toplam+=distance;
    }

    public void moveType(String baslangic, String bitis) {
        cords = baslangic.split("-", 2);
        x = Integer.parseInt(cords[0]);
        y = Integer.parseInt(cords[1]);
        cords = bitis.split("-", 2);
        x1 = Integer.parseInt(cords[0]);
        y1 = Integer.parseInt(cords[1]);
        distance = (int) Math.hypot(Math.abs(x - x1), Math.abs(y - y1));
      //  System.out.println("eski"+distance);
        distance = (distance*200*meter)/toplam;
        //System.out.println("yeni"+distance);
        if(distance != 0){
            temp = (int) getAngle(x, y, x1, y1);
                data.append("C");
                data.append(temp);
                data.append("-");
                data.append("F");
                data.append(distance);
                data.append("-");
        }

    }
    public double getAngle(int x, int y, int x1, int y1){
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(y1 - y, x1 - x);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI/2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);

        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}