#include <SoftwareSerial.h>
#include <Servo.h>

SoftwareSerial portTwo(2, 4);

Servo servo1;
Servo servo2;

#define in1 11 //L298n Motor Driver pins.
#define in2 3
#define in3 5
#define in4 6
String received;
String path;
int value;
int dest;
int start = 0;
int sub;
String token;
int index;
unsigned long eskiZaman = 0;
int Speed = 160; // 0 - 255.
int Speedsec = 170;
void setup() {
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  servo1.attach(10);
  servo2.attach(9);

  Serial.begin(9600);
  portTwo.begin(9600);
}
void loop() {
  if (portTwo.available() > 0 ) {
    received = portTwo.readStringUntil('!');
  }
  if (received.indexOf("-") != -1 ) {
    path = received;
    Serial.print(path);
    received = "XXXX";
  }
  index = path.indexOf('-');
  if (index != -1) {
    token = path.substring(0, index);
    if (token.charAt(0) == 'C') {
      dest = findValue(token);
      bool isRight;
      sub = 0;
      if (dest >= start) {
        sub = dest - start;
        isRight = true;
        if (sub > 180) {
          sub = start + (360 - dest);
          isRight = false;
        }
      }
      else {
        sub = start - dest;
        isRight = false;
        if (sub > 180) {
          sub = (360 - start) + dest;
          isRight = true;
        }
      }
      if (isRight) {
        right();
        if (millis() - eskiZaman >= sub * 10) {
          Stop();
          path = path.substring(index + 1);
          start = dest;
          eskiZaman = millis();
        }
      }
      else {
        left();
        if (millis() - eskiZaman >= sub * 10) {
          Stop();
          path = path.substring(index + 1);
          start = dest;
          eskiZaman = millis();
        }
      }
    }
    else if (token.charAt(0) == 'F') {
      dest = findValue(token);
      forward();
      if (millis() - eskiZaman >= dest * 10) {
        Stop();
        path = path.substring(index + 1);
        eskiZaman = millis();
      }
    }
  }
  else {
    eskiZaman = millis();
    start = 0;
  }
  if (received == "F") {
    forward();
  }
  else if (received == "X") {
    Stop();
    path = "";
  }
  else if (received == "G") {
    forwardleft();
  }

  else if (received == "I") {
    forwardright();
  }

  else if (received == "B") {
    back();
  }

  else if (received == "H") {
    backleft();
  }

  else if (received == "J") {
    backright();
  }

  else if (received == "L") {
    left();
  }

  else if (received == "R") {
    right();
  }

  else if (received == "S") {
    Stop();
    finishCommand();
  }
  else if (received.charAt(0) == 'T') {
    value = findValueServo(received);
    servo2.write(value);


  }
  else if (received.charAt(0) == 'P') {
    value = findValueServo(received);
    servo1.write(value);
  }

}
void finishCommand() {
  received = "X";
}

int findValue(String token) {
  String temp2;
  temp2 = token.substring(1);
  return temp2.toInt();
}

int findValueServo(String received) {
  String temp;
  temp = received.substring(1);
  value = temp.toInt();
  return value;
}
void forward() {
  analogWrite(in1, 145);
  analogWrite(in3, 170);
}

void back() {
  analogWrite(in2, Speed);
  analogWrite(in4, Speed);
}

void left() {
  analogWrite(in3, 230);
  analogWrite(in2, Speed);
}

void right() {
  analogWrite(in4, 175);
  analogWrite(in1, 190  );
}
void forwardleft() {
  analogWrite(in1, Speedsec);
  analogWrite(in3, Speed);
}
void forwardright() {
  analogWrite(in1, 200);
  analogWrite(in3, Speedsec);
}
void backright() {
  analogWrite(in2, Speed);
  analogWrite(in4, Speedsec);
}
void backleft() {
  analogWrite(in2, Speedsec);
  analogWrite(in4, Speed);
}

void Stop() {
  analogWrite(in1, 0);
  analogWrite(in2, 0);
  analogWrite(in3, 0);
  analogWrite(in4, 0);
}
