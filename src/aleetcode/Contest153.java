package aleetcode;

import java.time.Month;
import java.time.Year;

public class Contest153 {

    public static void main(String[] args) {

        int [] distance={1,2,3,4};
        int res=distanceBetweenBusStops(distance,0,1);


    }

    public String dayOfTheWeek(int day, int month, int year) {

        String[] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int weekOfDay = Year.of(year).atMonth(Month.of(month)).atDay(day).getDayOfWeek().getValue() - 1;
        return weeks[weekOfDay];

    }

    public static int distanceBetweenBusStops(int[] distance, int start, int destination) {

        int res=0;
        int re=0;
        int newStart=start<destination?start:destination;
        int newDes=start>destination?start:destination;
        for(int i=0;i<distance.length;i++){

            if(i>=newStart&&i<newDes)
                res+=distance[i];

            if(i>=newDes||i<newStart)
                re+=distance[i];

        }
        return  res<re?res:re;

    }
}
